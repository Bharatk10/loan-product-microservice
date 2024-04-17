package com.zettamine.mpa.lpm.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.aspectj.weaver.ltw.LTWWorld;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zettamine.mpa.lpm.audit.AuditAwareImpl;
import com.zettamine.mpa.lpm.constants.AppConstants;
import com.zettamine.mpa.lpm.constants.EscrowConstants;
import com.zettamine.mpa.lpm.constants.ValidationConstants;
import com.zettamine.mpa.lpm.entity.LoanProduct;
import com.zettamine.mpa.lpm.entity.PrepayPenalty;
import com.zettamine.mpa.lpm.entity.ProductStatusHistory;
import com.zettamine.mpa.lpm.entity.PropertyRestriction;
import com.zettamine.mpa.lpm.exception.DataViolationException;
import com.zettamine.mpa.lpm.exception.MismatchFoundException;
import com.zettamine.mpa.lpm.exception.ResourceAlreadyExistsException;
import com.zettamine.mpa.lpm.exception.ResourceNotFoundException;
import com.zettamine.mpa.lpm.mapper.LoanProductMapper;
import com.zettamine.mpa.lpm.mapper.PrepayPenaltyMapper;
import com.zettamine.mpa.lpm.mapper.ProductStatusHistoryMapper;
import com.zettamine.mpa.lpm.model.LoanReqDto;
import com.zettamine.mpa.lpm.model.LoanProdDto;
import com.zettamine.mpa.lpm.model.LoanProductCriteriaDto;
import com.zettamine.mpa.lpm.model.LoanProductDto;
import com.zettamine.mpa.lpm.model.LoanProductSearchCriteria;
import com.zettamine.mpa.lpm.model.PrePayPenalityDto;
import com.zettamine.mpa.lpm.model.PrepayPenaltyStatusDto;
import com.zettamine.mpa.lpm.model.ProductStatusHistoryDto;
import com.zettamine.mpa.lpm.model.SearchByReqDto;
import com.zettamine.mpa.lpm.model.UnderwritingCriteriaDto;
import com.zettamine.mpa.lpm.repository.LoanProductRepository;
import com.zettamine.mpa.lpm.repository.LoanProductStatusHistoryRepsitory;
import com.zettamine.mpa.lpm.repository.PrePayPenalityRepository;
import com.zettamine.mpa.lpm.repository.PropertyRestrictionRepository;
import com.zettamine.mpa.lpm.service.client.EscrowFeignClient;
import com.zettamine.mpa.lpm.service.client.UnderWritingCriteriaFeignClient;
import com.zettamine.mpa.lpm.util.LoanProductSpecifications;
import com.zettamine.mpa.lpm.util.StringNormalization;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoanProductServiceImpl implements ILoanProductService {

	private LoanProductRepository loanProductRepo;

	private PropertyRestrictionRepository propRestrRepo;

	private LoanProductStatusHistoryRepsitory loanStatusRepo;

	private AuditAwareImpl auditAwareImpl;

	private PrePayPenalityRepository prePayPenalityRepo;

	private EscrowFeignClient escrowClient;

	private UnderWritingCriteriaFeignClient criteriaClient;

	@Override
	@Transactional
	public void create(LoanProductDto loanProductDto) {

		loanProductDto = StringNormalization.processLoanProductDto(loanProductDto);

		LoanProdDto loanProd = LoanProductMapper.loanProductDtoToLoanProdDto(loanProductDto, new LoanProdDto());

		LoanProduct loanProduct = LoanProductMapper.loanProdDtoToLoanProduct(loanProd, new LoanProduct());

		Optional<LoanProduct> optLoanProductObj = loanProductRepo.findByProductName(loanProductDto.getProductName());

		if (optLoanProductObj.isPresent()) {
			throw new ResourceAlreadyExistsException(
					String.format(AppConstants.LOAN_PRODUCT_EXISTS_MSG, loanProductDto.getProductName()));
		}

		Map<String, String> conflicts = checkLoanProductConflicts(loanProd);

		if (!conflicts.isEmpty()) {

			throw new DataViolationException(conflicts);

		}

		List<String> missingRestrictions = new ArrayList<>();
		List<PropertyRestriction> propRestrictions = new ArrayList<>();

		if (loanProd.getPropertyRestrcitions() != null) {
			for (String restriction : loanProd.getPropertyRestrcitions()) {
				Optional<PropertyRestriction> propRestriction = propRestrRepo.findByRestrictionType(restriction);

				if (propRestriction.isEmpty()) {
					missingRestrictions.add(restriction);
				} else {
					propRestrictions.add(propRestriction.get());
				}
			}
		}

		if (!missingRestrictions.isEmpty()) {
			throw new ResourceNotFoundException(
					String.format(AppConstants.PROP_RSTR_NOT_EXISTS_MSG, missingRestrictions));
		} else {

			loanProduct.setPropertyRestrictionExists(AppConstants.STATUS_TRUE);
			loanProduct.setPropertyRestrcitions(propRestrictions);
			if (loanProd.getPropertyRestrcitions() == null || loanProd.getPropertyRestrcitions().isEmpty()) {

				loanProduct.setPropertyRestrictionExists(AppConstants.STATUS_FALSE);
			}
		}

		if (loanProd.getEscrowRequirements() == null) {

			loanProduct.setEscrowRequired(AppConstants.STATUS_FALSE);
		} else if (loanProd.getEscrowRequirements().isEmpty()) {
			loanProduct.setEscrowRequired(AppConstants.STATUS_FALSE);
		} else {
			loanProduct.setEscrowRequired(AppConstants.STATUS_TRUE);
		}

		if (loanProd.getUnderwritingCriteria() == null) {
			loanProduct.setStatus(AppConstants.STATUS_FALSE);
		} else if (loanProd.getUnderwritingCriteria().isEmpty()) {
			loanProduct.setStatus(AppConstants.STATUS_FALSE);
		} else {
			loanProduct.setStatus(AppConstants.STATUS_TRUE);
		}

		LoanProduct loanProdObj = loanProductRepo.save(loanProduct);

		if (loanProdObj.getStatus()) {

			updateLoanStatusHistory(loanProdObj.getProductId());

			addUnderWritingCriteria(loanProdObj.getProductId(), loanProd.getUnderwritingCriteria());

		}
		if (loanProdObj.getEscrowRequired()) {

			addEscrowRequirements(loanProdObj.getProductId(), loanProd.getEscrowRequirements());
		}

		if (loanProduct.getPrepayPenalty()) {

			PrePayPenalityDto penalityDto = PrepayPenaltyMapper.LoanProdDtoToPenalityDto(new PrePayPenalityDto(),
					loanProd);

			penalityDto.setProductId(loanProdObj.getProductId());

			createPrePayPenality(penalityDto);

		}

	}

	
	public void addUnderWritingCriteria(Integer productId, List<String> underwritingCriteria) {

		loanProductRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Loan product not found exception"));

		ResponseEntity<List<String>> responseEntityCriterias = criteriaClient.fetchAll();

		List<String> criteriaReqList = responseEntityCriterias.getBody();

		Set<String> missingCriterias = new HashSet<>();

		for (String criteria : underwritingCriteria) {

			if (!criteriaReqList.contains(criteria)) {
				missingCriterias.add(criteria);

			}

		}
		if (!missingCriterias.isEmpty()) {
			throw new ResourceNotFoundException(String.format(AppConstants.CRI_REQ_NOT_EXISTS_MSG, missingCriterias));
		}
		criteriaClient.addCriteriaToLoanProd(productId, criteriaReqList);

	}

	private void addEscrowRequirements(Integer productId, List<String> escrowRequirements) {

		ResponseEntity<List<String>> responseEntityReqs = escrowClient.getAllReq();

		List<String> escrowReqList = responseEntityReqs.getBody();

		Set<String> missingRequirements = new HashSet<>();

		for (String requirement : escrowRequirements) {

			if (!escrowReqList.contains(requirement)) {
				missingRequirements.add(requirement);
			}
		}
		if (!missingRequirements.isEmpty()) {

			throw new ResourceNotFoundException(
					String.format(EscrowConstants.ESCR_REQ_NOT_EXISTS_MSG, missingRequirements));
		}

		LoanReqDto loanReqDto = new LoanReqDto(productId, escrowRequirements);

		escrowClient.save(loanReqDto);
	}

	@Override
	public void addEscrowRequirementsToLoanProduct(Integer productId, List<String> escrowRequirements) {

		LoanProduct loanProduct = loanProductRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(AppConstants.LOAN_PROD_NOT_EXISTS_BY_ID_MSG, productId)));

		addEscrowRequirements(productId, escrowRequirements);

		if (!loanProduct.getEscrowRequired()) {

			loanProduct.setEscrowRequired(AppConstants.STATUS_TRUE);
			loanProductRepo.save(loanProduct);
		}

	}

	@Override
	public void addUnderWritingCriteriaToLoanProduct(Integer productId, List<String> underwritingCriteria) {

		LoanProduct loanProduct = loanProductRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(AppConstants.LOAN_PROD_NOT_EXISTS_BY_ID_MSG, productId)));

		addEscrowRequirements(productId, underwritingCriteria);

		if (!loanProduct.getStatus()) {
			loanProduct.setStatus(AppConstants.STATUS_FALSE);
			loanProductRepo.save(loanProduct);
		}

	}

	@Override
	public LoanProdDto getbyId(Integer productId) {

		LoanProduct loanProd = loanProductRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(AppConstants.LOAN_PROD_NOT_EXISTS_BY_ID_MSG, productId)));

		LoanProdDto loanProductDto = LoanProductMapper.loanProductToLoanproductDto(loanProd, new LoanProdDto());

		if (loanProd.getEscrowRequired()) {

			List<String> escrowRequirements = getEscrowRequirements(productId);
			loanProductDto.setEscrowRequirements(escrowRequirements);
		}
		
		List<UnderwritingCriteriaDto> criteriaDtoList = criteriaClient.fetchByLoanId(loanProd.getProductId()).getBody();
		
		 if(criteriaDtoList!=null) {
			 
			 List<String> criterias = criteriaDtoList.stream().map(criteriaDto -> criteriaDto.getCriteriaName()).collect(Collectors.toList());
			 loanProductDto.setUnderwritingCriteria(criterias);				 
		 }


		return loanProductDto;
	}

	public Map<String, String> checkLoanProductConflicts(LoanProdDto loanProd) {

		Map<String, String> conflicts = new HashMap<>();

		if (loanProd.getLoanTerm() < loanProd.getLockInPeriod()) {

			conflicts.put(AppConstants.LOCKIN_PERIOD, ValidationConstants.LOCKIN_PERIOD_GREATER_THAN_LOAN_TERM_MESSAGE);
		}

		if (loanProd.getMaxLoanAmount() < loanProd.getMinDownPayment()) {

			conflicts.put(AppConstants.MINDOWN_PAYMENT,
					ValidationConstants.MINDOWM_PAYMT_GREATER_THAN_MAXLOAN_AMNT_MESSAGE);
		}

		if (!(loanProd.getMinDownPaymentType().equalsIgnoreCase(AppConstants.ABSOLUTE)
				|| loanProd.getMinDownPaymentType().equalsIgnoreCase(AppConstants.PERCENTAGE))) {

			conflicts.put(AppConstants.MINDOWN_PAYMENT_TYPE, ValidationConstants.MINDOWN_PAYMT_TYPE_MESSAGE);
		}

		if (loanProd.getMinPenalityAmount() != null) {

			if (loanProd.getMaxLoanAmount() < loanProd.getMinPenalityAmount()) {

				conflicts.put(AppConstants.MIN_PENALITY_AMOUNT,
						ValidationConstants.MINIMUM_PENALITY_AMOUNT_GREATER_THAN_MAX_LOAN_AMNT_MESSAGE);

			}
		}

		if (loanProd.getMinPenalityAmount() != null) {
			if (loanProd.getPenalityPercentage() == null) {
				conflicts.put(AppConstants.PENALITY_PERCENTAGE, ValidationConstants.PENALITY_PERCENT_NULL_MESSAGE);
			}
		}

		if (loanProd.getPenalityPercentage() != null) {
			if (loanProd.getMinPenalityAmount() == null) {
				conflicts.put(AppConstants.MIN_PENALITY_AMOUNT, ValidationConstants.MIN_PENALITY_AMOUNT_NULL_MESSAGE);
			}
		}
		if (loanProd.getOriginationFee() != null) {
			if (loanProd.getOriginationFee() > loanProd.getMaxLoanAmount()) {
				conflicts.put(AppConstants.ORIGINATION_FEE,
						ValidationConstants.ORIGINATION_FEE_GREATER_THAN_MAX_LOAN_AMNT_MESSAGE);
			}
		} else {
			conflicts.put(AppConstants.ORIGINATION_FEE, "Must not be null in service layer");
		}

		return conflicts;
	}

	@Override
	@Transactional
	public void updateLoanProductStatus(Integer productId) {

		LoanProduct loanProduct = loanProductRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(AppConstants.LOAN_PROD_NOT_EXISTS_BY_ID_MSG, productId)));

		Boolean status = loanProduct.getStatus();
		LocalDateTime maxSupportedDateTime = LocalDateTime.of(9999, 12, 31, 23, 59, 59, 999999999);

		if (status) {

			loanProduct = loanProductRepo.findById(productId).get();

			loanProduct.setStatus(AppConstants.STATUS_FALSE);
			loanProductRepo.save(loanProduct);

			ProductStatusHistory prodStatusHistory = loanStatusRepo
					.findByProductIdAndEndDate(productId, maxSupportedDateTime)
					.orElseThrow(() -> new ResourceNotFoundException(
							String.format(AppConstants.LOAN_PROD_NOT_ACTIVE, productId)));

			prodStatusHistory.setEndDate(LocalDateTime.now());
			prodStatusHistory.setUpdatedBy(auditAwareImpl.getCurrentAuditor().get());

			loanStatusRepo.save(prodStatusHistory);

		} else {

			updateLoanStatusHistory(productId);
		}

	}

	@Override
	@Transactional
	public Boolean updateLoanStatusHistory(Integer productId) {

		LoanProduct loanProduct = loanProductRepo.findById(productId).get();
		if (!loanProduct.getStatus()) {
			LocalDateTime maxSupportedDateTime = LocalDateTime.of(9999, 12, 31, 23, 59, 59, 999999999);
			loanProduct.setStatus(AppConstants.STATUS_TRUE);
			loanProductRepo.save(loanProduct);
			Integer userId = auditAwareImpl.getCurrentAuditor().get();
			ProductStatusHistory prodStatusHistory = new ProductStatusHistory(loanProduct, LocalDateTime.now(),
					maxSupportedDateTime, userId);
			try {
				loanStatusRepo.save(prodStatusHistory);
			} catch (Exception ex) {
				return false;
			}

		}

		return true;

	}

	@Override
	public List<ProductStatusHistoryDto> getProductStatusHistory(Integer productId) {

		loanProductRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(AppConstants.LOAN_PROD_NOT_EXISTS_BY_ID_MSG, productId)));

		List<ProductStatusHistory> prodStatusHistoryList = loanStatusRepo.findByProductId(productId);

		if (prodStatusHistoryList.isEmpty()) {

			throw new ResourceNotFoundException(String.format(AppConstants.LOAN_PROD_NOT_ACTIVE, productId));
		} 
		List<ProductStatusHistoryDto> propHistoryDtoList = new ArrayList<>();
		ProductStatusHistoryDto propHistoryDto;
		for (ProductStatusHistory prodStatusHistory : prodStatusHistoryList) {
			propHistoryDto = ProductStatusHistoryMapper
					.productStatusHistoryToproductHistoryDto(new ProductStatusHistoryDto(), prodStatusHistory);
			if (propHistoryDto.getEndTime().toString().equals(AppConstants.END_DATE)) {
				propHistoryDto.setEndTime(AppConstants.CURENTLY_ACTIVE);
			}
			propHistoryDtoList.add(propHistoryDto);

		}

		return propHistoryDtoList;
	}

	@Override
	public void updatePrePayPenalityStatus(Integer productId) {

		LoanProduct loanProd = loanProductRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(AppConstants.LOAN_PROD_NOT_EXISTS_BY_ID_MSG, productId)));
		PrepayPenalty penality;
		if (loanProd.getPrepayPenalty()) {

			penality = prePayPenalityRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
					String.format(AppConstants.PRE_PAY_PENALITY_NOT_ACTIVE, productId)));
			loanProd.setPrepayPenalty(AppConstants.STATUS_FALSE);
			penality.setStatus(AppConstants.INACTIVE);

			loanProd.setPenalty(penality);
			loanProductRepo.save(loanProd);
		} else {
			penality = prePayPenalityRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
					String.format(AppConstants.PRE_PAY_PENALITY_NOT_ACTIVE, productId)));
			loanProd.setPrepayPenalty(AppConstants.STATUS_TRUE);
			penality.setStatus(AppConstants.ACTIVE);
			loanProd.setPenalty(penality);
			loanProductRepo.save(loanProd);
		}

	}

	@Override
	public void updatePrePayPenality(PrePayPenalityDto penalityDto) {

		Integer productId = penalityDto.getProductId();
		loanProductRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(AppConstants.LOAN_PROD_NOT_EXISTS_BY_ID_MSG, productId)));

		PrepayPenalty penality = prePayPenalityRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(AppConstants.PRE_PAY_PENALITY_UPDATE_ERROR, productId)));

		if (penality.getStatus().equals(AppConstants.INACTIVE)) {

			throw new ResourceNotFoundException(AppConstants.PRE_PAY_PENALITY_UPDATE_FALSE_ERROR);
		}

		penality.setPenaltyPercentage(penalityDto.getPenalityPercentage());
		penality.setMinimumPenaltyAmount(penalityDto.getMinPenalityAmount());

		prePayPenalityRepo.save(penality);

	}

	@Override
	public void createPrePayPenality(PrePayPenalityDto penalityDto) {

		Integer productId = penalityDto.getProductId();

		LoanProduct loanProd = loanProductRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(AppConstants.LOAN_PROD_NOT_EXISTS_BY_ID_MSG, productId)));

		Optional<PrepayPenalty> penality = prePayPenalityRepo.findById(productId);

		if (penality.isPresent()) {

			throw new MismatchFoundException(
					String.format(AppConstants.PREPAY_PENALITY_EXITS, penalityDto.getProductId()));
		}

		LoanProduct loanProduct = loanProductRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(AppConstants.LOAN_PROD_NOT_EXISTS_BY_ID_MSG, productId)));

		PrepayPenalty penalityObj = new PrepayPenalty(loanProduct, penalityDto.getMinPenalityAmount(),
				penalityDto.getPenalityPercentage(), AppConstants.ACTIVE);

		loanProd.setPrepayPenalty(AppConstants.STATUS_TRUE);
		loanProd.setPenalty(penalityObj);
		loanProductRepo.save(loanProd);

	}

	@Override
	public void updateLoanProduct(Integer productId, LoanProductDto loanProductDto) {
		loanProductDto = StringNormalization.processLoanProductDto(loanProductDto);

		LoanProduct loanProduct = loanProductRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(AppConstants.LOAN_PROD_NOT_EXISTS_BY_ID_MSG, productId)));

		if (loanProduct.getProductName().equalsIgnoreCase(loanProductDto.getProductName())) {
			LoanProdDto loanProdDto = LoanProductMapper.loanProductDtoToLoanProdDto(loanProductDto, new LoanProdDto());
			Map<String, String> conflicts = checkLoanProductConflicts(loanProdDto);
			if (!conflicts.isEmpty()) {
				throw new DataViolationException(conflicts);

			}
			LoanProduct loanProductObj = LoanProductMapper.loanProdDtoToLoanProduct(loanProdDto, new LoanProduct());
			loanProductObj.setProductId(productId);

			loanProductObj.setPropertyRestrcitions(loanProduct.getPropertyRestrcitions());

			loanProductRepo.save(loanProductObj);
		} else {
			throw new MismatchFoundException(AppConstants.LOAN_PRODUCT_NAME, loanProductDto.getProductName(),
					AppConstants.LOAN_PRODUCT);
		}
	}

	private List<String> checkLoanProductRestrictions(List<String> rsrtTypes) {
		List<String> checkRstr = new ArrayList<>();

		if (rsrtTypes != null) {
			for (String propRstr : rsrtTypes) {
				Optional<PropertyRestriction> optRestrictionType = propRestrRepo.findByRestrictionType(propRstr.trim());
				if (!optRestrictionType.isPresent()) {
					checkRstr.add(propRstr);
				}
			}
		}

		return checkRstr;
	}

	private List<PropertyRestriction> getExistsPropertyRestrictions(List<String> rsrtTypes) {
		List<PropertyRestriction> propRstrList = new ArrayList<>();
		if (rsrtTypes != null) {
			for (int i = 0; i < rsrtTypes.size(); i++) {
				PropertyRestriction propertyRestriction = propRestrRepo.findByRestrictionType(rsrtTypes.get(i)).get();
				propRstrList.add(propertyRestriction);
			}
		}
		return propRstrList;
	}

	@Override
	public Integer getLoanProductIdByProductName(String loanProductName) {
		String productName = StringNormalization.processSentance(loanProductName).toUpperCase().trim();

		LoanProduct loanProduct = loanProductRepo.findByProductName(productName)
				.orElseThrow(() -> new ResourceNotFoundException(
						String.format(AppConstants.LOAN_PROD_NOT_EXISTS_BY_NAME_MSG, loanProductName)));

		return loanProduct.getProductId();
	}

	@Override
	public List<LoanProdDto> searchLoanProducts(LoanProductSearchCriteria searchCriteria) {

		LoanProductSearchCriteria criteria = StringNormalization.searchCriteriaProcess(searchCriteria);

		Specification<LoanProduct> specification = Specification.where(null);

		if (criteria.getLoanTerm() != null) {
			specification = specification.and(LoanProductSpecifications.hasLoanTermLessThan(criteria.getLoanTerm()));
		}

		if (criteria.getCreditScore() != null) {
			specification = specification
					.and(LoanProductSpecifications.hasCreditScoreGreaterThan(criteria.getCreditScore()));
		}

		if (criteria.getPrivateMortgageInsurance() != null) {
			specification = specification
					.and(LoanProductSpecifications.hasPrivateMortgageInsurance(criteria.getPrivateMortgageInsurance()));
		}
		if (criteria.getCategoryTypes() != null) {

			for (String categortType : criteria.getCategoryTypes()) {

				specification = specification.and(LoanProductSpecifications.hasRestrictionCategoryType(categortType));
			}
		}
		if (criteria.getRestrictionTypes() != null) {
			for (String restrictionType : criteria.getRestrictionTypes()) {

				specification = specification.and(LoanProductSpecifications.hasRestrictionType(restrictionType));
			}
		}

		if (criteria.getPrepayPenality() != null) {
			specification = specification
					.and(LoanProductSpecifications.hasPrepayPenalityStatus(criteria.getPrepayPenality()));
		}

		if (criteria.getStatus() != null) {
			specification = specification.and(LoanProductSpecifications.hasStatus(criteria.getStatus()));
		}

		List<LoanProduct> loanProducts = loanProductRepo.findAll(specification);

		if (criteria.getEscrowRequirements() != null) {

			loanProducts = filterLoanProductsByEscrowRequirements(criteria.getEscrowRequirements(), loanProducts);

		}

		if (criteria.getCriterias() != null) {

			loanProducts = filterLoanProductsByUnderwritingCriterias(criteria.getCriterias(), loanProducts);
		}

		List<LoanProdDto> loanProdDtoList = new ArrayList<>();

		List<String> escrowRequirements = new ArrayList<>();
		
		List<String> criterias = new ArrayList<>();

		for (LoanProduct loanProd : loanProducts) {

			LoanProdDto loanProductDto = LoanProductMapper.loanProductToLoanproductDto(loanProd, new LoanProdDto());

			if (loanProd.getEscrowRequired()) {

				escrowRequirements = getEscrowRequirements(loanProd.getProductId());
				loanProductDto.setEscrowRequirements(escrowRequirements);

			}
			
			 List<UnderwritingCriteriaDto> criteriaDtoList = criteriaClient.fetchByLoanId(loanProd.getProductId()).getBody();
			 
			 if(criteriaDtoList!=null) {
				 
				 criterias = criteriaDtoList.stream().map(criteriaDto -> criteriaDto.getCriteriaName()).collect(Collectors.toList());
				 loanProductDto.setUnderwritingCriteria(criterias);				 
			 }

			loanProdDtoList.add(loanProductDto);
		}
		return loanProdDtoList;
	}

	private List<String> getEscrowRequirements(Integer productId) {

		return escrowClient.findByProdId(productId).getBody();

	}

	private List<LoanProduct> filterLoanProductsByUnderwritingCriterias(List<String> criterias,
			List<LoanProduct> loanProducts) {

		Set<Integer> productIdSet = criteriaClient.fetch(criterias).getBody();

		List<LoanProduct> loanProdList = new ArrayList<>();

		for (LoanProduct lProduct : loanProducts) {
			if (productIdSet.contains(lProduct.getProductId())) {
				loanProdList.add(lProduct);
			}
		}

		return loanProdList;
	}

	private List<LoanProduct> filterLoanProductsByEscrowRequirements(List<String> requirements,
			List<LoanProduct> loanProducts) {

		SearchByReqDto reqDto = new SearchByReqDto(requirements);
		ResponseEntity<List<Integer>> respEntityList = escrowClient.searchByReq(reqDto);
		List<Integer> productIdList = respEntityList.getBody();

		Set<Integer> productIdSet = new HashSet<>(productIdList);

		List<LoanProduct> loanProdList = new ArrayList<>();

		for (LoanProduct lProduct : loanProducts) {
			if (productIdSet.contains(lProduct.getProductId())) {
				loanProdList.add(lProduct);
			}
		}

		return loanProdList;
	}

	@Override
	public void addRestrictions(Integer productId, List<String> propRstr) {
		LoanProduct loanProduct = loanProductRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(AppConstants.LOAN_PROD_NOT_EXISTS_BY_ID_MSG, productId)));

		List<String> propRstrs = new ArrayList<>();

		if (propRstr != null) {
			propRstrs = propRstr.stream().map(rstr -> StringNormalization.processSentance(rstr)).toList();
		}
		List<String> restrictions = checkLoanProductRestrictions(propRstrs);

		if (restrictions.size() > 0) {
			throw new ResourceNotFoundException(String.format(AppConstants.PROP_RSTR_TYPES_NOT_EXIST, restrictions));
		}

		List<String> prodRstrs = new ArrayList<>();
		if (loanProduct.getPropertyRestrcitions() != null) {
			for (PropertyRestriction propRestr : loanProduct.getPropertyRestrcitions()) {
				prodRstrs.add(propRestr.getRestrictionType());
			}
		}
		System.err.println("adding list" + propRstrs);
		System.err.println(prodRstrs);
		List<String> existsRestrs = new ArrayList<>();

		for (String rstr : propRstrs) {

			if (prodRstrs.contains(rstr)) {
				existsRestrs.add(rstr);
			} else {
				prodRstrs.add(rstr);
			}

		}

		if (existsRestrs.size() > 0) {
			throw new ResourceAlreadyExistsException(String.format(AppConstants.RESTS_EXITS, existsRestrs, productId));
		}

		List<PropertyRestriction> existsRstr = getExistsPropertyRestrictions(prodRstrs);

		loanProduct.setPropertyRestrcitions(existsRstr);
		if (!loanProduct.getPropertyRestrictionExists()) {
			loanProduct.setPropertyRestrictionExists(AppConstants.STATUS_TRUE);
		}
		loanProductRepo.save(loanProduct);

	}

	@Override
	public void removeRestrictions(Integer productId, List<String> propRstrs) {
		LoanProduct loanProduct = loanProductRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(AppConstants.LOAN_PROD_NOT_EXISTS_BY_ID_MSG, productId)));

		if (propRstrs != null) {
			propRstrs = propRstrs.stream().map(m -> StringNormalization.processSentance(m)).toList();
		}
		List<String> restrictions = checkLoanProductRestrictions(propRstrs);

		if (restrictions.size() > 0) {
			throw new ResourceNotFoundException(String.format(AppConstants.PROP_RSTR_TYPES_NOT_EXIST, restrictions));
		}

		List<String> prodRstr = new ArrayList<>();
		if (loanProduct.getPropertyRestrcitions() != null) {
			prodRstr = loanProduct.getPropertyRestrcitions().stream().map(types -> types.getRestrictionType()).toList();
		}
		List<String> notExistRstr = new ArrayList<>();

		if (prodRstr != null) {
			for (String rstr : propRstrs) {
				if (!prodRstr.contains(rstr)) {
					notExistRstr.add(rstr);
				}
			}
		}
		if (notExistRstr.size() > 0) {
			throw new ResourceNotFoundException(
					String.format(AppConstants.PROP_RSTR_NOT_EXISTS, notExistRstr, productId.toString()));
		}

		List<String> restrictionTypes = new ArrayList<>();
		for (String rstr : prodRstr) {
			if (!propRstrs.contains(rstr)) {
				restrictionTypes.add(rstr);
			}
		}

		List<PropertyRestriction> existsRstr = getExistsPropertyRestrictions(restrictionTypes);

		if (existsRstr != null && existsRstr.size() != 0) {
			loanProduct.setPropertyRestrcitions(existsRstr);
			loanProductRepo.save(loanProduct);
		} else {
			loanProduct.setPropertyRestrictionExists(AppConstants.STATUS_FALSE);
			loanProduct.setPropertyRestrcitions(null);
			loanProductRepo.save(loanProduct);
		}

	}

	@Override
	public PrepayPenaltyStatusDto getPrepayPenaltyById(Integer prodId) {

		LoanProduct loanProd = loanProductRepo.findById(prodId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(AppConstants.LOAN_PROD_NOT_EXISTS_BY_ID_MSG, prodId)));

		Optional<PrepayPenalty> byId = prePayPenalityRepo.findById(prodId);

		PrepayPenalty prepayPenalty = byId.orElseThrow(
				() -> new ResourceNotFoundException(String.format(AppConstants.PREPAY_PENALTY_NOT_EXISTS_MSG, prodId)));

		PrepayPenaltyStatusDto prepayPenaltyStatusDto = PrepayPenaltyMapper
				.prepayPenaltyToPrepayPenaltyStatusDto(prepayPenalty, new PrepayPenaltyStatusDto());

		if (loanProd.getPrepayPenalty()) {

			prepayPenaltyStatusDto.setStatusDescription(String.format(AppConstants.PREPAY_PENALTY_ACTIVE_MSG, prodId));

			return prepayPenaltyStatusDto;

		}

		prepayPenaltyStatusDto.setStatusDescription(String.format(AppConstants.PREPAY_PENALTY_INACTIVE_MSG, prodId));

		return prepayPenaltyStatusDto;

	}
	@Override
	@Transactional
	public void removeEscrowRequirements(Integer productId, List<String> requirements) {

		LoanProduct loanProduct = loanProductRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(AppConstants.LOAN_PROD_NOT_FOUND_WITH_ID, productId.toString())));
		List<String> escrowRequirements = new ArrayList<>();
		if (requirements.isEmpty() || requirements == null) {
			throw new NullPointerException(AppConstants.RESOURCE_EMPTY_OR_NULL);
		}
		requirements.forEach(req -> escrowRequirements.add(StringNormalization.processSentance(req)));

		if (!loanProduct.getEscrowRequired()) {
			throw new ResourceNotFoundException(
					String.format(AppConstants.ESCROW_REQ_NOT_EXISTS, loanProduct.getProductName()));
		}
		deleteEscrowRequirements(productId, escrowRequirements);
	}

	private void deleteEscrowRequirements(Integer productId, List<String> escrowRequirements) {

		ResponseEntity<List<String>> responseEntityReqs = escrowClient.findByProdId(productId);

		List<String> escrowReqList = responseEntityReqs.getBody();

		Set<String> missingRequirements = new HashSet<>();

		for (String requirement : escrowRequirements) {

			if (!escrowReqList.contains(requirement)) {
				missingRequirements.add(requirement);

			}
		}

		if (!missingRequirements.isEmpty()) {

			throw new ResourceNotFoundException(
					String.format(EscrowConstants.ESCR_REQ_NOT_EXISTS_MSG, missingRequirements));
		}

		LoanReqDto loanReqDto = new LoanReqDto(productId, escrowRequirements);

		LoanProduct loanProduct = loanProductRepo.findById(productId).get();

		System.err.println("get" + escrowReqList);
		System.err.println("send" + escrowRequirements);
		if (escrowReqList.size() == escrowRequirements.size()) {
			loanProduct.setEscrowRequired(AppConstants.STATUS_FALSE);
			loanProductRepo.save(loanProduct);
		}
		escrowClient.deleteLoanReq(loanReqDto);

	}

}

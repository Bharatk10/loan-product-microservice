package com.zettamine.mpa.lpm.service;

import java.util.List;

import com.zettamine.mpa.lpm.entity.LoanProduct;
import com.zettamine.mpa.lpm.model.LoanProdDto;
import com.zettamine.mpa.lpm.model.LoanProductDto;
import com.zettamine.mpa.lpm.model.LoanProductSearchCriteria;
import com.zettamine.mpa.lpm.model.PrePayPenalityDto;
import com.zettamine.mpa.lpm.model.PrepayPenaltyStatusDto;
import com.zettamine.mpa.lpm.model.ProductStatusHistoryDto;

import jakarta.validation.Valid;

public interface ILoanProductService {

	/**
	 * 
	 * @param productDto
	 */
	public void create(LoanProductDto productDto);
	
	public LoanProdDto getbyId(Integer productId);
	
	public void updateLoanProductStatus(Integer productId);
	
	public List<ProductStatusHistoryDto> getProductStatusHistory(Integer productId);
	
	public void updatePrePayPenalityStatus(Integer productId);
	
	public void updatePrePayPenality(PrePayPenalityDto penalityDto);

	public void createPrePayPenality(PrePayPenalityDto penalityDto);
	
	public void updateLoanProduct(Integer productId, LoanProductDto loanProductDto);
	
	public List<LoanProdDto> searchLoanProducts(LoanProductSearchCriteria criteria);

	public Integer getLoanProductIdByProductName(String loanProductName);
	
	public Boolean updateLoanStatusHistory(Integer productId);
	
	public void removeRestrictions(Integer productId, List<String> propRstrs);
	
	public void addRestrictions(Integer productId, List<String> propRstr);
	
	public PrepayPenaltyStatusDto getPrepayPenaltyById(Integer prodId);
	
	public void addEscrowRequirementsToLoanProduct(Integer productId, List<String> escrowRequirements);
	
	public void addUnderWritingCriteriaToLoanProduct(Integer productId, List<String> underwritingCriteria);
	
	public void removeEscrowRequirements(Integer productId, List<String> requirements);
	
}

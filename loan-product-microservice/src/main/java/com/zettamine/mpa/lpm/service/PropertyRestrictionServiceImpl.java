package com.zettamine.mpa.lpm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zettamine.mpa.lpm.constants.AppConstants;
import com.zettamine.mpa.lpm.entity.PropertyRestriction;
import com.zettamine.mpa.lpm.entity.PropertyRestrictionCategory;
import com.zettamine.mpa.lpm.exception.MismatchFoundException;
import com.zettamine.mpa.lpm.exception.ResourceAlreadyExistsException;
import com.zettamine.mpa.lpm.exception.ResourceNotFoundException;
import com.zettamine.mpa.lpm.mapper.PropertyRestrictionMapper;
import com.zettamine.mpa.lpm.model.PropertyRestrictionDto;
import com.zettamine.mpa.lpm.repository.PropertyRestrictionRepository;
import com.zettamine.mpa.lpm.repository.PropertyRestrictionCategoryRepository;
import com.zettamine.mpa.lpm.util.RestrictionTypeReq;
import com.zettamine.mpa.lpm.util.StringNormalization;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PropertyRestrictionServiceImpl implements IPropertyRestrictionService {

	private PropertyRestrictionRepository propRestrRepo;
	private PropertyRestrictionCategoryRepository propRestrCatgRepo;

	@Override
	public void createPropertyRestriction(PropertyRestrictionDto dto) {
		PropertyRestrictionDto propertyRestrictionDto = StringNormalization.processPropRestDto(dto);

		PropertyRestrictionCategory propRestCat = propRestrCatgRepo
				.findByCategoryType(propertyRestrictionDto.getCategoryType())
				.orElseThrow(() -> new ResourceNotFoundException(String.format(AppConstants.PROP_RSTR_CATG_NOT_EXISTS_MSG,propertyRestrictionDto.getCategoryType())));

		Optional<PropertyRestriction> optRstrType = propRestrRepo
				.findByRestrictionType(propertyRestrictionDto.getRestrictionType());

		if (optRstrType.isPresent()) {
			throw new ResourceAlreadyExistsException(
					String.format(AppConstants.PROP_RSTR_EXISTS_MSG, propertyRestrictionDto.getRestrictionType()));
		} else {
			PropertyRestriction propRstr = PropertyRestrictionMapper
					.propertyRestrictionDtoToPropertyRestriction(new PropertyRestriction(), propertyRestrictionDto);
			propRstr.setRestrictionCategory(propRestCat);
			propRstr.setStatus(AppConstants.ACTIVE);
			propRestrRepo.save(propRstr);
		}
	}

	@Override
	public void updatePropertyRestriction(PropertyRestrictionDto propRestDto, Integer restrictionId) {

		PropertyRestrictionDto propertyRestrictionDto = StringNormalization.processPropRestDto(propRestDto);

		PropertyRestriction propRstr = propRestrRepo.findById(restrictionId)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(AppConstants.PROP_RSTR_NOT_EXISTS_BY_ID_MSG, restrictionId)));

		PropertyRestrictionCategory propRestCat = propRestrCatgRepo
				.findByCategoryType(propertyRestrictionDto.getCategoryType())
				.orElseThrow(() -> new ResourceNotFoundException(String.format(AppConstants.PROP_RSTR_CATG_NOT_EXISTS_MSG,propertyRestrictionDto.getCategoryType())));

		if (propertyRestrictionDto.getCategoryType().equalsIgnoreCase(propRstr.getRestrictionCategory().getCategoryType())) {

			if (propertyRestrictionDto.getRestrictionType().equalsIgnoreCase(propRstr.getRestrictionType())) {

				PropertyRestriction proRstrSave = PropertyRestrictionMapper
						.propertyRestrictionDtoToPropertyRestriction(propRstr, propertyRestrictionDto);

				propRestrRepo.save(proRstrSave);
			} else {

				throw new MismatchFoundException(AppConstants.PROP_RSTR_TYPE,
						propertyRestrictionDto.getRestrictionType(), AppConstants.PROP_RSTR);
			}
		} else {

			throw new MismatchFoundException(AppConstants.PROP_CATG_TYPE, propertyRestrictionDto.getCategoryType(),
					AppConstants.PROP_RSTR_CATG);
		}
	}

	@Override
	public List<PropertyRestrictionDto> getAllPropertyRestriction() {
		List<PropertyRestriction> allRestrictions = propRestrRepo.findAll();
		List<PropertyRestrictionDto> list = allRestrictions.stream().map(m -> PropertyRestrictionMapper
				.propertyRestrictioToPropertyRestrictionDto(new PropertyRestrictionDto(), m)).toList();
		return list;
	}

	@Override
	public PropertyRestrictionDto getPropertyRestrictionById(Integer restrictionId) {
		PropertyRestriction propRstr = propRestrRepo.findById(restrictionId)
				.orElseThrow(() ->  new ResourceNotFoundException(String.format(AppConstants.PROP_RSTR_NOT_EXISTS_BY_ID_MSG, restrictionId)));
		PropertyRestrictionDto restrictionDto = PropertyRestrictionMapper
				.propertyRestrictioToPropertyRestrictionDto(new PropertyRestrictionDto(), propRstr);
		return restrictionDto;
	}

	@Override
	public void deletePropertyRestrictionById(Integer restrictionId) {

		PropertyRestriction propRstr = propRestrRepo.findById(restrictionId)
				.orElseThrow(() ->  new ResourceNotFoundException(String.format(AppConstants.PROP_RSTR_NOT_EXISTS_BY_ID_MSG, restrictionId)));

		propRstr.setStatus(AppConstants.INACTIVE);
		propRestrRepo.save(propRstr);
	}

	@Override
	public List<String> getAllRestrictionTypes() {
		
		return propRestrRepo.findAllRestrictionTypes();
		
	}
	
	@Override
	public void updateRestrictionType(Integer restrictionId, RestrictionTypeReq restrictionType) {

		PropertyRestriction propertyRestriction = propRestrRepo.findById(restrictionId)
				.orElseThrow(() -> new ResourceNotFoundException(
						String.format(AppConstants.PROP_RSTR_NOT_EXISTS_BY_ID_MSG, restrictionId.toString())));
		String updateRestriction = StringNormalization.processSentance(restrictionType.getRestrictionType());
		if (restrictionType.getRestrictionType() == null) {
			throw new NullPointerException(String.format(AppConstants.RESOURCE_EMPTY_OR_NULL,
					restrictionType.getRestrictionType().toString()));
		}

		Optional<PropertyRestriction> optRestriction = propRestrRepo.findByRestrictionType(updateRestriction);

		if (optRestriction.isPresent()) {

			throw new ResourceNotFoundException(
					String.format(AppConstants.PROP_RSTR_TYPE_ALREADY_EXISTS, updateRestriction));
		}

		propertyRestriction.setRestrictionType(updateRestriction);

		propRestrRepo.save(propertyRestriction);

	}

}
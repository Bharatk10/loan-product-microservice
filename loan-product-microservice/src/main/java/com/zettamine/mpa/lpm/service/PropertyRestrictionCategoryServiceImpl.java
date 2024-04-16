package com.zettamine.mpa.lpm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zettamine.mpa.lpm.constants.AppConstants;
import com.zettamine.mpa.lpm.entity.PropertyRestrictionCategory;
import com.zettamine.mpa.lpm.exception.MismatchFoundException;
import com.zettamine.mpa.lpm.exception.ResourceAlreadyExistsException;
import com.zettamine.mpa.lpm.exception.ResourceNotFoundException;
import com.zettamine.mpa.lpm.mapper.PropertyRestrictionCategoryMapper;
import com.zettamine.mpa.lpm.model.PropertyRestrictionCategoryDto;
import com.zettamine.mpa.lpm.repository.PropertyRestrictionCategoryRepository;
import com.zettamine.mpa.lpm.util.StringNormalization;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PropertyRestrictionCategoryServiceImpl implements IPropertyRestrictionCategoryService {

	private PropertyRestrictionCategoryRepository propRestrCategoryRepo;

	@Override
	public void create(PropertyRestrictionCategoryDto categoryDto) {

		PropertyRestrictionCategoryDto propCategoryDto = StringNormalization.processPropRestrCatgDto(categoryDto);

		PropertyRestrictionCategory propRestCatgegory = PropertyRestrictionCategoryMapper
				.RestrictionCategoryDtoToRestrictionCategory(new PropertyRestrictionCategory(), propCategoryDto);

		Optional<PropertyRestrictionCategory> optPropRestCatg = propRestrCategoryRepo
				.findByCategoryType(propRestCatgegory.getCategoryType());

		if (optPropRestCatg.isPresent()) {
			

			throw new ResourceAlreadyExistsException(
					String.format(AppConstants.PROP_RSTR_CATG_EXISTS_MSG, propCategoryDto.getCategoryType()));
		}

		propRestrCategoryRepo.save(propRestCatgegory);

	}

	@Override
	public PropertyRestrictionCategoryDto getById(Integer categoryId) {

		PropertyRestrictionCategory propCategory = propRestrCategoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(AppConstants.PROP_RSTR_CATG_NOT_EXISTS_BY_ID_MSG, categoryId.toString())));

		PropertyRestrictionCategoryDto propRestCatgegoryDto = PropertyRestrictionCategoryMapper
				.RestrictionCategoryToRestrictionCategoryDto(propCategory, new PropertyRestrictionCategoryDto());

		return propRestCatgegoryDto;
	}

	@Override
	public List<PropertyRestrictionCategoryDto> getAll() {
		return null;
	}

	@Override
	public void update(PropertyRestrictionCategoryDto categoryDto, Integer categoryId) {

		PropertyRestrictionCategoryDto propCategoryDto = StringNormalization.processPropRestrCatgDto(categoryDto);

		PropertyRestrictionCategory propRestCatgegory = PropertyRestrictionCategoryMapper
				.RestrictionCategoryDtoToRestrictionCategory(new PropertyRestrictionCategory(), propCategoryDto);

		PropertyRestrictionCategory propCategory = propRestrCategoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(AppConstants.PROP_RSTR_CATG_NOT_EXISTS_BY_ID_MSG, categoryId.toString())));

		propRestrCategoryRepo.findByCategoryType(propRestCatgegory.getCategoryType())
				.orElseThrow(() -> new ResourceNotFoundException(String.format(AppConstants.PROP_RSTR_CATG_NOT_EXISTS_MSG,propCategory.getCategoryType())));

		if (!(propRestCatgegory.getCategoryType()).equalsIgnoreCase(propCategory.getCategoryType())) {

			throw new MismatchFoundException(AppConstants.PROP_RSTR_CATG, AppConstants.PROP_CATG_TYPE,
					AppConstants.PROP_CATG_ID);
		}

		propRestCatgegory.setCategoryId(categoryId);

		propRestrCategoryRepo.save(propRestCatgegory);

	}


	@Override
	public void updateCategoryType(String categoryType, Integer categoryId) {
		
		PropertyRestrictionCategory propCategory = propRestrCategoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(AppConstants.PROP_RSTR_CATG_NOT_EXISTS_BY_ID_MSG, categoryId.toString())));

		String processedCategoryType = StringNormalization.processSentance(categoryType);
		
		Optional<PropertyRestrictionCategory> byCategoryType = propRestrCategoryRepo.findByCategoryType(processedCategoryType);
		
		if(byCategoryType.isPresent()) {
			
			throw new ResourceAlreadyExistsException(String.format(AppConstants.PROP_RSTR_CATG_EXISTS_MSG,processedCategoryType));
		}
		
		propCategory.setCategoryType(processedCategoryType);
		
		propRestrCategoryRepo.save(propCategory);
		
		
	}

	@Override
	public void delete(Integer categoryId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> findAllCategoryTypes() {
	
		
		return propRestrCategoryRepo.findCategoryTypes();
	}

}
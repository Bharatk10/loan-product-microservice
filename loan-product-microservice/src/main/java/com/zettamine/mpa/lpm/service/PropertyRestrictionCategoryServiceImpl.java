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

	 /**
    * Creates a new property restriction category based on the provided DTO.
    *
    * @param categoryDto The DTO containing information about the property restriction category to be created.
    * @throws ResourceAlreadyExistsException If the property restriction category already exists.
    */
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
	   /**
     * Retrieves a property restriction category by its ID.
     *
     * @param categoryId The ID of the property restriction category to retrieve.
     * @return The DTO representing the property restriction category.
     * @throws ResourceNotFoundException If the property restriction category does not exist.
     */
	@Override
	public PropertyRestrictionCategoryDto getById(Integer categoryId) {

		PropertyRestrictionCategory propCategory = propRestrCategoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(AppConstants.PROP_RSTR_CATG_NOT_EXISTS_BY_ID_MSG, categoryId.toString())));

		PropertyRestrictionCategoryDto propRestCatgegoryDto = PropertyRestrictionCategoryMapper
				.RestrictionCategoryToRestrictionCategoryDto(propCategory, new PropertyRestrictionCategoryDto());

		return propRestCatgegoryDto;
	}

	 /**
     * Updates an existing property restriction category identified by its ID.
     *
     * @param categoryDto The DTO containing updated information about the property restriction category.
     * @param categoryId The ID of the property restriction category to update.
     * @throws ResourceNotFoundException If the property restriction category or its corresponding category type does not exist.
     * @throws MismatchFoundException If there is a mismatch between the category type or category ID.
     */

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

	 /**
    * Updates the category type of a property restriction category.
    *
    * @param categoryType The new category type.
    * @param categoryId The ID of the property restriction category to update.
    * @throws ResourceNotFoundException If the property restriction category does not exist.
    * @throws ResourceAlreadyExistsException If the new category type already exists.
    */
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
	   /**
     * Retrieves all property restriction categories.
     *
     * @return A list of DTOs representing all property restriction categories.
     */

	@Override
	public List<String> findAllCategoryTypes() {
	
		
		return propRestrCategoryRepo.findCategoryTypes();
	}

}
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

	/**
	 * Creates a new property restriction based on the provided DTO.
	 *
	 * @param dto The DTO containing information about the property restriction to
	 *            be created.
	 * @throws ResourceNotFoundException      If the corresponding property
	 *                                        restriction category does not exist.
	 * @throws ResourceAlreadyExistsException If the property restriction already
	 *                                        exists.
	 */
	@Override
	public void createPropertyRestriction(PropertyRestrictionDto dto) {
		PropertyRestrictionDto propertyRestrictionDto = StringNormalization.processPropRestDto(dto);

		PropertyRestrictionCategory propRestCat = propRestrCatgRepo
				.findByCategoryType(propertyRestrictionDto.getCategoryType())
				.orElseThrow(() -> new ResourceNotFoundException(String
						.format(AppConstants.PROP_RSTR_CATG_NOT_EXISTS_MSG, propertyRestrictionDto.getCategoryType())));

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

	/**
	 * Updates an existing property restriction identified by its ID.
	 *
	 * @param propRestDto   The DTO containing updated information about the
	 *                      property restriction.
	 * @param restrictionId The ID of the property restriction to be updated.
	 * @throws ResourceNotFoundException If the property restriction or its category
	 *                                   does not exist.
	 * @throws MismatchFoundException    If there is a mismatch between the category
	 *                                   type or restriction type.
	 */
	@Override
	public void updatePropertyRestriction(PropertyRestrictionDto propRestDto, Integer restrictionId) {

		PropertyRestrictionDto propertyRestrictionDto = StringNormalization.processPropRestDto(propRestDto);

		PropertyRestriction propRstr = propRestrRepo.findById(restrictionId)
				.orElseThrow(() -> new ResourceNotFoundException(
						String.format(AppConstants.PROP_RSTR_NOT_EXISTS_BY_ID_MSG, restrictionId)));

		PropertyRestrictionCategory propRestCat = propRestrCatgRepo
				.findByCategoryType(propertyRestrictionDto.getCategoryType())
				.orElseThrow(() -> new ResourceNotFoundException(String
						.format(AppConstants.PROP_RSTR_CATG_NOT_EXISTS_MSG, propertyRestrictionDto.getCategoryType())));

		if (propertyRestrictionDto.getCategoryType()
				.equalsIgnoreCase(propRstr.getRestrictionCategory().getCategoryType())) {

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

	/**
	 * Retrieves all property restrictions.
	 *
	 * @return A list of DTOs representing all property restrictions.
	 */
	@Override
	public List<PropertyRestrictionDto> getAllPropertyRestriction() {
		List<PropertyRestriction> allRestrictions = propRestrRepo.findAll();
		List<PropertyRestrictionDto> list = allRestrictions.stream().map(m -> PropertyRestrictionMapper
				.propertyRestrictioToPropertyRestrictionDto(new PropertyRestrictionDto(), m)).toList();
		return list;
	}

	/**
	 * Retrieves a property restriction by its ID.
	 *
	 * @param restrictionId The ID of the property restriction to retrieve.
	 * @return The DTO representing the property restriction.
	 * @throws ResourceNotFoundException If the property restriction does not exist.
	 */

	@Override
	public PropertyRestrictionDto getPropertyRestrictionById(Integer restrictionId) {
		PropertyRestriction propRstr = propRestrRepo.findById(restrictionId)
				.orElseThrow(() -> new ResourceNotFoundException(
						String.format(AppConstants.PROP_RSTR_NOT_EXISTS_BY_ID_MSG, restrictionId)));
		PropertyRestrictionDto restrictionDto = PropertyRestrictionMapper
				.propertyRestrictioToPropertyRestrictionDto(new PropertyRestrictionDto(), propRstr);
		return restrictionDto;
	}

	/**
	 * Deletes a property restriction by its ID.
	 *
	 * @param restrictionId The ID of the property restriction to delete.
	 * @throws ResourceNotFoundException If the property restriction does not exist.
	 */
	@Override
	public void deletePropertyRestrictionById(Integer restrictionId) {

		PropertyRestriction propRstr = propRestrRepo.findById(restrictionId)
				.orElseThrow(() -> new ResourceNotFoundException(
						String.format(AppConstants.PROP_RSTR_NOT_EXISTS_BY_ID_MSG, restrictionId)));

		propRstr.setStatus(AppConstants.INACTIVE);
		propRestrRepo.save(propRstr);
	}

	/**
	 * Retrieves all restriction types.
	 *
	 * @return A list of all restriction types.
	 */
	@Override
	public List<String> getAllRestrictionTypes() {

		return propRestrRepo.findAllRestrictionTypes();

	}

	/**
	 * Updates the restriction type of a property restriction.
	 *
	 * @param restrictionId   The ID of the property restriction to update.
	 * @param restrictionType The new restriction type.
	 * @throws ResourceNotFoundException If the property restriction does not exist.
	 * @throws NullPointerException      If the provided restriction type is null.
	 * @throws ResourceNotFoundException If the new restriction type already exists.
	 */
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
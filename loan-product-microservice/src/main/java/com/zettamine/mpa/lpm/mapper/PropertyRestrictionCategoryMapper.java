package com.zettamine.mpa.lpm.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.zettamine.mpa.lpm.entity.PropertyRestrictionCategory;
import com.zettamine.mpa.lpm.model.PropertyRestrictionCategoryDto;
import com.zettamine.mpa.lpm.model.PropertyRestrictionDto;

public class PropertyRestrictionCategoryMapper {

	public static PropertyRestrictionCategoryDto RestrictionCategoryToRestrictionCategoryDto(

			PropertyRestrictionCategory restrictionCategory, PropertyRestrictionCategoryDto restrictionCategoryDto) {
		
		restrictionCategoryDto.setCategoryId(restrictionCategory.getCategoryId());

		restrictionCategoryDto.setCategoryType(restrictionCategory.getCategoryType());

		restrictionCategoryDto.setCategoryDescription(restrictionCategory.getCategoryDescription());
		
		List<PropertyRestrictionDto> propRestDto = restrictionCategory.getPropertyRestrictions().stream().map(
				prop->PropertyRestrictionMapper.propertyRestrictioToPropertyRestrictionDto(new PropertyRestrictionDto(), prop))
				.collect(Collectors.toList());
		
		restrictionCategoryDto.setPropertyRestrictions(propRestDto);

		return restrictionCategoryDto;

	}

	public static PropertyRestrictionCategory RestrictionCategoryDtoToRestrictionCategory(

			PropertyRestrictionCategory restrictionCategory, PropertyRestrictionCategoryDto restrictionCategoryDto) {

		restrictionCategory.setCategoryType(restrictionCategoryDto.getCategoryType());

		restrictionCategory.setCategoryDescription(restrictionCategoryDto.getCategoryDescription());

		return restrictionCategory;

	}

}

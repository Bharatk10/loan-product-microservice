package com.zettamine.mpa.lpm.mapper;

import com.zettamine.mpa.lpm.entity.PropertyRestriction;
import com.zettamine.mpa.lpm.model.PropertyRestrictionDto;

public class PropertyRestrictionMapper {

	public static PropertyRestriction propertyRestrictionDtoToPropertyRestriction(
			PropertyRestriction propertyRestriction, PropertyRestrictionDto propertyRestrictionDto) {

		propertyRestriction.setRestrictionDescription(propertyRestrictionDto.getRestrictionDescription());

		propertyRestriction.setRestrictionType(propertyRestrictionDto.getRestrictionType());

		//propertyRestriction.setRestrictionCategory(propertyRestrictionDto.get);

		return propertyRestriction;
	}

	public static PropertyRestrictionDto propertyRestrictioToPropertyRestrictionDto(
			PropertyRestrictionDto propertyRestrictionDto, PropertyRestriction propertyRestriction) {
		
		propertyRestrictionDto.setRestrictionId(propertyRestriction.getRestrictionId());
		propertyRestrictionDto.setCategoryType(propertyRestriction.getRestrictionCategory().getCategoryType());

		propertyRestrictionDto.setRestrictionDescription(propertyRestriction.getRestrictionDescription());

		propertyRestrictionDto.setRestrictionType(propertyRestriction.getRestrictionType());

		return propertyRestrictionDto;
	}
}
package com.zettamine.mpa.lpm.service;

import java.util.List;

import com.zettamine.mpa.lpm.model.PropertyRestrictionDto;
import com.zettamine.mpa.lpm.util.RestrictionTypeReq;

public interface IPropertyRestrictionService {

	/**
	 * 
	 * @param propertyRestrictionDto
	 */
	public void createPropertyRestriction(PropertyRestrictionDto propertyRestrictionDto);

	/**
	 * 
	 * @param propertyRestrictionDto
	 * @param restrictionId
	 */
	public void updatePropertyRestriction(PropertyRestrictionDto propertyRestrictionDto, Integer restrictionId);

	/**
	 * 
	 * @return List<PropertyRestrictionDto>
	 */
	public List<PropertyRestrictionDto> getAllPropertyRestriction();

	/**
	 * 
	 * @param restrictionId
	 * @return PropertyRestrictionDto
	 */
	public PropertyRestrictionDto getPropertyRestrictionById(Integer restrictionId);

	/**
	 * 
	 * @param restrictionId
	 */
	public void deletePropertyRestrictionById(Integer restrictionId);
	/**
	 * 
	 * @return List<String>
	 */
	public List<String> getAllRestrictionTypes();
	
	/**
	 * 
	 * @param restrictionId
	 * @param restrictionType
	 */
	public void updateRestrictionType(Integer restrictionId, RestrictionTypeReq restrictionType);
}
package com.zettamine.mpa.lpm.service;

import java.util.List;

import com.zettamine.mpa.lpm.model.PropertyRestrictionCategoryDto;

public interface IPropertyRestrictionCategoryService {

	/**
	 * 
	 * @param categoryDto
	 */
	public void create(PropertyRestrictionCategoryDto categoryDto);
	
	/**
	 * 
	 * @param categoryId
	 * @return PropertyRestrictionCategoryDto
	 */
	public PropertyRestrictionCategoryDto getById(Integer categoryId);
	
	/**
	 *
	 * @return List<PropertyRestrictionCategoryDto>
	 */
	public List<PropertyRestrictionCategoryDto> getAll();
	
	/**
	 * 
	 * @param categoryId
	 */
	public void update(PropertyRestrictionCategoryDto categoryDto,Integer categoryId);
	
	/**
	 * 
	 * @param categoryId
	 */
	public void delete(Integer categoryId);
	
	/**
	 * 
	 * @return
	 */
	public List<String> findAllCategoryTypes();
	
	/**
	 * 
	 * @param categoryType
	 * @param categoryId
	 */
	public void updateCategoryType(String categoryType, Integer categoryId);
	
}

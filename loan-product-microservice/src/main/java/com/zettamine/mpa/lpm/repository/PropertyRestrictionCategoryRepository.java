package com.zettamine.mpa.lpm.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zettamine.mpa.lpm.entity.PropertyRestrictionCategory;

/**
 * This interface represents a repository for managing
 * PropertyRestrictionCategory entities, which define categories of property
 * restrictions. It extends JpaRepository to inherit basic CRUD (Create, Read,
 * Update, Delete) operations.
 */
public interface PropertyRestrictionCategoryRepository
		extends JpaRepository<PropertyRestrictionCategory, Serializable> {

	/**
	 * Retrieve a PropertyRestrictionCategory by category type.
	 *
	 * @param categoryType the type of property restriction category to search for
	 * @return an Optional containing the PropertyRestrictionCategory if found, or
	 *         empty if not found
	 */
	Optional<PropertyRestrictionCategory> findByCategoryType(String categoryType);

	/**
	 * Retrieve a list of all category types.
	 *
	 * @return a List containing all category types of property restriction
	 *         categories
	 */
	@Query(value = "select c.catg_type from prop_rstr_catg c", nativeQuery = true)
	List<String> findCategoryTypes();

}

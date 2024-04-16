package com.zettamine.mpa.lpm.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zettamine.mpa.lpm.entity.PropertyRestrictionCategory;

public interface PropertyRestrictionCategoryRepository extends JpaRepository<PropertyRestrictionCategory, Serializable> {

	Optional<PropertyRestrictionCategory> findByCategoryType(String categoryType);
	
	@Query(value="select c.catg_type from prop_rstr_catg c",nativeQuery = true)
	List<String> findCategoryTypes();

}

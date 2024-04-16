package com.zettamine.mpa.lpm.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zettamine.mpa.lpm.entity.PropertyRestriction;

@Repository
public interface PropertyRestrictionRepository extends JpaRepository<PropertyRestriction, Serializable> {

	Optional<PropertyRestriction> findByRestrictionType(String restrictionType);
	
	@Query(value = "select r.rstr_type from prop_rstr r",nativeQuery = true)
	List<String> findAllRestrictionTypes();


}

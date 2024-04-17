package com.zettamine.mpa.lpm.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zettamine.mpa.lpm.entity.PropertyRestriction;

/**
 * This interface represents a repository for managing PropertyRestriction
 * entities, which define various types of property restrictions. It extends
 * JpaRepository to inherit basic CRUD (Create, Read, Update, Delete)
 * operations.
 */
@Repository
public interface PropertyRestrictionRepository extends JpaRepository<PropertyRestriction, Serializable> {

	/**
	 * Retrieve a PropertyRestriction by restriction type.
	 *
	 * @param restrictionType the type of property restriction to search for
	 * @return an Optional containing the PropertyRestriction if found, or empty if
	 *         not found
	 */
	Optional<PropertyRestriction> findByRestrictionType(String restrictionType);

	/**
	 * Retrieve a list of all restriction types.
	 *
	 * @return a List containing all restriction types of property restrictions
	 */
	@Query(value = "select r.rstr_type from prop_rstr r", nativeQuery = true)
	List<String> findAllRestrictionTypes();

}

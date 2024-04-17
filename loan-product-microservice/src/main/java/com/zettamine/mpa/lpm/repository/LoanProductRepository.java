package com.zettamine.mpa.lpm.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.zettamine.mpa.lpm.entity.LoanProduct;


/**
 * This interface represents a repository for managing LoanProduct entities.
 * It extends JpaRepository and JpaSpecificationExecutor to inherit basic CRUD (Create, Read, Update, Delete) operations
 * and support for dynamic query execution.
 */
public interface LoanProductRepository extends JpaRepository<LoanProduct, Serializable>,JpaSpecificationExecutor<LoanProduct>{

    /**
     * Retrieve a LoanProduct by its product name.
     *
     * @param productName the name of the loan product to search for
     * @return an Optional containing the LoanProduct if found, or empty if not found
     */
	Optional<LoanProduct> findByProductName(String productName);

    /**
     * Retrieve a list of LoanProducts by their IDs.
     *
     * @param productIdList a list of IDs of loan products to retrieve
     * @return a List containing the LoanProducts matching the provided IDs
     */
	@Query(value = "select * from loan_prod where id in :productIdList",nativeQuery = true)
	List<LoanProduct> findAllById(List<Integer> productIdList);
	
	
}

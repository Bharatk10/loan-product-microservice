package com.zettamine.mpa.lpm.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.zettamine.mpa.lpm.entity.LoanProduct;

public interface LoanProductRepository extends JpaRepository<LoanProduct, Serializable>,JpaSpecificationExecutor<LoanProduct>{

	Optional<LoanProduct> findByProductName(String productName);
	
	@Query(value = "select * from loan_prod where id in :productIdList",nativeQuery = true)
	List<LoanProduct> findAllById(List<Integer> productIdList);
	
	
}

package com.zettamine.mpa.lpm.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zettamine.mpa.lpm.entity.LoanProductStatusHistoryKey;
import com.zettamine.mpa.lpm.entity.ProductStatusHistory;

public interface LoanProductStatusHistoryRepsitory extends JpaRepository<ProductStatusHistory,LoanProductStatusHistoryKey>{
	
	@Query(value ="select * from loan_prod_status_history h where h.loan_product_id = :productId and h.end_dt = :endDate",nativeQuery = true)
	Optional<ProductStatusHistory> findByProductIdAndEndDate(Integer productId,LocalDateTime endDate);
	
	@Query(value ="select * from loan_prod_status_history h where h.loan_product_id = :productId",nativeQuery = true)
	List<ProductStatusHistory> findByProductId(Integer productId);

}

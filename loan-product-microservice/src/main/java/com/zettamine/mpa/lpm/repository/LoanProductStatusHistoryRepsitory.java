package com.zettamine.mpa.lpm.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zettamine.mpa.lpm.entity.LoanProductStatusHistoryKey;
import com.zettamine.mpa.lpm.entity.ProductStatusHistory;

/**
 * This interface represents a repository for managing ProductStatusHistory
 * entities related to loan product statuses. It extends JpaRepository to
 * inherit basic CRUD (Create, Read, Update, Delete) operations.
 */
public interface LoanProductStatusHistoryRepsitory
		extends JpaRepository<ProductStatusHistory, LoanProductStatusHistoryKey> {
	/**
	 * Retrieve a ProductStatusHistory by loan product ID and end date.
	 *
	 * @param productId the ID of the loan product
	 * @param endDate   the end date of the status history
	 * @return an Optional containing the ProductStatusHistory if found, or empty if
	 *         not found
	 */
	@Query(value = "select * from loan_prod_status_history h where h.loan_product_id = :productId and h.end_dt = :endDate", nativeQuery = true)
	Optional<ProductStatusHistory> findByProductIdAndEndDate(Integer productId, LocalDateTime endDate);

	/**
	 * Retrieve a list of ProductStatusHistory entries by loan product ID.
	 *
	 * @param productId the ID of the loan product
	 * @return a List containing the ProductStatusHistory entries for the specified
	 *         loan product
	 */
	@Query(value = "select * from loan_prod_status_history h where h.loan_product_id = :productId", nativeQuery = true)
	List<ProductStatusHistory> findByProductId(Integer productId);

}

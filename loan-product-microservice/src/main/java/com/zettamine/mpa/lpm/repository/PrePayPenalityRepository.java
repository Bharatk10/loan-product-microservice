package com.zettamine.mpa.lpm.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zettamine.mpa.lpm.entity.LoanProduct;
import com.zettamine.mpa.lpm.entity.PrepayPenalty;

/**
 * This interface represents a repository for managing PrepayPenalty entities
 * related to loan product prepayment penalties. It extends JpaRepository to
 * inherit basic CRUD (Create, Read, Update, Delete) operations.
 */
public interface PrePayPenalityRepository extends JpaRepository<PrepayPenalty, Serializable> {

	/**
	 * Retrieve a LoanProduct by product ID and status.
	 *
	 * @param productId the ID of the loan product
	 * @param active    the status of the loan product (usually represented by a
	 *                  character, e.g., 'Y' for active)
	 * @return an Optional containing the LoanProduct if found, or empty if not
	 *         found
	 */
	Optional<LoanProduct> findByProductIdAndStatus(Integer productId, Character active);

}

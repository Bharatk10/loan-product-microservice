package com.zettamine.mpa.lpm.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zettamine.mpa.lpm.entity.LoanProduct;
import com.zettamine.mpa.lpm.entity.PrepayPenalty;

public interface PrePayPenalityRepository extends JpaRepository<PrepayPenalty,Serializable> {

	

	Optional<LoanProduct> findByProductIdAndStatus(Integer productId, Character active);

}

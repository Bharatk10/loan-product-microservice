package com.zettamine.mpa.lpm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "loan_prod_status_history")
@Setter
@Getter
@IdClass(LoanProductStatusHistoryKey.class)
@NoArgsConstructor
public class ProductStatusHistory {

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loan_product_id")
	private LoanProduct loanProduct;

	@Id
	@Column(name = "start_date")
	private LocalDateTime startDate;

	@Column(name = "end_dt")
	private LocalDateTime endDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	public ProductStatusHistory(LoanProduct loanProduct, LocalDateTime startDate, LocalDateTime endDate, Integer createdBy) {
		super();
		this.loanProduct = loanProduct;
		this.startDate = startDate;
		this.endDate = endDate;
		this.createdBy = createdBy;
	}
	
	

}

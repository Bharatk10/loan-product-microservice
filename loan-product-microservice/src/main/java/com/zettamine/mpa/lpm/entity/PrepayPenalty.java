package com.zettamine.mpa.lpm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pre_pay_penalty_master")
@Setter
@Getter
@NoArgsConstructor
public class PrepayPenalty extends BaseEntity {
	
	@Id
	private Integer productId;

	@OneToOne
	@MapsId
	@JoinColumn(name = "productId")
	private LoanProduct loanProduct;

	@Column(name = "min_penalty_amt")
	private Float minimumPenaltyAmount;

	@Column(name = "penalty_percentage")
	private Float penaltyPercentage;
	
	private Character status;

	public PrepayPenalty(LoanProduct loanProduct,Float minimumPenaltyAmount, Float penaltyPercentage,Character status) {
		super();
		
		this.loanProduct = loanProduct;
		this.minimumPenaltyAmount = minimumPenaltyAmount;
		this.penaltyPercentage = penaltyPercentage;
		this.status=status;
	}
	
	
	

}

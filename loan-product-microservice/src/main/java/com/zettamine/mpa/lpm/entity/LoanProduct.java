package com.zettamine.mpa.lpm.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "loan_prod")
@Getter
@Setter
public class LoanProduct extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_name")
	@SequenceGenerator(name = "sequence_name", sequenceName = "loanProduct_seq", allocationSize = 1, initialValue = 10001)
	@Column(name = "prod_id")
	private Integer productId;

	@Column(name = "prod_name")
	private String productName;

	@Column(name = "intr_rate")
	private Float interestRate;

	@Column(name = "loan_term")
	private Integer loanTerm;

	@Column(name = "max_loan_amt")
	private Double maximumLoanAmount;

	@Column(name = "mdp")
	private Float minimumDownPayment;

	@Column(name = "mdp_type")
	private String minimumDownPaymentType;

	@Column(name = "min_cr_score")
	private Integer minimumCreditScore;

	@Column(name = "max_ltv_ratio")
	private Integer maximumLoanToValueRatio;

	@Column(name = "pmi_required")
	private Boolean privateMortgageInsuranceRequired;

	@Column(name = "origin_fee")
	private Float originationFee;

	@Column(name = "prepay_penalty")
	private Boolean prepayPenalty;

	@Column(name = "lockin_period")
	private Integer lockInPeriod;

	@Column(name = "docu_reqrm_notes")
	private String documentRequirementNotes;

	@Column(name = "escrow_req")
	private Boolean escrowRequired;

	@Column(name = "prop_rstr_exists")
	private Boolean propertyRestrictionExists;

	@Column(name = "status")
	private Boolean status;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "loan_prod_prop_rstr", joinColumns = @JoinColumn(name = "prod_id"), inverseJoinColumns = @JoinColumn(name = "rstr_id"))
	private List<PropertyRestriction> propertyRestrcitions;
	
	@OneToOne(mappedBy = "loanProduct", cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id")
	private PrepayPenalty penalty;
	
}

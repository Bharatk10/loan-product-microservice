package com.zettamine.mpa.lpm.model;

import java.util.List;

import com.zettamine.mpa.lpm.constants.PatternConstants;
import com.zettamine.mpa.lpm.constants.ValidationConstants;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoanProdDto {

	
	private Integer productId;

	private String productName;

	private Float interestRate;

	private Integer loanTerm;

	private Double maxLoanAmount;

	private Float minDownPayment;

	private String minDownPaymentType;

	private Integer minCreditScore;

	private Integer maxLoanToValueRatio;

	private Boolean privateMortgageInsuranceRequired;

	private Float originationFee;

	private Float minPenalityAmount;
	
	private Float penalityPercentage;

	private Integer lockInPeriod;

	private String documentRequirementNotes;
	
	private List<String> propertyRestrcitions;

	private List<String> escrowRequirements;

	private List<String> underwritingCriteria;


	private Boolean status;

	
}
package com.zettamine.mpa.lpm.model;

import java.util.List;
import com.zettamine.mpa.lpm.constants.PatternConstants;
import com.zettamine.mpa.lpm.constants.ValidationConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoanProductDto {

	
	@NotBlank(message = ValidationConstants.BLANK_ERROR_MESSAGE)
	private String productName;
	
	@NotBlank(message = ValidationConstants.BLANK_ERROR_MESSAGE)
	@Pattern(regexp = PatternConstants.INTEREST_PATTERN,message  = ValidationConstants.INTREST_PATTERN_ERROR_MESSAGE)
	private String interestRate;
	
	@NotBlank(message = ValidationConstants.BLANK_ERROR_MESSAGE)
	@Pattern(regexp = PatternConstants.INTEGER_PATTERN,message = ValidationConstants.INTEGER_PATTERN_ERROR_MESSAGE)
	private String loanTerm;
	
	@NotBlank(message = ValidationConstants.BLANK_ERROR_MESSAGE)
	@Pattern(regexp = PatternConstants.MAX_LOAN_AMOUNT_PATTERN,message = ValidationConstants.MAX_LOAN_AMOUNT_PATTERN_ERROR_MESSAGE)
	private String maxLoanAmount;
	
	@NotBlank(message = ValidationConstants.BLANK_ERROR_MESSAGE)
	@Pattern(regexp = PatternConstants.MIN_DOWN_PAY_PATTERN,message =ValidationConstants.MIN_DOWN_PAYEMNT_ERROR_MESSAGE)
	private String minDownPayment;
	
	@NotBlank(message = ValidationConstants.BLANK_ERROR_MESSAGE)
	@Pattern(regexp = PatternConstants.STR_PATTERN,message = ValidationConstants.STR_PATTERN_ERROR_MESSAGE)
	private String minDownPaymentType;
	
	@NotBlank(message = ValidationConstants.BLANK_ERROR_MESSAGE)
	@Pattern(regexp = PatternConstants.CREDIT_RANGE_PATTERN,message = ValidationConstants.CREDIT_RANGE_PATTERN_ERROR_MESSAGE)
	private String minCreditScore;
	
	@NotBlank(message = ValidationConstants.BLANK_ERROR_MESSAGE)
	@Pattern(regexp = PatternConstants.LTV_RANGE_PATTERN,message =ValidationConstants.LTV_RANGE_PATTERN_ERROR_MESSAGE)
	private String maxLoanToValueRatio;
	
	@NotNull(message = ValidationConstants.BLANK_ERROR_MESSAGE)
	private Boolean privateMortgageInsuranceReq;
	
	@Pattern(regexp = PatternConstants.AMOUNT_PATTERN,message =ValidationConstants.AMOUNT_PATTERN_ERROR_MESSAGE)
	private String orginationFee;
	
	@NotBlank(message = ValidationConstants.BLANK_ERROR_MESSAGE)
	@Pattern(regexp = PatternConstants.INTEGER_PATTERN,message = ValidationConstants.INTEGER_PATTERN_ERROR_MESSAGE)
	private String lockinPeriod;
	
	@Pattern(regexp = PatternConstants.AMOUNT_PATTERN,message =ValidationConstants.AMOUNT_PATTERN_ERROR_MESSAGE)
	private String minPenalityAmount;
	
	@Pattern(regexp = PatternConstants.INTEREST_PATTERN,message  = ValidationConstants.INTREST_PATTERN_ERROR_MESSAGE)
	private String penalityPercentage;
	
	@NotBlank(message = ValidationConstants.BLANK_ERROR_MESSAGE)
	private String documentRequirement;
	
	
	private List<@Pattern(regexp = PatternConstants.STRING_PATTERN,
			message = ValidationConstants.STRING_PATTERN_ERROR_MESSAGE)
	        String> escrowRequirements;
	
	private List<@Pattern(regexp = PatternConstants.STRING_PATTERN,
			message = ValidationConstants.STRING_PATTERN_ERROR_MESSAGE) 
			String> propertyRestrictionTypes;
	
	private List<@Pattern(regexp = PatternConstants.STRING_PATTERN,
			message = ValidationConstants.STRING_PATTERN_ERROR_MESSAGE)
			String> underWritingCriteria;
	
}

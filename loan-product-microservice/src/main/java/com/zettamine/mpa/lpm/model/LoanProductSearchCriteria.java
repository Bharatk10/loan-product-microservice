package com.zettamine.mpa.lpm.model;

import java.util.List;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class LoanProductSearchCriteria {
	
	private Integer loanTerm;
	
	@Min(value = 300)
	@Max(value = 900)
	private Integer creditScore;
	
	private Boolean privateMortgageInsurance;
	
	private List<String> categoryTypes;
	
	private List<String> restrictionTypes;
	
	private List<String> escrowRequirements;
	
	private List<String> criterias;
	
	private Boolean prepayPenality;
	
	private Boolean status;
	
	
}

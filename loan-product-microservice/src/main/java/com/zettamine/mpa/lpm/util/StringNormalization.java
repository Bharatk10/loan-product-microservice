package com.zettamine.mpa.lpm.util;

import java.util.ArrayList;
import java.util.List;

import com.zettamine.mpa.lpm.model.LoanProductDto;
import com.zettamine.mpa.lpm.model.LoanProductSearchCriteria;
import com.zettamine.mpa.lpm.model.PropertyRestrictionCategoryDto;
import com.zettamine.mpa.lpm.model.PropertyRestrictionDto;

public class StringNormalization {

	public static String processString(String input) {

		if (input == null || input.isBlank()) {

			return input;
		}

		String processedString = input.toUpperCase().trim();
		processedString = processedString.replaceAll("\\s+", "");
		return processedString;
	}

	public static String processSentance(String input) {

		if (input == null || input.isBlank()) {

			return input;
		}

		String processedString = input.toUpperCase().trim();
		processedString = processedString.replaceAll("\\s+", " ");
		return processedString;
	}

	public static PropertyRestrictionCategoryDto processPropRestrCatgDto(PropertyRestrictionCategoryDto categoryDto) {

		categoryDto.setCategoryType(processSentance(categoryDto.getCategoryType()));

		categoryDto.setCategoryDescription(processSentance(categoryDto.getCategoryDescription()));

		return categoryDto;

	}

	public static PropertyRestrictionDto processPropRestDto(PropertyRestrictionDto propRestrDto) {

		propRestrDto.setCategoryType(processSentance(propRestrDto.getCategoryType()));
		propRestrDto.setRestrictionDescription(processSentance(propRestrDto.getRestrictionDescription()));
		propRestrDto.setRestrictionType(processSentance(propRestrDto.getRestrictionType()));

		return propRestrDto;
	}
	
	public static LoanProductDto processLoanProductDto(LoanProductDto loanProductDto) {
		
		loanProductDto.setProductName(processSentance(loanProductDto.getProductName()));
		loanProductDto.setInterestRate(processString(loanProductDto.getInterestRate()));
		loanProductDto.setLoanTerm(processString(loanProductDto.getLoanTerm()));
		loanProductDto.setMaxLoanAmount(processString(loanProductDto.getMaxLoanAmount()));
		loanProductDto.setMinDownPayment(processString(loanProductDto.getMinDownPayment()));
		loanProductDto.setMinDownPaymentType(processString(loanProductDto.getMinDownPaymentType()));
		loanProductDto.setMinCreditScore(processString(loanProductDto.getMinCreditScore()));
		loanProductDto.setMaxLoanToValueRatio(processString(loanProductDto.getMaxLoanToValueRatio()));
		loanProductDto.setOrginationFee(processString(loanProductDto.getOrginationFee()));
		loanProductDto.setLockinPeriod(processString(loanProductDto.getLockinPeriod()));
		loanProductDto.setMinPenalityAmount(processString(loanProductDto.getMinPenalityAmount()));
		loanProductDto.setPenalityPercentage(processString(loanProductDto.getPenalityPercentage()));
		loanProductDto.setDocumentRequirement(processSentance(loanProductDto.getDocumentRequirement()));
		
		if(loanProductDto.getEscrowRequirements()!=null) {
			
			List<String> escrowReqList = new ArrayList<>();
			for(String escrowReq : loanProductDto.getEscrowRequirements() ) {
				
				escrowReqList.add(processSentance(escrowReq));
			}
			
			loanProductDto.setEscrowRequirements(escrowReqList);
		}
		
		if(loanProductDto.getUnderWritingCriteria()!=null) {
			
			List<String> criteaList = new ArrayList<>();
			
			for(String criteria : loanProductDto.getUnderWritingCriteria()) {
				
				criteaList.add(processSentance(criteria));
			}
			
			loanProductDto.setUnderWritingCriteria(criteaList);
		}
		
		if(loanProductDto.getPropertyRestrictionTypes()!=null) {
			
			List<String> propRestTypeList = new ArrayList<>();
			
			for(String propRestType : loanProductDto.getPropertyRestrictionTypes()) {
				
				propRestTypeList.add(processSentance(propRestType));
			}
			
			loanProductDto.setPropertyRestrictionTypes(propRestTypeList);
		}
		
		return loanProductDto;
	}
	public static LoanProductSearchCriteria searchCriteriaProcess(LoanProductSearchCriteria criteria) {
	
		if (criteria != null) {
			if(criteria.getCategoryTypes()!=null) {
				
				criteria.setCategoryTypes(criteria.getCategoryTypes().stream().map(catg -> StringNormalization.processSentance(catg)).toList());
			}
			if(criteria.getRestrictionTypes()!=null) {
				
				criteria.setRestrictionTypes(criteria.getRestrictionTypes().stream().map(rstr -> StringNormalization.processSentance(rstr)).toList());
			}
			if(criteria.getEscrowRequirements()!=null) {
				
				criteria.setEscrowRequirements(criteria.getEscrowRequirements().stream().map(escrow -> StringNormalization.processSentance(escrow)).toList());
			}
			if(criteria.getCriterias()!=null) {
				criteria.setCriterias(criteria.getCriterias().stream().map(critr -> StringNormalization.processSentance(critr)).toList());
			}
			
			criteria.setCreditScore(criteria.getCreditScore());
			criteria.setLoanTerm(criteria.getLoanTerm());
			criteria.setPrepayPenality(criteria.getPrepayPenality());
			criteria.setPrivateMortgageInsurance(criteria.getPrivateMortgageInsurance());
			criteria.setStatus(criteria.getStatus());
		}
		return criteria;
	}

}

package com.zettamine.mpa.lpm.mapper;

import java.util.List;

import com.zettamine.mpa.lpm.constants.AppConstants;
import com.zettamine.mpa.lpm.entity.LoanProduct;
import com.zettamine.mpa.lpm.entity.PrepayPenalty;
import com.zettamine.mpa.lpm.entity.PropertyRestriction;
import com.zettamine.mpa.lpm.model.LoanProdDto;
import com.zettamine.mpa.lpm.model.LoanProductDto;

public class LoanProductMapper {

	public static LoanProdDto loanProductDtoToLoanProdDto(LoanProductDto loanProductDto, LoanProdDto loanProdDto) {

		loanProdDto.setProductName(loanProductDto.getProductName());
		loanProdDto.setInterestRate(Float.parseFloat(loanProductDto.getInterestRate()));
		loanProdDto.setLoanTerm(Integer.parseInt(loanProductDto.getLoanTerm()));
		loanProdDto.setMaxLoanAmount(Double.parseDouble(loanProductDto.getMaxLoanAmount()));
		loanProdDto.setMinDownPayment(Float.parseFloat(loanProductDto.getMinDownPayment()));
		loanProdDto.setMinDownPaymentType(loanProductDto.getMinDownPaymentType());
		loanProdDto.setMinCreditScore(Integer.parseInt(loanProductDto.getMinCreditScore()));
		loanProdDto.setMaxLoanToValueRatio(Integer.parseInt(loanProductDto.getMaxLoanToValueRatio()));
		loanProdDto.setPrivateMortgageInsuranceRequired(loanProductDto.getPrivateMortgageInsuranceReq());
		if (loanProductDto.getOrginationFee() != null) {

			loanProdDto.setOriginationFee(Float.parseFloat(loanProductDto.getOrginationFee()));
		}

		if (loanProductDto.getMinPenalityAmount() != null) {

			loanProdDto.setMinPenalityAmount(Float.parseFloat(loanProductDto.getMinPenalityAmount()));
			
		}
		
		if(loanProductDto.getPenalityPercentage() != null) {
			loanProdDto.setPenalityPercentage(Float.parseFloat(loanProductDto.getPenalityPercentage()));
		}
		

		loanProdDto.setLockInPeriod(Integer.parseInt(loanProductDto.getLockinPeriod()));

		loanProdDto.setDocumentRequirementNotes(loanProductDto.getDocumentRequirement());

		if (loanProductDto.getEscrowRequirements()!=null ) {

			loanProdDto.setEscrowRequirements(loanProductDto.getEscrowRequirements());

		}
		if (loanProductDto.getUnderWritingCriteria()!=null) {

			loanProdDto.setUnderwritingCriteria(loanProductDto.getUnderWritingCriteria());

		}
		if (loanProductDto.getPropertyRestrictionTypes()!=null) {

			loanProdDto.setPropertyRestrcitions(loanProductDto.getPropertyRestrictionTypes());

		}
		
		return loanProdDto;

	}
	
	public static LoanProduct loanProdDtoToLoanProduct(LoanProdDto loanProdDto,LoanProduct loanProd) {
		
		loanProd.setProductId(loanProdDto.getProductId());
		loanProd.setProductName(loanProdDto.getProductName());
		loanProd.setInterestRate(loanProdDto.getInterestRate());
		loanProd.setLoanTerm(loanProdDto.getLoanTerm());
		loanProd.setMaximumLoanAmount(loanProdDto.getMaxLoanAmount());
		loanProd.setMinimumDownPayment(loanProdDto.getMinDownPayment());
		loanProd.setMinimumDownPaymentType(loanProdDto.getMinDownPaymentType());
		loanProd.setMinimumCreditScore(loanProdDto.getMinCreditScore());
		loanProd.setPrivateMortgageInsuranceRequired(loanProdDto.getPrivateMortgageInsuranceRequired());
		loanProd.setMaximumLoanToValueRatio(loanProdDto.getMaxLoanToValueRatio());
		loanProd.setOriginationFee(loanProdDto.getOriginationFee());
		loanProd.setLockInPeriod(loanProdDto.getLockInPeriod());
		loanProd.setDocumentRequirementNotes(loanProdDto.getDocumentRequirementNotes());
		
		if(loanProdDto.getMinPenalityAmount()!=null && loanProdDto.getPenalityPercentage()!=null) {
			
			loanProd.setPrepayPenalty(AppConstants.STATUS_TRUE);
		}
		else {
			loanProd.setPrepayPenalty(AppConstants.STATUS_FALSE);
		}
		
		return loanProd;
		
	}
	public static LoanProdDto loanProductToLoanproductDto(LoanProduct loanProduct, LoanProdDto loanProdDto) {
		loanProdDto.setProductId(loanProduct.getProductId());
		loanProdDto.setProductName(loanProduct.getProductName());
		loanProdDto.setInterestRate(loanProduct.getInterestRate());
		loanProdDto.setLoanTerm(loanProduct.getLoanTerm());
		loanProdDto.setMaxLoanAmount(loanProduct.getMaximumLoanAmount());
		loanProdDto.setMinDownPayment(loanProduct.getMinimumDownPayment());
		loanProdDto.setMinDownPaymentType(loanProduct.getMinimumDownPaymentType());
		loanProdDto.setMinCreditScore(loanProduct.getMinimumCreditScore());
		loanProdDto.setPrivateMortgageInsuranceRequired(loanProduct.getPrivateMortgageInsuranceRequired());
		loanProdDto.setMaxLoanToValueRatio(loanProduct.getMaximumLoanToValueRatio());
		loanProdDto.setOriginationFee(loanProduct.getOriginationFee());
		if (!loanProduct.getPrepayPenalty()) {
			loanProdDto.setMinPenalityAmount(0.0f);
			loanProdDto.setPenalityPercentage(0.0f);
		} else {
			loanProdDto.setMinPenalityAmount(loanProduct.getPenalty().getMinimumPenaltyAmount());
			loanProdDto.setPenalityPercentage(loanProduct.getPenalty().getPenaltyPercentage());
		}

		loanProdDto.setLockInPeriod(loanProduct.getLockInPeriod());
		loanProdDto.setDocumentRequirementNotes(loanProduct.getDocumentRequirementNotes());
		loanProdDto.setPropertyRestrcitions(
				loanProduct.getPropertyRestrcitions().stream().map(m -> m.getRestrictionType()).toList());
		loanProdDto.setEscrowRequirements(null);
		loanProdDto.setUnderwritingCriteria(null);
		loanProdDto.setStatus(loanProduct.getStatus());
		return loanProdDto;
	}

}

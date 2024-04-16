package com.zettamine.mpa.lpm.mapper;


import com.zettamine.mpa.lpm.entity.PrepayPenalty;
import com.zettamine.mpa.lpm.model.LoanProdDto;
import com.zettamine.mpa.lpm.model.PrePayPenalityDto;
import com.zettamine.mpa.lpm.model.PrepayPenaltyStatusDto;

public class PrepayPenaltyMapper {
	
	public static PrePayPenalityDto LoanProdDtoToPenalityDto(PrePayPenalityDto penality,LoanProdDto loanProd) {
		
		penality.setMinPenalityAmount(loanProd.getMinPenalityAmount());
		penality.setPenalityPercentage(loanProd.getPenalityPercentage());
	
		return penality;
		
	}
	
	public static PrepayPenaltyStatusDto prepayPenaltyToPrepayPenaltyStatusDto(PrepayPenalty prepayPenalty,
			PrepayPenaltyStatusDto prepayPenaltyStatusDto) {

		prepayPenaltyStatusDto.setMinPenalityAmount(prepayPenalty.getMinimumPenaltyAmount());

		prepayPenaltyStatusDto.setPenalityPercentage(prepayPenalty.getPenaltyPercentage());

		prepayPenaltyStatusDto.setStatus(prepayPenalty.getStatus());

		return prepayPenaltyStatusDto;

	}

}

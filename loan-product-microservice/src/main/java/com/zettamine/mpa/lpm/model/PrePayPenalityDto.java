package com.zettamine.mpa.lpm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
public class PrePayPenalityDto {
	
	private Integer productId;
	
	private Float minPenalityAmount;
	
	private Float penalityPercentage;

}

package com.zettamine.mpa.lpm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrepayPenaltyStatusDto {

	private Float minPenalityAmount;

	private Float penalityPercentage;

	private Character status;

	private String statusDescription;

}
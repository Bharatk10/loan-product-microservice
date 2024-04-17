package com.zettamine.mpa.lpm.model;



import com.zettamine.mpa.lpm.constants.AppConstants;
import com.zettamine.mpa.lpm.constants.ValidationConstants;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnderwritingCriteriaDto {

	private Long criteriaId;

	@NotBlank(message = ValidationConstants.BLANK_ERROR_MESSAGE)
	private String criteriaName;
	@NotBlank(message = ValidationConstants.BLANK_ERROR_MESSAGE)
	private String notes;
}

package com.zettamine.mpa.lpm.model;

import java.util.List;

import com.zettamine.mpa.lpm.constants.ValidationConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanProductCriteriaDto {

	@NotBlank(message = ValidationConstants.BLANK_ERROR_MESSAGE)
	private String productName;
	
	@NotNull
	private List<String> criteriaNames;
}

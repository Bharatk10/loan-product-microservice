package com.zettamine.mpa.lpm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.zettamine.mpa.lpm.constants.PatternConstants;
import com.zettamine.mpa.lpm.constants.ValidationConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PropertyRestrictionDto {

	@JsonProperty(access = Access.READ_ONLY)
	private Integer restrictionId;

	@NotBlank(message = ValidationConstants.BLANK_ERROR_MESSAGE)
	@Pattern(regexp = PatternConstants.STRING_PATTERN, message = ValidationConstants.STRING_PATTERN_ERROR_MESSAGE)
	private String categoryType;

	@NotBlank(message = ValidationConstants.BLANK_ERROR_MESSAGE)
	@Pattern(regexp = PatternConstants.STRING_PATTERN, message = ValidationConstants.STRING_PATTERN_ERROR_MESSAGE)
	private String restrictionType;

	@NotBlank(message = ValidationConstants.BLANK_ERROR_MESSAGE)
	@Pattern(regexp = PatternConstants.STRING_PATTERN, message = ValidationConstants.STRING_PATTERN_ERROR_MESSAGE)
	private String restrictionDescription;

}
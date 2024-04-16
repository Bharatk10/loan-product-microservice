package com.zettamine.mpa.lpm.model;



import com.zettamine.mpa.lpm.constants.EscrowConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EscrowReqDto {
	
	private Integer reqId;

	@NotBlank(message = "*Required")
	@Pattern(regexp = EscrowConstants.NAMES_REGEX,message = EscrowConstants.INVALID_NAME)
	private String reqName;
	
	private String description;
}

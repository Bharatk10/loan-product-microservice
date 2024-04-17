package com.zettamine.mpa.lpm.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
public class ProductStatusHistoryDto {
	
	

	@JsonProperty(access = Access.WRITE_ONLY)
	private Integer productId;
	
	private LocalDateTime startDate;
	
	private Object endTime;

}

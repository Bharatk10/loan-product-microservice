package com.zettamine.mpa.lpm.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchByReqDto {
	
	private List<String> requirments;

}

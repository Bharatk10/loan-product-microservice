package com.zettamine.mpa.lpm.mapper;

import com.zettamine.mpa.lpm.entity.ProductStatusHistory;
import com.zettamine.mpa.lpm.model.ProductStatusHistoryDto;

public class ProductStatusHistoryMapper {
	
	
	public static ProductStatusHistoryDto productStatusHistoryToproductHistoryDto(ProductStatusHistoryDto propStatusHistoryDto,
																				 ProductStatusHistory prodStatusHistory) {
		
		
		propStatusHistoryDto.setStartDate(prodStatusHistory.getStartDate());
		propStatusHistoryDto.setEndTime(prodStatusHistory.getEndDate());
		
		return propStatusHistoryDto;
	}

}

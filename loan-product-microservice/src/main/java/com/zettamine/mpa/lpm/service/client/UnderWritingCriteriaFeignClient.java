package com.zettamine.mpa.lpm.service.client;

import java.util.List;
import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.zettamine.mpa.lpm.model.LoanProductCriteriaDto;
import com.zettamine.mpa.lpm.model.ResponseDto;
import com.zettamine.mpa.lpm.model.UnderwritingCriteriaDto;

import jakarta.validation.Valid;

@FeignClient("underwriting-service")
public interface UnderWritingCriteriaFeignClient {
	
	@PostMapping(path = "/api/v1/underwriting/criteria/add-criteria-to-loanProd")
	public ResponseEntity<ResponseDto> addCriteriaToProd(@RequestBody LoanProductCriteriaDto loanProductCriteriaDto)
			throws IllegalArgumentException, IllegalAccessException;
	
	@PostMapping("/api/v1/underwriting/criteria/add-criteria-to-loanProd/{prodId}")
	public ResponseEntity<ResponseDto> addCriteriaToLoanProd(@PathVariable ("prodId")Integer productId,@RequestBody List<String> loanProductCriteriaDto);
	
	@GetMapping(path = "/api/v1/underwriting/criteria/fetchAll-criteria-names")
	public ResponseEntity<List<String>> fetchAll();
	
	@PostMapping("/api/v1/underwriting/criteria/get-loan-by-criteria-name")
	public ResponseEntity<Set<Integer>> fetch(@RequestBody List<String> criteriaNames);
	
	@GetMapping("/api/v1/underwriting/criteria/fetch-by-loan-id/{id}")
	public ResponseEntity<List<UnderwritingCriteriaDto>> fetchByLoanId(@PathVariable Integer id);

}

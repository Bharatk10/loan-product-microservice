package com.zettamine.mpa.lpm.service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


import com.zettamine.mpa.lpm.model.LoanReqDto;
import com.zettamine.mpa.lpm.model.ResponseDto;
import com.zettamine.mpa.lpm.model.SearchByReqDto;

import jakarta.validation.Valid;

@FeignClient("escrow-service")
public interface EscrowFeignClient {

	@GetMapping(path = "/api/v1/escrow/requirements/fetch-req")
	public ResponseEntity<List<String>> getAllReq();
	
	@PostMapping(path = "/api/v1/escrow/loan-requirment/add-requirment")
	public ResponseEntity<?> save(LoanReqDto  reqLoanDto);
	
	@PostMapping("/api/v1/escrow/loan-requirment/search-by-requirments")
	public ResponseEntity<List<Integer>> searchByReq( @RequestBody SearchByReqDto searchDto);
	
	@GetMapping("/api/v1/escrow/loan-requirment/find-by-prodid/{prodId}")
	public ResponseEntity<List<String>> findByProdId(@PathVariable Integer prodId);
	
	@PutMapping(path="/api/v1/escrow/loan-requirment/delete") 
	public ResponseEntity<ResponseDto> deleteLoanReq(LoanReqDto loanReqDto);
	
	
}

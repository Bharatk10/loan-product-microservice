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

/**
 * Feign client interface for interacting with the Escrow service.
 */
@FeignClient("escrow-service")
public interface EscrowFeignClient {

    /**
     * Fetches all escrow requirements.
     *
     * @return A ResponseEntity containing a list of all escrow requirements.
     */
    @GetMapping(path = "/api/v1/escrow/requirements/fetch-req")
    public ResponseEntity<List<String>> getAllReq();

    /**
     * Adds escrow requirements to a loan product.
     *
     * @param reqLoanDto The DTO containing information about the loan requirement.
     * @return A ResponseEntity indicating the success of the operation.
     */
    @PostMapping(path = "/api/v1/escrow/loan-requirment/add-requirment")
    public ResponseEntity<?> save(LoanReqDto reqLoanDto);

    /**
     * Searches for loan products by escrow requirements.
     *
     * @param searchDto The DTO containing search criteria.
     * @return A ResponseEntity containing a list of loan product IDs matching the requirements.
     */
    @PostMapping("/api/v1/escrow/loan-requirment/search-by-requirments")
    public ResponseEntity<List<Integer>> searchByReq(@RequestBody SearchByReqDto searchDto);

    /**
     * Retrieves escrow requirements for a specific loan product.
     *
     * @param prodId The ID of the loan product.
     * @return A ResponseEntity containing a list of escrow requirements for the specified loan product.
     */
    @GetMapping("/api/v1/escrow/loan-requirment/find-by-prodid/{prodId}")
    public ResponseEntity<List<String>> findByProdId(@PathVariable Integer prodId);

    /**
     * Deletes escrow requirements from a loan product.
     *
     * @param loanReqDto The DTO containing information about the loan requirement to be deleted.
     * @return A ResponseEntity indicating the success of the operation.
     */
    @PutMapping(path = "/api/v1/escrow/loan-requirment/delete")
    public ResponseEntity<ResponseDto> deleteLoanReq(LoanReqDto loanReqDto);

}
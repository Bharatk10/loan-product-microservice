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


/**
 * Feign client interface for interacting with the Underwriting service to manage loan criteria.
 */
@FeignClient("underwriting-service")
public interface UnderWritingCriteriaFeignClient {

    /**
     * Adds criteria to a loan product.
     *
     * @param loanProductCriteriaDto The DTO containing information about the loan product criteria.
     * @return A ResponseEntity indicating the success of the operation.
     * @throws IllegalArgumentException If the provided criteria DTO is invalid.
     * @throws IllegalAccessException If access to the criteria DTO is denied.
     */
    @PostMapping(path = "/api/v1/underwriting/criteria/add-criteria-to-loanProd")
    public ResponseEntity<ResponseDto> addCriteriaToProd(@RequestBody LoanProductCriteriaDto loanProductCriteriaDto)
            throws IllegalArgumentException, IllegalAccessException;

    /**
     * Adds criteria to a loan product identified by its ID.
     *
     * @param productId             The ID of the loan product.
     * @param loanProductCriteriaDto The list of criteria to be added to the loan product.
     * @return A ResponseEntity indicating the success of the operation.
     */
    @PostMapping("/api/v1/underwriting/criteria/add-criteria-to-loanProd/{prodId}")
    public ResponseEntity<ResponseDto> addCriteriaToLoanProd(@PathVariable("prodId") Integer productId,
                                                             @RequestBody List<String> loanProductCriteriaDto);

    /**
     * Fetches all available criteria names.
     *
     * @return A ResponseEntity containing a list of all criteria names.
     */
    @GetMapping(path = "/api/v1/underwriting/criteria/fetchAll-criteria-names")
    public ResponseEntity<List<String>> fetchAll();

    /**
     * Fetches loans that match the specified criteria names.
     *
     * @param criteriaNames The list of criteria names to search for.
     * @return A ResponseEntity containing a set of loan IDs that match the specified criteria.
     */
    @PostMapping("/api/v1/underwriting/criteria/get-loan-by-criteria-name")
    public ResponseEntity<Set<Integer>> fetch(@RequestBody List<String> criteriaNames);

    /**
     * Fetches criteria for a specific loan ID.
     *
     * @param id The ID of the loan.
     * @return A ResponseEntity containing a list of criteria associated with the specified loan ID.
     */
    @GetMapping("/api/v1/underwriting/criteria/fetch-by-loan-id/{id}")
    public ResponseEntity<List<UnderwritingCriteriaDto>> fetchByLoanId(@PathVariable Integer id);

}
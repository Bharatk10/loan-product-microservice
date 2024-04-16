package com.zettamine.mpa.lpm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zettamine.mpa.lpm.constants.AppConstants;
import com.zettamine.mpa.lpm.model.ErrorResponseDto;
import com.zettamine.mpa.lpm.model.PrePayPenalityDto;
import com.zettamine.mpa.lpm.model.PrepayPenaltyStatusDto;
import com.zettamine.mpa.lpm.model.ResponseDto;
import com.zettamine.mpa.lpm.service.ILoanProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/loan-product/prepay-penalty")
@AllArgsConstructor
public class PrepayPenaltyController {

	private ILoanProductService loanProductService;

	private static final Logger LOGGER = LoggerFactory.getLogger(PrepayPenaltyController.class);

	@Operation(summary = "Create Prepay Penalty  REST API", description = "REST API to create a Prepay Penalty")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
			@ApiResponse(responseCode = "409", description = "HTTP Status CONFLICT"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@PostMapping("/create")
	public ResponseEntity<?> createPrePayPenality(@Valid @RequestBody PrePayPenalityDto penalityDto) {

		loanProductService.createPrePayPenality(penalityDto);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(AppConstants.STATUS_201, AppConstants.LOAN_PRODUCT_PENALITY_CREATED_MESSAGE));
	}

	@Operation(summary = "Update Prepay Penalty Details REST API", description = "REST API to update Prepay Penalty details")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })	
	@PutMapping("/update")
	public ResponseEntity<?> updateprePayPenality(@Valid @RequestBody PrePayPenalityDto penalityDto) {

		loanProductService.updatePrePayPenality(penalityDto);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDto(AppConstants.STATUS_200, AppConstants.LOAN_PRODUCT_PENALITY_MESSAGE));
	}

	@Operation(summary = "Update Prepay Penalty Status By Loan Product Id REST API", description = "REST API to  Update Prepay Penalty Status based on Loan Product Id")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@PutMapping("/update/status/{prodId}")
	public ResponseEntity<?> updateprePayPenalityStatus(@PathVariable("prodId") Integer prodId) {

		loanProductService.updatePrePayPenalityStatus(prodId);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDto(AppConstants.STATUS_200, AppConstants.LOAN_PRODUCT_PENALITY_STATUS_MESSAGE));
	}

	@Operation(summary = "Fetch Prepay Penalty Details By Loan Product Id REST API", description = "REST API to fetch Prepay Penalty Details based on Loan Product Id REST API")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@GetMapping("/fetch/{prodId}")
	public ResponseEntity<PrepayPenaltyStatusDto> getPrepayPenaltyById(@PathVariable("prodId") Integer prodId) {

		PrepayPenaltyStatusDto prepaypenalty = loanProductService.getPrepayPenaltyById(prodId);

		return ResponseEntity.status(HttpStatus.OK).body(prepaypenalty);

	}
}
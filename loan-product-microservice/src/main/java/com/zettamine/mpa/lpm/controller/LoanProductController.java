package com.zettamine.mpa.lpm.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zettamine.mpa.lpm.constants.AppConstants;
import com.zettamine.mpa.lpm.model.ErrorResponseDto;
import com.zettamine.mpa.lpm.model.LoanProdDto;
import com.zettamine.mpa.lpm.model.LoanProductDto;
import com.zettamine.mpa.lpm.model.LoanProductSearchCriteria;
import com.zettamine.mpa.lpm.model.ProductStatusHistoryDto;
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
@RequestMapping("/api/v1/loan-product/product")
@AllArgsConstructor
public class LoanProductController {

	private ILoanProductService loanProductService;

	private static final Logger LOGGER = LoggerFactory.getLogger(LoanProductController.class);

	  /**
     * Create LoanProduct REST API.
     *
     * @param loanProductDto The DTO containing information about the LoanProduct to be created.
     * @return ResponseEntity<?> HTTP response indicating the status of the operation.
     */
	@Operation(summary = "Create LoanProduct REST API", description = "REST API to create new LoanProduct")
		@ApiResponses({@ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
			@ApiResponse(responseCode = "409", description = "HTTP Status CONFLICT"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
	@PostMapping("/create")
	public ResponseEntity<?> addLoanProduct(@Valid @RequestBody LoanProductDto loanProductDto) {

		loanProductService.create(loanProductDto);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(AppConstants.STATUS_201, AppConstants.CREATE_LOAN_PRODUCT_MESSAGE));
	}


    /**
     * Fetch LoanProduct Details By LoanProductId REST API.
     *
     * @param prodId The ID of the LoanProduct to fetch details for.
     * @return ResponseEntity<?> HTTP response containing DTO representation of LoanProduct details.
     */
	@Operation(summary = "Fetch LoanProduct Details By LoanProductId REST API", description = "REST API to fetch LoanProduct details based on LoanProduct Id ")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@GetMapping("/fetch/{prodId}")
	public ResponseEntity<?> getloanProductById(@PathVariable("prodId") Integer prodId) {

		LoanProdDto loanProductDto = loanProductService.getbyId(prodId);

		return ResponseEntity.status(HttpStatus.OK).body(loanProductDto);
	}


    /**
     * Update LoanProduct status REST API.
     *
     * @param prodId The ID of the LoanProduct to update status for.
     * @return ResponseEntity<?> HTTP response indicating the status of the operation.
     */
	@Operation(summary = "Update LoanProduct status  REST API", description = "REST API to LoanProduct status details based on LoanProduct Id for loan product microservice")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "417", description = "Expectation Failed"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })	
	@PutMapping("/update/loanProductStatus/{prodId}")
	public ResponseEntity<?> updateLoanProductStatus(@PathVariable("prodId") Integer prodId) {

		loanProductService.updateLoanProductStatus(prodId);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDto(AppConstants.STATUS_200, AppConstants.LOAN_PRODUCT_STATUS_MESSAGE));
	}
	 /**
     * Fetch LoanProduct Status History Details By LoanProduct Id REST API.
     *
     * @param prodId The ID of the LoanProduct to fetch status history for.
     * @return ResponseEntity<?> HTTP response containing DTO representation of LoanProduct status history details.
     */

	@Operation(summary = "Fetch LoanProduct Status History Details By LoanProduct Id REST API", description = "REST API to fetch LoanProduct Status History  details based on LoanProduct Id ")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@GetMapping("/fetch/productHistory/{prodId}")
	public ResponseEntity<?> getProductStatusHistory(@PathVariable("prodId") Integer prodId) {

		List<ProductStatusHistoryDto> productStatusHistory = loanProductService.getProductStatusHistory(prodId);

		return ResponseEntity.status(HttpStatus.OK).body(productStatusHistory);

	}
	 /**
     * Update LoanProduct Details By LoanProductId REST API.
     *
     * @param prodId        The ID of the LoanProduct to update.
     * @param loanProductDto The DTO containing updated information about the LoanProduct.
     * @return ResponseEntity<?> HTTP response indicating the status of the operation.
     */

	@Operation(summary = "Update LoanProduct Details By LoanProductId REST API", description = "REST API to LoanProduct Details based on a LoanProductId ")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })  
	@PutMapping("/update/loanProduct/{prodId}")
	public ResponseEntity<?> updateLoanProduct(@PathVariable("prodId") Integer prodId,
			@Valid @RequestBody LoanProductDto loanProductDto) {

		loanProductService.updateLoanProduct(prodId, loanProductDto);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDto(AppConstants.STATUS_200, AppConstants.UPDATE_LOAN_PRODUCT_MESSAGE));
	}
	 /**
     * Search LoanProduct Details By criteria REST API.
     *
     * @param criteria The search criteria to filter LoanProducts.
     * @return ResponseEntity<?> HTTP response containing list of LoanProducts matching the criteria.
     */

	@Operation(summary = " Search LoanProduct Details By criteria REST API", description = "REST API to  search for LoanProduct details by Search Criteria")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@PostMapping("/search")
	public ResponseEntity<?> searchLoanProduct(@Valid @RequestBody LoanProductSearchCriteria criteria) {

		List<LoanProdDto> loanProducts = loanProductService.searchLoanProducts(criteria);

		return ResponseEntity.status(HttpStatus.OK).body(loanProducts);
	}

    /**
     * Fetch Loan Product Id By Loan Product Name REST API.
     *
     * @param loanProductName The name of the Loan Product to fetch ID for.
     * @return ResponseEntity<?> HTTP response containing the ID of the Loan Product.
     */
	
	@Operation(summary = "Fetch Loan Product Id By Loan Product Name  REST API", description = "REST API to  Loan Product Id based on Loan Product Name")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@GetMapping("/fetch/loan-product-id/{prodName}")
	public ResponseEntity<?> getLoanProductIdByName(@PathVariable("prodName") String loanProductName) {
		Integer loanProductId = loanProductService.getLoanProductIdByProductName(loanProductName);

		return ResponseEntity.status(HttpStatus.OK).body(loanProductId);
	}
	   /**
     * Update Loan Product Status By Loan Product Id REST API.
     *
     * @param productId The ID of the Loan Product to update status for.
     * @return ResponseEntity<Boolean> HTTP response indicating the success of the operation.
     */

	@Operation(summary = "Update Loan Product Status By Loan Product Id REST API", description = "REST API to Update Loan Product Status  Loan Product Id  based on a Loan Product Id for under writing criteria")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })	
	@PutMapping("/update/loan-status/{productId}")
	public ResponseEntity<Boolean> updateLoanProdStatus(@PathVariable("productId") Integer productId) {

		Boolean status = loanProductService.updateLoanStatusHistory(productId);

		return ResponseEntity.status(HttpStatus.OK).body(status);

	}

    /**
     * Add Property Restrictions To Loan Product By Loan Product Id.
     *
     * @param productId    The ID of the Loan Product to add restrictions to.
     * @param restrictions The list of restrictions to add.
     * @return ResponseEntity<?> HTTP response indicating the status of the operation.
     */

	@Operation(summary = "Add Propperty Restrictions To Loan Product By Loan Product Id", description = "REST API to Add Propperty Restrictions To Loan Product By Loan Prodcut Id")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@PostMapping("/loan-product/add/prop-rstr/{prodId}")
	public ResponseEntity<?> addPropRestrictions(@Valid @PathVariable("prodId") Integer productId,
			@RequestBody List<String> restrictions) {
		loanProductService.addRestrictions(productId, restrictions);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(AppConstants.STATUS_201, AppConstants.PROP_RSTR_ADDED_SUCCESSFULL));

	}
	 /**
     * Remove Property Restrictions Of a Loan Product By Loan Product Id.
     *
     * @param productId    The ID of the Loan Product to remove restrictions from.
     * @param restrictions The list of restrictions to remove.
     * @return ResponseEntity<?> HTTP response indicating the status of the operation.
     */

	@Operation(summary = "Remove Propperty Restrictions Of a  Loan Product By Loan Prodcut Id", description = "REST API to Remove Propperty Restrictions of  Loan Product Based on Loan Prodcut Id" )
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@DeleteMapping("remove/loan-product-prop-rstr/{prodId}")
	public ResponseEntity<?> removePropRestrictions(@Valid @PathVariable("prodId") Integer productId,
			@RequestBody List<String> restructions) {
		loanProductService.removeRestrictions(productId, restructions);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDto(AppConstants.STATUS_200, AppConstants.PROP_RSTR_REMOVED_SUCCESSFULL));
	}

	 /**
     * Remove Escrow Requirements Of a Loan Product By Loan Product Id.
     *
     * @param productId     The ID of the Loan Product to remove escrow requirements from.
     * @param requirements  The list of escrow requirements to remove.
     * @return ResponseEntity<?> HTTP response indicating the status of the operation.
     */

	@Operation(summary = "Remove Escrow Requirements Of a  Loan Product By Loan Prodcut Id", description = "REST API to Remove Propperty Restrictions of  Loan Product Based on Loan Prodcut Id" )
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@DeleteMapping("remove/loan-product-escrw-req/{prodId}")
	public ResponseEntity<?> removeEscrowReqirements(@Valid @PathVariable("prodId") Integer productId,

			@RequestBody List<String> requirements) {
		loanProductService.removeEscrowRequirements(productId, requirements);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDto(AppConstants.STATUS_200, AppConstants.ESCROW_REQ_REMOVED_SUCCESSFULL));
	}

	 /**
     * Add Escrow Requirements Of a Loan Product By Loan Product Id.
     *
     * @param productId     The ID of the Loan Product to add escrow requirements to.
     * @param requirements  The list of escrow requirements to add.
     * @return ResponseEntity<?> HTTP response indicating the status of the operation.
     */

	@Operation(summary = "Add Escrow Requirements Of a  Loan Product By Loan Prodcut Id", description = "REST API to Remove Propperty Restrictions of  Loan Product Based on Loan Prodcut Id" )
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "HTTP Status CRETAED"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@PostMapping("add/loan-product-escrow-req/{productId}")
	public ResponseEntity<?> addEscrowRequirements(@PathVariable("productId") Integer productId,
			@RequestBody List<String> requirements) {

		loanProductService.addEscrowRequirementsToLoanProduct(productId, requirements);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(AppConstants.STATUS_201, AppConstants.ESCROW_REQUI_ADDED_SUCCESSFULL));
	}


    /**
     * Add Underwriting Criteria Of a Loan Product By Loan Product Id.
     *
     * @param productId The ID of the Loan Product to add underwriting criteria to.
     * @param criterias The list of underwriting criteria to add.
     * @return ResponseEntity<?> HTTP response indicating the status of the operation.
     */

	@Operation(summary = "Add Underwriting Criteria Of a  Loan Product By Loan Prodcut Id", description = "REST API to Remove Propperty Restrictions of  Loan Product Based on Loan Prodcut Id" )
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@PostMapping("add/loan-product-criteria/{productId}")
	public ResponseEntity<?> addUnderWritingCriteria(@PathVariable("productId") Integer productId,
			@RequestBody List<String> criterias) {

		loanProductService.addUnderWritingCriteriaToLoanProduct(productId, criterias);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(AppConstants.STATUS_201, AppConstants.CRITERIA_ADDED_SUCCESSFULL));
	}

}

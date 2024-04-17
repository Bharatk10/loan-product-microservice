package com.zettamine.mpa.lpm.controller;

import java.util.List;

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
import com.zettamine.mpa.lpm.entity.PropertyRestrictionCategory;
import com.zettamine.mpa.lpm.model.ErrorResponseDto;
import com.zettamine.mpa.lpm.model.PropertyRestrictionCategoryDto;
import com.zettamine.mpa.lpm.model.ResponseDto;
import com.zettamine.mpa.lpm.service.IPropertyRestrictionCategoryService;
import com.zettamine.mpa.lpm.util.CatTypeReq;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/loan-product/restr-ctrgy")
public class PropertyRestrictionCategoryController {
	
	private IPropertyRestrictionCategoryService categoryService;

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyRestrictionCategory.class);


	/**
	 * Endpoint to create a new property restriction category.
	 *
	 * @param propertyRestrictionCategoryDto The DTO containing information about the property restriction category to be created.
	 * @return ResponseEntity indicating the status of the request.
	 */
	@Operation(summary = "Create Property Restriction Category REST API", description = "REST API to create Property Restriction Category")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
		@ApiResponse(responseCode = "409", description = "HTTP Status CONFLICT"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@PostMapping("/create")
	public ResponseEntity<?> addPropertyRestrictionCategory(
			@Valid @RequestBody PropertyRestrictionCategoryDto propertyRestrictionCategoryDto) {

		categoryService.create(propertyRestrictionCategoryDto);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(AppConstants.STATUS_201, AppConstants.CREATE_PROPERTY_RSTR_CATG_MESSAGE));

	}
	/**
	 * Endpoint to fetch property restriction category details by category ID.
	 *
	 * @param categoryId The ID of the property restriction category to fetch.
	 * @return ResponseEntity containing the property restriction category details.
	 */
	@Operation(summary = "Fetch Property Restriction Category Details By Category Id REST API", description = "REST API to fetch Property Restriction Category Details By based on a Category Id")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@GetMapping("/fetch/{id}")
	public ResponseEntity<?> getPropertyRestrictionCategoryById(@PathVariable("id") Integer CategoryId) {

		PropertyRestrictionCategoryDto propRestCategoryDto = categoryService.getById(CategoryId);

		return ResponseEntity.status(HttpStatus.OK).body(propRestCategoryDto);

	}
	/**
	 * Endpoint to update property restriction category description by category ID and category type.
	 *
	 * @param propertyRestrictionCategoryDto The DTO containing updated information about the property restriction category.
	 * @param categoryId                     The ID of the property restriction category to update.
	 * @return ResponseEntity indicating the status of the request.
	 */

	
	@Operation(summary = "Update Property Restriction Category Description Details By Category Id & Category Type REST API", description = "REST API to update Property Restriction Category Description   based on a Category Id and Category Type")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updatePropertyRestrictionCategory(
			@Valid @RequestBody PropertyRestrictionCategoryDto propertyRestrictionCategoryDto,
			@PathVariable("id") Integer CategoryId) {

		categoryService.update(propertyRestrictionCategoryDto, CategoryId);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDto(AppConstants.STATUS_200, AppConstants.UPDATE_MESSAGE));

	}
	/**
	 * Endpoint to fetch all property restriction category details.
	 *
	 * @return ResponseEntity containing a list of property restriction category types.
	 */
	
	@Operation(summary = "Fetch All Property Restriction Category Types REST API", description = "REST API to fetch  Property Restriction Category Details")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@GetMapping("/fetch/category-types")
	public ResponseEntity<?> getAllCategoryTypes(){
		
		List<String> categoryTypes = categoryService.findAllCategoryTypes();
		
		return ResponseEntity.status(HttpStatus.OK).body(categoryTypes);
		
	}
	/**
	 * Endpoint to update property restriction category type by category ID.
	 *
	 * @param requestCategoryType The request body containing the new category type.
	 * @param categoryId          The ID of the property restriction category to update.
	 * @return ResponseEntity indicating the status of the request.
	 */
	
	
	@Operation(summary = "Update Property Restriction Category Type By Category Id REST API", description = "REST API to update Property Restriction Category Type   based on a Category Id")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@PutMapping("/update/category-type/{id}")
	public ResponseEntity<?> updatePropertyRestrictionCategoryType(
			@RequestBody CatTypeReq RequestCategoryType,
			@PathVariable("id") Integer CategoryId) {
		
		String categoryType = RequestCategoryType.getCategoryType();

		categoryService.updateCategoryType(categoryType,CategoryId);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDto(AppConstants.STATUS_200, AppConstants.UPDATE_MESSAGE));

	}

}
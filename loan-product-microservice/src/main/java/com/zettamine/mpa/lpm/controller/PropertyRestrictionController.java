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
import com.zettamine.mpa.lpm.model.ErrorResponseDto;
import com.zettamine.mpa.lpm.model.PropertyRestrictionDto;
import com.zettamine.mpa.lpm.model.ResponseDto;
import com.zettamine.mpa.lpm.service.IPropertyRestrictionService;
import com.zettamine.mpa.lpm.util.RestrictionTypeReq;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/loan-product/restr")
@AllArgsConstructor
public class PropertyRestrictionController {

	private IPropertyRestrictionService restrService;

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyRestrictionController.class);

	@Operation(summary = "Create a Property Restriction Rest Api", description = "REST API to create a new Property Restriction")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
		@ApiResponse(responseCode = "409", description = "HTTP Status CONFLICT"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@PostMapping("/create")
	public ResponseEntity<ResponseDto> createPropertyRestriction(@Valid @RequestBody PropertyRestrictionDto rstrDto) {

		restrService.createPropertyRestriction(rstrDto);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(AppConstants.STATUS_201, AppConstants.CREATE_PROPERTY_RSTR_MESSAGE));
	}

	@Operation(summary = "Update a Property Restriction Details By Property Restriction Id REST API", description = "REST API to update Property Restriction Details based on a Property Restriction Id")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@PutMapping("/update/{id}")
	public ResponseEntity<ResponseDto> updatePropertyRestriction(@Valid @PathVariable("id") Integer id,
			@Valid @RequestBody PropertyRestrictionDto rstrDto) {

		restrService.updatePropertyRestriction(rstrDto, id);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDto(AppConstants.STATUS_200, AppConstants.UPDATE_PROPERTY_RSTR_MESSAGE));
	}
	
	
	@Operation(summary = "Fetch Property Restriction  details By Property Restriction Id REST API", description = "REST API to fetch Property Restriction  Details based on a Property Restriction Id ")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@GetMapping("/fetch/{id}")
	public ResponseEntity<PropertyRestrictionDto> getPropertyRestriction(@Valid @PathVariable("id") Integer id) {
		PropertyRestrictionDto restrictionDto = restrService.getPropertyRestrictionById(id);
		return ResponseEntity.status(HttpStatus.OK).body(restrictionDto);
	}

	@Operation(summary = "Fetch All Property Restriction's Details  REST API", description = "REST API to fetch All Property Restriction's details  Available ")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@GetMapping("/fetch")
	public ResponseEntity<List<PropertyRestrictionDto>> getAllPropertyRestriction() {
		List<PropertyRestrictionDto> allPropertyRestriction = restrService.getAllPropertyRestriction();
		return ResponseEntity.status(HttpStatus.OK).body(allPropertyRestriction);
	}
	
	@Operation(summary = "Fetch All Property Restriction Type's REST API", description = "REST API to fetch All Property Restriction Type's  Details")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@GetMapping("/fetch/rstr-types")
	public ResponseEntity<?> getAllRestrictionTypes(){
		
		List<String> restrictionTypes = restrService.getAllRestrictionTypes();
		
		return ResponseEntity.status(HttpStatus.OK).body(restrictionTypes);
	}
	
	@Operation(summary = "Update a Property Restriction type By Property Restriction Id REST API", description = "REST API to update Property Restriction type based on a Property Restriction Id")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
			@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))) })
	@PutMapping("/update/rstr-type/{rstrId}")
	public ResponseEntity<?> updateRestrictionType(@PathVariable("rstrId") Integer restrictionId,
			@RequestBody RestrictionTypeReq restrictionType) {

		restrService.updateRestrictionType(restrictionId, restrictionType);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDto(AppConstants.STATUS_200, AppConstants.RSTR_UPDATED_SUCCESS));
	}

}
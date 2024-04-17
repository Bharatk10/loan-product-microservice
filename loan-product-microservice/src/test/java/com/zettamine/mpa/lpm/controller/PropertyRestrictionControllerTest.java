package com.zettamine.mpa.lpm.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zettamine.mpa.lpm.constants.AppConstants;
import com.zettamine.mpa.lpm.exception.GlobalExceptionHandler;
import com.zettamine.mpa.lpm.exception.MismatchFoundException;
import com.zettamine.mpa.lpm.exception.ResourceAlreadyExistsException;
import com.zettamine.mpa.lpm.exception.ResourceNotFoundException;
import com.zettamine.mpa.lpm.model.PropertyRestrictionDto;
import com.zettamine.mpa.lpm.service.IPropertyRestrictionService;
import com.zettamine.mpa.lpm.util.RestrictionTypeReq;

@AutoConfigureMockMvc
@SpringBootTest
public class PropertyRestrictionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private IPropertyRestrictionService restrService;

	@InjectMocks
	private PropertyRestrictionController controller;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();
	}

	@Test
	public void testCreatePropertyRestriction() throws Exception {
		PropertyRestrictionDto dto = new PropertyRestrictionDto();
		dto.setRestrictionType("one");
		dto.setRestrictionDescription("Number one");
		dto.setCategoryType("first category");

		doNothing().when(restrService).createPropertyRestriction(dto);

		mockMvc.perform(post("/api/v1/loan-product/restr/create").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.statusCode").value(AppConstants.STATUS_201))
				.andExpect(jsonPath("$.statusMsg").value(AppConstants.CREATE_PROPERTY_RSTR_MESSAGE));

		verify(restrService, times(1)).createPropertyRestriction(dto);
	}

	@Test
	public void testUpdatePropertyRestriction() throws Exception {
		PropertyRestrictionDto dto = new PropertyRestrictionDto();

		dto.setRestrictionType("one");
		dto.setRestrictionDescription("Number one");
		dto.setCategoryType("first category");
		doNothing().when(restrService).updatePropertyRestriction(any(PropertyRestrictionDto.class), anyInt());

		mockMvc.perform(put("/api/v1/loan-product/restr/update/{id}", 1).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk())
				.andExpect(jsonPath("$.statusCode").value(AppConstants.STATUS_200))
				.andExpect(jsonPath("$.statusMsg").value(AppConstants.UPDATE_PROPERTY_RSTR_MESSAGE));

		verify(restrService, times(1)).updatePropertyRestriction(any(PropertyRestrictionDto.class), anyInt());
	}

	@Test
	public void testGetPropertyRestriction() throws Exception {

		PropertyRestrictionDto dto = new PropertyRestrictionDto(); // Populate with test data

		dto.setRestrictionType("one");
		dto.setRestrictionDescription("Number one");
		dto.setCategoryType("first category");

		when(restrService.getPropertyRestrictionById(anyInt())).thenReturn(dto);

		mockMvc.perform(get("/api/v1/loan-product/restr/fetch/{id}", 1)).andExpect(status().isOk())
				.andExpect(jsonPath("$.restrictionType").value("one"))
				.andExpect(jsonPath("$.restrictionDescription").value("Number one"))
				.andExpect(jsonPath("$.categoryType").value("first category"));

		verify(restrService, times(1)).getPropertyRestrictionById(anyInt());
	}

	@Test
	public void testGetAllPropertyRestriction() throws Exception {
		List<PropertyRestrictionDto> dtoList = Arrays.asList(new PropertyRestrictionDto(),
				new PropertyRestrictionDto()); // Populate with test data
		when(restrService.getAllPropertyRestriction()).thenReturn(dtoList);

		mockMvc.perform(get("/api/v1/loan-product/restr/fetch")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(dtoList.size()));

		verify(restrService, times(1)).getAllPropertyRestriction();
	}

	@Test
	public void testGetAllRestrictionTypes() throws Exception {
		List<String> types = Arrays.asList("Type1", "Type2");
		when(restrService.getAllRestrictionTypes()).thenReturn(types);

		mockMvc.perform(get("/api/v1/loan-product/restr/fetch/rstr-types")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(types.size()));

		verify(restrService, times(1)).getAllRestrictionTypes();
	}

	@Test
	public void testCreatePropertyRestriction_ResourceAlreadyExistsException() throws Exception {
		PropertyRestrictionDto dto = new PropertyRestrictionDto();
		dto.setRestrictionType("one");
		dto.setRestrictionDescription("Number one");
		dto.setCategoryType("first category");
		doThrow(new ResourceAlreadyExistsException(
				String.format(AppConstants.PROP_RSTR_EXISTS_MSG, dto.getRestrictionType()))).when(restrService)
				.createPropertyRestriction(any(PropertyRestrictionDto.class));

		mockMvc.perform(post("/api/v1/loan-product/restr/create").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isConflict())
				.andExpect(jsonPath("$.apiPath").exists())
				.andExpect(jsonPath("$.errorCode").value(HttpStatus.CONFLICT.name()))
				.andExpect(jsonPath("$.errorMessage")
						.value(String.format(AppConstants.PROP_RSTR_EXISTS_MSG, dto.getRestrictionType())))
				.andExpect(jsonPath("$.errorTime").exists());
		;

		verify(restrService, times(1)).createPropertyRestriction(any(PropertyRestrictionDto.class));
	}

	@Test
	public void testUpdatePropertyRestriction_ResourceNotFoundException() throws Exception {
		PropertyRestrictionDto dto = new PropertyRestrictionDto();
		dto.setRestrictionType("one");
		dto.setRestrictionDescription("Number one");
		dto.setCategoryType("first category");
		Integer propRestId = 1;
		doThrow(new ResourceNotFoundException(
				String.format(AppConstants.PROP_RSTR_NOT_EXISTS_BY_ID_MSG, propRestId.toString()))).when(restrService)
				.updatePropertyRestriction(any(PropertyRestrictionDto.class), anyInt());

		mockMvc.perform(put("/api/v1/loan-product/restr/update/{id}", 1).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.apiPath").exists())
				.andExpect(jsonPath("$.errorCode").value(HttpStatus.BAD_REQUEST.name()))
				.andExpect(jsonPath("$.errorMessage")
						.value(String.format(AppConstants.PROP_RSTR_NOT_EXISTS_BY_ID_MSG, propRestId.toString())))
				.andExpect(jsonPath("$.errorTime").exists());
		;

		verify(restrService, times(1)).updatePropertyRestriction(any(PropertyRestrictionDto.class), anyInt());
	}

	@Test
	public void testUpdatePropertyRestriction_MismatchFoundException() throws Exception {
		PropertyRestrictionDto dto = new PropertyRestrictionDto(); // Populate with test data
		dto.setRestrictionType("one");
		dto.setRestrictionDescription("Number one");
		dto.setCategoryType("first category");
		Integer propRestId = 1;

		doThrow(new MismatchFoundException(AppConstants.PROP_RSTR_TYPE, AppConstants.PROP_RSTR_ID,
				AppConstants.PROP_RSTR)).when(restrService).updatePropertyRestriction(dto, propRestId);

		String expectedErrorMessage = new MismatchFoundException(AppConstants.PROP_RSTR_TYPE, AppConstants.PROP_RSTR_ID,
				AppConstants.PROP_RSTR).getMessage();

		mockMvc.perform(put("/api/v1/loan-product/restr/update/" + propRestId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.apiPath").exists())
				.andExpect(jsonPath("$.errorCode").value(HttpStatus.BAD_REQUEST.name()))
				.andExpect(jsonPath("$.errorMessage").value(expectedErrorMessage))
				.andExpect(jsonPath("$.errorTime").exists());
		;

		verify(restrService, times(1)).updatePropertyRestriction(any(PropertyRestrictionDto.class), anyInt());
	}

	@Test
	public void testGetPropertyRestriction_ResourceNotFoundException() throws Exception {
		Integer propRestId = 1;
		when(restrService.getPropertyRestrictionById(anyInt())).thenThrow(new ResourceNotFoundException(
				String.format(AppConstants.PROP_RSTR_NOT_EXISTS_BY_ID_MSG, propRestId.toString())));

		mockMvc.perform(get("/api/v1/loan-product/restr/fetch/{id}", 1)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.apiPath").exists())
				.andExpect(jsonPath("$.errorCode").value(HttpStatus.BAD_REQUEST.name()))
				.andExpect(jsonPath("$.errorMessage")
						.value(String.format(AppConstants.PROP_RSTR_NOT_EXISTS_BY_ID_MSG, propRestId.toString())))
				.andExpect(jsonPath("$.errorTime").exists());
		;

		verify(restrService, times(1)).getPropertyRestrictionById(anyInt());
	}
	
	@Test
    public void updateRestrictionType_ResourceNotFoundException() throws Exception {
        RestrictionTypeReq req = new RestrictionTypeReq();
        req.setRestrictionType("NewType");
        
        Integer propRestId =1 ;

        doThrow(new ResourceNotFoundException(String.format(AppConstants.PROP_RSTR_NOT_EXISTS_BY_ID_MSG, propRestId.toString())))
                .when(restrService).updateRestrictionType(anyInt(), any(RestrictionTypeReq.class));

        mockMvc.perform(put("/api/v1/loan-product/restr/update/rstr-type/{rstrId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
	.andExpect(jsonPath("$.apiPath").exists())
	.andExpect(jsonPath("$.errorCode").value(HttpStatus.BAD_REQUEST.name()))
	.andExpect(jsonPath("$.errorMessage")
			.value(String.format(AppConstants.PROP_RSTR_NOT_EXISTS_BY_ID_MSG, propRestId.toString())))
	.andExpect(jsonPath("$.errorTime").exists());
    }

    @Test
    public void updateRestrictionType_ResourceAlreadyExistsException() throws Exception {
        RestrictionTypeReq req = new RestrictionTypeReq();
        req.setRestrictionType("ExistingType");

        doThrow(new ResourceAlreadyExistsException(
				String.format(AppConstants.PROP_RSTR_EXISTS_MSG, req.getRestrictionType())))
                .when(restrService).updateRestrictionType(anyInt(), any(RestrictionTypeReq.class));

        mockMvc.perform(put("/api/v1/loan-product/restr/update/rstr-type/{rstrId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(jsonPath("$.apiPath").exists())
		.andExpect(jsonPath("$.errorCode").value(HttpStatus.CONFLICT.name()))
		.andExpect(jsonPath("$.errorMessage")
				.value(String.format(AppConstants.PROP_RSTR_EXISTS_MSG, req.getRestrictionType())))
		.andExpect(jsonPath("$.errorTime").exists());
    }

    @Test
    public void updateRestrictionType_Success() throws Exception {
        RestrictionTypeReq req = new RestrictionTypeReq();
        req.setRestrictionType("NewValidType");

        doNothing().when(restrService).updateRestrictionType(anyInt(), any(RestrictionTypeReq.class));

        mockMvc.perform(put("/api/v1/loan-product/restr/update/rstr-type/{rstrId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(AppConstants.STATUS_200))
                .andExpect(jsonPath("$.statusMsg").value(AppConstants.RSTR_UPDATED_SUCCESS));
    }
    
 

}

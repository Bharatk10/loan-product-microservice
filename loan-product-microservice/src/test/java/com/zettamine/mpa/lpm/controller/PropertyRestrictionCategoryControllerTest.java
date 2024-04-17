package com.zettamine.mpa.lpm.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zettamine.mpa.lpm.constants.AppConstants;
import com.zettamine.mpa.lpm.exception.GlobalExceptionHandler;
import com.zettamine.mpa.lpm.exception.MismatchFoundException;
import com.zettamine.mpa.lpm.exception.ResourceAlreadyExistsException;
import com.zettamine.mpa.lpm.exception.ResourceNotFoundException;
import com.zettamine.mpa.lpm.model.PropertyRestrictionCategoryDto;
import com.zettamine.mpa.lpm.service.IPropertyRestrictionCategoryService;
import com.zettamine.mpa.lpm.util.CatTypeReq;

@AutoConfigureMockMvc
@SpringBootTest
public class PropertyRestrictionCategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private IPropertyRestrictionCategoryService categoryService;

	@InjectMocks
	private PropertyRestrictionCategoryController controller;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();
	}

	@Test
	public void testAddPropertyRestrictionCategoryValid() throws Exception {

		PropertyRestrictionCategoryDto propertyRestrictionCategoryDto = new PropertyRestrictionCategoryDto();
		propertyRestrictionCategoryDto.setCategoryType("one");
		propertyRestrictionCategoryDto.setCategoryDescription("number one");

		doNothing().when(categoryService).create(propertyRestrictionCategoryDto);

		MockHttpServletRequestBuilder requestBuilder = post("/api/v1/loan-product/restr-ctrgy/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(propertyRestrictionCategoryDto));

		mockMvc.perform(requestBuilder).andExpect(status().isCreated())
				.andExpect(jsonPath("$.statusCode").value(AppConstants.STATUS_201))
				.andExpect(jsonPath("$.statusMsg").value(AppConstants.CREATE_PROPERTY_RSTR_CATG_MESSAGE));
	}

	@Test
	public void testAddPropertyRestrictionCategoryTypeAlreadyExists() throws Exception {

		PropertyRestrictionCategoryDto propertyRestrictionCategoryDto = new PropertyRestrictionCategoryDto();
		propertyRestrictionCategoryDto.setCategoryType("one");
		propertyRestrictionCategoryDto.setCategoryDescription("number one");

		doThrow(new ResourceAlreadyExistsException(String.format(AppConstants.PROP_RSTR_CATG_EXISTS_MSG,
				propertyRestrictionCategoryDto.getCategoryType()))).when(categoryService)
				.create(propertyRestrictionCategoryDto);

		MockHttpServletRequestBuilder requestBuilder = post("/api/v1/loan-product/restr-ctrgy/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(propertyRestrictionCategoryDto));

		mockMvc.perform(requestBuilder).andExpect(status().isConflict()).andExpect(jsonPath("$.apiPath").exists())
				.andExpect(jsonPath("$.errorCode").value(HttpStatus.CONFLICT.name()))
				.andExpect(jsonPath("$.errorMessage").value(String.format(AppConstants.PROP_RSTR_CATG_EXISTS_MSG,
						propertyRestrictionCategoryDto.getCategoryType())))
				.andExpect(jsonPath("$.errorTime").exists());
	}

	@Test
	public void testGetPropertyRestrictionCategoryById_valid() throws Exception {

		Integer categoryId = 1;
		PropertyRestrictionCategoryDto propertyRestrictionCategoryDto = new PropertyRestrictionCategoryDto();
		propertyRestrictionCategoryDto.setCategoryType("one");
		propertyRestrictionCategoryDto.setCategoryDescription("number one");

		when(categoryService.getById(categoryId)).thenReturn(propertyRestrictionCategoryDto);

		mockMvc.perform(get("/api/v1/loan-product/restr-ctrgy/fetch/" + categoryId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.categoryType").value("one"))
				.andExpect(jsonPath("$.categoryDescription").value("number one"));

	}

	@Test
	public void testGetPropertyRestrictionCategoryByIdInvalidId() throws Exception {

		Integer categoryId = 1;

		doThrow(new ResourceNotFoundException(
				String.format(AppConstants.PROP_RSTR_CATG_NOT_EXISTS_BY_ID_MSG, categoryId.toString())))
				.when(categoryService).getById(categoryId);

		mockMvc.perform(get("/api/v1/loan-product/restr-ctrgy/fetch/" + categoryId)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.apiPath").exists())
				.andExpect(jsonPath("$.errorCode").value(HttpStatus.BAD_REQUEST.name()))
				.andExpect(jsonPath("$.errorMessage")
						.value(String.format(AppConstants.PROP_RSTR_CATG_NOT_EXISTS_BY_ID_MSG, categoryId.toString())))
				.andExpect(jsonPath("$.errorTime").exists());
	}

	@Test
	public void testUpdatePropertyRestrictionCategory() throws Exception {
		int categoryId = 1;
		PropertyRestrictionCategoryDto propertyRestrictionCategoryDto = new PropertyRestrictionCategoryDto();
		propertyRestrictionCategoryDto.setCategoryType("updated");
		propertyRestrictionCategoryDto.setCategoryDescription("updated description");

		doNothing().when(categoryService).update(propertyRestrictionCategoryDto, categoryId);

		mockMvc.perform(
				put("/api/v1/loan-product/restr-ctrgy/update/" + categoryId).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(propertyRestrictionCategoryDto)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(AppConstants.STATUS_200))
				.andExpect(jsonPath("$.statusMsg").value(AppConstants.UPDATE_MESSAGE));
	}

	@Test
	public void testUpdatePropertyRestrictionCategoryInvalidId() throws Exception {

		Integer categoryId = 1;

		PropertyRestrictionCategoryDto propertyRestrictionCategoryDto = new PropertyRestrictionCategoryDto();
		propertyRestrictionCategoryDto.setCategoryType("update");
		propertyRestrictionCategoryDto.setCategoryDescription("updated description");

		doThrow(new ResourceNotFoundException(
				String.format(AppConstants.PROP_RSTR_CATG_NOT_EXISTS_BY_ID_MSG, categoryId.toString())))
				.when(categoryService).update(propertyRestrictionCategoryDto, categoryId);

		MockHttpServletRequestBuilder requestBuilder = put("/api/v1/loan-product/restr-ctrgy/update/" + categoryId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(propertyRestrictionCategoryDto));

		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest()).andExpect(jsonPath("$.apiPath").exists())
				.andExpect(jsonPath("$.errorCode").value(HttpStatus.BAD_REQUEST.name()))
				.andExpect(jsonPath("$.errorMessage")
						.value(String.format(AppConstants.PROP_RSTR_CATG_NOT_EXISTS_BY_ID_MSG, categoryId.toString())))
				.andExpect(jsonPath("$.errorTime").exists());

	}

	@Test
	public void testUpdatePropertyRestrictionCategoryInvalidCategoryType() throws Exception {

		Integer categoryId = 1;

		PropertyRestrictionCategoryDto propertyRestrictionCategoryDto = new PropertyRestrictionCategoryDto();
		propertyRestrictionCategoryDto.setCategoryType("update");
		propertyRestrictionCategoryDto.setCategoryDescription("updated description");

		doThrow(new ResourceNotFoundException(String.format(AppConstants.PROP_RSTR_CATG_NOT_EXISTS_MSG,
				propertyRestrictionCategoryDto.getCategoryType()))).when(categoryService)
				.update(propertyRestrictionCategoryDto, categoryId);

		MockHttpServletRequestBuilder requestBuilder = put("/api/v1/loan-product/restr-ctrgy/update/" + categoryId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(propertyRestrictionCategoryDto));

		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest()).andExpect(jsonPath("$.apiPath").exists())
				.andExpect(jsonPath("$.errorCode").value(HttpStatus.BAD_REQUEST.name()))
				.andExpect(jsonPath("$.errorMessage").value(String.format(AppConstants.PROP_RSTR_CATG_NOT_EXISTS_MSG,
						propertyRestrictionCategoryDto.getCategoryType())))
				.andExpect(jsonPath("$.errorTime").exists());

	}

	@Test
	public void testUpdatePropertyRestrictionCategoryCategoryTypeMismatch() throws Exception {

		Integer categoryId = 1;

		PropertyRestrictionCategoryDto propertyRestrictionCategoryDto = new PropertyRestrictionCategoryDto();
		propertyRestrictionCategoryDto.setCategoryType("update");
		propertyRestrictionCategoryDto.setCategoryDescription("updated description");

		doThrow(new MismatchFoundException(AppConstants.PROP_CATG_TYPE, AppConstants.PROP_CATG_ID,
				AppConstants.PROP_RSTR_CATG)).when(categoryService).update(propertyRestrictionCategoryDto, categoryId);

		MockHttpServletRequestBuilder requestBuilder = put("/api/v1/loan-product/restr-ctrgy/update/" + categoryId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(propertyRestrictionCategoryDto));

		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest()).andExpect(jsonPath("$.apiPath").exists())
				.andExpect(jsonPath("$.errorCode").value(HttpStatus.BAD_REQUEST.name()))
				.andExpect(jsonPath("$.errorMessage").value(String.format("%s is mismatched with %s of resource %s",
						AppConstants.PROP_CATG_TYPE, AppConstants.PROP_CATG_ID, AppConstants.PROP_RSTR_CATG)))
				.andExpect(jsonPath("$.errorTime").exists());

	}
	
	@Test
	public void testGetAllCategoryTypes() throws Exception{
		
		List<String> types = new ArrayList<>();
		types.add("one");
		types.add("two");
		
		when(categoryService.findAllCategoryTypes()).thenReturn(types);
		
		mockMvc.perform(get("/api/v1/loan-product/restr-ctrgy/fetch/category-types")).andExpect(status().isOk());
		
		
	}
	
	@Test
	public void updatePropertyRestrictionCategoryTypeSuccess() throws Exception{
		
		CatTypeReq  catTypeReq = new CatTypeReq();
		
		catTypeReq.setCategoryType("one");
		
		MockHttpServletRequestBuilder requestBuilder = put("/api/v1/loan-product/restr-ctrgy/update/category-type/{id}" , 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(catTypeReq));
		
		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(AppConstants.STATUS_200))
				.andExpect(jsonPath("$.statusMsg").value(AppConstants.UPDATE_MESSAGE));
		
	}
	
}
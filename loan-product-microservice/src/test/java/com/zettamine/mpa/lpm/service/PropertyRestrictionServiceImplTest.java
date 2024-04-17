package com.zettamine.mpa.lpm.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.zettamine.mpa.lpm.entity.PropertyRestriction;
import com.zettamine.mpa.lpm.entity.PropertyRestrictionCategory;
import com.zettamine.mpa.lpm.exception.MismatchFoundException;
import com.zettamine.mpa.lpm.exception.ResourceAlreadyExistsException;
import com.zettamine.mpa.lpm.exception.ResourceNotFoundException;
import com.zettamine.mpa.lpm.model.PropertyRestrictionDto;
import com.zettamine.mpa.lpm.repository.PropertyRestrictionCategoryRepository;
import com.zettamine.mpa.lpm.repository.PropertyRestrictionRepository;
import com.zettamine.mpa.lpm.util.RestrictionTypeReq;

@SpringBootTest
public class PropertyRestrictionServiceImplTest {

	@Mock
	private PropertyRestrictionRepository propRestrRepo;

	@Mock
	private PropertyRestrictionCategoryRepository propRestrCatgRepo;

	@InjectMocks
	private PropertyRestrictionServiceImpl service;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void createPropertyRestriction_ThrowsResourceAlreadyExistsException() {
		PropertyRestrictionDto dto = new PropertyRestrictionDto();
		dto.setCategoryType("Test Category");
		dto.setRestrictionType("Test Type");

		when(propRestrCatgRepo.findByCategoryType(anyString()))
				.thenReturn(Optional.of(new PropertyRestrictionCategory()));
		when(propRestrRepo.findByRestrictionType(anyString())).thenReturn(Optional.of(new PropertyRestriction()));

		assertThrows(ResourceAlreadyExistsException.class, () -> service.createPropertyRestriction(dto));
	}

	@Test
	public void createPropertyRestriction_ThrowsResourceAlreadyExistsExceptionCategoryType() {
		PropertyRestrictionDto dto = new PropertyRestrictionDto();
		dto.setCategoryType("Test Category");
		dto.setRestrictionType("Test Type");

		when(propRestrCatgRepo.findByCategoryType(anyString())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.createPropertyRestriction(dto));
	}

	@Test
	public void createPropertyRestriction_Success() {
		PropertyRestrictionDto dto = new PropertyRestrictionDto();
		dto.setCategoryType("Test Category");
		dto.setRestrictionType("Test Type");

		when(propRestrCatgRepo.findByCategoryType(anyString()))
				.thenReturn(Optional.of(new PropertyRestrictionCategory()));
		when(propRestrRepo.findByRestrictionType(anyString())).thenReturn(Optional.empty());

		assertDoesNotThrow(() -> service.createPropertyRestriction(dto));

		verify(propRestrRepo).save(any(PropertyRestriction.class));
	}

	@Test
	public void updatePropertyRestriction_ThrowsResourceNotFoundExceptionPropertyId() {
		PropertyRestrictionDto dto = new PropertyRestrictionDto();
		dto.setCategoryType("Test Category");
		dto.setRestrictionType("Test Type");

		when(propRestrRepo.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.updatePropertyRestriction(dto, 1));
	}

	@Test
	public void updatePropertyRestriction_ThrowsResourceNotFoundExceptionCategoryType() {
		PropertyRestrictionDto dto = new PropertyRestrictionDto();
		dto.setCategoryType("Test Category");
		dto.setRestrictionType("Test Type");

		when(propRestrRepo.findById(anyInt())).thenReturn(Optional.of(new PropertyRestriction()));
		when(propRestrRepo.findByRestrictionType(anyString())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.updatePropertyRestriction(dto, 1));
	}

	@Test
	public void getPropertyRestrictionById_ThrowsResourceNotFoundException() {
		when(propRestrRepo.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.getPropertyRestrictionById(1));
	}

	@Test
	public void getPropertyRestrictionById_success() {

		PropertyRestriction propertyRestrictionOne = new PropertyRestriction();

		PropertyRestrictionCategory propertyRestrictionCategory = new PropertyRestrictionCategory();

		propertyRestrictionOne.setRestrictionCategory(propertyRestrictionCategory);

		when(propRestrRepo.findById(anyInt())).thenReturn(Optional.of(propertyRestrictionOne));

		assertDoesNotThrow(() -> service.getPropertyRestrictionById(anyInt()));

	}

	@Test
	public void deletePropertyRestrictionById_ThrowsResourceNotFoundException() {
		when(propRestrRepo.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.deletePropertyRestrictionById(1));
	}

	@Test
	public void getAllPropertyRestriction_Success() {

		PropertyRestriction propertyRestrictionOne = new PropertyRestriction();

		PropertyRestriction propertyRestrictionTwo = new PropertyRestriction();

		PropertyRestrictionCategory propertyRestrictionCategory = new PropertyRestrictionCategory();

		propertyRestrictionOne.setRestrictionCategory(propertyRestrictionCategory);

		propertyRestrictionTwo.setRestrictionCategory(propertyRestrictionCategory);

		when(propRestrRepo.findAll()).thenReturn(Arrays.asList(propertyRestrictionOne, propertyRestrictionTwo));

		List<PropertyRestrictionDto> result = service.getAllPropertyRestriction();

		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test
	public void getAllRestrictionTypes_Success() {

		when(propRestrRepo.findAllRestrictionTypes()).thenReturn(Arrays.asList("Type1", "Type2"));

		List<String> result = service.getAllRestrictionTypes();

		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test
	public void updatePropertyRestriction_ThrowsMismatchFoundExceptionPropertyType() {
		PropertyRestrictionDto dto = new PropertyRestrictionDto();
		dto.setCategoryType("Test Category");
		dto.setRestrictionType("New Type");

		PropertyRestriction existingRestriction = new PropertyRestriction();
		PropertyRestrictionCategory category = new PropertyRestrictionCategory();
		category.setCategoryType("Test Category");
		existingRestriction.setRestrictionCategory(category);
		existingRestriction.setRestrictionType("Old Type");

		when(propRestrRepo.findById(anyInt())).thenReturn(Optional.of(existingRestriction));
		when(propRestrCatgRepo.findByCategoryType(anyString())).thenReturn(Optional.of(category));

		assertThrows(MismatchFoundException.class, () -> service.updatePropertyRestriction(dto, 1));
	}

	@Test
	public void updatePropertyRestriction_ThrowsMismatchFoundExceptionCategoryType() {
		PropertyRestrictionDto dto = new PropertyRestrictionDto();
		dto.setCategoryType("Test Category");
		dto.setRestrictionType("New Type");

		PropertyRestriction existingRestriction = new PropertyRestriction();
		PropertyRestrictionCategory category = new PropertyRestrictionCategory();
		category.setCategoryType("Test Two Category");
		existingRestriction.setRestrictionCategory(category);
		existingRestriction.setRestrictionType("New Type");

		when(propRestrRepo.findById(anyInt())).thenReturn(Optional.of(existingRestriction));
		when(propRestrCatgRepo.findByCategoryType(anyString())).thenReturn(Optional.of(category));

		assertThrows(MismatchFoundException.class, () -> service.updatePropertyRestriction(dto, 1));
	}

	@Test
	public void updatePropertyRestriction_Success() {
		PropertyRestrictionDto dto = new PropertyRestrictionDto();
		dto.setCategoryType("Test Category");
		dto.setRestrictionType("Test Type");

		PropertyRestriction existingRestriction = new PropertyRestriction();
		PropertyRestrictionCategory category = new PropertyRestrictionCategory();
		category.setCategoryType("Test Category");
		existingRestriction.setRestrictionCategory(category);
		existingRestriction.setRestrictionType("Test Type");

		when(propRestrRepo.findById(anyInt())).thenReturn(Optional.of(existingRestriction));
		when(propRestrCatgRepo.findByCategoryType(anyString())).thenReturn(Optional.of(category));

		assertDoesNotThrow(() -> service.updatePropertyRestriction(dto, 1));
	}

	@Test
	public void updateRestrictionType_ThrowsResourceNotFoundExceptionRestId() {

		when(propRestrRepo.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class,
				() -> service.updateRestrictionType(1, new RestrictionTypeReq("New Type")));
	}

	@Test
	public void updateRestrictionType_ThrowsResourceAlreadyExistsExceptionRestType() {
		PropertyRestriction existingRestriction = new PropertyRestriction();
		existingRestriction.setRestrictionType("Old Type");

		when(propRestrRepo.findById(anyInt())).thenReturn(Optional.of(existingRestriction));

		when(propRestrRepo.findByRestrictionType(any(String.class))).thenReturn(Optional.of(new PropertyRestriction()));

		assertThrows(ResourceNotFoundException.class,
				() -> service.updateRestrictionType(1, new RestrictionTypeReq("New Type")));
	}

	@Test
	public void updateRestrictionType_NullPointerException() {

		when(propRestrRepo.findById(anyInt())).thenReturn(Optional.of(new PropertyRestriction()));

		assertThrows(NullPointerException.class, () -> service.updateRestrictionType(1, null));
	}

	@Test
	public void updateRestrictionType_Success() {
		PropertyRestriction existingRestriction = new PropertyRestriction();
		existingRestriction.setRestrictionType("Old Type");

		when(propRestrRepo.findById(anyInt())).thenReturn(Optional.of(existingRestriction));
		when(propRestrRepo.findByRestrictionType(anyString())).thenReturn(Optional.empty());

		assertDoesNotThrow(() -> service.updateRestrictionType(1, new RestrictionTypeReq("New Type")));

		verify(propRestrRepo, times(1)).save(existingRestriction);
	}

	@Test
	public void deletePropertyRestrictionById_Success() {
		PropertyRestriction existingRestriction = new PropertyRestriction();

		when(propRestrRepo.findById(anyInt())).thenReturn(Optional.of(existingRestriction));

		assertDoesNotThrow(() -> service.deletePropertyRestrictionById(1));
	}
}

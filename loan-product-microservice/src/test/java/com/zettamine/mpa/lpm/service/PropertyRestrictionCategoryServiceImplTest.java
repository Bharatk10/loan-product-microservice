package com.zettamine.mpa.lpm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.zettamine.mpa.lpm.mapper.PropertyRestrictionCategoryMapper;
import com.zettamine.mpa.lpm.model.PropertyRestrictionCategoryDto;
import com.zettamine.mpa.lpm.repository.PropertyRestrictionCategoryRepository;
import com.zettamine.mpa.lpm.util.CatTypeReq;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class PropertyRestrictionCategoryServiceImplTest {

	@Mock
	private PropertyRestrictionCategoryRepository propRestrCategoryRepo;

	@InjectMocks
	private PropertyRestrictionCategoryServiceImpl categoryService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateValid() {

		PropertyRestrictionCategoryDto propertyRestrictionCategory = new PropertyRestrictionCategoryDto();
		propertyRestrictionCategory.setCategoryType("new ");
		propertyRestrictionCategory.setCategoryDescription("To be created");

		when(propRestrCategoryRepo.findByCategoryType(any(String.class))).thenReturn(Optional.empty());

		when(propRestrCategoryRepo.save(any(PropertyRestrictionCategory.class)))
				.thenReturn(any(PropertyRestrictionCategory.class));

		categoryService.create(propertyRestrictionCategory);

		verify(propRestrCategoryRepo).save(any(PropertyRestrictionCategory.class));

	}

	@Test
	public void testCreateValidCategoryTypeExists() {

		PropertyRestrictionCategoryDto propertyRestrictionCategoryDto = new PropertyRestrictionCategoryDto();
		propertyRestrictionCategoryDto.setCategoryType("Existing");
		propertyRestrictionCategoryDto.setCategoryDescription("Already Exisiting");

		when(propRestrCategoryRepo.findByCategoryType(any(String.class)))
				.thenReturn(Optional.of(new PropertyRestrictionCategory()));

		assertThrows(ResourceAlreadyExistsException.class,
				() -> categoryService.create(propertyRestrictionCategoryDto));

	}

	@Test
	public void testGetByIdValid() {

		Integer categoryId = 1;

		PropertyRestrictionCategory existingCategory = new PropertyRestrictionCategory();
		existingCategory.setCategoryId(categoryId);
		existingCategory.setCategoryType("Residential");
		existingCategory.setCategoryDescription("Description for Residential");

		List<PropertyRestriction> restrictions = new ArrayList<>();

		PropertyRestriction restriction1 = new PropertyRestriction();
		restriction1.setRestrictionId(101);
		restriction1.setRestrictionType("Type1");
		restriction1.setRestrictionDescription("Description1");
		restriction1.setStatus('A');
		restriction1.setRestrictionCategory(existingCategory);
		restrictions.add(restriction1);

		PropertyRestriction restriction2 = new PropertyRestriction();
		restriction2.setRestrictionId(102);
		restriction2.setRestrictionType("Type2");
		restriction2.setRestrictionDescription("Description2");
		restriction2.setStatus('I');
		restriction2.setRestrictionCategory(existingCategory);
		restrictions.add(restriction2);

		existingCategory.setPropertyRestrictions(restrictions);

		when(propRestrCategoryRepo.findById(categoryId)).thenReturn(Optional.of(existingCategory));

		PropertyRestrictionCategoryDto restrictionCategorybyId = categoryService.getById(categoryId);

		assertEquals("Residential", restrictionCategorybyId.getCategoryType());
		assertEquals("Description for Residential", restrictionCategorybyId.getCategoryDescription());
		assertEquals(2, restrictionCategorybyId.getPropertyRestrictions().size());
		assertEquals("Type1", restrictionCategorybyId.getPropertyRestrictions().get(0).getRestrictionType());
		assertEquals("Type2", restrictionCategorybyId.getPropertyRestrictions().get(1).getRestrictionType());

		verify(propRestrCategoryRepo).findById(categoryId);

	}

	@Test
	public void testGetByIdInvalidId() {

		Integer categoryId = 1;

		when(propRestrCategoryRepo.findById(categoryId)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> categoryService.getById(categoryId));

		verify(propRestrCategoryRepo).findById(categoryId);

	}

	@Test
	public void testUpdateValid() {

		Integer categoryId = 1;

		PropertyRestrictionCategoryDto propertyRestrictionCategoryDto = new PropertyRestrictionCategoryDto();
		propertyRestrictionCategoryDto.setCategoryType("updated");
		propertyRestrictionCategoryDto.setCategoryDescription("updated description");

		PropertyRestrictionCategory restrictionCategory = PropertyRestrictionCategoryMapper
				.RestrictionCategoryDtoToRestrictionCategory(new PropertyRestrictionCategory(),
						propertyRestrictionCategoryDto);

		when(propRestrCategoryRepo.findById(categoryId)).thenReturn(Optional.of(restrictionCategory));

		when(propRestrCategoryRepo.findByCategoryType(any(String.class)))
				.thenReturn(Optional.of(new PropertyRestrictionCategory()));

		when(propRestrCategoryRepo.save(any(PropertyRestrictionCategory.class)))
				.thenReturn(any(PropertyRestrictionCategory.class));

		categoryService.update(propertyRestrictionCategoryDto, categoryId);

		verify(propRestrCategoryRepo).save(any(PropertyRestrictionCategory.class));

		verify(propRestrCategoryRepo).findByCategoryType(any(String.class));

		verify(propRestrCategoryRepo).findById(categoryId);
	}

	@Test
	public void testUpdateInValidId() {

		Integer categoryId = 1;

		PropertyRestrictionCategoryDto propertyRestrictionCategoryDto = new PropertyRestrictionCategoryDto();
		propertyRestrictionCategoryDto.setCategoryType("updated");
		propertyRestrictionCategoryDto.setCategoryDescription("updated description");

		when(propRestrCategoryRepo.findById(categoryId)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class,
				() -> categoryService.update(propertyRestrictionCategoryDto, categoryId));

		verify(propRestrCategoryRepo).findById(categoryId);

	}

	@Test
	public void testUpdateCategoryTypeNotFound() {

		Integer categoryId = 1;

		PropertyRestrictionCategoryDto propertyRestrictionCategoryDto = new PropertyRestrictionCategoryDto();
		propertyRestrictionCategoryDto.setCategoryType("updated");
		propertyRestrictionCategoryDto.setCategoryDescription("updated description");

		PropertyRestrictionCategory restrictionCategory = PropertyRestrictionCategoryMapper
				.RestrictionCategoryDtoToRestrictionCategory(new PropertyRestrictionCategory(),
						propertyRestrictionCategoryDto);

		when(propRestrCategoryRepo.findById(categoryId)).thenReturn(Optional.of(restrictionCategory));

		when(propRestrCategoryRepo.findByCategoryType(any(String.class))).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class,
				() -> categoryService.update(propertyRestrictionCategoryDto, categoryId));

		verify(propRestrCategoryRepo).findByCategoryType(any(String.class));

		verify(propRestrCategoryRepo).findById(categoryId);

	}

	@Test
	public void testUpdateMismatchException() {

		Integer categoryId = 1;

		PropertyRestrictionCategoryDto propertyRestrictionCategoryDto = new PropertyRestrictionCategoryDto();
		propertyRestrictionCategoryDto.setCategoryType("updated");
		propertyRestrictionCategoryDto.setCategoryDescription("updated description");

		PropertyRestrictionCategory restrictionCategory = PropertyRestrictionCategoryMapper
				.RestrictionCategoryDtoToRestrictionCategory(new PropertyRestrictionCategory(),
						propertyRestrictionCategoryDto);
		restrictionCategory.setCategoryType("mismatch");

		when(propRestrCategoryRepo.findById(categoryId)).thenReturn(Optional.of(restrictionCategory));

		when(propRestrCategoryRepo.findByCategoryType(any(String.class))).thenReturn(Optional.of(restrictionCategory));

		assertThrows(MismatchFoundException.class,
				() -> categoryService.update(propertyRestrictionCategoryDto, categoryId));

		verify(propRestrCategoryRepo).findByCategoryType(any(String.class));

		verify(propRestrCategoryRepo).findById(categoryId);

	}
	
	@Test
    public void testUpdateCategoryType_Success() {
       
        Integer categoryId = 1;
        String newType = "NewType";
       
		when(propRestrCategoryRepo.findById(categoryId)).thenReturn(Optional.of(new PropertyRestrictionCategory()));
		
		when(propRestrCategoryRepo.findByCategoryType(any(String.class))).thenReturn(Optional.empty());


        
        categoryService.updateCategoryType(newType, categoryId);

        verify(propRestrCategoryRepo, times(1)).save(any()); // Adjust according to actual method call
    }

    @Test
    public void testUpdateCategoryType_ResourceNotFoundException() {
      
        Integer categoryId = 1;
        String newType = "NewType";
       
        when(propRestrCategoryRepo.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
        	categoryService.updateCategoryType(newType,categoryId);
        });

        verify(propRestrCategoryRepo).findById(categoryId);
    }
    
    @Test
    public void testUpdateCategoryType_ResourceAlreadyExistsException() {
      
        Integer categoryId = 1;
        String newType = "NewType";
       
      

		when(propRestrCategoryRepo.findById(categoryId)).thenReturn(Optional.of(new PropertyRestrictionCategory()));

		when(propRestrCategoryRepo.findByCategoryType(any(String.class))).thenReturn(Optional.of(new PropertyRestrictionCategory()));

		assertThrows(ResourceAlreadyExistsException.class,
				() -> categoryService.updateCategoryType(newType, categoryId));

		verify(propRestrCategoryRepo).findByCategoryType(any(String.class));

		verify(propRestrCategoryRepo).findById(categoryId);
    }
}

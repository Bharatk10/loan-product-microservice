package com.zettamine.mpa.lpm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.zettamine.mpa.lpm.audit.AuditAwareImpl;
import com.zettamine.mpa.lpm.entity.PropertyRestrictionCategory;

@DataJpaTest
@Import(AuditAwareImpl.class)
class PropertyRestrictionCategoryRepositoryTest {

	@Autowired
	private PropertyRestrictionCategoryRepository categoryRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testFindByCategoryTypeValid() {

		PropertyRestrictionCategory propertyRestrictionCategory = new PropertyRestrictionCategory();
		propertyRestrictionCategory.setCategoryType("updated");
		propertyRestrictionCategory.setCategoryDescription("updated description");
		entityManager.persist(propertyRestrictionCategory);
		entityManager.flush();

		Optional<PropertyRestrictionCategory> categoryType = categoryRepo.findByCategoryType("updated");

		assertThat(categoryType.isPresent()).isTrue();
		assertThat(categoryType.get().getCategoryType()).isEqualTo("updated");
		assertThat(categoryType.get().getCategoryDescription()).isEqualTo("updated description");
	}

	@Test
	public void testFindByCategoryTypeNotValid() {

		String invalidCategoryType = "updated";

		Optional<PropertyRestrictionCategory> categoryType = categoryRepo.findByCategoryType(invalidCategoryType);

		assertThat(categoryType.isPresent()).isFalse();

	}
}
package com.zettamine.mpa.lpm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.zettamine.mpa.lpm.audit.AuditAwareImpl;
import com.zettamine.mpa.lpm.entity.PropertyRestriction;

@DataJpaTest
@Import(AuditAwareImpl.class)
public class PropertyRestrictionRepositoryTest {

	@Autowired
	private PropertyRestrictionRepository propertyRestrictionRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void findByRestrictionType_WhenExists() {

		PropertyRestriction propertyRestriction = new PropertyRestriction();
		propertyRestriction.setRestrictionType("TestType");
		entityManager.persist(propertyRestriction);
		entityManager.flush();

		Optional<PropertyRestriction> found = propertyRestrictionRepository.findByRestrictionType("TestType");

		assertThat(found.isPresent()).isTrue();
		assertThat(found.get().getRestrictionType()).isEqualTo("TestType");
	}

	@Test
	public void findByRestrictionType_WhenNotExists() {

		Optional<PropertyRestriction> found = propertyRestrictionRepository.findByRestrictionType("NonExistentType");

		assertThat(found.isPresent()).isFalse();
	}

	@Test
	public void findAllRestrictionTypes() {

		PropertyRestriction propertyRestriction1 = new PropertyRestriction();
		propertyRestriction1.setRestrictionType("Type1");
		entityManager.persist(propertyRestriction1);
		entityManager.flush();

		PropertyRestriction propertyRestriction2 = new PropertyRestriction();
		propertyRestriction2.setRestrictionType("Type2");
		propertyRestrictionRepository.save(propertyRestriction2);

		List<String> restrictionTypes = propertyRestrictionRepository.findAllRestrictionTypes();

		assertThat(restrictionTypes).hasSize(2);
		assertThat(restrictionTypes).containsExactlyInAnyOrder("Type1", "Type2");
	}
}
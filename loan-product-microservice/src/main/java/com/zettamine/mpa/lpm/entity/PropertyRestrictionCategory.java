package com.zettamine.mpa.lpm.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "prop_rstr_catg")
@Setter
@Getter
public class PropertyRestrictionCategory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "catg_id")
	private Integer categoryId;

	@Column(name = "catg_type")
	private String categoryType;

	@Column(name = "catg_desc")
	private String categoryDescription;

	@OneToMany(mappedBy = "restrictionCategory", cascade = CascadeType.ALL)
	private List<PropertyRestriction> propertyRestrictions;

}

package com.zettamine.mpa.lpm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "prop_rstr")
@Getter
@Setter
public class PropertyRestriction extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rstr_id")
	private Integer restrictionId;

	@ManyToOne
	@JoinColumn(name = "rstr_catg_id", referencedColumnName = "catg_id")
	private PropertyRestrictionCategory restrictionCategory;

	@Column(name = "rstr_type")
	private String restrictionType;

	@Column(name = "rstr_desc")
	private String restrictionDescription;

	@Column(name = "status")
	private Character status;

}

package com.zettamine.mpa.lpm.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class BaseEntity {

	@CreatedDate
	@Column(name = "created_on", updatable = false)
	private LocalDateTime createdOn;

	@CreatedBy
	@Column(name = "created_by", updatable = false)
	private Integer createdBy;

	@LastModifiedDate
	@Column(name = "updated_on", insertable = false)
	private LocalDateTime updatedOn;

	@LastModifiedBy
	@Column(name = "updated_by", insertable = false)
	private Integer updatedBy;
}

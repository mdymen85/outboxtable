package com.outboxtable.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "obt")
@AllArgsConstructor
@NoArgsConstructor
public class Outboxtable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID", nullable = false, unique = true)
	private Long id;
	
	@Column(name="IDENTITY", nullable = false, unique = true)
	private String identity;
	
	@Column(name="MESSAGE", nullable = false)
	private String message;
	
}

package com.outboxtable.base.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name = "obt_spending")
@Builder
public class Spending {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID", nullable = false, unique = true)
	private Long id;
	
	@Column(name="IDENTITY", nullable = false, unique = true)
	private String identity;
	
	@Column(name="NAME", nullable = false)
	private String name;
	
	@Column(name="SURNAME", nullable = false)
	private String surname;
	
	@Column(name="AMOUNT", nullable = false)
	private BigDecimal amount;
	
	
}
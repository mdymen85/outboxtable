package com.outboxtable.job.entity;

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
import lombok.ToString;

@Entity
@Data
@Builder
@Table(name = "obt")
@AllArgsConstructor
@NoArgsConstructor
@ToString
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

package com.outboxtable.base.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class SpendingDTO {

	private Long id;
	private String identity;
	private String name;
	private String surname;
	private BigDecimal amount;
	
	
}
package com.outboxtable.consumer.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SpendingDTO {

	private Long id;
	private String identity;
	private String name;
	private String surname;
	private BigDecimal amount;
	
	
}
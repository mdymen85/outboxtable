package com.outboxtable.consumer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OutboxtableDTO {

	private String identify;
	private String message;
	
}

package com.outboxtable.base.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.outboxtable.base.dto.OutboxtableDTO;
import com.outboxtable.base.entity.Outboxtable;
import com.outboxtable.base.service.OutboxtableService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OutboxtableController {

	private final OutboxtableService outboxtableService;
	
	@RequestMapping(method = RequestMethod.GET, path = "/api/v1/outboxtable")
	public List<OutboxtableDTO> getOutboxtableNotIntegrated() {
		return this.outboxtableService.getOutboxtableNotIntegrated().stream()
				.map(obt -> this.toOutboxtableDTO(obt))
				.collect(Collectors.toList());
	}
	
	private OutboxtableDTO toOutboxtableDTO(Outboxtable outboxtable) {
		return OutboxtableDTO
				.builder()
				.identify(outboxtable.getIdentity())
				.message(outboxtable.getMessage())
				.build();
				
	}
	
}

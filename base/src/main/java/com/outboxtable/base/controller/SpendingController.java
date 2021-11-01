package com.outboxtable.base.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.outboxtable.base.dto.SpendingDTO;
import com.outboxtable.base.entity.Spending;
import com.outboxtable.base.service.SpendingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SpendingController {

	private final SpendingService spendingService;
	
	@RequestMapping(method = RequestMethod.POST, path = "/api/v1/spending")
	public Spending addSpending(@RequestBody SpendingDTO spending) {
		return this.spendingService.save(Spending.builder()
				.amount(spending.getAmount())
				.identity(spending.getIdentity())
				.name(spending.getName())
				.surname(spending.getSurname())
				.build());
	}
	
}

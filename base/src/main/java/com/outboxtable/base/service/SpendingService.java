package com.outboxtable.base.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outboxtable.base.entity.Outboxtable;
import com.outboxtable.base.entity.Spending;
import com.outboxtable.base.repository.OutboxtableRepository;
import com.outboxtable.base.repository.SpendingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpendingService {

	private final SpendingRepository spendingRepository;
	private final OutboxtableRepository outboxtableRepository;
	private final ObjectMapper objectMapper;
	
	public Spending save(Spending spending) {				
		
		
		final Spending result = this.spendingRepository.save(spending);
		
		try {
			this.outboxtableRepository.save(Outboxtable.builder()
					.identity(spending.getIdentity())
					.message(objectMapper.writeValueAsString(result))
					.build());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
		return result;
		
	}
	
}

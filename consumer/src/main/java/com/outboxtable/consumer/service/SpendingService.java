package com.outboxtable.consumer.service;

import org.springframework.stereotype.Service;

import com.outboxtable.consumer.entity.Spending;
import com.outboxtable.consumer.repository.SpendingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpendingService {

	private final SpendingRepository spendingRepository;
	
	public Spending save(Spending spending) {
		
		log.info("Saveing new registry at the end of the trip {}.", spending);
		
		return this.spendingRepository.save(spending);
	}

	public boolean exist(String identify) {
		
		log.info("Idempotency checking {}", identify);
		
		return spendingRepository.existsByIdentity(identify);		
	}
	
}

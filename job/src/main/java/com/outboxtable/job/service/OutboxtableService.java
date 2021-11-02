package com.outboxtable.job.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.outboxtable.job.entity.Outboxtable;
import com.outboxtable.job.repository.OutboxtableRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxtableService {

	private final OutboxtableRepository outboxtableRepository;
	
	public List<Outboxtable> getOutboxtable() {
		return this.outboxtableRepository.findAll();
	}
	
	public void delete(Outboxtable outboxtable) {
		this.outboxtableRepository.delete(outboxtable);
	}
	
}

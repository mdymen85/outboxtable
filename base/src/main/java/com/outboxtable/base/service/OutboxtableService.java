package com.outboxtable.base.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.outboxtable.base.entity.Outboxtable;
import com.outboxtable.base.repository.OutboxtableRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxtableService {

	private final OutboxtableRepository outboxtableRepository;
	
	public List<Outboxtable> getOutboxtableNotIntegrated() {
		return outboxtableRepository.findByIntegrated(false);
	}
}

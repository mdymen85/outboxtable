package com.outboxtable.job.feign;

import java.util.List;

import org.springframework.stereotype.Service;

import com.outboxtable.job.dto.OutboxtableDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BaseOutboxtableService {

	private final IBaseOutboxtableClient iBaseOutboxtableClient;
	
	public List<OutboxtableDTO> getOutboxtableNotIntegrated() {
		return iBaseOutboxtableClient.getOutboxtableNotIntegrated();
	}
	
}

package com.outboxtable.job.watchdog;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.outboxtable.job.dto.OutboxtableDTO;
import com.outboxtable.job.feign.BaseOutboxtableService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class Job {

	private final BaseOutboxtableService baseOutboxtableService; 
	
	@Scheduled(fixedDelay = 1000)
	public void job() {
		List<OutboxtableDTO> outboxtables = baseOutboxtableService.getOutboxtableNotIntegrated();
		outboxtables.stream().forEach(o -> log.info("outboxtable {}", o));
	}
	
}

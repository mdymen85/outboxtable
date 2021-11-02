package com.outboxtable.job.watchdog;

import java.util.List;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.outboxtable.job.dto.OutboxtableDTO;
import com.outboxtable.job.feign.BaseOutboxtableService;
import com.outboxtable.sqs.SqsSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class Job {

	private final BaseOutboxtableService baseOutboxtableService; 
	private final SqsSender sqsSender;
	
	@Value("${application.job-outboxtable.enabled:true}")
	private boolean jobEnabled;
	
	@Scheduled(fixedDelay = 5000)
	public void job() throws JMSException {
		if (!jobEnabled) {
			log.info("Outboxtable job is disabled!!!");
			return;
		}
		
		List<OutboxtableDTO> outboxtables = baseOutboxtableService.getOutboxtableNotIntegrated();
		outboxtables.stream().forEach(o -> {
		
			log.info("outboxtable {}", o);
			try {
				sqsSender.sendMessage(o);
			} catch (JsonProcessingException | JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});
	}
	
}

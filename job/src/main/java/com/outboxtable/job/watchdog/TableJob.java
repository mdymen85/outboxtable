package com.outboxtable.job.watchdog;

import java.util.List;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.outboxtable.job.dto.OutboxtableDTO;
import com.outboxtable.job.entity.Outboxtable;
import com.outboxtable.job.feign.BaseOutboxtableService;
import com.outboxtable.job.service.OutboxtableService;
import com.outboxtable.job.sqs.SqsSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class TableJob {

	private final OutboxtableService outboxtableService; 
	private final SqsSender sqsSender;
	
	@Value("${application.job-outboxtable-db.enabled:true}")
	private boolean jobEnabled;
	
	@Scheduled(fixedDelay = 5000)
	public void job() throws JMSException {
		if (!jobEnabled) {
			return;
		}
		
		List<Outboxtable> outboxtables = outboxtableService.getOutboxtable();
		outboxtables.stream().forEach(outboxtable -> {
		
			log.info("Picking new register from Outboxtable {}", outboxtable);
			
			try {
				
				sqsSender.sendMessage(OutboxtableDTO.builder()
						.identify(outboxtable.getIdentity())
						.message(outboxtable.getMessage())
						.build());
				
				this.outboxtableService.delete(outboxtable);
				
			} catch (JsonProcessingException | JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});
	}
}

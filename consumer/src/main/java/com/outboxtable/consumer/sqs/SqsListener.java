package com.outboxtable.consumer.sqs;

import java.util.Map;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outboxtable.consumer.dto.OutboxtableDTO;
import com.outboxtable.consumer.dto.SpendingDTO;
import com.outboxtable.consumer.entity.Spending;
import com.outboxtable.consumer.repository.SpendingRepository;
import com.outboxtable.consumer.service.SpendingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SqsListener {

	private final ObjectMapper objectMapper;
	private final SpendingService spendingService;
	
    @JmsListener(destination = "${application.queue-outboxtable.name:sqs_outboxtable_project}")
    public void messageConsumer(@Headers Map<String, Object> messageAttributes,
                       @Payload String message) throws JsonMappingException, JsonProcessingException {
    	
    	log.info("Reading message from queue {}", message);

        final OutboxtableDTO outboxtableDTO = objectMapper.readValue(message, OutboxtableDTO.class);
        
        boolean exist = this.spendingService.exist(outboxtableDTO.getIdentify());
        
        if (exist) {
        	log.info("Register {} already exist.", outboxtableDTO.getIdentify());
        	return;
        }
        
        final SpendingDTO spendingDTO = objectMapper.readValue(outboxtableDTO.getMessage(), SpendingDTO.class);                
        
        log.info("Saveing new register {}.", spendingDTO);
        
        this.spendingService.save(Spending.builder()
        		.amount(spendingDTO.getAmount())
        		.identity(spendingDTO.getIdentity())
        		.name(spendingDTO.getName())
        		.surname(spendingDTO.getSurname())
        		.build());
        
    }
	
}

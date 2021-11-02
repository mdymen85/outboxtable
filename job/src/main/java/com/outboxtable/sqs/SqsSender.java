package com.outboxtable.sqs;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outboxtable.job.dto.OutboxtableDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SqsSender {

	private final SQSConnection connection;
	private final ObjectMapper objectMapper;
	
	@Value("${application.queue-outboxtable.name:sqs_outboxtable_project}")
	private String sqsQueue;
	
	public void sendMessage(OutboxtableDTO outboxtableDTO) throws JMSException, JsonProcessingException {
		//Create the nontransacted session with AUTO_ACKNOWLEDGE mode
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		// Create a queue identity and specify the queue name to the session
		Queue queue = session.createQueue(sqsQueue);
		 
		// Create a producer for the 'MyQueue'
		MessageProducer producer = session.createProducer(queue);
		
		//Create the text message
		TextMessage message = session.createTextMessage(objectMapper.writeValueAsString(outboxtableDTO));
		 
		// Send the message
		producer.send(message);
//		System.out.println("JMS Message " + message.getJMSMessageID());
	}
	
}

package com.outboxtable.sqs;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConectionService {
	
	public void receiveMessage() throws JMSException {
		
		// Create a new connection factory with all defaults (credentials and region) set automatically
		SQSConnectionFactory connectionFactory = new SQSConnectionFactory(
		        new ProviderConfiguration(),
		        AmazonSQSClientBuilder.defaultClient()
		        );
		 
		// Create the connection.
		SQSConnection connection = connectionFactory.createConnection();
		
		AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();
		 
		// Create an SQS queue named MyQueue, if it doesn't already exist
		if (!client.queueExists("sqs_outboxtable_project")) {
		    client.createQueue("sqs_outboxtable_project");
		}
		
		// Create the nontransacted session with AUTO_ACKNOWLEDGE mode
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//		
//		// Create a queue identity and specify the queue name to the session
//		Queue queue = session.createQueue("sqs_outboxtable_project");
//		 
//		// Create a producer for the 'MyQueue'
//		MessageProducer producer = session.createProducer(queue);
//		
//		// Create the text message
//		TextMessage message = session.createTextMessage("Hello World!");
//		 
//		// Send the message
//		producer.send(message);
//		System.out.println("JMS Message " + message.getJMSMessageID());
	}
	


}

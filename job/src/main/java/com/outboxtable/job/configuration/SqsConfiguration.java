package com.outboxtable.job.configuration;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outboxtable.job.sqs.SqsSender;

@Configuration
public class SqsConfiguration {

	@Value("${application.queue-outboxtable.name:sqs_outboxtable_project}")
	private String sqsQueue;

	@Bean
	public SqsSender getSqsSender() throws JMSException {
		return new SqsSender(sqsConnection(), new ObjectMapper());
	}
		
 
	@Bean
    public SQSConnectionFactory createSQSConnectionFactory() {
		SQSConnectionFactory connectionFactory = new SQSConnectionFactory(
		        new ProviderConfiguration(),
		        AmazonSQSClientBuilder.defaultClient()
		        );
		 return connectionFactory;
    }
 
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(createSQSConnectionFactory());
        return factory;
    }
 
    @Bean
    public JmsTemplate defaultJmsTemplate() {
        return new JmsTemplate(createSQSConnectionFactory());
    }
    
    @Bean 
    public SQSConnection sqsConnection() throws JMSException {
    	return createSQSConnectionFactory().createConnection();
    }
    
    @PostConstruct
    public void init() throws JMSException {
		AmazonSQSMessagingClientWrapper client = sqsConnection().getWrappedAmazonSQSClient();
		 
		// Create an SQS queue named MyQueue, if it doesn't already exist
		if (!client.queueExists(sqsQueue)) {
		    client.createQueue(sqsQueue);
		}
		
    }
}

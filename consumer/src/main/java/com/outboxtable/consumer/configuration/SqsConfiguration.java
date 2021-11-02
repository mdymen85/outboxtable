package com.outboxtable.consumer.configuration;

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
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

@Configuration
public class SqsConfiguration {

	@Value("${application.queue-outboxtable.name:sqs_outboxtable_project}")
	private String sqsQueue;	
 
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

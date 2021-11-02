package com.outboxtable.consumer.configuration;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableJms
@ComponentScan(basePackages = {"com.outboxtable.consumer*"})
@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = {"com.outboxtable.consumer.repository"})
@EntityScan(basePackages = {"com.outboxtable.consumer.entity"})
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
    
}

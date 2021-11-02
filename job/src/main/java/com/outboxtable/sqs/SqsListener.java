package com.outboxtable.sqs;

import java.util.Map;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class SqsListener {

//    @JmsListener(destination = "${application.queue-outboxtable.name:sqs_outboxtable_project}")
//    public void messageConsumer(@Headers Map<String, Object> messageAttributes,
//                       @Payload String message) {
//        // Do something
//        System.out.println("Messages attributes: " + messageAttributes);
//        System.out.println("Body: " + message);
//    }
	
}

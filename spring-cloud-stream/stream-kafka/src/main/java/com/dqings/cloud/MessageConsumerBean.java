package com.dqings.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.SubscribableChannel;

import javax.annotation.PostConstruct;

@Configuration
@EnableBinding({Sink.class})
public class MessageConsumerBean {

    @Autowired
    private Sink sink;

    @Autowired
    @Qualifier(Sink.INPUT)
    private SubscribableChannel subscribableChannel;



    /*@PostConstruct
    public void subscribable(){
        subscribableChannel.subscribe(message -> {
            System.out.println("subscribable"+message.getPayload());
        });
    }*/

    /*@ServiceActivator(inputChannel=Sink.INPUT)
    public void onMessage(Object message){
        System.out.println("@ServiceActivator"+message);
    }*/

    @StreamListener(Sink.INPUT)
    public void onMessage(String message){
        System.out.println("@StreamListener"+message);
    }


}

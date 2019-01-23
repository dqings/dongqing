package com.dqings.boot;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BootKafkaConsumer {

    /*@KafkaListener(topics = "${kafka.topic}")
    public void onMessage(String message){
        System.out.println("接收到消息:"+message);
    }*/

}

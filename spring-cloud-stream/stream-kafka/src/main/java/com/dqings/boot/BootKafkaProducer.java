package com.dqings.boot;

import com.dqings.cloud.MessageProducerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BootKafkaProducer {

    private final KafkaTemplate<String,String> kafkaTemplate;

    private final String topic;

    @Autowired
    private MessageProducerBean messageProducerBean;

    @Autowired
    public BootKafkaProducer(KafkaTemplate<String, String> kafkaTemplate, @Value("${kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @PostMapping("/send/message")
    public void sendMessage(@RequestParam String message){
        kafkaTemplate.send(topic,message);
    }

    @PostMapping("/send/message/stream")
    public void sendMessage1(@RequestParam String message){
        messageProducerBean.send(message);
    }
    @PostMapping("/send/message/dq")
    public void sendMessage2(@RequestParam String message){
        messageProducerBean.sendDongQing(message);
    }

}

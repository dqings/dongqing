package com.dqings.cloud;

import com.dqings.cloud.messaging.MessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

@Configuration
@EnableBinding({Source.class,MessageSource.class})
public class MessageProducerBean {

    @Autowired
    @Qualifier(Source.OUTPUT)
    private MessageChannel messageChannel;

    @Autowired
    private Source source;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    @Qualifier(MessageSource.OUTPUT)
    private MessageChannel dongQingMessageChannel;

    public void send(String message){
        messageChannel.send(MessageBuilder.withPayload(message).build());
    }

    public void sendDongQing(String message){
        dongQingMessageChannel.send(MessageBuilder.withPayload(message).build());
    }

}

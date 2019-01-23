package com.dqings.cloud.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MessageSource {

    String OUTPUT="dongqing";

    @Output(OUTPUT)
    MessageChannel dongqing();

}

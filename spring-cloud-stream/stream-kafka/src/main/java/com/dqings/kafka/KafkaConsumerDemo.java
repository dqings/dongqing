package com.dqings.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerDemo {

    public static void main(String[] args) {

        Properties props = new Properties();
        //服务器列表
        props.put("bootstrap.servers", "47.97.100.55:10114");
        //消费者组
        props.put("group.id", "dongqing");
        props.put("auto.offset.reset", "earliest");
        //消息是否自动提交
        props.put("enable.auto.commit", true);
        //自动提交的时间间隔
      //  props.put("auto.commit.interval.ms", 1000);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList("test1"));
        ConsumerRecords<String, String> records = consumer.poll(1000);
        for (ConsumerRecord<String, String> record : records) {
            System.err.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }
    }

}

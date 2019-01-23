# spring cloud stream



## kafka 

kafka 的安装部署参见官方文档:http://kafka.apache.org/quickstart

### kafka java 标准API

1、导入maven依懒

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

2、生产者

```java
public class KafkaProducerDemo {

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("bootstrap.servers", "47.97.100.55:10114");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String,String> kafkaProducer = new KafkaProducer<String, String>(props);
        //创建消息
        ProducerRecord<String,String> producerRecord =
                new ProducerRecord<String, String>("test1","hello,world");
        //发送消息
        Future<RecordMetadata> future = kafkaProducer.send(producerRecord);
        try {
            RecordMetadata metadata = future.get();
            System.out.println("发送结果:" + metadata.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
```

3、消费者

```java
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
```

### spring kafka

Spring 社区对 data(spring-data) 操作，有一个基本的模式， Template 模式：


JDBC : JdbcTemplate

Redis : RedisTemplate

Kafka : KafkaTemplate

JMS : JmsTemplate

Rest: RestTemplate


XXXTemplate 一定实现 XXXOpeations

KafkaTemplate 实现了 KafkaOperations



### spring boot kafka

1、自动装配器 KafkaAutoConfiguration

会自动装配KafkaTemplate

```java
@Bean
	@ConditionalOnMissingBean(KafkaTemplate.class)
	public KafkaTemplate<?, ?> kafkaTemplate(
			ProducerFactory<Object, Object> kafkaProducerFactory,
			ProducerListener<Object, Object> kafkaProducerListener) {
		KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<Object, Object>(
				kafkaProducerFactory);
		kafkaTemplate.setProducerListener(kafkaProducerListener);
		kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
		return kafkaTemplate;
	}
```

#### 创建生产者

```java
@RestController
public class BootKafkaProducer {

    private final KafkaTemplate<String,String> kafkaTemplate;

    private final String topic;

    @Autowired
    public BootKafkaProducer(KafkaTemplate<String, String> kafkaTemplate, @Value("${kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @PostMapping("/send/message")
    public void sendMessage(@RequestParam String message){
        kafkaTemplate.send(topic,message);
    }

}
```



#### 创建消费者

```java
@Component
public class BootKafkaConsumer {

    @KafkaListener(topics = "${kafka.topic}")
    public void onMessage(String message){
        System.out.println("接收到消息:"+message);
    }

}
```



#### 配置application.properties配置文件

```properties
## 应用名称
spring.application.name=stream-kafka
## 端口号
server.port=7078

## kafka服务器列表
spring.kafka.bootstrap-servers=47.97.100.55:10114
## 发送端配置
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
## kafka 消费者组
spring.kafka.consumer.group-id=dongqing
## 消费端配置
spring.kafka.consumer.keyDeserializer =org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.valueDeserializer =org.apache.kafka.common.serialization.StringDeserializer


## 主题
kafka.topic=test1
```

## spring cloud stream

#### 定义标准消息发送源

```java

@Configuration
@EnableBinding({Source.class})
public class MessageProducerBean {

    @Autowired
    @Qualifier(Source.OUTPUT)
    private MessageChannel messageChannel;

    @Autowired
    private Source source;

}
```



#### 自定义标准消息发送源

```java
import org.springframework.messaging.MessageChannel;

public interface MessageSource {

    String OUTPUT="dongqing";

    @Output(OUTPUT)
    MessageChannel dongqing();

}
```



#### 发送消息

```java
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
```

#### 实现标准sink监听

```java
@Configuration
@EnableBinding({Sink.class})
public class MessageConsumerBean {

    @Autowired
    private Sink sink;
    
    @Autowired
    @Qualifier(Sink.INPUT)
    private SubscribableChannel subscribableChannel;
}
```

##### 通过SubscribableChannel订阅消息

```java
@PostConstruct
public void subscribable(){
    subscribableChannel.subscribe(message -> {
        System.out.println("subscribable"+message.getPayload());
    });
}
```

##### 通过`@ServiceActivator` 订阅消息

```java
@ServiceActivator(inputChannel=Sink.INPUT)
public void onMessage(Object message){
    System.out.println("@ServiceActivator"+message);
}
```

##### 通过`@StreamListener` 订阅消息

```java
@StreamListener(Sink.INPUT)
public void onMessage(String message){
    System.out.println("@StreamListener"+message);
}
```

## 


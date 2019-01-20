# Spring cloud netflix feign

## 声明式Web服务客户端

声明式：接口声明、annotation驱动

Web服务：Http的方式作为通讯协议

客户端：用户服务调用的存根

Feign:原生并不是spring Mvc 的实现，基于JSX-RS(Java REST 规范) 实现，spring cloud封装了feign，使其支持spring Mvc。

## Feign 声明接口(契约)

定义一种java 强类型接口

模块：person-api

1、导入maven依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

2、导入eureka客户端依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

3、PersonService

```java
@FeignClient(value = "person-service-provider")
public interface PersonService {

    @PostMapping(value = "/person/save")
    boolean save(@RequestBody  Person person);

    @GetMapping(value = "/person/all")
    Collection<Person> findAll();
    
}
```

## Feign 客户(服务消费)端 调用Feign 声明接口

person-service-consumer

spring boot 引导类

```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients = PersonService.class)
public class PersonServiceConsumerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PersonServiceConsumerApplication.class,args);
    }
}
```

application.properties

```properties
## 服务消费端应用名称
spring.application.name=person-service-consumer
## 端口
server.port=7075

## Eureka Server 服务 URL,用于客户端注册
eureka.client.serviceUrl.defaultZone=\
  http://localhost:12345/eureka

management.security.enabled = false
```

## Feign 服务(服务提供)端

不一定强制实现feign 声明接口

spring boot 引导类

```java
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceProviderApplication.class,args);
    }
}
```

application.properties

```properties
## 服务消费端应用名称
spring.application.name=person-service-provider
## 端口
server.port=7076

## Eureka Server 服务 URL,用于客户端注册
eureka.client.serviceUrl.defaultZone=\
  http://localhost:12345/eureka

management.security.enabled = false
```

## 调用顺序

浏览器  --> person-service-consumer  ---> person-service-provider

person-api 中定义了 `@FeignClient(value = "person-service-provider")`  person-service-provider 是一个服务提供方的应用名称

person-service-consumer 和 person-service-provider都注册到了eureka 服务器中

person-service-consumer 和可以感知person-service-provider的存在，并且 Spring Cloud 帮助解析 PersonService 中声明的应用名称：“person-service-provider”，因此 person-service-consumer  在调用 `PersonService `服务时，实际就路由到 person-service-provider 的 URL



## 整合 netflix Ribbon

1、关闭Eureka注册

```properties
## 关闭eureka 注册
ribbon.eureka.enabled=false
```

2、定义ribbon的服务列表

```properties
## ribbon的服务列表
person-service-provider.ribbon.listOfServers=\
http://localhost:12345,http://localhost:12345,http://localhost:12345
```

3、自定义Ribbon

实现IRule 接口

```java
public class FirstServerForeverRule extends AbstractLoadBalancerRule {


    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        
    }

    @Override
    public Server choose(Object o) {
        ILoadBalancer loadBalancer = getLoadBalancer();
        List<Server> allServers = loadBalancer.getAllServers();
        return allServers.get(0);
    }
}
```

4、暴露自定义实现为spring bean

```java
@Bean
public FirstServerForeverRule firstServerForeverRule(){
    return new FirstServerForeverRule();
}
```

5、激活自定义的配置

```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients = PersonService.class)
@RibbonClient(value = "person-service-provider",configuration = PersonServiceConsumerApplication.class)
public class PersonServiceConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonServiceConsumerApplication.class,args);
    }


    @Bean
    public FirstServerForeverRule firstServerForeverRule(){
        return new FirstServerForeverRule();
    }

}
```



## 整合netflix hystrix



### 调整feign接口

```java
@FeignClient(value = "person-service-provider",fallback = PersonServiceFallback.class)
public interface PersonService {

    @PostMapping(value = "/person/save")
    boolean save(@RequestBody  Person person);

    @GetMapping(value = "/person/all")
    Collection<Person> findAll();

}
```

### 添加fallback实现

```java
public class PersonServiceFallback implements PersonService {

    @Override
    public boolean save(Person person) {
        return false;
    }

    @Override
    public Collection<Person> findAll() {
        return Collections.emptyList();
    }
}
```

### 激活客户端

```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients = PersonService.class)
@EnableCircuitBreaker
//@RibbonClient(value = "person-service-provider",configuration = PersonServiceConsumerApplication.class)
public class PersonServiceConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonServiceConsumerApplication.class,args);
    }


    @Bean
    public FirstServerForeverRule firstServerForeverRule(){
        return new FirstServerForeverRule();
    }

}
```


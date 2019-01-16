# spring cloud netflix eureka

**eureka 服务端**

实现步骤：

1、导入maven依懒

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka-server</artifactId>
</dependency>

```

2、在spring boot 引导类上添加`@EnableServerConfig`注解

3、配置application.properties配置文件

```properties
## 配置应用名称
spring.application.name=spring-cloud-eureka-server
##配置eureka 服务器的端口
server.port=12345
##取消eureka服务器的自我注册
eureka.client.registerWithEureka=false
##不去检索服务
eureka.client.fetchRegistry=false
##eureka server url 用于客户端注册
eureka.client.serviceUrl.defaultZone=http://localhost:${server.port}/eureka/

```

**eureka客户端**

1、添加服务发现客户端依懒

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

2、在服务提供方的spring boot 引导类中添加`@EnableDiscoveryClient` 或者`@EnableEurekaClient`注解

3、服务提供方的application.properties配置

```properties
##服务提供方的应用名称
spring.application.name=user-service-provider
## 服务提供方的端口
server.port=7070
## eureka 服务器的端口
eureka.server.port=12345
## eureka 服务URL，用于客户端的注册
eureka.client.service-url.defaultZone=\
http://localhost:${eureka.server.port}/eureka
##全局 安全失效
management.security.enabled=false

```

4、在服务提供方的spring boot 引导类中添加`@EnableDiscoveryClient` 或者`@EnableEurekaClient`注解

5、服务消费方的application.properties配置

```properties
##服务提供方的应用名称
spring.application.name=user-service-consumer
## 服务提供方的端口
server.port=7071
## eureka 服务器的端口
eureka.server.port=12345
## eureka 服务URL，用于客户端的注册
eureka.client.service-url.defaultZone=\
http://localhost:${eureka.server.port}/eureka
##全局 安全失效
management.security.enabled=false

```

6、将restTemplate暴露为spring的bean

```java
@LoadBalanced
@Bean
public RestTemplate getRestTemplate(){
    return new RestTemplate();
}
```

7、消费端注入`RestTemplate`调用API

```java
private final RestTemplate restTemplate;

private static final String USER_SERVICE_PREFIX_URL="http://user-service-provider";

@Autowired
public UserServiceProxy(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
}
```


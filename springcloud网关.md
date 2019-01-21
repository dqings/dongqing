# spring cloud zuul

## zuul基本使用

1、导入zuul的maven依懒

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
</dependency>
```

2、在spring boot 引导类上添加`@EnableZuulProxy`注解

```java
@SpringBootApplication
@EnableZuulProxy
public class SpringCloudZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudZuulApplication.class,args);
    }

}
```

3、配置路由规则

基本模式：zuul.routes.${app-name}=/${app-url-prefix}/**

## 整合Ribbon

1、启动应用

`Eureka server`

`person-service-provider`

2、application.propoerties 配置文件

```properties
## 路由规则
#zuul.routes.${app-name}=/${app-url-prefix}/**
## 应用名称
spring.application.name=spring-cloud-zuul
## 端口号
server.port=7077
## 配置 person-service-provider 服务调用
zuul.routes.person-service-provider=/person-service-provider/**

## Ribbon 取消Eureka整合
ribbon.eureka.enabled=false
## 配置person-service-provider 的负载均衡服务器列表
person-service-provider.ribbon.listOfServers =\
http://localhost:7076
```

调用结果

http://localhost:7077/person-service-provider/person/all

person-service-provider 是app-url-prefix 前缀，

person/all  是person-service-provider 服务中具体的URI



## 整合Eureka

1、导入Eureka客户端的maven依懒

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

2、在spring boot 引导类中添加`@@EnableDiscoveryClient` 注解

```java
@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class SpringCloudZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudZuulApplication.class,args);
    }

}
```

3、修改application.properties配置文件

```properties
## 路由规则
#zuul.routes.${app-name}=/${app-url-prefix}/**
## 应用名称
spring.application.name=spring-cloud-zuul
## 端口号
server.port=7077
## 配置 person-service-provider 服务调用
zuul.routes.person-service-provider=/person-service-provider/**

## Eureka Server 服务 URL,用于客户端注册
eureka.client.serviceUrl.defaultZone=\
  http://localhost:12345/eureka

## Ribbon 取消Eureka整合
#ribbon.eureka.enabled=false
## 配置person-service-provider 的负载均衡服务器列表
#person-service-provider.ribbon.listOfServers =\
http://localhost:7076
```



## 整合hystrix

1、导入Hystrix的maven依懒

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```



2、服务提供方：person-service-provider 激活hystrix 

```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class PersonServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonServiceProviderApplication.class,args);
    }
}
```

3、配置hystrix规则

```java
@GetMapping(value = "/person/all")
    @HystrixCommand(fallbackMethod = "findAllPersonsFallBack",
            commandProperties =
                    {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "100")})
    public Collection<Person> findAllPersons() throws InterruptedException {
        int anInt = random.nextInt(200);
        System.out.println("person 服务调用时间 : "+anInt);
        Thread.sleep(anInt);
        return personServiceImpl.findAllPersons();
    }


    public Collection<Person> findAllPersonsFallBack(){
        System.err.println("person服务超时");
        return Collections.emptyList();
    }
```



## 整合Feign

1、服务消费端：person-service-consumer

2、服务调用链路:spring cloud zuul -->person-service-consumer --> person-service-provider

3、person-service-consumer 注册到Eureka 服务器

4、端口信息：

​	person-service-consumer:7075

​	person-service-provider:7076

​	spring cloud zuul :7077

​	Eureka server:12345

5、启动person-service-consumer

```properties
## 服务消费端应用名称
spring.application.name=person-service-consumer
## 端口
server.port=7075
#server.port=0

## Eureka Server 服务 URL,用于客户端注册
eureka.client.serviceUrl.defaultZone=\
  http://localhost:12345/eureka

management.security.enabled = false
```

6、配置网关应用：

```properties
## 网关应用路由增加 person-service-consumer
zuul.routes.person-service-consumer=/person-service-consumer/**
```

7、测试链路：

http://localhost:7077/person-service-consumer/person/all

spring cloud zuu(7077) -->person-service-consumer(7075) -->person-service-provider(7076)

8、等价的Ribbon，不走注册中心

```properties
## Ribbon 取消Eureka整合
ribbon.eureka.enabled=false
## 配置person-service-provider 的负载均衡服务器列表
person-service-provider.ribbon.listOfServers =\
http://localhost:7076
person-service-consumer.ribbon.listOfServers =\
http://localhost:7075
```

## 整合config server

1、配置服务器  spring-cloud-config-server

​      端口信息：

​	person-service-consumer:7075

​	person-service-provider:7076

​	spring cloud zuul :7077

​	Eureka server:12345

​	config server:9090

2、调整配置项

```properties
##配置配置服务端的应用名称
spring.application.name=spring-cloud-config-server
##配置服务端的端口
server.port=9090
## 本地仓库的git uri 配置
spring.cloud.config.server.git.uri=\
file:///E:/projectdemo/gitpropertydir

## 全局关闭 actuator 安全
#management.security.enabled=false
## 细粒度的开放 actuator Endpoints
## sensitive 关注敏感性，安全
endpoints.env.sensitive=false
```



3、为spring-cloud-zuul 增加配置文件

zuul.properties

zuul-test.properties

zuul-prod.properties



zuul.properties

```properties
## 配置 person-service-provider 服务调用
zuul.routes.person-service-provider=/person-service-provider/**
```



zuul-test.properties

```properties
## 配置 person-service-consumer 服务调用
zuul.routes.person-service-consumer=/person-service-consumer/**
```



zuul-prod.properties

```properties
## 配置 person-service-provider 服务调用
zuul.routes.person-service-provider=/person-service-provider/**

## 配置 person-service-consumer 服务调用
zuul.routes.person-service-consumer=/person-service-consumer/**
```



添加到本地git仓库

$ git add *.properties

提交到本地仓库

$ git commit -m'add zuul properties'
[master 4708756] add zuul properties
 3 files changed, 9 insertions(+)
 create mode 100644 zuul-prod.properties
 create mode 100644 zuul-test.properties
 create mode 100644 zuul.properties

4、将config server注册到Eureka server

4.1添加eureka client的maven依懒

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

4.2激活服务注册、发现

```java
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class SpringCloudConfigServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringCloudConfigServerApplication.class)
                .run(args);
    }

}
```

4.3调整config server 的application.properties 配置文件

```properties
## Eureka Server 服务 URL,用于客户端注册
eureka.client.serviceUrl.defaultZone=\
  http://localhost:12345/eureka
```

5、测试配置是否正确

http://localhost:9090/zuul/default

http://localhost:9090/zuul/test

http://localhost:9090/zuul/prod

6、配置网关服务 spring-cloud-zuul

6.1增加config client 的maven依懒

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

6.2创建bootstrap.properties配置文件

```properties
## 整合 Eureka
## Eureka Server 服务 URL,用于客户端注册
## application.properties 会继承bootstrap.properties 属性
## 因此，application.properties 没有必要配置 eureka.client.serviceUrl.defaultZone
eureka.client.serviceUrl.defaultZone=\
  http://localhost:12345/eureka

### bootstrap 上下文配置
# 配置客户端应用名称: zuul , 可当前应用是 spring-cloud-zuul
spring.cloud.config.name = zuul
# profile 是激活配置
spring.cloud.config.profile = prod
# label 在Git中指的分支名称
spring.cloud.config.label = master
# 采用 Discovery client 连接方式
## 激活 discovery 连接配置项的方式
spring.cloud.config.discovery.enabled=true
## 配置 config server 应用名称
spring.cloud.config.discovery.serviceId = spring-cloud-config-server

```

测试调用：

http://localhost:7077/person-service-consumer/person/all

spring cloud zuul -->person-service-consumer -->person-service-provider

http://localhost:7077/person-service-provider/person/all

spring cloud zuul  -->person-service-provider


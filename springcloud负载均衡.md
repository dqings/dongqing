# Spring Cloud Netflix Ribbon

**eureka 客户端高可用**

只需要在客户端增加eureka服务端的地址即可

```properties
eureka.server.port=12345
eureka.client.service-url.defaultZone=\
http://localhost:${eureka.server.port}/eureka,http://localhost:12346/eureka
```

如果eureka客户端应用配置了多个eureka注册服务器，那么默认情况，只有第一台**可用**的服务器存在注册信息,

如果第一台可用的eureka服务器down掉了，那么eureka客户端应用将会选择下一台**可用**的eureka服务器

配置的源码(EurekaClientConfigBean):

```java
属性：public static final String DEFAULT_ZONE = "defaultZone";
private Map<String, String> serviceUrl = new HashMap<>();

{
    this.serviceUrl.put(DEFAULT_ZONE, DEFAULT_URL);
}
public List<String> getEurekaServerServiceUrls(String myZone) {
    String serviceUrls = this.serviceUrl.get(myZone);
    if (serviceUrls == null || serviceUrls.isEmpty()) {
        serviceUrls = this.serviceUrl.get(DEFAULT_ZONE);
    }
    if (!StringUtils.isEmpty(serviceUrls)) {
        final String[] serviceUrlsSplit = StringUtils.commaDelimitedListToStringArray(serviceUrls);//通过','分割为多个地址
    }
```

获取注册信息时间间隔(即eureka客户端从eureka服务端拉取数据的时间间隔)

Eureka 客户端需要从eureka服务端获取服务信息，方便调用

EurekaClient 关联Applications应用集合

Application 关联多个应用实列 InstanceInfo

```java
@XStreamImplicit
private final Set<InstanceInfo> instances;
```

当 Eureka 客户端需要调用具体某个服务时，比如user-service-consumer 调用user-service-provider，user-service-provider实际对应对象是Application,关联了许多应用实例(InstanceInfo)。

如果应用user-service-provider的应用实例发生变化时，那么user-service-consumer是需要感知的。比如：user-service-provider机器从10 台降到了5台，那么，作为调用方的user-service-consumer需要知道这个变化情况。可是这个变化过程，可能存在一定的延迟，可以通过调整注册信息时间间隔来减少错误。

默认配置：

```pro
/**
* Indicates how often(in seconds) to fetch the registry information from the eureka
* server.
*/
private int registryFetchIntervalSeconds = 30;
```

具体配置项：

```properties
eureka.client.registry-fetch-interval-seconds=5
```

实列信息复制时间间隔(eureka应用客户端向eureka服务器上报信息的时间间隔)

具体就是客户端信息的上报到 Eureka 服务器时间。当 Eureka 客户端应用上报的频率越频繁，那么 Eureka 服务器的应用状态管理一致性就越高。

```properties
## 调整客户端应用状态信息上报的周期
eureka.client.instanceInfoReplicationIntervalSeconds = 5
```

实列ID：

从eureka dashboard里可以看到具体某个应用的实列id，比如

```properties
USER-SERVICE-CONSUMER	n/a (1)	(1)	UP (1) - localhost:user-service-consumer:7071
```

默认的命名模式：${hostname}:${spring.application.name}:${server.port}

对应的实列类:`EurekaInstanceConfigBean`

属性：private String instanceId

具体配置项：

```properties
##配置 eureka 实列应用的id
eureka.instance.instance-id=${spring.application.name}:${server.port}
```

实列端点映射：

默认方式："/info" 列如：http://localhost:7071/info 

对应java类：`EurekaClientConfugBean`

属性:private String statusPageUrlPath = "/info"

具体配置：

```properties
##eureka客户端实列状态URL
eureka.instance.status-page-url-path=/health
```

**eureka服务端高可用**

构建eureka服务器相互注册：

Eureka server1 -> Profile:peer1

#### 配置项

```properties
## 配置应用名称
spring.application.name=spring-cloud-eureka-server
##配置eureka 服务器的端口
server.port=12345
##取消eureka服务器的自我注册
eureka.client.registerWithEureka=true
##不去检索服务
eureka.client.fetchRegistry=true
##eureka server url 用于客户端注册
eureka.client.serviceUrl.defaultZone=http://localhost:12346/eureka/
```



Eureka server2 -> Profile:peer2

##### 配置项

```properties
## 配置应用名称
spring.application.name=spring-cloud-eureka-server
##配置eureka 服务器的端口
server.port=12346
##取消eureka服务器的自我注册
eureka.client.registerWithEureka=true
##不去检索服务
eureka.client.fetchRegistry=true
##eureka server url 用于客户端注册
eureka.client.serviceUrl.defaultZone=http://localhost:12345/eureka/
```



通过`--spring.profiles.active=peer1` 和 `--spring.profiles.active=peer2`

分别激活Eureka server1 和Eureka server2

#### Spring RestTemplate

##### Http消息转换器

`HttpMessageConverter`



##### Http 适配工厂

`ClientHttpRequestFactory`

这个方面主要考虑大家的使用 HttpClient 偏好：


Spring 实现


​	SimpleClientHttpRequestFactory


HttpClient


​	HttpComponentsClientHttpRequestFactory


OkHttp

​	OkHttp3ClientHttpRequestFactory
​	OkHttpClientHttpRequestFactory



##### Http请求拦截器

`ClientHttpRequestInterceptor`



#### 整合 netflix ribbon

RestTemplate增加一个LoadBalancerInterceptor，调用Netflix 中的LoadBalander实现，根据 Eureka 客户端应用获取目标应用 IP+Port 信息，轮训的方式调用。

##### 实际请求客户端

```
LoadBalancerClient
	实现类：RibbonLoadBalancerClient
```

##### 负载均衡上下文

```java
LoadBalancerContext
	RibbonLoadBalancerContext
```

##### 负载均衡器

```java
ILoadBalancer
	BaseLoadBalancer
	DynamicServerListLoadBalancer
	ZoneAwareLoadBalancer
	NoOpLoadBalancer
```

##### 负载均衡规则

IRule

​	随机规则：RandomRule
​	最可用规则：BestAvailableRule
​	轮训规则：RoundRobinRule
​	重试实现：RetryRule
​	客户端配置：ClientConfigEnabledRoundRobinRule
​	可用性过滤规则：AvailabilityFilteringRule
​	RT权重规则：WeightedResponseTimeRule
​	规避区域规则：ZoneAvoidanceRule



##### PING 策略

##### 核心策略接口


​	IPingStrategy



##### PING 接口


​	IPing

​	NoOpPing
​	DummyPing
​	PingConstant
​	PingUrl

##### Discovery Client 实现


​	NIWSDiscoveryPing
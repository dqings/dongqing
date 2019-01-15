# 配置服务端

**实现步骤：**

1、在项目中引入spring cloud config server 的maven依懒

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```

2、在spring boot 引导类添加`@EnableConfigServer`注解

3、配置文件目录(基于git,即配置文件在.git目录的同级)

dongqing.properties 默认配置文件

dongqing-dev.properties (profile="dev") 开发环境

dongqing-test.properties(profile="test") 测试环境

dongqing-staging.properties(profile="staging") 预发布环境

dongqing-prod.properties(profile="prod")生产环境

4、服务端配置版本库(本地或者远程)

```properties
## 本地仓库的git uri 配置
spring.cloud.config.server.git.uri=\
file:///E:/projectdemo/gitpropertydir
```

5、服务端的完整配置(基于spring boot 1.5.19版本)

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



# 配置客户端

**实现步骤：**

1、创建bootstrap.properties 或者bootstrap.yml文件

2、在配置文件(bootstrap.properties)配置客户端信息

```properties
## bootstrap 上下文配置
## 配置服务器uri
spring.cloud.config.uri=http://localhost:9090/
## 配置客户端的应用名称(文件的名称)
spring.cloud.config.name=dongqing
##配置profile
spring.cloud.config.profile=prod
##配置lable，在git中指分支名称
spring.cloud.config.label=master
```

3、配置客户端的application.properties文件

```properties
## 应用名称
spring.application.name=spring-cloud-config-client
## 服务端口
server.port=8080
## 全局关闭actuator 安全
##management.security.enabled=false
## 关闭endpoints 的安全
endpoints.env.sensitive=false
endpoints.refresh.sensitive=false
endpoints.beans.sensitive=false
endpoints.actuator.sensitive=false
endpoints.health.sensitive=false
```



**@RefreshScope**注解的用法

```java
@RestController
@RefreshScope
public class EchoController {

    @Value("${name}")
    private String name;

    @GetMapping("name")
    public String getName(){
        return name;
    }

}
```

通过客户端的/refresh 接口更新配置项

**自动更新客户端的配置**

1、注入api

```java
private final ContextRefresher contextRefresher;
private final Environment environment;
@Autowired
public EchoController(ContextRefresher contextRefresher, Environment environment) {
    this.contextRefresher = contextRefresher;
    this.environment = environment;
}
```

2、定时任务代码

```java
@Scheduled(fixedRate = 5*1000,initialDelay = 3*1000)
public void autoRefresh(){
    Set<String> stringSet = contextRefresher.refresh();
    stringSet.forEach(name ->{
        System.err.printf("[Thread:%s],服务端配置已更新,name=%s,value=%s\n",
                          Thread.currentThread().getName(),
                          name,
                          environment.getProperty(name));
    });
}
```

3、执行结果

[Thread:pool-1-thread-1],服务端配置已更新,name=name,value=prod

**健康检查**

意义：检查服务应用的健康指标

端点的URL：/health

实现类：healthEndpoint

健康指示器：healthIndicator






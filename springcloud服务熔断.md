# Spring Cloud Netflix Hystrix

## spring cloud hystrix client

### 1、在项目中导入hystrix client 的maven依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

### 2、在spring boot引导类中添加`@EnableHystrix`注解

​	**Hystrix 配置信息wiki**：https://github.com/Netflix/Hystrix/wiki/Configuration

### 3、注解方式熔断服务(Annotation)

```java
@GetMapping("hello-world")
    @HystrixCommand(fallbackMethod = "fault",
            commandProperties =
                    {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "100")})
    public String helloWorld() throws InterruptedException {
        Random random = new Random();
        int anInt = random.nextInt(200);
        System.out.println("方法执行时间:"+anInt);
        Thread.sleep(anInt);
        return "hello,world";
    }

    public String fault(){
        return "Fault";
    }
```

### 4、编程方式

```java
@GetMapping("hello-world-2")
    public String helloWorld2(){
        return new HelloWorldCommnd().execute();
    }

    private class HelloWorldCommnd extends com.netflix.hystrix.HystrixCommand<String> {

        protected HelloWorldCommnd() {
            super(HystrixCommandGroupKey.Factory.asKey("helloWorld"),100);
        }

        @Override
        protected String run() throws Exception {
            Random random = new Random();
            int anInt = random.nextInt(200);
            System.out.println("方法执行时间:"+anInt);
            Thread.sleep(anInt);
            return "hello,world";
        }

        @Override
        protected String getFallback() {
            return HystrixDemoController.this.fault();
        }
    }
```

### 5、对比其他java 执行方式

#### future

```java
public class FutureDemo {

    private static final Random random = new Random();
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);

    public static void main(String[] args) {
        Future<String> future = executor.submit(() -> {
            Random random = new Random();
            int anInt = random.nextInt(200);
            System.out.println("方法执行时间:" + anInt);
            Thread.sleep(anInt);
            return "hello,world";
        });
        try {
            String s = future.get(100, TimeUnit.MILLISECONDS);
            System.out.println(s);
        } catch (Exception e) {
            System.out.println("超时保护");
        }
        executor.shutdown();
    }
}
```

#### Rxjava

```java
public class RxJavaDemo {

    public static void main(String[] args) {
        Single.just("hello,world")
                .subscribeOn(Schedulers.immediate())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("执行结束");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("超时保护");
                    }

                    @Override
                    public void onNext(String s) {
                        Random random = new Random();
                        int anInt = random.nextInt(200);
                        System.out.println("方法执行时间:"+anInt);
                        if(anInt>100) throw new RuntimeException("time out");
                        System.out.println(s);
                    }
                });
    }

}
```

### 6、health Endpoint

```json
{
    status: "UP",
    diskSpace: {
        status: "UP",
        total: 987661070336,
        free: 549900189696,
        threshold: 10485760
    },
    hystrix: {
        status: "UP"
    }
}
```

### 7、激活spring cloud 的熔断保护

`@EnableCircuitBreaker` 激活`@EnableHystrix` +spring cloud 功能

`@EnableHystrix` 激活netflix 的熔断保护，没有spring cloud 的功能，比如：`/hystrix.stream`端点

#### Hystrix Endpoint(/hystrix.stream)

```json
data: {
	"type": "HystrixCommand",
	"name": "HelloWorldCommnd",
	"group": "helloWorld",
	"currentTime": 1547885169392,
	"isCircuitBreakerOpen": false,
	"errorPercentage": 100,
	"errorCount": 1,
	"requestCount": 1,
	"rollingCountBadRequests": 0,
	"rollingCountCollapsedRequests": 0,
	"rollingCountEmit": 0,
	"rollingCountExceptionsThrown": 0,
	"rollingCountFailure": 0,
	"rollingCountFallbackEmit": 0,
	"rollingCountFallbackFailure": 0,
	"rollingCountFallbackMissing": 0,
	"rollingCountFallbackRejection": 0,
	"rollingCountFallbackSuccess": 0,
	"rollingCountResponsesFromCache": 0,
	"rollingCountSemaphoreRejected": 0,
	"rollingCountShortCircuited": 0,
	"rollingCountSuccess": 0,
	"rollingCountThreadPoolRejected": 0,
	"rollingCountTimeout": 0,
	"currentConcurrentExecutionCount": 0,
	"rollingMaxConcurrentExecutionCount": 0,
	"latencyExecute_mean": 0,
	"latencyExecute": {
		"0": 0,
		"25": 0,
		"50": 0,
		"75": 0,
		"90": 0,
		"95": 0,
		"99": 0,
		"99.5": 0,
		"100": 0
	},
	"latencyTotal_mean": 0,
	"latencyTotal": {
		"0": 0,
		"25": 0,
		"50": 0,
		"75": 0,
		"90": 0,
		"95": 0,
		"99": 0,
		"99.5": 0,
		"100": 0
	},
	"propertyValue_circuitBreakerRequestVolumeThreshold": 20,
	"propertyValue_circuitBreakerSleepWindowInMilliseconds": 5000,
	"propertyValue_circuitBreakerErrorThresholdPercentage": 50,
	"propertyValue_circuitBreakerForceOpen": false,
	"propertyValue_circuitBreakerForceClosed": false,
	"propertyValue_circuitBreakerEnabled": true,
	"propertyValue_executionIsolationStrategy": "THREAD",
	"propertyValue_executionIsolationThreadTimeoutInMilliseconds": 100,
	"propertyValue_executionTimeoutInMilliseconds": 100,
	"propertyValue_executionIsolationThreadInterruptOnTimeout": true,
	"propertyValue_executionIsolationThreadPoolKeyOverride": null,
	"propertyValue_executionIsolationSemaphoreMaxConcurrentRequests": 10,
	"propertyValue_fallbackIsolationSemaphoreMaxConcurrentRequests": 10,
	"propertyValue_metricsRollingStatisticalWindowInMilliseconds": 10000,
	"propertyValue_requestCacheEnabled": true,
	"propertyValue_requestLogEnabled": true,
	"reportingHosts": 1,
	"threadPool": "helloWorld"
}

```

### 8、spring cloud hystrix dashboard

导入maven依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
</dependency>
```

在spring boot 引导类中加入`@EnableHystrixDashboard`注解

访问：http://localhost:7074/hystrix

需要监控的地址：http://localhost:7073/hystrix.stream


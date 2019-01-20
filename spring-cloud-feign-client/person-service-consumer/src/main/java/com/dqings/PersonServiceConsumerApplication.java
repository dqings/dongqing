package com.dqings;

import com.dqings.person.api.PersonService;
import com.dqings.ribbon.FirstServerForeverRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;

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

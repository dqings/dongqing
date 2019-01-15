package com.dqings.springcloudmergeapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SpringCloudMergeApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(SpringCloudMergeApplication.class)
				.web(true)
				.run(args);
	}

	@Configuration
	@Order(value = Ordered.HIGHEST_PRECEDENCE)
	public static class MyPropertySourceLocator implements PropertySourceLocator{
		@Override
		public PropertySource<?> locate(Environment environment) {
			Map<String,Object> source = new HashMap<>();
			source.put("server.port","8080");
			MapPropertySource propertySource = new MapPropertySource("my-property-source",source);
			return propertySource;
		}
	}



}


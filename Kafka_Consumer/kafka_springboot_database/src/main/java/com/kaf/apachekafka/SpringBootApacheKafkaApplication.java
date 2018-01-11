package com.kaf.apachekafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories
@ComponentScan({"com.kaf.*","com.hackthon.*"})
public class SpringBootApacheKafkaApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(SpringBootApacheKafkaApplication.class, args);
	}
}

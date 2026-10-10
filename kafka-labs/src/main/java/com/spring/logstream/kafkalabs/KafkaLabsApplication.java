package com.spring.logstream.kafkalabs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class KafkaLabsApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaLabsApplication.class, args);
    }
}

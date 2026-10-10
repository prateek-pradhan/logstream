package com.spring.logstream.kafkalabs.config;

import com.spring.logstream.kafkalabs.KafkaMessageRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class ApplicationConfig {

    @Bean
    public KafkaMessageRunner kafkaMessageRunner(KafkaTemplate<String, String> kafkaTemplate) {
        return new KafkaMessageRunner(kafkaTemplate);
    }
}

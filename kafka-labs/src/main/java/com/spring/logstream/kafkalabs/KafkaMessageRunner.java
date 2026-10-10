package com.spring.logstream.kafkalabs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaMessageRunner implements CommandLineRunner {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaMessageRunner(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        String topic = "lab-topic";
        long start = System.currentTimeMillis();

        log.info("Sending message to Kafka topic: {}}", topic);
        for (int i = 0; i < 10000; i++) {
            String message = "Hello World " + i;
            kafkaTemplate.send(topic, message);
        }
        long end = System.currentTimeMillis();
        log.info("Message sent successfully");
        log.info("Time taken: {} ms", end - start);
        log.info("Throughput: {} msgs/sec", 10000/((end - start) / 1000.0));
    }
}

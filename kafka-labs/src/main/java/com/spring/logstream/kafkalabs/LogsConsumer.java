package com.spring.logstream.kafkalabs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogsConsumer {

    @KafkaListener(topics = "lab-topic", groupId = "kafka-labs-group")
    public void consume(String message) {
    }
}

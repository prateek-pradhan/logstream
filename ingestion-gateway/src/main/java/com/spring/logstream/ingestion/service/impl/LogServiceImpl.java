package com.spring.logstream.ingestion.service.impl;

import com.spring.logstream.common.dto.HttpLogEventRequest;
import com.spring.logstream.common.envelope.LogEvent;
import com.spring.logstream.ingestion.exceptions.InvalidArgumentException;
import com.spring.logstream.ingestion.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    @Override
    public LogEvent ingestLog(HttpLogEventRequest logEvent) {

        if (logEvent.getEventTime() > System.currentTimeMillis() + 1000) {
            throw new InvalidArgumentException("Invalid event time");
        }

        long ingestedAt = System.currentTimeMillis();

        return LogEvent.builder()
                .eventId(logEvent.getEventId())
                .eventTime(logEvent.getEventTime())
                .message(logEvent.getMessage())
                .source(logEvent.getSource())
                .severity(logEvent.getSeverity())
                .traceId(logEvent.getTraceId())
                .schemaVersion(1)
                .ingestedAt(ingestedAt)
                .build();
    }
}

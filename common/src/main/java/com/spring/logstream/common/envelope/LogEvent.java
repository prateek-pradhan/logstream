package com.spring.logstream.common.envelope;

import com.spring.logstream.common.enums.LogSeverity;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class LogEvent {
    private final String eventId;
    private final long eventTime;
    private final String message;
    private final LogSeverity severity;
    private final String source;
    private final String traceId;
    private final int schemaVersion;
    private final long ingestedAt;
}

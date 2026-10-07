package com.spring.logstream.ingestion.service;

import com.spring.logstream.common.dto.HttpLogEventRequest;
import com.spring.logstream.common.envelope.LogEvent;

public interface LogService {
    public LogEvent ingestLog(HttpLogEventRequest logEvent);
}

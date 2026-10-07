package com.spring.logstream.ingestion.controller;

import com.spring.logstream.common.dto.HttpLogEventRequest;
import com.spring.logstream.common.envelope.LogEvent;
import com.spring.logstream.ingestion.service.LogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/logs")
@RestController
@RequiredArgsConstructor
public class LogHttpController {

    private final LogService logService;

    @PostMapping
    public ResponseEntity<LogEvent> log(@Valid @RequestBody HttpLogEventRequest logEvent) {
        return new ResponseEntity<>(logService.ingestLog(logEvent), HttpStatus.ACCEPTED);
    }
}

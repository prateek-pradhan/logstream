package com.spring.logstream.common.dto;

import com.spring.logstream.common.enums.LogSeverity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpLogEventRequest {

    @NotBlank
    private String eventId;

    @Min(value = 946684800000L, message = "Timestamp cannot be before Year 2000")
    private long eventTime;

    @NotBlank(message = "Message cannot be empty")
    private String message;

    @NotNull
    private LogSeverity severity;

    @NotBlank
    private String source;

    @NotBlank
    private String traceId;
}



package com.spring.logstream.common.enums;

import lombok.Getter;

@Getter
public enum LogSeverity {
    ERROR(0),
    WARN(1),
    INFO(2),
    DEBUG(3);

    private final int value;

    LogSeverity(int value) {
        this.value = value;
    }

}

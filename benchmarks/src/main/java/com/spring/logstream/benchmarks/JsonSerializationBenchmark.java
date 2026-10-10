package com.spring.logstream.benchmarks;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import com.spring.logstream.common.enums.LogSeverity;
import com.spring.logstream.common.envelope.LogEvent;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class JsonSerializationBenchmark {

    private ObjectMapper mapper;
    private LogEvent logEvent;
    private String jsonLog;
    private String jsonLogs;
    private List<LogEvent> logEvents;

    @Setup(Level.Trial)
    public void setup() {
        this.mapper = new ObjectMapper();
        this.logEvent = LogEvent.builder().eventId("123").message("test message").ingestedAt(System.currentTimeMillis()).severity(LogSeverity.INFO).source("test service").schemaVersion(1).traceId("abc").eventTime(System.currentTimeMillis()).build();
        this.jsonLog = mapper.writeValueAsString(logEvent);
        byte[] bytes = mapper.writeValueAsBytes(logEvent);
        this.logEvents = new ArrayList<>();

        for (int idx = 0; idx < 100; idx++) {
            LogEvent log = LogEvent.builder().eventId(String.valueOf(idx)).message("test message").ingestedAt(System.currentTimeMillis()).severity(LogSeverity.INFO).source("test service").schemaVersion(1).traceId("abc").eventTime(System.currentTimeMillis()).build();
            logEvents.add(log);
        }
        this.jsonLogs = mapper.writeValueAsString(logEvents);
        System.out.println("Bytes Length" + bytes.length);
    }

    @Benchmark
    public void benchmarkJsonSerialization(Blackhole blackhole) {
        String json = mapper.writeValueAsString(logEvent);
        blackhole.consume(json);
    }

    @Benchmark
    public void benchmarkJsonDeserialization(Blackhole blackhole) {
        LogEvent log = mapper.readValue(jsonLog, LogEvent.class);
        blackhole.consume(log);
    }

    @Benchmark
    public void benchmarkBulkJsonSerialization(Blackhole blackhole) {
        String json = mapper.writeValueAsString(logEvents);
        blackhole.consume(json);
    }

    @Benchmark
    public void benchmarkBulkJsonDeserialization(Blackhole blackhole) {
        List<LogEvent> log = mapper.readValue(jsonLogs, new TypeReference<List<LogEvent>>() {});
        blackhole.consume(log);
    }
}

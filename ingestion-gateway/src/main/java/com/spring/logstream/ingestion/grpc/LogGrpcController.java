package com.spring.logstream.ingestion.grpc;

import com.spring.logstream.GrpcLogEvent;
import com.spring.logstream.GrpcLogSeverity;
import com.spring.logstream.LogEventGrpcRequest;
import com.spring.logstream.LogServiceGrpc;
import com.spring.logstream.common.dto.HttpLogEventRequest;
import com.spring.logstream.common.enums.LogSeverity;
import com.spring.logstream.common.envelope.LogEvent;
import com.spring.logstream.ingestion.service.LogService;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogGrpcController extends LogServiceGrpc.LogServiceImplBase {

    private final LogService logService;

    @Override
    public void ingestLog(LogEventGrpcRequest request, StreamObserver<GrpcLogEvent> responseObserver) {
        HttpLogEventRequest logEventRequest = new HttpLogEventRequest();
        logEventRequest.setEventId(request.getEventId());
        logEventRequest.setEventTime(request.getEventTime());
        logEventRequest.setMessage(request.getMessage());
        logEventRequest.setSeverity(LogSeverity.valueOf(request.getSeverity().name()));
        logEventRequest.setSource(request.getSource());
        logEventRequest.setTraceId(request.getTraceId());

        LogEvent logEvent = logService.ingestLog(logEventRequest);

        GrpcLogEvent response = GrpcLogEvent.newBuilder()
                .setEventId(logEvent.getEventId())
                .setEventTime(logEvent.getEventTime())
                .setMessage(logEvent.getMessage())
                .setSource(logEvent.getSource())
                .setSeverity(GrpcLogSeverity.forNumber(logEvent.getSeverity().getValue()))
                .setTraceId(logEvent.getTraceId())
                .setSchemaVersion(logEvent.getSchemaVersion())
                .setIngestedAt(logEvent.getIngestedAt()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


}

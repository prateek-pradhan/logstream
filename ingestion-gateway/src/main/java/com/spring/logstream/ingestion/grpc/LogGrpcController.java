package com.spring.logstream.ingestion.grpc;

import com.spring.logstream.*;
import com.spring.logstream.common.dto.HttpLogEventRequest;
import com.spring.logstream.common.enums.LogSeverity;
import com.spring.logstream.common.envelope.LogEvent;
import com.spring.logstream.ingestion.service.LogService;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogGrpcController extends LogServiceGrpc.LogServiceImplBase {

    private final LogService logService;

    @Override
    public void ingestLog(LogEventGrpcRequest request, StreamObserver<GrpcLogEvent> responseObserver) {

        HttpLogEventRequest logEventRequest = getHttpLogEventRequest(request);
        LogEvent logEvent = logService.ingestLog(logEventRequest);

        GrpcLogEvent response = getGrpcLogEvent(logEvent);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<LogEventGrpcRequest> ingestLogStream(StreamObserver<GrpcLogEventList> responseObserver) {
        return new StreamObserver<LogEventGrpcRequest>() {

            private int successfulLogs = 0;
            private  int failedLogs = 0;
            private final ArrayList<GrpcLogEvent> logEvents = new ArrayList<>();
            private final List<GrpcErrorLog> errorLogs = new ArrayList<>();


            @Override
            public void onNext(LogEventGrpcRequest logEventGrpcRequest) {
                try {
                    GrpcLogEvent event = getGrpcLogEvent(logService.ingestLog(getHttpLogEventRequest(logEventGrpcRequest)));
                    logEvents.add(event);
                    successfulLogs++;
                }
                catch (Exception e) {
                    GrpcErrorLog grpcErrorLog = GrpcErrorLog.newBuilder().setEventId(logEventGrpcRequest.getEventId()).setErrorMessage(e.getMessage()).build();
                    errorLogs.add(grpcErrorLog);
                    failedLogs++;
                }
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("Stream aborted by client: {}", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                GrpcLogEventList response = GrpcLogEventList.newBuilder()
                        .addAllErrorLogs(errorLogs)
                        .setSuccessfulLogs(successfulLogs)
                        .setFailedLogs(failedLogs)
                        .addAllLogEvents(logEvents)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }

    private HttpLogEventRequest getHttpLogEventRequest(LogEventGrpcRequest request) {
        HttpLogEventRequest logEventRequest = new HttpLogEventRequest();
        logEventRequest.setEventId(request.getEventId());
        logEventRequest.setEventTime(request.getEventTime());
        logEventRequest.setMessage(request.getMessage());
        logEventRequest.setSeverity(LogSeverity.valueOf(request.getSeverity().name()));
        logEventRequest.setSource(request.getSource());
        logEventRequest.setTraceId(request.getTraceId());
        return logEventRequest;
    }

    private GrpcLogEvent getGrpcLogEvent(LogEvent logEvent) {
        return GrpcLogEvent.newBuilder()
                .setEventId(logEvent.getEventId())
                .setEventTime(logEvent.getEventTime())
                .setMessage(logEvent.getMessage())
                .setSource(logEvent.getSource())
                .setSeverity(GrpcLogSeverity.forNumber(logEvent.getSeverity().getValue()))
                .setTraceId(logEvent.getTraceId())
                .setSchemaVersion(logEvent.getSchemaVersion())
                .setIngestedAt(logEvent.getIngestedAt()).build();
    }
}

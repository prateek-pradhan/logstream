package com.spring.logstream.ingestion.grpc;

import io.grpc.*;
import org.springframework.stereotype.Component;

@Component
public class GrpcExceptionHandlingServerInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        ServerCall.Listener<ReqT> serverCallListener = next.startCall(serverCall, headers);
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(serverCallListener) {
            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (RuntimeException ex) {
                    handleException(ex, serverCall, headers);
                }
            }

            @Override
            public void onReady() {
                try {
                    super.onReady();
                } catch (RuntimeException ex) {
                    handleException(ex, serverCall, headers);
                }
            }
        };
    }

    private void handleException(RuntimeException ex, ServerCall<?, ?> call, Metadata headers) {
        Status status;

        if (ex instanceof IllegalArgumentException) {
            status = Status.INVALID_ARGUMENT.withDescription(ex.getMessage());
        }
        else {
            status = Status.INTERNAL.withDescription("An unexpected error occurred.").withCause(ex);
        }
        call.close(status, headers);
    }
}

package com.spring.logstream.config;

import com.spring.logstream.ingestion.grpc.GrpcExceptionHandlingServerInterceptor;
import com.spring.logstream.ingestion.grpc.LogGrpcController;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfiguration {

    @Bean
    public Server grpcServer(LogGrpcController grpcController, GrpcExceptionHandlingServerInterceptor grpcExceptionHandlingServerInterceptor) {
        return ServerBuilder.forPort(9090)
                .addService(grpcController)
                .intercept(grpcExceptionHandlingServerInterceptor)
                .build();
    }

    @Bean
    public GrpcServerLifecycle server(Server server) {
        return new GrpcServerLifecycle(server);
    }
}

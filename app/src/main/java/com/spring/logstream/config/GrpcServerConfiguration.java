package com.spring.logstream.config;

import com.spring.logstream.ingestion.grpc.LogGrpcController;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfiguration {

    @Bean
    public Server grpcServer(LogGrpcController grpcController) {
        return ServerBuilder.forPort(9090)
                .addService(grpcController)
                .build();
    }

    @Bean
    public GrpcServerLifecycle server(Server server) {
        return new GrpcServerLifecycle(server);
    }
}

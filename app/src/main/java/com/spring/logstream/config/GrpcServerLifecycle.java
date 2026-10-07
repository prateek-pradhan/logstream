package com.spring.logstream.config;

import io.grpc.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;

import java.io.IOException;

@Slf4j
public class GrpcServerLifecycle implements SmartLifecycle {

    private final Server server;
    private boolean running = false;

    public GrpcServerLifecycle(Server server) {
        this.server = server;
    }

    @Override
    public void start() {
        try {
            this.server.start();
            this.running = true;
            log.info("gRPC Server started, listening on {}", this.server.getPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        this.server.shutdown();
        this.running = false;
        log.info("gRPC Server stopped");
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }
}

package org.gxstar.randomdatatwo.grpc.server.service;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.protobuf.services.HealthStatusManager;
import io.grpc.protobuf.services.ProtoReflectionService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GrpcServer {
    private final HealthStatusManager healthStatusManager = new HealthStatusManager();
    private final Server server;
    public GrpcServer(final BindableService bindableService, final Integer port) {
        final ServerBuilder<?> sb = NettyServerBuilder.forPort(port)
                .keepAliveTime(60, TimeUnit.SECONDS)
                .addService(healthStatusManager.getHealthService())
                .addService(ProtoReflectionService.newInstance())
                .addService(bindableService);
        this.server = sb.build();

        try {
            log.info("starting grpc server {}", this.server);
            this.server.start();
            log.info("Started {}", this.server);
        } catch (IOException e) {
            log.error("Cant start server:{}", this.server);
            throw new RuntimeException("Can't Start grpc");
        }
    }

    public void shutdown() throws InterruptedException {
        log.debug("Stopping gRPC server");
        healthStatusManager.enterTerminalState();
        log.debug("Stopping {}", this.server);
        this.server.shutdown().awaitTermination();
    }
}

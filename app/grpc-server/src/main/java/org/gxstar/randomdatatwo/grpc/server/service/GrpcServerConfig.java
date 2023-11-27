package org.gxstar.randomdatatwo.grpc.server.service;

import io.grpc.BindableService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.gxstar.randomdatatwo.grpc")
public class GrpcServerConfig {
    @Value("${grpc.server.port:6565}")
    private Integer port;

    @Bean(destroyMethod = "shutdown")
    public GrpcServer individualServer(final BindableService service) {
        return new GrpcServer(service, port);
    }
}

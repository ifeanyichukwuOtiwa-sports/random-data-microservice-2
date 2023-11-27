package org.gxstar.randomdatatwo.config;

import org.gxstar.randomdatatwo.consumer.config.ConsumerConfig;
import org.gxstar.randomdatatwo.grpc.server.service.GrpcServerConfig;
import org.gxstar.randomdatatwo.persistence.repository.config.PersistenceConfig;
import org.gxstar.randomdatatwo.service.config.ServiceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
        PersistenceConfig.class,
        GrpcServerConfig.class,
        ServiceConfig.class,
        ConsumerConfig.class
})
public class AppConfig {
}

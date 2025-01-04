package io.dev.jobprep.core.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Bean(name = "mongoTransactionManager")
    public MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

}

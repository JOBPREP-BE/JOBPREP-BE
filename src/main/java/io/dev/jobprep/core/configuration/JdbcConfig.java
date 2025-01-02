package io.dev.jobprep.core.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JdbcConfig {

    private final DataSource dataSource;

    @Bean(name = "jdbcTransactionManager")
    public PlatformTransactionManager JdbcTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}

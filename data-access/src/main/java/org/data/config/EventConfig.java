package org.data.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.extern.log4j.Log4j2;
import org.data.model.params.EventLogParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import java.time.Duration;
import static io.r2dbc.spi.ConnectionFactoryOptions.*;


@Configuration
@Log4j2
@EnableR2dbcRepositories
public class EventConfig {

    @Autowired
    private EventLogParams eventLogParams;

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .option(DRIVER, eventLogParams.getDriver())
                        .option(HOST, eventLogParams.getHost())
                        .option(PORT, eventLogParams.getPort())
                        .option(USER, eventLogParams.getUser()) // ts: postgres
                        .option(PASSWORD, eventLogParams.getPassword()) // ts: password
                        .option(DATABASE, eventLogParams.getDatabase())
                        .build());

        ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(connectionFactory)
                .maxIdleTime(Duration.ofMillis(eventLogParams.getMaxIdleTime()))
                .maxSize(eventLogParams.getPoolSize())
                .build();

        return new ConnectionPool(configuration);
    }
}

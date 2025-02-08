package io.dev.jobprep.base;

import io.dev.jobprep.constants.ContainerConstants;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static io.dev.jobprep.constants.ContainerConstants.*;

@ExtendWith(SpringExtension.class)
@Testcontainers
public abstract class BaseTestContainer {

    protected static final DockerComposeContainer<?> composeContainer =
            new DockerComposeContainer<>(
                    new File("docker-compose-for-test-container.yml"))
                    .withExposedService(MYSQL_SRV, ContainerConstants.MYSQL_PORT, Wait.forListeningPort())
                    .withExposedService(MONGO_SRV_PRI, MONGO_PORT_PRI, Wait.forHealthcheck())
                    .withExposedService(REDIS_HOST, REDIS_PORT, Wait.forListeningPort()
            );

    @BeforeAll
    static void beforeAll() {
        composeContainer.start();
    }

    @AfterAll
    static void afterAll() {
        composeContainer.stop();
    }

    @DynamicPropertySource
    protected static void configureProperties(DynamicPropertyRegistry registry) {
        // MySQL 설정
        String mySqlHost = composeContainer.getServiceHost(MYSQL_SRV, MYSQL_PORT);
        Integer mySqlPort = composeContainer.getServicePort(MYSQL_SRV, MYSQL_PORT);

        registry.add("spring.datasource.url", () -> generateMySQLUri(mySqlHost, mySqlPort));
        registry.add("spring.datasource.username", () -> TEST_USER);
        registry.add("spring.datasource.password", () -> TEST_PASSWORD);

        // MongoDB 설정
        String mongoHost = composeContainer.getServiceHost(MONGO_SRV_PRI, MONGO_PORT_PRI);
        Integer mongoPort = composeContainer.getServicePort(MONGO_SRV_PRI, MONGO_PORT_PRI);

        registry.add("spring.data.mongodb.uri", () -> generateMongoUri(mongoHost, mongoPort));

        // Redis 설정
        String redisHost = composeContainer.getServiceHost(REDIS_HOST, REDIS_PORT);
        Integer redisPort = composeContainer.getServicePort(REDIS_HOST, REDIS_PORT);

        registry.add("spring.data.redis.host", () -> redisHost);
        registry.add("spring.data.redis.port", () -> redisPort);
    }

    private static String generateMySQLUri(String mySqlHost, Integer mySqlPort) {
        return "jdbc:mysql://" + mySqlHost + ":" + mySqlPort + "/" + MYSQL_TEST_DB
                + "?connectionTimeZone=Asia/Seoul&serverTimezone=Asia/Seoul?useSSL=false";
    }

    private static String generateMongoUri(String mongoHost, Integer mongoPort) {
        return "mongodb://" + TEST_USER + ":" + TEST_PASSWORD + "@"
                + mongoHost + ":" + mongoPort + "/" + MONGO_TEST_DB
                + "?directConnection=true&serverSelectionTimeoutMS=2000&authSource=" + MONGO_TEST_DB;
    }
}

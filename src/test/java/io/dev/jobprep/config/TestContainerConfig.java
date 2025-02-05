package io.dev.jobprep.config;

import io.dev.jobprep.util.ContainerConstants;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static io.dev.jobprep.util.ContainerConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Testcontainers
public class TestContainerConfig {

    private static final DockerComposeContainer<?> composeContainer =
            new DockerComposeContainer<>(
                    new File("docker-compose-for-test-container.yml"))
                    .withExposedService(MYSQL_SRV, ContainerConstants.MYSQL_PORT, Wait.forListeningPort())
                    .withExposedService(MONGO_SRV_PRI, MONGO_PORT_PRI, Wait.forHealthcheck()
            );

    @BeforeAll
    static void startContainer() {
        composeContainer.start();
    }

    @AfterAll
    static void stopContainer() {
        composeContainer.stop();
    }

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
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

    @Test
    public void dockerComposeContainerIsRunning() {

        assertThat(composeContainer.getContainerByServiceName(MYSQL_SRV)).isNotNull();
        assertThat(composeContainer.getContainerByServiceName(MYSQL_SRV).isPresent()).isTrue();

        assertThat(composeContainer.getContainerByServiceName(MONGO_SRV_PRI)).isNotNull();
        assertThat(composeContainer.getContainerByServiceName(MONGO_SRV_PRI).isPresent()).isTrue();

    }

}

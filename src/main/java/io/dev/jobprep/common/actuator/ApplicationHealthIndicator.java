package io.dev.jobprep.common.actuator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class ApplicationHealthIndicator implements HealthIndicator {

    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_HOST = "localhost";
    private static final String PING_COMMAND = "ping -c 1 localhost";
    private static final String DEFAULT_STATE = "UP";

    private final AtomicReference<Health> health = new AtomicReference<>(
            Health.down().build()
    );

    @Override
    public Health health() {

        boolean pingStatus = isPingSuccessful();
        boolean portStatus = isPortOpen();
        log.info("Application health-check in ping '{}', port '{}'", convert(pingStatus), convert(portStatus));

        return adaptiveFetchHealth(pingStatus, portStatus);
    }

    public void setHealth(final Health health) {
        this.health.set(health);
    }

    private boolean isPingSuccessful() {
        try {
            Process process = Runtime.getRuntime().exec(PING_COMMAND);
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    private boolean isPortOpen() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(DEFAULT_HOST, DEFAULT_PORT), 2000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private Health adaptiveFetchHealth(boolean pingStatus, boolean portStatus) {
        return pingStatus && portStatus ? healthy() : unhealthy(pingStatus, portStatus);
    }

    private Health healthy() {
        return Health.up()
                .withDetail("Ping", DEFAULT_STATE)
                .withDetail("Port", DEFAULT_STATE)
                .build();
    }

    private Health unhealthy(boolean pingStatus, boolean portStatus) {
        return Health.down()
                .withDetail("Ping", convert(pingStatus))
                .withDetail("Port", convert(portStatus))
                .build();
    }

    private String convert(boolean status) {
        return status ? "UP" : "DOWN";
    }
}

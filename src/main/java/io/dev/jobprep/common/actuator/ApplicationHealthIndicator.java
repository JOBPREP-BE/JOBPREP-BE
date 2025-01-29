package io.dev.jobprep.common.actuator;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationHealthIndicator implements HealthIndicator {

    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_HOST = "localhost";
    private static final String PING_COMMAND = "ping -c 1 localhost";
    private static final String DEFAULT_STATE = "UP";
    private static final String BLUE = "34.22.104.185";

    private final HttpServletRequest httpServletRequest;

    @Override
    public Health health() {

        boolean pingStatus = isPingSuccessful();
        boolean portStatus = isPortOpen();
        log.info("Application health-check in ping '{}', port '{}'", convert(pingStatus), convert(portStatus));

        return adaptiveFetchHealth(pingStatus, portStatus);
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
                .withDetail("Server", getServerIp())
                .build();
    }

    private Health unhealthy(boolean pingStatus, boolean portStatus) {
        return Health.down()
                .withDetail("Ping", convert(pingStatus))
                .withDetail("Port", convert(portStatus))
                .withDetail("Server", getServerIp())
                .build();
    }

    private String convert(boolean status) {
        return status ? "UP" : "DOWN";
    }

    private String getServerIp() {
        String serverIp = httpServletRequest.getLocalAddr();
        return serverIp.equals(BLUE) ? "BLUE" : "GREEN";
    }
}

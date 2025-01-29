package io.dev.jobprep.common.actuator;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationHealthIndicator implements HealthIndicator {

    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_STATE = "UP";
    private static final String BLUE = "34.22.104.185";
    private static final String PREFIX = "http://";
    private static final String HEALTH_URL = "/internal/health/server";

    private final HttpServletRequest httpServletRequest;

    @Override
    public Health health() {

        boolean httpStatus = isServerReachable();
        boolean portStatus = isPortOpen();
        log.info("Application health-check in server '{}', port '{}'", convert(httpStatus), convert(portStatus));

        return adaptiveFetchHealth(httpStatus, portStatus);
    }

    private boolean isServerReachable() {
        try {
            URL url = new URL(makeUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            return connection.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private String makeUrl() {
        return PREFIX + DEFAULT_HOST + ":" + DEFAULT_PORT + HEALTH_URL;
    }

    private boolean isPortOpen() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(DEFAULT_HOST, DEFAULT_PORT), 2000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private Health adaptiveFetchHealth(boolean httpStatus, boolean portStatus) {
        return httpStatus && portStatus ? healthy() : unhealthy(httpStatus, portStatus);
    }

    private Health healthy() {
        return Health.up()
                .withDetail("Http", DEFAULT_STATE)
                .withDetail("Port", DEFAULT_STATE)
                .withDetail("Server", getServerIp())
                .build();
    }

    private Health unhealthy(boolean httpStatus, boolean portStatus) {
        return Health.down()
                .withDetail("Http", convert(httpStatus))
                .withDetail("Port", convert(portStatus))
                .withDetail("Server", getServerIp())
                .build();
    }

    private String convert(boolean status) {
        return status ? "UP" : "DOWN";
    }

    private String getServerIp() {
        String serverIp = httpServletRequest.getLocalAddr();
        log.info("incoming server IP: {}", serverIp);
        return serverIp.equals(BLUE) ? "BLUE" : "GREEN";
    }
}

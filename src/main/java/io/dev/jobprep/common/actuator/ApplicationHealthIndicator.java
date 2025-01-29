package io.dev.jobprep.common.actuator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

@Slf4j
@Component
public class ApplicationHealthIndicator implements HealthIndicator {

    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_STATE = "UP";
    private static final String BLUE = "34.22.104.185";
    private static final String PREFIX = "http://";
    private static final String HEALTH_URL = "/internal/health/server";

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
        try {
            URL url = new URL("https://checkip.amazonaws.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String publicIP = in.readLine();
            in.close();

            return publicIP.equals(BLUE) ? "BLUE" : "GREEN";
        } catch (IOException e) {
            log.info("Failed to get server IP cause {}", e.getMessage());
            return "UNKNOWN";
        }
    }
}

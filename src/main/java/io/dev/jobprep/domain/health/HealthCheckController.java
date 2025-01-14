package io.dev.jobprep.domain.health;

import io.dev.jobprep.common.actuator.MutableHealthIndicator;
import io.dev.jobprep.system.internal.developer.DeveloperTokenHelper;
import io.dev.jobprep.util.IpAddressHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RestController
@RequestMapping("/internal/health")
@RequiredArgsConstructor
public class HealthCheckController {

    private final MutableHealthIndicator healthIndicator;
    private final DeveloperTokenHelper developerTokenHelper;

    @PutMapping("/up")
    public void up(HttpServletRequest request, @RequestHeader(AUTHORIZATION) String token) {

        developerTokenHelper.verify(token);
        fetchIncomingIp(request);

        healthIndicator.setHealth(Health.up().build());
        log.info("Health check up!");
    }

    @PutMapping("/down")
    public void down(HttpServletRequest request, @RequestHeader(AUTHORIZATION) String token) {

        developerTokenHelper.verify(token);
        fetchIncomingIp(request);

        healthIndicator.setHealth(Health.down().build());
        log.info("Health check down!");
    }

    private void fetchIncomingIp(HttpServletRequest request) {
        String clientIp = IpAddressHelper.getClientIp(request);
        log.info("Call internal health-check API with Incoming-Ip {}", clientIp);
    }
}

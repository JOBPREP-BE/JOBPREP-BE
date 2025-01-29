package io.dev.jobprep.system.internal.health;

import io.dev.jobprep.common.actuator.ApplicationHealthIndicator;
import io.dev.jobprep.system.internal.developer.DeveloperTokenHelper;
import io.dev.jobprep.util.IpAddressHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RestController
@RequestMapping("/internal/health")
@RequiredArgsConstructor
public class HealthCheckController {

    private final ApplicationHealthIndicator healthIndicator;
    private final DeveloperTokenHelper developerTokenHelper;

    @GetMapping("/server")
    public ResponseEntity<Void> serverHealthCheck(HttpServletRequest req, HttpServletResponse res) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/up")
    public void up(HttpServletRequest request, @RequestHeader(AUTHORIZATION) String token) {

        developerTokenHelper.verify(token);
        fetchIncomingIp(request);

        // healthIndicator.setHealth(Health.up().build());
        log.info("Health check up!");
    }

    @PutMapping("/down")
    public void down(HttpServletRequest request, @RequestHeader(AUTHORIZATION) String token) {

        developerTokenHelper.verify(token);
        fetchIncomingIp(request);

        // healthIndicator.setHealth(Health.down().build());
        log.info("Health check down!");
    }

    private void fetchIncomingIp(HttpServletRequest request) {
        String clientIp = IpAddressHelper.getClientIp(request);
        log.info("Call internal health-check API with Incoming-Ip {}", clientIp);
    }
}

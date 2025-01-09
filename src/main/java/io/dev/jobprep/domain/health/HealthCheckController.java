package io.dev.jobprep.domain.health;

import io.dev.jobprep.common.actuator.MutableHealthIndicator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    private final MutableHealthIndicator healthIndicator;

    @PutMapping("/health/up")
    public void up() {
        healthIndicator.setHealth(Health.up().build());
    }

    @PutMapping("/health/down")
    public void down() {
        healthIndicator.setHealth(Health.down().build());
    }
}

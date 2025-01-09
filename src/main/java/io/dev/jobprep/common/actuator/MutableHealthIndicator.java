package io.dev.jobprep.common.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class MutableHealthIndicator implements HealthIndicator {

    private final AtomicReference<Health> health = new AtomicReference<>(
            Health.down().build()
    );

    @Override
    public Health health() {
        return health.get();
    }

    public void setHealth(final Health health) {
        this.health.set(health);
    }
}

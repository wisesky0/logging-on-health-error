package org.springframework.boot.actuate.health;

public class HealthAccessor {

    private Health health;
    public HealthAccessor(Health health) {
        this.health = health;
    }

    public Health withoutDetails() {
        return health.withoutDetails();
    }

}

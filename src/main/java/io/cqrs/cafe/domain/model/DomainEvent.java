package io.cqrs.cafe.domain.model;

import java.util.Date;

public interface DomainEvent {
    public Date occurredOn();
}

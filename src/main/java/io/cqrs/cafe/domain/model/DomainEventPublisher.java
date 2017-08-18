package io.cqrs.cafe.domain.model;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}

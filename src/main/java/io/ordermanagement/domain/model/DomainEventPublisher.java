package io.ordermanagement.domain.model;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}

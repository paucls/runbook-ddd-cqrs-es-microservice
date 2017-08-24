package io.cqrs.taskmanagement.domain.model;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}

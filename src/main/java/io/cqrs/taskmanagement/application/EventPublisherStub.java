package io.cqrs.taskmanagement.application;

import io.cqrs.taskmanagement.domain.model.DomainEvent;
import io.cqrs.taskmanagement.domain.model.DomainEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisherStub implements DomainEventPublisher {
    @Override
    public void publish(DomainEvent event) {
        System.out.println(event);
    }
}

package io.cqrs.taskmanagement.application;

import io.cqrs.taskmanagement.domain.model.DomainEvent;

import java.util.List;

public interface EventStore {

    public void append(DomainEvent aDomainEvent);

    public void append(List<DomainEvent> domainEvents);

}

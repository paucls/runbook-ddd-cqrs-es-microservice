package io.cqrs.taskmanagement.event.sourcing;

import io.cqrs.taskmanagement.domain.model.DomainEvent;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface EventStore {

    public void appendToStream(String aggregateId, DomainEvent event, int originalVersion);

    public void appendToStream(String aggregateId, List<DomainEvent> events, int originalVersion);

    public EventStream loadEventStream(String aggregateId) throws EntityNotFoundException;

}

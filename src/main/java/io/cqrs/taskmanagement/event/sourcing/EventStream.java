package io.cqrs.taskmanagement.event.sourcing;

import io.cqrs.taskmanagement.domain.model.DomainEvent;
import lombok.Value;

import java.util.List;

@Value
public class EventStream {
    private List<DomainEvent> events;
    private int version;
}

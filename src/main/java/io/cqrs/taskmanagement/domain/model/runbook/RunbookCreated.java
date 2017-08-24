package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.DomainEvent;
import lombok.Value;

@Value
public class RunbookCreated implements DomainEvent {
    String runbookId;
    String name;
}

package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.Aggregate;
import io.cqrs.taskmanagement.domain.model.DomainEventPublisher;

class Runbook implements Aggregate {

    private final String projectId;
    private final String runbookId;
    private final String name;
    private final DomainEventPublisher eventPublisher;

    Runbook(String projectId, String runbookId, String name, DomainEventPublisher eventPublisher) {
        this.projectId = projectId;
        this.runbookId = runbookId;
        this.name = name;
        this.eventPublisher = eventPublisher;

        eventPublisher.publish(new RunbookCreated(runbookId, name));
    }

    void handle(AddTask c) {
        eventPublisher.publish(new TaskAdded(c.getTaskId(), c.getName()));
    }
}

package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.DomainEventPublisher;

public class Runbook {

    public Runbook(String projectId, String runbookId, String name, DomainEventPublisher eventPublisherMock) {
        eventPublisherMock.publish(new RunbookCreated(runbookId, name));
    }
}

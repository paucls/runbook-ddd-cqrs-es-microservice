package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.DomainEventPublisher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

public class RunbookTest {

    private DomainEventPublisher eventPublisherMock;
    private Runbook runbook;

    @Before
    public void setup() {
        eventPublisherMock = Mockito.mock(DomainEventPublisher.class);
        runbook = new Runbook("project-id", "runbook-id", "runbook-name", eventPublisherMock);
    }

    @Test
    public void can_create_runbook() {
        // Then
        verify(eventPublisherMock).publish(new RunbookCreated("runbook-id", "runbook-name"));
    }

    @Test
    public void can_add_task() {
        // When
        runbook.handle(new AddTask("addTask-id", "addTask-name", "addTask description", "user-id"));

        // Then
        verify(eventPublisherMock).publish(new TaskAdded("addTask-id", "addTask-name"));
    }
}

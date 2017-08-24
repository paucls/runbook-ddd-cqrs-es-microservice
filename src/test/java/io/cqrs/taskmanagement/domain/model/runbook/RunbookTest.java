package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.DomainEventPublisher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

public class RunbookTest {

    private DomainEventPublisher eventPublisherMock;
    private Runbook runbook;

    @Rule
    public ExpectedException exception = ExpectedException.none();

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
        runbook.handle(new AddTask("task-id", "name", "description", "user-id"));

        // Then
        verify(eventPublisherMock).publish(new TaskAdded("task-id", "name", "description", "user-id"));
        assertThat(runbook.tasks().size(), is(1)); // TODO do we really need this?
    }

    @Test
    public void can_start_task() {
        // Given
        runbook.apply(new TaskAdded("task-id", "name", "description", "user-id"));

        // When
        runbook.handle(new StartTask("task-id", "user-id"));

        // Then
        verify(eventPublisherMock).publish(new TaskMarkedInProgress("task-id"));
    }

    @Test
    public void cannot_start_task_assigned_to_different_user() {
        // Given
        runbook.apply(new TaskAdded("task-id", "name", "description", "user-id-1"));

        exception.expect(TaskAssignedToDifferentUserException.class);

        // When
        runbook.handle(new StartTask("task-id", "user-id-2"));
    }
}

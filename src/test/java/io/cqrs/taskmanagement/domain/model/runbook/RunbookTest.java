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
        runbook = new Runbook(eventPublisherMock);
    }

    @Test
    public void can_create_runbook() {
        // When
        Runbook newRunbook = new Runbook(new CreateRunbook("project-id", "runbook-id", "runbook-name", "owner-id"), eventPublisherMock);

        // Then
        verify(eventPublisherMock).publish(new RunbookCreated("project-id", "runbook-id", "runbook-name", "owner-id"));

        // Assert aggregate state is initialized properly
        assertThat(newRunbook.projectId(), is("project-id")); // TODO do we really need to be explicit assertint this here?
        assertThat(newRunbook.runbookId(), is("runbook-id"));
        assertThat(newRunbook.name(), is("runbook-name"));
        assertThat(newRunbook.ownerId(), is("owner-id"));
        assertThat(newRunbook.isCompleted(), is(false));
    }

    @Test
    public void can_add_task() {
        // Given

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

    @Test
    public void can_complete_task() {
        // Given
        runbook.apply(new TaskAdded("task-id", "name", "description", "user-id"));
        runbook.apply(new TaskMarkedInProgress("task-id"));

        // When
        runbook.handle(new CompleteTask("task-id", "user-id"));

        // Then
        verify(eventPublisherMock).publish(new TaskCompleted("task-id", "user-id"));
    }

    @Test
    public void cannot_complete_task_assigned_to_different_user() {
        // Given
        runbook.apply(new TaskAdded("task-id", "name", "description", "user-id-1"));
        runbook.apply(new TaskMarkedInProgress("task-id"));

        exception.expect(TaskAssignedToDifferentUserException.class);

        // When
        runbook.handle(new CompleteTask("task-id", "user-id-2"));
    }

    @Test
    public void cannot_complete_task_that_is_not_started() {
        // Given
        runbook.apply(new TaskAdded("task-id", "name", "description", "user-id"));

        exception.expect(CanOnlyCompleteInProgressTaskException.class);

        // When
        runbook.handle(new CompleteTask("task-id", "user-id"));
    }

    @Test
    public void cannot_complete_runbook_if_not_the_owner() {
        // Given
        runbook.apply(new RunbookCreated("project-id", "runbook-id", "name", "user-id-1"));

        exception.expect(RunbookOwnedByDifferentUserException.class);

        // When
        runbook.handle(new CloseRunbook("runbook-id", "user-id-2"));
    }

    @Test
    public void can_complete_runbook() {
        // Given
        runbook.apply(new RunbookCreated("project-id", "runbook-id", "name", "user-id"));

        // When
        runbook.handle(new CloseRunbook("runbook-id", "user-id"));

        // Then
        verify(eventPublisherMock).publish(new RunbookCompleted("runbook-id"));
        assertThat(runbook.isCompleted(), is(true));
    }

    // TODO can not complete runbook with pending tasks
}

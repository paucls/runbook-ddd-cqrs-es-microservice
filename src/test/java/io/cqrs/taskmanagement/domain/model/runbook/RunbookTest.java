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

    private static final String RUNBOOK_ID = "runbook-id";
    private static final String PROJECT_ID = "project-id";
    private static final String RUNBOOK_NAME = "runbook-name";
    private static final String OWNER_ID = "owner-id";
    private static final String TASK_ID = "task-id";
    private static final String TASK_NAME = "name";
    private static final String TASK_DESCRIPTION = "description";
    private static final String USER_ID = "user-id";

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
        Runbook newRunbook = new Runbook(new CreateRunbook(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, OWNER_ID), eventPublisherMock);

        // Then
        verify(eventPublisherMock).publish(new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, OWNER_ID));

        // Assert aggregate state is initialized properly
        assertThat(newRunbook.getProjectId(), is(PROJECT_ID)); // TODO do we really need to be explicit asserting this here?
        assertThat(newRunbook.getRunbookId(), is(RUNBOOK_ID));
        assertThat(newRunbook.getName(), is(RUNBOOK_NAME));
        assertThat(newRunbook.getOwnerId(), is(OWNER_ID));
        assertThat(newRunbook.isCompleted(), is(false));
    }

    @Test
    public void can_add_task() {
        // Given
        runbook.apply(new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, OWNER_ID));

        // When
        runbook.handle(new AddTask(RUNBOOK_ID, TASK_ID, TASK_NAME, TASK_DESCRIPTION, USER_ID));

        // Then
        verify(eventPublisherMock).publish(new TaskAdded(TASK_ID, TASK_NAME, TASK_DESCRIPTION, USER_ID));
        assertThat(runbook.getTasks().size(), is(1)); // TODO do we really need this?
    }

    @Test
    public void can_start_task() {
        // Given
        runbook.apply(new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, OWNER_ID));
        runbook.apply(new TaskAdded(TASK_ID, TASK_NAME, TASK_DESCRIPTION, USER_ID));

        // When
        runbook.handle(new StartTask(RUNBOOK_ID, TASK_ID, USER_ID));

        // Then
        verify(eventPublisherMock).publish(new TaskMarkedInProgress(TASK_ID));
    }

    @Test
    public void cannot_start_task_assigned_to_different_user() {
        // Given
        runbook.apply(new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, OWNER_ID));
        runbook.apply(new TaskAdded(TASK_ID, TASK_NAME, TASK_DESCRIPTION, USER_ID));

        exception.expect(TaskAssignedToDifferentUserException.class);

        // When
        runbook.handle(new StartTask(RUNBOOK_ID, TASK_ID, "user-id-2"));
    }

    @Test
    public void can_complete_task() {
        // Given
        runbook.apply(new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, OWNER_ID));
        runbook.apply(new TaskAdded(TASK_ID, TASK_NAME, TASK_DESCRIPTION, USER_ID));
        runbook.apply(new TaskMarkedInProgress(TASK_ID));

        // When
        runbook.handle(new CompleteTask(TASK_ID, USER_ID));

        // Then
        verify(eventPublisherMock).publish(new TaskCompleted(TASK_ID, USER_ID));
    }

    @Test
    public void cannot_complete_task_assigned_to_different_user() {
        // Given
        runbook.apply(new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, OWNER_ID));
        runbook.apply(new TaskAdded(TASK_ID, TASK_NAME, TASK_DESCRIPTION, USER_ID));
        runbook.apply(new TaskMarkedInProgress(TASK_ID));

        exception.expect(TaskAssignedToDifferentUserException.class);

        // When
        runbook.handle(new CompleteTask(TASK_ID, "user-id-2"));
    }

    @Test
    public void cannot_complete_task_that_is_not_started() {
        // Given
        runbook.apply(new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, OWNER_ID));
        runbook.apply(new TaskAdded(TASK_ID, TASK_NAME, TASK_DESCRIPTION, USER_ID));

        exception.expect(CanOnlyCompleteInProgressTaskException.class);

        // When
        runbook.handle(new CompleteTask(TASK_ID, USER_ID));
    }

    @Test
    public void cannot_complete_runbook_if_not_the_owner() {
        // Given
        runbook.apply(new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, USER_ID));

        exception.expect(RunbookOwnedByDifferentUserException.class);

        // When
        runbook.handle(new CompleteRunbook(RUNBOOK_ID, "user-id-2"));
    }

    @Test
    public void can_complete_runbook() {
        // Given
        runbook.apply(new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, USER_ID));

        // When
        runbook.handle(new CompleteRunbook(RUNBOOK_ID, USER_ID));

        // Then
        verify(eventPublisherMock).publish(new RunbookCompleted(RUNBOOK_ID));
        assertThat(runbook.isCompleted(), is(true));
    }

    @Test
    public void can_not_complete_runbook_with_pending_tasks() {
        // Given
        runbook.apply(new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, USER_ID));
        runbook.apply(new TaskAdded("task-id-1", TASK_NAME, TASK_DESCRIPTION, USER_ID));
        runbook.apply(new TaskAdded("task-id-2", TASK_NAME, TASK_DESCRIPTION, USER_ID));
        runbook.apply(new TaskCompleted("task-id-1", USER_ID));

        exception.expect(RunBookWithPendingTasksException.class);

        // When
        runbook.handle(new CompleteRunbook(RUNBOOK_ID, USER_ID));
    }

    @Test
    public void can_complete_runbook_with_all_tasks_completed() {
        // Given
        runbook.apply(new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, USER_ID));
        runbook.apply(new TaskAdded(TASK_ID, TASK_NAME, TASK_DESCRIPTION, USER_ID));
        runbook.apply(new TaskCompleted(TASK_ID, USER_ID));

        // When
        runbook.handle(new CompleteRunbook(RUNBOOK_ID, USER_ID));

        // Then
        verify(eventPublisherMock).publish(new RunbookCompleted(RUNBOOK_ID));
        assertThat(runbook.isCompleted(), is(true));
    }
}

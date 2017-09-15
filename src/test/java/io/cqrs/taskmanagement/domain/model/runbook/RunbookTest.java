package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.Command;
import io.cqrs.taskmanagement.domain.model.DomainEvent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

public class RunbookTest {

    private static final String RUNBOOK_ID = "runbook-id";
    private static final String PROJECT_ID = "project-id";
    private static final String RUNBOOK_NAME = "runbook-name";
    private static final String OWNER_ID = "owner-id";
    private static final String TASK_ID = "task-id";
    private static final String TASK_NAME = "name";
    private static final String TASK_DESCRIPTION = "description";
    private static final String USER_ID = "user-id";

    private Runbook runbook;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        runbook = new Runbook();
    }

    @Test
    public void can_create_runbook() {
        // When
        Runbook newRunbook = new Runbook(new CreateRunbook(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, OWNER_ID));

        // Then
        assertThat(newRunbook.getUncommitedEvents().get(0), is(new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, OWNER_ID)));

        // Assert aggregate state is initialized properly
        assertThat(newRunbook.getProjectId(), is(PROJECT_ID)); // TODO do we really need to be explicit asserting this here?
        assertThat(newRunbook.getRunbookId(), is(RUNBOOK_ID));
        assertThat(newRunbook.getName(), is(RUNBOOK_NAME));
        assertThat(newRunbook.getOwnerId(), is(OWNER_ID));
        assertThat(newRunbook.isCompleted(), is(false));
    }

    @Test
    public void can_add_task() {
        given(
                new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, OWNER_ID)
        );

        when(
                new AddTask(RUNBOOK_ID, TASK_ID, TASK_NAME, TASK_DESCRIPTION, USER_ID)
        );

        then(
                new TaskAdded(TASK_ID, TASK_NAME, TASK_DESCRIPTION, USER_ID)
        );
        assertThat(runbook.getTasks().size(), is(1)); // TODO do we really need this?
    }

    @Test
    public void can_start_task() {
        given(
                new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, OWNER_ID),
                new TaskAdded(TASK_ID, TASK_NAME, TASK_DESCRIPTION, USER_ID)
        );

        when(
                new StartTask(RUNBOOK_ID, TASK_ID, USER_ID)
        );

        then(
                new TaskMarkedInProgress(TASK_ID)
        );
    }

    @Test
    public void cannot_start_task_assigned_to_different_user() {
        given(
                new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, OWNER_ID),
                new TaskAdded(TASK_ID, TASK_NAME, TASK_DESCRIPTION, USER_ID)
        );

        exception.expect(TaskAssignedToDifferentUserException.class);

        // When
        runbook.handle(new StartTask(RUNBOOK_ID, TASK_ID, "user-id-2"));
    }

    @Test
    public void can_complete_task() {
        given(
                new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, OWNER_ID),
                new TaskAdded(TASK_ID, TASK_NAME, TASK_DESCRIPTION, USER_ID),
                new TaskMarkedInProgress(TASK_ID)
        );

        when(
                new CompleteTask(RUNBOOK_ID, TASK_ID, USER_ID)
        );

        then(
                new TaskCompleted(TASK_ID, USER_ID)
        );
    }

    @Test
    public void cannot_complete_task_assigned_to_different_user() {
        given(
                new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, OWNER_ID),
                new TaskAdded(TASK_ID, TASK_NAME, TASK_DESCRIPTION, USER_ID),
                new TaskMarkedInProgress(TASK_ID)
        );

        exception.expect(TaskAssignedToDifferentUserException.class);

        // When
        runbook.handle(new CompleteTask(RUNBOOK_ID, TASK_ID, "user-id-2"));
    }

    @Test
    public void cannot_complete_task_that_is_not_started() {
        given(
                new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, OWNER_ID),
                new TaskAdded(TASK_ID, TASK_NAME, TASK_DESCRIPTION, USER_ID)
        );

        exception.expect(CanOnlyCompleteInProgressTaskException.class);

        // When
        runbook.handle(new CompleteTask(RUNBOOK_ID, TASK_ID, USER_ID));
    }

    @Test
    public void cannot_complete_runbook_if_not_the_owner() {
        given(
                new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, USER_ID)
        );

        exception.expect(RunbookOwnedByDifferentUserException.class);

        // When
        runbook.handle(new CompleteRunbook(RUNBOOK_ID, "user-id-2"));
    }

    @Test
    public void can_complete_runbook() {
        given(
                new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, USER_ID)
        );

        when(
                new CompleteRunbook(RUNBOOK_ID, USER_ID)
        );

        then(
                new RunbookCompleted(RUNBOOK_ID)
        );
        assertThat(runbook.isCompleted(), is(true));
    }

    @Test
    public void can_not_complete_runbook_with_pending_tasks() {
        given(
                new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, USER_ID),
                new TaskAdded("task-id-1", TASK_NAME, TASK_DESCRIPTION, USER_ID),
                new TaskAdded("task-id-2", TASK_NAME, TASK_DESCRIPTION, USER_ID),
                new TaskCompleted("task-id-1", USER_ID)
        );

        exception.expect(RunBookWithPendingTasksException.class);

        // When
        runbook.handle(new CompleteRunbook(RUNBOOK_ID, USER_ID));
    }

    @Test
    public void can_complete_runbook_with_all_tasks_completed() {
        given(
                new RunbookCreated(PROJECT_ID, RUNBOOK_ID, RUNBOOK_NAME, USER_ID),
                new TaskAdded(TASK_ID, TASK_NAME, TASK_DESCRIPTION, USER_ID),
                new TaskCompleted(TASK_ID, USER_ID)
        );

        when(
                new CompleteRunbook(RUNBOOK_ID, USER_ID)
        );

        then(
                new RunbookCompleted(RUNBOOK_ID)
        );
        assertThat(runbook.isCompleted(), is(true));
    }

    private void given(DomainEvent... events) {
        runbook.apply(Arrays.asList(events));
    }

    private void when(Command command) {
        runbook.handle(command);
    }

    private void then(DomainEvent... events) {
        List<DomainEvent> uncommitedEvents = runbook.getUncommitedEvents();

        for (DomainEvent event : events) {
            assertThat("Expected domain event was not published", uncommitedEvents, hasItem(event));
        }

        assertThat("Unexpected number of published domain events", uncommitedEvents.size(), is(events.length));
    }
}

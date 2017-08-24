package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.Aggregate;
import io.cqrs.taskmanagement.domain.model.DomainEventPublisher;

import java.util.HashMap;

class Runbook implements Aggregate {

    private final String projectId;
    private final String runbookId;
    private final String name;
    private final DomainEventPublisher eventPublisher;
    private HashMap<String, Task> tasks = new HashMap<>();

    Runbook(String projectId, String runbookId, String name, DomainEventPublisher eventPublisher) {
        this.projectId = projectId;
        this.runbookId = runbookId;
        this.name = name;
        this.eventPublisher = eventPublisher;

        eventPublisher.publish(new RunbookCreated(runbookId, name));
    }

    HashMap<String, Task> tasks() {
        return this.tasks;
    }


    //
    // Handle
    //

    void handle(AddTask c) {
        TaskAdded taskAdded = new TaskAdded(c.getTaskId(), c.getName(), "description", "user-id");
        eventPublisher.publish(taskAdded);
        apply(taskAdded);
    }

    void handle(StartTask c) {
        verifyAssignee(c.getTaskId(), c.getUserId());

        TaskMarkedInProgress taskMarkedInProgress = new TaskMarkedInProgress(c.getTaskId());
        eventPublisher.publish(taskMarkedInProgress);
        apply(taskMarkedInProgress);
    }

    void handle(CompleteTask c) {
        verifyAssignee(c.getTaskId(), c.getUserId());
        verifyInProgress(c.getTaskId());

        eventPublisher.publish(new TaskCompleted(c.getTaskId(), c.getUserId()));
    }

    private void verifyAssignee(String taskId, String userId) {
        Task task = tasks.get(taskId);
        if (!task.getUserId().equals(userId)) {
            throw new TaskAssignedToDifferentUserException();
        }
    }

    private void verifyInProgress(String taskId) {
        if (!tasks.get(taskId).isInProgress()) { // TODO should not this go down to the Task aggregate?
            throw new CanOnlyCompleteInProgressTaskException();
        }
    }


    //
    // Apply
    //

    void apply(TaskAdded e) {
        tasks.put(e.getTaskId(), new Task(e.getTaskId(), e.getUserId()));
    }

    void apply(TaskMarkedInProgress e) {
        // TODO Which aggregate should be responsible to apply the task status change?
        tasks.get(e.getTaskId()).apply(e);
    }
}

package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.Aggregate;
import io.cqrs.taskmanagement.domain.model.DomainEventPublisher;

import java.util.HashMap;

class Runbook implements Aggregate {

    private String projectId;
    private String runbookId;
    private String name;
    private String ownerId;
    private DomainEventPublisher eventPublisher;
    private HashMap<String, Task> tasks = new HashMap<>();

    // constructor needed for reconstruction
    Runbook(DomainEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    HashMap<String, Task> tasks() {
        return this.tasks;
    }

    // We won't need accessors if we do not use this Entity as a read model
    String projectId() {
        return this.projectId;
    }

    String runbookId() {
        return this.runbookId;
    }

    String name() {
        return this.name;
    }

    public Object ownerId() {
        return ownerId;
    }

    //
    // Handle
    //

    public Runbook(CreateRunbook c, DomainEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;

        RunbookCreated runbookCreated = new RunbookCreated(c.getProjectId(), c.getRunbookId(), c.getName(), c.getOwnerId());
        eventPublisher.publish(runbookCreated);
        apply(runbookCreated);
    }

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

    public void handle(CloseRunbook c) {
        if (this.ownerId.equals(c.getUserId())) throw new RunbookOwnedByDifferentUserException();


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

    void apply(RunbookCreated c) {
        this.projectId = c.getProjectId();
        this.runbookId = c.getRunbookId();
        this.name = c.getName();
        this.ownerId = c.getOwnerId();
    }

    void apply(TaskAdded e) {
        tasks.put(e.getTaskId(), new Task(e.getTaskId(), e.getUserId()));
    }

    void apply(TaskMarkedInProgress e) {
        // TODO Which aggregate should be responsible to apply the task status change?
        tasks.get(e.getTaskId()).apply(e);
    }
}

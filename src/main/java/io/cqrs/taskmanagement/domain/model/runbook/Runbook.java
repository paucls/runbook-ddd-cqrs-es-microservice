package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.Aggregate;
import io.cqrs.taskmanagement.domain.model.DomainEventPublisher;

import java.util.HashMap;

class Runbook implements Aggregate {

    private String projectId;
    private String runbookId;
    private String name;
    private String ownerId;
    private boolean isCompleted;
    private HashMap<String, Task> tasks;
    private DomainEventPublisher eventPublisher;

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

    boolean isCompleted() {
        return this.isCompleted;
    }

    String ownerId() {
        return ownerId;
    }

    //
    // Handle
    //

    // Note this constructor is also a command handler
    Runbook(CreateRunbook c, DomainEventPublisher eventPublisher) {
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

        TaskCompleted taskCompleted = new TaskCompleted(c.getTaskId(), c.getUserId());
        eventPublisher.publish(taskCompleted);
        apply(taskCompleted);
    }

    void handle(CompleteRunbook c) {
        verifyIsOwner(c.getUserId());
        verifyAllTasksCompleted();

        RunbookCompleted runbookCompleted = new RunbookCompleted(c.getRunbookId());
        eventPublisher.publish(runbookCompleted);
        apply(runbookCompleted);
    }

    private void verifyIsOwner(String userId) {
        if (!this.ownerId.equals(userId)) throw new RunbookOwnedByDifferentUserException();
    }

    private void verifyAllTasksCompleted() {
        boolean hasPendingTasks = tasks.entrySet()
                .stream()
                .anyMatch(entry -> !entry.getValue().isClosed());
        if (hasPendingTasks) throw new RunBookWithPendingTasksException();
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
        this.isCompleted = false;
        this.tasks = new HashMap<>();
    }

    void apply(TaskAdded e) {
        tasks.put(e.getTaskId(), new Task(e.getTaskId(), e.getUserId()));
    }

    void apply(TaskMarkedInProgress e) {
        // TODO Which aggregate should be responsible to apply the task status change?
        tasks.get(e.getTaskId()).apply(e);
    }

    void apply(RunbookCompleted e) {
        this.isCompleted = true;
    }

    void apply(TaskCompleted e) {
        tasks.get(e.getTaskId()).apply(e);
    }
}

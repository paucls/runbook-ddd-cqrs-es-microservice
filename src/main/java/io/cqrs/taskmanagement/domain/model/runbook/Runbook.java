package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.DomainEvent;
import io.cqrs.taskmanagement.event.sourcing.EventStream;
import io.cqrs.taskmanagement.domain.model.EventSourcedAggregate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Runbook extends EventSourcedAggregate {

    private String runbookId;
    private Map<String, Task> tasks;
    private String projectId;
    private String name;
    private String ownerId;
    private boolean isCompleted;

    private List<DomainEvent> uncommitedEvents;

    // empty constructor for rest api TODO: probably we need a DTO there
    Runbook() {
        this.tasks = new HashMap<>();
        this.uncommitedEvents = new ArrayList<>();
    }

    public Runbook(EventStream eventStream) {
        this.tasks = new HashMap<>();
        this.uncommitedEvents = new ArrayList<>();

        // Reinstate this aggregate to latest version
        this.apply(eventStream.getEvents());
    }

    Map<String, Task> getTasks() {
        return this.tasks;
    }

    // We won't need accessors if we do not use this Entity as a read model
    public String getProjectId() {
        return this.projectId;
    }

    public String getRunbookId() {
        return this.runbookId;
    }

    public String getName() {
        return this.name;
    }

    public boolean isCompleted() {
        return this.isCompleted;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public List<DomainEvent> getUncommitedEvents() {
        return uncommitedEvents;
    }

    //
    // Handle
    //

    // Note this constructor is also a command handler
    public Runbook(CreateRunbook c) {
        this.tasks = new HashMap<>();
        this.uncommitedEvents = new ArrayList<>();

        RunbookCreated runbookCreated = new RunbookCreated(c.getProjectId(), c.getRunbookId(), c.getName(), c.getOwnerId());
        uncommitedEvents.add(runbookCreated);
        apply(runbookCreated);
    }

    public void handle(AddTask c) {
        TaskAdded taskAdded = new TaskAdded(c.getRunbookId(), c.getTaskId(), c.getName(), c.getDescription(), c.getAssigneeId());
        uncommitedEvents.add(taskAdded);
        apply(taskAdded);
    }

    public void handle(StartTask c) {
        verifyAssignee(c.getTaskId(), c.getUserId());

        TaskMarkedInProgress taskMarkedInProgress = new TaskMarkedInProgress(c.getTaskId());
        uncommitedEvents.add(taskMarkedInProgress);
        apply(taskMarkedInProgress);
    }

    public void handle(CompleteTask c) {
        verifyAssignee(c.getTaskId(), c.getUserId());
        verifyInProgress(c.getTaskId());

        TaskCompleted taskCompleted = new TaskCompleted(c.getTaskId(), c.getUserId());
        uncommitedEvents.add(taskCompleted);
        apply(taskCompleted);
    }

    public void handle(CompleteRunbook c) {
        verifyIsOwner(c.getUserId());
        verifyAllTasksCompleted();

        RunbookCompleted runbookCompleted = new RunbookCompleted(c.getRunbookId());
        uncommitedEvents.add(runbookCompleted);
        apply(runbookCompleted);
    }

    private void verifyIsOwner(String userId) {
        if (!this.ownerId.equals(userId)) throw new RunbookOwnedByDifferentUserException();
    }

    private void verifyAllTasksCompleted() {
        boolean hasPendingTasks = tasks.entrySet()
                .stream()
                .anyMatch(entry -> !entry.getValue().isClosed());
        if (hasPendingTasks) throw new RunbookWithPendingTasksException();
    }

    private void verifyAssignee(String taskId, String userId) {
        Task task = tasks.get(taskId);
        if (!task.getAssigneeId().equals(userId)) {
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

    private void apply(RunbookCreated c) {
        this.projectId = c.getProjectId();
        this.runbookId = c.getRunbookId();
        this.name = c.getName();
        this.ownerId = c.getOwnerId();
        this.isCompleted = false;
        this.tasks = new HashMap<>();
    }

    private void apply(TaskAdded e) {
        tasks.put(e.getTaskId(), new Task(e.getTaskId(), e.getName(), e.getDescription(), e.getAssigneeId()));
    }

    private void apply(TaskMarkedInProgress e) {
        tasks.get(e.getTaskId()).apply(e);
    }

    private void apply(RunbookCompleted e) {
        this.isCompleted = true;
    }

    private void apply(TaskCompleted e) {
        tasks.get(e.getTaskId()).apply(e);
    }

}

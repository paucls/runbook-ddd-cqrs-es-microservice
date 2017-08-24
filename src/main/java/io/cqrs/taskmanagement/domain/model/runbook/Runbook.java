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

    public HashMap<String, Task> tasks() {
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

    public void handle(StartTask c) {
        Task task = tasks.get(c.getTaskId());
        if (!task.getUserId().equals(c.getUserId())) {
            throw new TaskAssignedToDifferentUserException();
        }
        eventPublisher.publish(new TaskMarkedInProgress(c.getTaskId()));
    }

    //
    // Apply
    //

    public void apply(TaskAdded e) {
        tasks.put(e.getTaskId(), new Task(e.getTaskId(), e.getUserId()));
    }

}

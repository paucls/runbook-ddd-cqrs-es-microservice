package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.Aggregate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Task implements Aggregate {
    private static final String OPENED = "OPEN";
    private static final String IN_PROGRESS = "IN_PROGRESS";
    private static final String COMPLETED = "COMPLETED";

    @Id
    private String taskId;

    @Column(name = "runbook_id")
    private String runbookId;

    private String assigneeId;
    private String name;
    private String description;
    private String status;

    // constructor needed for JPA persistence
    public Task() {
    }

    public Task(String taskId, String name, String description, String assigneeId) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.assigneeId = assigneeId;
        this.status = OPENED;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getRunbookId() {
        return runbookId;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public boolean isInProgress() {
        return this.status.equals(IN_PROGRESS);
    }

    public boolean isClosed() {
        return this.status.equals(COMPLETED);
    }

    //
    // Apply
    //

    void apply(TaskMarkedInProgress e) {
        // TODO Which aggregate should be responsible to apply the task status change?
        this.status = IN_PROGRESS;
    }

    public void apply(TaskCompleted e) {
        this.status = COMPLETED;
    }
}

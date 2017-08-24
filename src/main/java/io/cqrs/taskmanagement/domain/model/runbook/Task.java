package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.Aggregate;

public class Task implements Aggregate {
    public static final String OPENED = "OPEN";
    public static final String IN_PROGRESS = "IN_PROGRESS";
    public static final String CLOSED = "CLOSED";

    private final String taskId;
    private String userId;
    private String status;

    public Task(String taskId, String userId) {
        this.taskId = taskId;
        this.userId = userId;
        this.status = OPENED;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isInProgress() {
        return this.status.equals(IN_PROGRESS);
    }

    //
    // Apply
    //

    void apply(TaskMarkedInProgress e) {
        // TODO Which aggregate should be responsible to apply the task status change?
        this.status = IN_PROGRESS;
    }
}

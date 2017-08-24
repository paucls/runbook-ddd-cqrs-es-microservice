package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.Aggregate;

public class Task implements Aggregate {
    private final String taskId;
    private final String userId;

    public Task(String taskId, String userId) {

        this.taskId = taskId;
        this.userId = userId;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getUserId() {
        return userId;
    }
}

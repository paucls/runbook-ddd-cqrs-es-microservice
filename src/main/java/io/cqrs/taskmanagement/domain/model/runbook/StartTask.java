package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.Command;

public class StartTask implements Command {
    private String taskId;
    private String userId;

    public StartTask(String taskId, String userId) {
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

package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.DomainEvent;

public class TaskMarkedInProgress implements DomainEvent {
    private String taskId;

    public TaskMarkedInProgress(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskMarkedInProgress that = (TaskMarkedInProgress) o;

        return taskId != null ? taskId.equals(that.taskId) : that.taskId == null;
    }

    @Override
    public int hashCode() {
        return taskId != null ? taskId.hashCode() : 0;
    }
}

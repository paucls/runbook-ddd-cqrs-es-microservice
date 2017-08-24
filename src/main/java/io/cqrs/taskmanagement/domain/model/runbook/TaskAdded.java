package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.DomainEvent;

public class TaskAdded implements DomainEvent {
    private final String taskId;
    private final String name;
    private final String description;
    private final String userId;

    public TaskAdded(String taskId, String name, String description, String userId) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.userId = userId;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskAdded taskAdded = (TaskAdded) o;

        if (taskId != null ? !taskId.equals(taskAdded.taskId) : taskAdded.taskId != null) return false;
        if (name != null ? !name.equals(taskAdded.name) : taskAdded.name != null) return false;
        if (description != null ? !description.equals(taskAdded.description) : taskAdded.description != null)
            return false;
        return userId != null ? userId.equals(taskAdded.userId) : taskAdded.userId == null;
    }

    @Override
    public int hashCode() {
        int result = taskId != null ? taskId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}

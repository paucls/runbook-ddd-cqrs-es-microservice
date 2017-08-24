package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.DomainEvent;

public class TaskAdded implements DomainEvent {
    private final String taskId;
    private final String name;

    public TaskAdded(String taskId, String name) {
        this.taskId = taskId;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskAdded taskAdded = (TaskAdded) o;

        if (!taskId.equals(taskAdded.taskId)) return false;
        return name.equals(taskAdded.name);
    }

    @Override
    public int hashCode() {
        int result = taskId.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}

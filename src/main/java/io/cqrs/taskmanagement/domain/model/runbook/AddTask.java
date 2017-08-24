package io.cqrs.taskmanagement.domain.model.runbook;

public class AddTask {
    private final String taskId;
    private final String name;
    private final String description;
    private final String assigneeId;

    public AddTask(String taskId, String name, String description, String assigneeId) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.assigneeId = assigneeId;
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

    public String getAssigneeId() {
        return assigneeId;
    }
}

package io.cqrs.taskmanagement.read.model.runbook;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TaskEntity {
    private String runbookId;
    @Id
    private String taskId;
    private String assigneeId;
    private String name;
    private String description;
    private String status;

    private TaskEntity() {
    }

    public TaskEntity(String runbookId, String taskId, String assigneeId, String name, String description, String status) {
        this.runbookId = runbookId;
        this.taskId = taskId;
        this.assigneeId = assigneeId;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public String getRunbookId() {
        return runbookId;
    }

    public void setRunbookId(String runbookId) {
        this.runbookId = runbookId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

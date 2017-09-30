package io.cqrs.taskmanagement.read.model.runbook;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RunbookEntity {
    @Id
    private String runbookId;
    private String projectId;
    private String name;
    private String ownerId;
    private boolean isCompleted;

    public RunbookEntity() {
    }

    public RunbookEntity(String runbookId, String projectId, String name, String ownerId, boolean isCompleted) {
        this.runbookId = runbookId;
        this.projectId = projectId;
        this.name = name;
        this.ownerId = ownerId;
        this.isCompleted = isCompleted;
    }

    public String getRunbookId() {
        return runbookId;
    }

    public void setRunbookId(String runbookId) {
        this.runbookId = runbookId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}

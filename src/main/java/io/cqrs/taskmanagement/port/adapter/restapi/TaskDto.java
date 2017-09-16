package io.cqrs.taskmanagement.port.adapter.restapi;

import lombok.Data;

@Data
public class TaskDto {
    public String runbookId;
    public String taskId;
    public String assigneeId;
    public String name;
    public String description;
    public String status;

    public TaskDto() {
    }

    public TaskDto(String runbookId, String assigneeId, String name) {
        this.runbookId = runbookId;
        this.assigneeId = assigneeId;
        this.name = name;
    }
}

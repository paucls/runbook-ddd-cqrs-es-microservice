package io.cqrs.taskmanagement.api.runbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    public String runbookId;
    public String taskId;
    public String assigneeId;
    public String name;
    public String description;
    public String status;

    public TaskDto(String runbookId, String assigneeId, String name) {
        this.runbookId = runbookId;
        this.assigneeId = assigneeId;
        this.name = name;
    }
}

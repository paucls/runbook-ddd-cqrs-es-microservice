package io.cqrs.taskmanagement.port.adapter.restapi;

import lombok.Data;

@Data
public class TaskDto {
    public String taskId;
    public String assigneeId;
    public String name;
    public String description;
    public String status;
}

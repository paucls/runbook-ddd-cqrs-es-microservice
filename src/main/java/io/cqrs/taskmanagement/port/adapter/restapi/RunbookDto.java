package io.cqrs.taskmanagement.port.adapter.restapi;

import lombok.Data;

@Data
public class RunbookDto {
    public String runbookId;
    public String projectId;
    public String name;
    public String ownerId;
    public boolean isCompleted;
}

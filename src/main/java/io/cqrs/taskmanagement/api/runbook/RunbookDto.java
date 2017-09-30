package io.cqrs.taskmanagement.api.runbook;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RunbookDto {
    public String runbookId;
    public String projectId;
    public String name;
    public String ownerId;
    public boolean isCompleted;

    public RunbookDto(String projectId, String name, String ownerId) {
        this.projectId = projectId;
        this.name = name;
        this.ownerId = ownerId;
    }
}

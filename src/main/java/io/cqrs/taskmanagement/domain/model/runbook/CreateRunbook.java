package io.cqrs.taskmanagement.domain.model.runbook;

import lombok.Value;

@Value
public class CreateRunbook {
    String projectId;
    String runbookId;
    String name;
    String ownerId;
}

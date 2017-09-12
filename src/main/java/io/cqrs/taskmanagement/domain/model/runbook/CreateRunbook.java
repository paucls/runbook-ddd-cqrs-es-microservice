package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.Command;
import lombok.Value;

@Value
public class CreateRunbook implements Command {
    String projectId;
    String runbookId;
    String name;
    String ownerId;
}

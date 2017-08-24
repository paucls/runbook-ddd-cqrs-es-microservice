package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.Command;
import lombok.Value;

@Value
public class StartTask implements Command {
    String taskId;
    String userId;
}

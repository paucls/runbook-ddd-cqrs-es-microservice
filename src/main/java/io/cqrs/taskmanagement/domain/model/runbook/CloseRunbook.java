package io.cqrs.taskmanagement.domain.model.runbook;

import lombok.Value;

@Value
public class CloseRunbook {
    String runbookId;
    String userId;
}

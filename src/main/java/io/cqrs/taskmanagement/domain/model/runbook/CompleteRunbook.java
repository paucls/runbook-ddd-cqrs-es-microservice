package io.cqrs.taskmanagement.domain.model.runbook;

import lombok.Value;

@Value
public class CompleteRunbook {
    String runbookId;
    String userId;
}

package io.cqrs.taskmanagement.api.runbook;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class RunbookDto {
    public String runbookId;
    @NonNull
    public String projectId;
    @NonNull
    public String name;
    @NonNull
    public String ownerId;
    public boolean isCompleted;
}

package io.cqrs.taskmanagement.read.model.runbook;

import io.cqrs.taskmanagement.api.runbook.RunbookDto;
import io.cqrs.taskmanagement.api.runbook.TaskDto;

import java.util.List;

public interface RunbookQueries {
    List<RunbookDto> uncompletedRunbooks();

    List<TaskDto> tasksForRunbook(String runbookId);
}

package io.cqrs.taskmanagement.read.model.runbook;

import io.cqrs.taskmanagement.api.runbook.TaskDto;

import java.util.List;

public interface TaskQueries {
    List<TaskDto> tasksForRunbook(String runbookId);
}

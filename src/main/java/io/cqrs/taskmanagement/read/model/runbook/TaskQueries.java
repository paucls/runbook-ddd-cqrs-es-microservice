package io.cqrs.taskmanagement.read.model.runbook;

import io.cqrs.taskmanagement.port.adapter.restapi.TaskDto;

import java.util.List;

public interface TaskQueries {
    List<TaskDto> tasksForRunbook(String runbookId);
}

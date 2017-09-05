package io.cqrs.taskmanagement.port.adapter.persistence;

import io.cqrs.taskmanagement.domain.model.runbook.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskRepository {

    public Task getOne(String taskId) {
        return null;
    }

    public List<Task> findTasksByRunbookId(String runbookId) {
        return new ArrayList<>();
    }

}

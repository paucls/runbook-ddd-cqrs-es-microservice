package io.cqrs.taskmanagement.read.model.runbook;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cqrs.taskmanagement.api.runbook.RunbookDto;
import io.cqrs.taskmanagement.api.runbook.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JpaRunbookQueries implements RunbookQueries {

    private RunbookRepository runbookRepository;
    private TaskRepository taskRepository;
    private ObjectMapper objectMapper;

    @Autowired
    public JpaRunbookQueries(RunbookRepository runbookRepository, TaskRepository taskRepository) {
        this.runbookRepository = runbookRepository;
        this.taskRepository = taskRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<RunbookDto> uncompletedRunbooks() {
        List<RunbookEntity> runbooks = runbookRepository.findAll();

        return runbooks.stream()
                .map(entity -> objectMapper.convertValue(entity, RunbookDto.class))
                .collect(Collectors.toList());
    }

    public List<TaskDto> tasksForRunbook(String runbookId) {
        List<TaskEntity> tasks = taskRepository.findTasksByRunbookId(runbookId);

        return tasks.stream()
                .map(entity -> objectMapper.convertValue(entity, TaskDto.class))
                .collect(Collectors.toList());
    }
}

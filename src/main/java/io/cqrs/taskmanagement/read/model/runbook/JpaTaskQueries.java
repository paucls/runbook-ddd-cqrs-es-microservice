package io.cqrs.taskmanagement.read.model.runbook;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cqrs.taskmanagement.port.adapter.restapi.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JpaTaskQueries implements TaskQueries {

    private TaskRepository taskRepository;
    private ObjectMapper objectMapper;

    @Autowired
    public JpaTaskQueries(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.objectMapper = new ObjectMapper();
    }

    public List<TaskDto> tasksForRunbook(String runbookId) {
        List<TaskEntity> tasks = taskRepository.findTasksByRunbookId(runbookId);

        return tasks.stream()
                .map(entity -> objectMapper.convertValue(entity, TaskDto.class))
                .collect(Collectors.toList());
    }
}

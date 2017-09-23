package io.cqrs.taskmanagement.read.model.runbook;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cqrs.taskmanagement.domain.model.runbook.TaskAdded;
import io.cqrs.taskmanagement.port.adapter.persistence.JpaEventStore;
import io.cqrs.taskmanagement.port.adapter.restapi.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

@Service
public class JpaTaskQueries implements TaskQueries, Observer {

    private TaskRepository taskRepository;

    private ObjectMapper objectMapper;

    @Autowired
    public JpaTaskQueries(JpaEventStore jpaEventStore, TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.objectMapper = new ObjectMapper();

        // Subscribe Read Model to new Events
        jpaEventStore.addObserver(this);
    }

    public List<TaskDto> tasksForRunbook(String runbookId) {
        List<TaskEntity> tasks = taskRepository.findTasksByRunbookId(runbookId);

        return tasks.stream()
                .map(entity -> objectMapper.convertValue(entity, TaskDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void update(Observable observable, Object object) {
        // Handles only Events this Read Model cares about
        if (object instanceof TaskAdded) {
            handleTaskAdded((TaskAdded) object);
        }
    }

    private void handleTaskAdded(TaskAdded event) {
        TaskEntity task = new TaskEntity(
                event.getRunbookId(),
                event.getTaskId(),
                event.getAssigneeId(),
                event.getName(),
                event.getDescription(),
                null
        );

        taskRepository.save(task);
    }
}

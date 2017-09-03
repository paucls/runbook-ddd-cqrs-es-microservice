package io.cqrs.taskmanagement.application;

import io.cqrs.taskmanagement.domain.model.DomainEventPublisher;
import io.cqrs.taskmanagement.domain.model.runbook.AddTask;
import io.cqrs.taskmanagement.domain.model.runbook.CreateRunbook;
import io.cqrs.taskmanagement.domain.model.runbook.Runbook;
import io.cqrs.taskmanagement.port.adapter.persistence.repository.RunbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RunbookApplicationService {

    @Autowired
    private DomainEventPublisher eventPublisher;

    @Autowired
    private RunbookRepository runbookRepository;

    public void createRunbook(CreateRunbook createRunbook) {
        // Dispatch Create Runbook
        Runbook runbook = new Runbook(createRunbook, eventPublisher);

        // Persist
        runbookRepository.save(runbook);
    }

    public void addTask(AddTask addTask) {
        // Retrieve Aggregate
        Runbook runbook = runbookRepository.getOne(addTask.getRunbookId());

        // Dispatch Add Task
        runbook.handle(addTask);

        // Persist
        runbookRepository.save(runbook);
    }
}

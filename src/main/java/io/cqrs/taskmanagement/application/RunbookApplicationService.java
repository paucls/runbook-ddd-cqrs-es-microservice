package io.cqrs.taskmanagement.application;

import io.cqrs.taskmanagement.domain.model.DomainEventPublisher;
import io.cqrs.taskmanagement.domain.model.runbook.AddTask;
import io.cqrs.taskmanagement.domain.model.runbook.CompleteRunbook;
import io.cqrs.taskmanagement.domain.model.runbook.CompleteTask;
import io.cqrs.taskmanagement.domain.model.runbook.CreateRunbook;
import io.cqrs.taskmanagement.domain.model.runbook.Runbook;
import io.cqrs.taskmanagement.domain.model.runbook.StartTask;
import io.cqrs.taskmanagement.port.adapter.persistence.RunbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RunbookApplicationService {

    private DomainEventPublisher eventPublisher;
    private RunbookRepository runbookRepository;
    private EventStore eventStore;

    @Autowired
    public RunbookApplicationService(DomainEventPublisher eventPublisher,
                                     RunbookRepository runbookRepository,
                                     EventStore eventStore) {
        this.eventPublisher = eventPublisher;
        this.runbookRepository = runbookRepository;
        this.eventStore = eventStore;
    }

    public void createRunbook(CreateRunbook createRunbook) {
        // Dispatch Create Runbook
        Runbook runbook = new Runbook(createRunbook, eventPublisher);

        // Persist
        runbookRepository.save(runbook);
        eventStore.append(runbook.getUncommitedEvents());
    }

    public void addTask(AddTask c) {
        // Retrieve Aggregate
        Runbook runbook = runbookRepository.getOne(c.getRunbookId());

        // Dispatch Add Task
        runbook.handle(c);

        // Persist
        runbookRepository.save(runbook);
        eventStore.append(runbook.getUncommitedEvents());
    }

    public void startTask(StartTask c) {
        // Retrieve Aggregate
        Runbook runbook = runbookRepository.getOne(c.getRunbookId());

        // Dispatch Command
        runbook.handle(c);

        // Persist
        runbookRepository.save(runbook);
        eventStore.append(runbook.getUncommitedEvents());
    }

    public void completeTask(CompleteTask c) {
        // Retrieve Aggregate
        Runbook runbook = runbookRepository.getOne(c.getRunbookId());

        // Dispatch Command
        runbook.handle(c);

        // Persist
        runbookRepository.save(runbook);
        eventStore.append(runbook.getUncommitedEvents());
    }

    public void completeRunbook(CompleteRunbook c) {
        // Retrieve Aggregate
        Runbook runbook = runbookRepository.getOne(c.getRunbookId());

        // Dispatch Command
        runbook.handle(c);

        // Persist
        runbookRepository.save(runbook);
        eventStore.append(runbook.getUncommitedEvents());
    }
}

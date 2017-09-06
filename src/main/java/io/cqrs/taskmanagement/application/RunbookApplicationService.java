package io.cqrs.taskmanagement.application;

import io.cqrs.taskmanagement.domain.model.runbook.AddTask;
import io.cqrs.taskmanagement.domain.model.runbook.CompleteRunbook;
import io.cqrs.taskmanagement.domain.model.runbook.CompleteTask;
import io.cqrs.taskmanagement.domain.model.runbook.CreateRunbook;
import io.cqrs.taskmanagement.domain.model.runbook.Runbook;
import io.cqrs.taskmanagement.domain.model.runbook.StartTask;
import io.cqrs.taskmanagement.event.sourcing.EventStore;
import io.cqrs.taskmanagement.event.sourcing.EventStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RunbookApplicationService {

    private EventStore eventStore;

    @Autowired
    public RunbookApplicationService(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public void createRunbook(CreateRunbook createRunbook) {
        // Dispatch Create Runbook
        Runbook runbook = new Runbook(createRunbook);

        // Persist
        eventStore.appendToStream(runbook.getRunbookId(), runbook.getUncommitedEvents(), 0); // Another option is to introduce a EventSourcedRepository and handle this inside the save method
    }

    public void addTask(AddTask c) {
        // Retrieve Aggregate
        EventStream eventStream = eventStore.loadEventStream(c.getRunbookId());
        Runbook runbook = new Runbook(eventStream);

        // Dispatch Add Task
        runbook.handle(c);

        // Persist
        eventStore.appendToStream(runbook.getRunbookId(), runbook.getUncommitedEvents(), eventStream.getVersion());
    }

    public void startTask(StartTask c) {
        // Retrieve Aggregate
        EventStream eventStream = eventStore.loadEventStream(c.getRunbookId());
        Runbook runbook = new Runbook(eventStream);

        // Dispatch Command
        runbook.handle(c);

        // Persist
        eventStore.appendToStream(runbook.getRunbookId(), runbook.getUncommitedEvents(), eventStream.getVersion());
    }

    public void completeTask(CompleteTask c) {
        // Retrieve Aggregate
        EventStream eventStream = eventStore.loadEventStream(c.getRunbookId());
        Runbook runbook = new Runbook(eventStream);

        // Dispatch Command
        runbook.handle(c);

        // Persist
        eventStore.appendToStream(runbook.getRunbookId(), runbook.getUncommitedEvents(), eventStream.getVersion());
    }

    public void completeRunbook(CompleteRunbook c) {
        // Retrieve Aggregate
        EventStream eventStream = eventStore.loadEventStream(c.getRunbookId());
        Runbook runbook = new Runbook(eventStream);

        // Dispatch Command
        runbook.handle(c);

        // Persist
        eventStore.appendToStream(runbook.getRunbookId(), runbook.getUncommitedEvents(), eventStream.getVersion());
    }
}

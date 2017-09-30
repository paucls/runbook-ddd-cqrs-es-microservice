package io.cqrs.taskmanagement.read.model.runbook;

import io.cqrs.taskmanagement.domain.model.runbook.RunbookCreated;
import io.cqrs.taskmanagement.persistence.JpaEventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Observable;
import java.util.Observer;

/**
 * Read Model Projection
 */
@Component
public class UncompletedRunbooksProjection implements Observer {

    private RunbookRepository runbookRepository;

    @Autowired
    public UncompletedRunbooksProjection(JpaEventStore jpaEventStore, RunbookRepository runbookRepository) {
        this.runbookRepository = runbookRepository;

        // Subscribe Read Model to new Events
        jpaEventStore.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object object) {
        // Handles only Events this Projection cares about
        if (object instanceof RunbookCreated) {
            handleRunbookCreated((RunbookCreated) object);
        }
    }

    private void handleRunbookCreated(RunbookCreated event) {
        // For now persisting read models as Entities, but the read model DTOs could be persisted directly to file system, etc.

        RunbookEntity runbook = new RunbookEntity(
                event.getRunbookId(),
                event.getProjectId(),
                event.getName(),
                event.getOwnerId(),
                false
        );

        runbookRepository.save(runbook);
    }

}

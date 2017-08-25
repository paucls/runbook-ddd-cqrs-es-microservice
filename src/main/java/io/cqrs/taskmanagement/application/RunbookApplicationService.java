package io.cqrs.taskmanagement.application;

import io.cqrs.taskmanagement.domain.model.DomainEventPublisher;
import io.cqrs.taskmanagement.domain.model.runbook.CreateRunbook;
import io.cqrs.taskmanagement.domain.model.runbook.Runbook;
import io.cqrs.taskmanagement.port.adapter.restapi.RunbookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RunbookApplicationService {

    @Autowired
    private DomainEventPublisher eventPublisher;

    public RunbookDto createRunbook(String projectId, String name, String userId) {
        String newId = UUID.randomUUID().toString();
        CreateRunbook createRunbook = new CreateRunbook(projectId, newId, name, userId);

        Runbook runbook = new Runbook(createRunbook, eventPublisher);

        // Use a marshaller
        RunbookDto runbookDto = new RunbookDto();
        runbookDto.setProjectId(runbook.projectId());
        runbookDto.setRunbookId(runbook.runbookId());
        runbookDto.setName(runbook.name());
        runbookDto.setOwnerId(runbook.ownerId());
        runbookDto.setCompleted(runbook.isCompleted());

        return runbookDto;
    }
}

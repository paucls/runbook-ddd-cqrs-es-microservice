package io.cqrs.taskmanagement.application;

import io.cqrs.taskmanagement.domain.model.DomainEventPublisher;
import io.cqrs.taskmanagement.domain.model.runbook.CreateRunbook;
import io.cqrs.taskmanagement.domain.model.runbook.Runbook;
import io.cqrs.taskmanagement.port.adapter.persistence.repository.RunbookRepository;
import io.cqrs.taskmanagement.port.adapter.restapi.RunbookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RunbookApplicationService {

    @Autowired
    private DomainEventPublisher eventPublisher;

    @Autowired
    private RunbookRepository runbookRepository; //TODO use a interface instead of impl class

    public RunbookDto createRunbook(String projectId, String name, String userId) {
        String newId = UUID.randomUUID().toString();
        CreateRunbook createRunbook = new CreateRunbook(projectId, newId, name, userId);

        // Dispatch Create Runbook
        Runbook runbook = new Runbook(createRunbook, eventPublisher);

        // Persist
        runbookRepository.save(runbook);

        // Marshall it back to a DTO
        RunbookDto runbookDto = new RunbookDto();
        runbookDto.setProjectId(runbook.getProjectId()); // TODO use a marshaller for this
        runbookDto.setRunbookId(runbook.getRunbookId());
        runbookDto.setName(runbook.getName());
        runbookDto.setOwnerId(runbook.getOwnerId());
        runbookDto.setCompleted(runbook.isCompleted());

        return runbookDto;
    }
}

package io.cqrs.taskmanagement.port.adapter.restapi;

import io.cqrs.taskmanagement.application.RunbookApplicationService;
import io.cqrs.taskmanagement.domain.model.runbook.CreateRunbook;
import io.cqrs.taskmanagement.domain.model.runbook.Runbook;
import io.cqrs.taskmanagement.port.adapter.persistence.repository.RunbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class RunbooksApiController implements RunbooksApi {

    @Autowired
    private RunbookApplicationService runbookApplicationService;

    @Autowired
    private RunbookRepository runbookRepository;

    @Override
    public ResponseEntity<RunbookDto> createRunbook(@RequestBody RunbookDto runbook) {
        RunbookDto createdRunbook = runbookApplicationService.createRunbook(
                runbook.getProjectId(),
                runbook.getName(),
                "user-id"
        );

        return new ResponseEntity<>(createdRunbook, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Runbook>> getAllRunbooks() {
        return new ResponseEntity<>(runbookRepository.findAll(), HttpStatus.OK);
    }
}

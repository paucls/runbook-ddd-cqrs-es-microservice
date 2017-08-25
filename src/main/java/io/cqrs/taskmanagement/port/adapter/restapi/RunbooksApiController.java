package io.cqrs.taskmanagement.port.adapter.restapi;

import io.cqrs.taskmanagement.application.RunbookApplicationService;
import io.cqrs.taskmanagement.domain.model.runbook.CreateRunbook;
import io.cqrs.taskmanagement.domain.model.runbook.Runbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RunbooksApiController implements RunbooksApi {

    @Autowired
    private RunbookApplicationService runbookApplicationService;

    @Override
    public ResponseEntity<RunbookDto> createRunbook(@RequestBody RunbookDto runbook) {
        RunbookDto createdRunbook = runbookApplicationService.createRunbook(
                runbook.getProjectId(),
                runbook.getName(),
                "user-id"
        );

        return new ResponseEntity<>(createdRunbook, HttpStatus.OK);
    }
}

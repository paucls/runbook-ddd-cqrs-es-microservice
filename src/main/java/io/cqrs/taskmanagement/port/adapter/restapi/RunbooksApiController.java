package io.cqrs.taskmanagement.port.adapter.restapi;

import io.cqrs.taskmanagement.domain.model.runbook.Runbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RunbooksApiController implements RunbooksApi {
    @Override
    public ResponseEntity<Runbook> createRunbook(@RequestBody Runbook runbook) {

        System.out.println("*****************");
        System.out.println(runbook.projectId());
        System.out.println("*****************");

        Runbook createdRunbook = null;
        return new ResponseEntity<>(createdRunbook, HttpStatus.OK);
    }
}

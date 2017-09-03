package io.cqrs.taskmanagement.port.adapter.restapi;

import io.cqrs.taskmanagement.domain.model.runbook.Runbook;
import io.cqrs.taskmanagement.domain.model.runbook.Task;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Api(value = "runbooks", description = "Runbooks API")
public interface RunbooksApi {
    // TODO
}

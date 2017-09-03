package io.cqrs.taskmanagement.port.adapter.restapi;

import io.cqrs.taskmanagement.application.RunbookApplicationService;
import io.cqrs.taskmanagement.domain.model.runbook.AddTask;
import io.cqrs.taskmanagement.domain.model.runbook.CreateRunbook;
import io.cqrs.taskmanagement.domain.model.runbook.Runbook;
import io.cqrs.taskmanagement.domain.model.runbook.Task;
import io.cqrs.taskmanagement.port.adapter.persistence.repository.RunbookRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
public class RunbooksApiController implements RunbooksApi {

    @Autowired
    private RunbookApplicationService runbookApplicationService;

    @Autowired
    private RunbookRepository runbookRepository;

    /**
     * Commands
     */

    @ApiOperation(value = "", notes = "Creates a new Runbook", response = RunbookDto.class, tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Runbook created successfully", response = Runbook.class)})
    @RequestMapping(value = "/runbooks", method = RequestMethod.POST)
    public ResponseEntity<Runbook> createRunbook(@RequestBody RunbookDto runbookDto) {
        String runbookId = UUID.randomUUID().toString();

        runbookApplicationService.createRunbook(new CreateRunbook(
                runbookDto.getProjectId(),
                runbookId,
                runbookDto.getName(),
                "user-id"
        ));

        Runbook createdRunbook = runbookRepository.getOne(runbookId);

        return new ResponseEntity<>(createdRunbook, HttpStatus.OK);
    }

    @ApiOperation(value = "", notes = "Add a new Task to provided Runbook", response = Task.class, tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Task added successfully", response = Task.class)})
    @RequestMapping(value = "/runbooks/{runbook-id}/actions/addtask", method = RequestMethod.POST)
    public ResponseEntity<Task> addTask(@RequestParam String runbookId, @RequestBody AddTask c) {
        String taskId = UUID.randomUUID().toString();

        runbookApplicationService.addTask(new AddTask(
                runbookId,
                taskId,
                c.getName(),
                c.getDescription(),
                "user-id"));

        Task createdTask = null; //TODO

        return new ResponseEntity<Task>(createdTask, HttpStatus.OK);
    }

    /**
     * Queries
     */

    @ApiOperation(value = "", notes = "Gets the list of `Runbook` objects for the current user. ", response = Runbook.class, responseContainer = "List", tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "An array of `Runbook` objects", response = Runbook.class)})
    @RequestMapping(value = "/runbooks", method = RequestMethod.GET)
    public ResponseEntity<List<Runbook>> getAllRunbooks() {
        return new ResponseEntity<>(runbookRepository.findAll(), HttpStatus.OK);
    }
}

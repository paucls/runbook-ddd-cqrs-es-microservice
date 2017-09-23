package io.cqrs.taskmanagement.port.adapter.restapi;

import io.cqrs.taskmanagement.application.RunbookApplicationService;
import io.cqrs.taskmanagement.domain.model.runbook.AddTask;
import io.cqrs.taskmanagement.domain.model.runbook.CompleteRunbook;
import io.cqrs.taskmanagement.domain.model.runbook.CompleteTask;
import io.cqrs.taskmanagement.domain.model.runbook.CreateRunbook;
import io.cqrs.taskmanagement.domain.model.runbook.Runbook;
import io.cqrs.taskmanagement.domain.model.runbook.StartTask;
import io.cqrs.taskmanagement.port.adapter.persistence.RunbookRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class RunbooksApiController implements RunbooksApi {

    private static final String SAMPLE_USER_ID = "user-id";

    @Autowired
    private RunbookApplicationService runbookApplicationService;

    @Autowired
    private RunbookRepository runbookRepository;

    /**
     * Commands
     */

    @ApiOperation(value = "", notes = "Creates a new Runbook", response = Runbook.class, tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Runbook created successfully", response = Runbook.class)})
    @RequestMapping(value = "/runbooks", method = RequestMethod.POST)
    public ResponseEntity<Runbook> createRunbook(@RequestBody RunbookDto runbookDto) {
        String runbookId = UUID.randomUUID().toString();

        runbookApplicationService.createRunbook(new CreateRunbook(
                runbookDto.getProjectId(),
                runbookId,
                runbookDto.getName(),
                SAMPLE_USER_ID
        ));

        return new ResponseEntity<>(runbookRepository.getOne(runbookId), HttpStatus.OK);
    }

    @ApiOperation(value = "", notes = "Complete Runbook", response = Runbook.class, tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Runbook completed successfully", response = Runbook.class)})
    @RequestMapping(value = "/runbooks/{runbookId}/actions/complete", method = RequestMethod.POST)
    public ResponseEntity<Runbook> completeRunbook(@PathVariable String runbookId) {
        runbookApplicationService.completeRunbook(new CompleteRunbook(
                runbookId,
                SAMPLE_USER_ID));

        return new ResponseEntity<>(runbookRepository.getOne(runbookId), HttpStatus.OK);
    }

    @ApiOperation(value = "", notes = "Add a new Task to provided Runbook", response = TaskDto.class, tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Task added successfully", response = TaskDto.class)})
    @RequestMapping(value = "/runbooks/{runbookId}/tasks", method = RequestMethod.POST)
    public ResponseEntity<TaskDto> addTask(@PathVariable String runbookId, @RequestBody TaskDto taskDto) {
        String taskId = UUID.randomUUID().toString();

        runbookApplicationService.addTask(new AddTask(
                runbookId,
                taskId,
                taskDto.getName(),
                taskDto.getDescription(),
                taskDto.getAssigneeId()));

        // TODO: Retrieve Task from query model
        TaskDto createdTaskDto = new TaskDto();
        return new ResponseEntity<>(createdTaskDto, HttpStatus.OK);
    }

    @ApiOperation(value = "", notes = "Start Task", response = TaskDto.class, tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Task started successfully", response = TaskDto.class)})
    @RequestMapping(value = "/runbooks/{runbookId}/tasks/{taskId}/actions/start", method = RequestMethod.POST)
    public ResponseEntity<TaskDto> startTask(@PathVariable String runbookId, @PathVariable String taskId) {
        runbookApplicationService.startTask(new StartTask(
                runbookId,
                taskId,
                SAMPLE_USER_ID));

        // TODO: Retrieve Task from query model
        return new ResponseEntity<>(new TaskDto(), HttpStatus.OK);
    }

    @ApiOperation(value = "", notes = "Complete Task", response = TaskDto.class, tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Task completed successfully", response = TaskDto.class)})
    @RequestMapping(value = "/runbooks/{runbookId}/tasks/{taskId}/actions/complete", method = RequestMethod.POST)
    public ResponseEntity<TaskDto> completeTask(@PathVariable String runbookId, @PathVariable String taskId) {
        runbookApplicationService.completeTask(new CompleteTask(
                runbookId,
                taskId,
                SAMPLE_USER_ID));

        // TODO: Retrieve Task from query model
        return new ResponseEntity<>(new TaskDto(), HttpStatus.OK);
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

    @ApiOperation(value = "", notes = "Gets the list of Tasks for a Runbook. ", response = TaskDto.class, responseContainer = "List", tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "An array of `Task` objects", response = TaskDto.class)})
    @RequestMapping(value = "/runbooks/{runbookId}/tasks", method = RequestMethod.GET)
    public ResponseEntity<List<TaskDto>> getTasksForRunbook(@PathVariable String runbookId) {
        // TODO: Retrieve Tasks from query model
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }
}

package io.cqrs.taskmanagement.api.runbook;

import io.cqrs.taskmanagement.application.RunbookApplicationService;
import io.cqrs.taskmanagement.domain.model.runbook.AddTask;
import io.cqrs.taskmanagement.domain.model.runbook.CompleteRunbook;
import io.cqrs.taskmanagement.domain.model.runbook.CompleteTask;
import io.cqrs.taskmanagement.domain.model.runbook.CreateRunbook;
import io.cqrs.taskmanagement.domain.model.runbook.StartTask;
import io.cqrs.taskmanagement.read.model.runbook.RunbookQueries;
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
public class RunbooksController {

    private static final String SAMPLE_USER_ID = "user-id";

    @Autowired
    private RunbookApplicationService runbookApplicationService;

    @Autowired
    private RunbookQueries runbookQueries;

    /**
     * Commands
     */

    @ApiOperation(value = "Create new Runbook", response = RunbookDto.class)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Runbook created successfully", response = RunbookDto.class)})
    @RequestMapping(value = "/runbooks", method = RequestMethod.POST)
    public ResponseEntity<RunbookDto> createRunbook(@RequestBody RunbookDto runbookDto) {
        String runbookId = UUID.randomUUID().toString();

        runbookApplicationService.createRunbook(new CreateRunbook(
                runbookDto.getProjectId(),
                runbookId,
                runbookDto.getName(),
                runbookDto.getOwnerId()
        ));

        // TODO: Retrieve from query model
        runbookDto.setRunbookId(runbookId);
        return new ResponseEntity<>(runbookDto, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Complete Runbook", response = RunbookDto.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Runbook completed successfully", response = RunbookDto.class)})
    @RequestMapping(value = "/runbooks/{runbookId}/actions/complete", method = RequestMethod.POST)
    public ResponseEntity<RunbookDto> completeRunbook(@PathVariable String runbookId) {
        runbookApplicationService.completeRunbook(new CompleteRunbook(
                runbookId,
                SAMPLE_USER_ID));

        // TODO: Retrieve from query model
        return new ResponseEntity<>(new RunbookDto(), HttpStatus.OK);
    }

    @ApiOperation(value = "Add new Task to Runbook", response = TaskDto.class)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Task added successfully", response = TaskDto.class)})
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
        taskDto.setTaskId(taskId);
        return new ResponseEntity<>(taskDto, HttpStatus.CREATED);
    }

    @ApiOperation("Start Task")
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

    @ApiOperation("Complete Task")
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

    @ApiOperation(value = "Get Uncompleted Runbooks", response = RunbookDto.class, responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A list of `RunbookDto` objects", response = RunbookDto.class)})
    @RequestMapping(value = "/runbooks", method = RequestMethod.GET)
    public ResponseEntity<List<RunbookDto>> getUncompletedRunbooks() {
        return new ResponseEntity<>(runbookQueries.uncompletedRunbooks(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Tasks for Runbook", response = TaskDto.class, responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A list of `TaskDto` objects", response = TaskDto.class)})
    @RequestMapping(value = "/runbooks/{runbookId}/tasks", method = RequestMethod.GET)
    public ResponseEntity<List<TaskDto>> getTasksForRunbook(@PathVariable String runbookId) {
        return new ResponseEntity<>(runbookQueries.tasksForRunbook(runbookId), HttpStatus.OK);
    }
}

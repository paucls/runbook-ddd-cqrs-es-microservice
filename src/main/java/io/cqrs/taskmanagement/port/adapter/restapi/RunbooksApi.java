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

@Api(value = "tasks", description = "the tasks API")
public interface RunbooksApi {

    @ApiOperation(value = "", notes = "Creates a new `Runbook` object for the current user.", response = RunbookDto.class, tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Runbook created successfully", response = RunbookDto.class)})
    @RequestMapping(value = "/runbooks", method = RequestMethod.POST)
    ResponseEntity<RunbookDto> createRunbook(
            @ApiParam(value = "Runbook to create", required = true) @RequestBody RunbookDto runbook
    );

//    @ApiOperation(value = "", notes = "Gets the list of `Runbook` objects for the current user. ", response = RunbookDto.class, responseContainer = "List", tags = {})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "An array of `Runbook` objects", response = RunbookDto.class)})
//    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
//    ResponseEntity<List<RunbookDto>> getAllRunbooks();

    @ApiOperation(value = "", notes = "Gets the list of `Runbook` objects for the current user. ", response = Runbook.class, responseContainer = "List", tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "An array of `Runbook` objects", response = Runbook.class)})
    @RequestMapping(value = "/runbooks", method = RequestMethod.GET)
    ResponseEntity<List<Runbook>> getAllRunbooks();

//    @ApiOperation(value = "", notes = "Gets the list of `Tasks` for a given `Runbook.", response = Task.class, responseContainer = "List", tags = {})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "An array of `Task` objects", response = Task.class)})
//    @RequestMapping(value = "/runbooks/{projectId}/tasks", method = RequestMethod.GET)
//    ResponseEntity<List<Task>> getTasksForRunbook(
//            @ApiParam(value = "Project identifier", required = true) @PathVariable("projectId") String projectId
//    );

}

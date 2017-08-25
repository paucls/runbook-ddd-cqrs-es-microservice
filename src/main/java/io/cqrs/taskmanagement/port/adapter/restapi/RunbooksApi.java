package io.cqrs.taskmanagement.port.adapter.restapi;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api(value = "tasks", description = "the tasks API")
public interface RunbooksApi {

    @ApiOperation(value = "", notes = "Creates a new `Runbook` object for the current user.", response = RunbookDto.class, tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Runbook created successfully", response = RunbookDto.class)})
    @RequestMapping(value = "/runbooks", method = RequestMethod.POST)
    ResponseEntity<RunbookDto> createRunbook(
            @ApiParam(value = "Runbook to create", required = true) @RequestBody RunbookDto runbook
    );

}

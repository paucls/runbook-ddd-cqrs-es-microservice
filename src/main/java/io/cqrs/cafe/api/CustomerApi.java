package io.cqrs.cafe.api;

import io.cqrs.cafe.domain.model.Customer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Api(value = "customers", description = "the customers API")
public interface CustomerApi {

    @ApiOperation(value = "", notes = "Gets the list of all `Customer` resources. ", response = Customer.class, responseContainer = "List", tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "An array of `Customer` objects", response = Customer.class)})
    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    ResponseEntity<List<Customer>> getCustomers();

}

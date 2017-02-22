package io.ordermanagement.api;

import io.ordermanagement.domain.model.Customer;
import io.ordermanagement.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CustomerApiController implements CustomerApi {

    @Autowired
    private CustomerRepository customerRepository;

    public ResponseEntity<List<Customer>> getCustomers() {
        return new ResponseEntity<>(customerRepository.findAll(), HttpStatus.OK);
    }

}

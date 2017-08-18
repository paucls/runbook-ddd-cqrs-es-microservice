package io.cqrs.cafe.api;

import io.cqrs.cafe.domain.model.Customer;
import io.cqrs.cafe.repository.CustomerRepository;
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

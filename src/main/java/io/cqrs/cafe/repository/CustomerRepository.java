package io.cqrs.cafe.repository;

import io.cqrs.cafe.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}

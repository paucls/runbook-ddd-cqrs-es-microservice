package io.cqrs.taskmanagement.port.adapter.persistence.repository;

import io.cqrs.taskmanagement.domain.model.runbook.Runbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunbookRepository extends JpaRepository<Runbook, String> {
}

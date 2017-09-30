package io.cqrs.taskmanagement.read.model.runbook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunbookRepository extends JpaRepository<RunbookEntity, String> {
}

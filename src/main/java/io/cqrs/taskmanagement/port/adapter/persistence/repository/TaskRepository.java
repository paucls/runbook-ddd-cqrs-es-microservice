package io.cqrs.taskmanagement.port.adapter.persistence.repository;

import io.cqrs.taskmanagement.domain.model.runbook.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    public List<Task> findTasksByRunbookId(String runbookId);

}

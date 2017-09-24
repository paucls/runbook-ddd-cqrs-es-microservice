package io.cqrs.taskmanagement.read.model.runbook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, String> {
    public List<TaskEntity> findTasksByRunbookId(String runbookId);
}

package io.cqrs.taskmanagement.persistence.runbook;

import io.cqrs.taskmanagement.domain.model.runbook.Runbook;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RunbookRepository {

    public Runbook getOne(String runbookId) {
        return null;
    }

    public List<Runbook> findAll() {
        return new ArrayList<>();
    }
}

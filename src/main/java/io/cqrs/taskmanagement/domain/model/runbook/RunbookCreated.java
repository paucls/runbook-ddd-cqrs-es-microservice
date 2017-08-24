package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.DomainEvent;

public class RunbookCreated implements DomainEvent {

    private final String runbookId;
    private final String name;

    public RunbookCreated(String runbookId, String name) {
        this.runbookId = runbookId;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RunbookCreated that = (RunbookCreated) o;

        if (!runbookId.equals(that.runbookId)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = runbookId.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}

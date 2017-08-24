package io.cqrs.taskmanagement.domain.model.runbook;

import io.cqrs.taskmanagement.domain.model.DomainEventPublisher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

public class RunbookTest {

    private DomainEventPublisher eventPublisherMock;

    @Before
    public void setup() {
        eventPublisherMock = Mockito.mock(DomainEventPublisher.class);
    }

    @Test
    public void can_create_runbook() {
        new Runbook("project-id", "runbook-id", "runbook-name", eventPublisherMock);

        verify(eventPublisherMock).publish(new RunbookCreated("runbook-id", "runbook-name"));
    }

}

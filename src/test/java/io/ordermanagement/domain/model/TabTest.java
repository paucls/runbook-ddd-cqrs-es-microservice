package io.ordermanagement.domain.model;

import io.ordermanagement.application.OpenTab;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TabTest {

    private Tab aggregate;
    private DomainEventPublisher eventPublisherMock;

    private String testId;
    private int testTable;
    private String testWaiter;

    @Before
    public void setup() {
        eventPublisherMock = mock(DomainEventPublisher.class);
        aggregate = new Tab(eventPublisherMock);
        testId = UUID.randomUUID().toString();
        testTable = 3;
        testWaiter = "John";
    }

    @Test
    public void can_open_a_new_tab() {
        OpenTab openTab = new OpenTab(testId, testTable, testWaiter);

        aggregate.handle(openTab);

        verify(eventPublisherMock)
                .publish(new TabOpened(testId, testTable, testWaiter));
    }

}

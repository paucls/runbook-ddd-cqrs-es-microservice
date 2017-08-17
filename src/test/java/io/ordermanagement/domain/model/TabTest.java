package io.ordermanagement.domain.model;

import io.ordermanagement.application.OpenTab;
import io.ordermanagement.application.PlaceOrder;
import org.hibernate.criterion.Order;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TabTest {

    private static final String testTabId = UUID.randomUUID().toString();
    private static final int testTable = 3;
    private static final String testWaiter = "John";

    private static final OrderItem testDrink1 = new OrderItem();
    private static final OrderItem testDrink2 = new OrderItem();
    private static final OrderItem testFood1 = new OrderItem();
    private static final OrderItem testFood2 = new OrderItem();

    private Tab aggregate;
    private DomainEventPublisher eventPublisherMock;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        eventPublisherMock = mock(DomainEventPublisher.class);
        aggregate = new Tab(eventPublisherMock);
    }

    @Test
    public void can_open_a_new_tab() {
        OpenTab openTab = new OpenTab(testTabId, testTable, testWaiter);

        aggregate.handle(openTab);

        verify(eventPublisherMock)
                .publish(new TabOpened(testTabId, testTable, testWaiter));
    }

    @Test
    public void can_not_order_with_unopened_tab() {
        List<OrderItem> items = Arrays.asList(testDrink1, testDrink2);
        PlaceOrder placeOrder = new PlaceOrder(testTabId, items);

        exception.expect(TabNotOpen.class);

        aggregate.handle(placeOrder);
    }

}

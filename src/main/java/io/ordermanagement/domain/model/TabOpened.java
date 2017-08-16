package io.ordermanagement.domain.model;

import java.util.Date;

public class TabOpened implements DomainEvent {

    private Date occurredOn;
    private String id;
    private int tableNumber;
    private String waiter;

    public TabOpened(String id, int tableNumber, String waiter) {
        this.occurredOn = new Date();
        this.id = id;
        this.tableNumber = tableNumber;
        this.waiter = waiter;
    }

    @Override
    public Date occurredOn() {
        return this.occurredOn;
    }

    public String id() {
        return id;
    }

    public int tableNumber() {
        return tableNumber;
    }

    public String waiter() {
        return waiter;
    }

}

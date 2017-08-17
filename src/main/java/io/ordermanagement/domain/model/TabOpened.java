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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TabOpened tabOpened = (TabOpened) o;

        if (tableNumber != tabOpened.tableNumber) return false;
        if (id != null ? !id.equals(tabOpened.id) : tabOpened.id != null) return false;
        return waiter != null ? waiter.equals(tabOpened.waiter) : tabOpened.waiter == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + tableNumber;
        result = 31 * result + (waiter != null ? waiter.hashCode() : 0);
        return result;
    }
}

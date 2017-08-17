package io.ordermanagement.domain.model;

import java.util.Date;
import java.util.List;

public class FoodOrdered implements DomainEvent {

    private Date occurredOn;
    private String tabId;
    private List<OrderItem> items;

    FoodOrdered(String tabId, List<OrderItem> items) {
        this.occurredOn = new Date();
        this.tabId = tabId;
        this.items = items;
    }

    @Override
    public Date occurredOn() {
        return this.occurredOn;
    }

    public String getTabId() {
        return tabId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodOrdered that = (FoodOrdered) o;

        if (!tabId.equals(that.tabId)) return false;
        return items.equals(that.items);
    }

    @Override
    public int hashCode() {
        int result = tabId.hashCode();
        result = 31 * result + items.hashCode();
        return result;
    }
}

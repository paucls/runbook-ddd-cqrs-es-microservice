package io.cqrs.cafe.application;

import io.cqrs.cafe.domain.model.tab.OrderItem;

import java.util.List;

public class PlaceOrder {
    private String id;
    private List<OrderItem> items;

    public PlaceOrder(String id, List<OrderItem> items) {
        this.id = id;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}

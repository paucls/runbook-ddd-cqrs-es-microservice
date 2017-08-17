package io.ordermanagement.application;

import io.ordermanagement.domain.model.OrderItem;

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
}

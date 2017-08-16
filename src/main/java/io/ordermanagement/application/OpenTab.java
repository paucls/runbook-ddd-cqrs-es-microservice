package io.ordermanagement.application;

public class OpenTab {
    private String id;
    private int tableNumber;
    private String waiter;

    public OpenTab(String id, int tableNumber, String waiter) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.waiter = waiter;
    }

    public String getId() {
        return id;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public String getWaiter() {
        return waiter;
    }
}

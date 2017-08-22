package io.cqrs.cafe.domain.model.tab;

import io.cqrs.cafe.domain.model.DomainEvent;

import java.util.Date;

public class TabClosed implements DomainEvent {

    private Date occurredOn;
    private String tabId;
    private Double amountPaid;
    private Double orderValue;
    private Double tipValue;

    TabClosed(String tabId, Double amountPaid, Double orderValue, Double tipValue) {
        this.occurredOn = new Date();
        this.tabId = tabId;
        this.amountPaid = amountPaid;
        this.orderValue = orderValue;
        this.tipValue = tipValue;
    }

    @Override
    public Date occurredOn() {
        return this.occurredOn;
    }

    public String tabId() {
        return tabId;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public Double getOrderValue() {
        return orderValue;
    }

    public Double getTipValue() {
        return tipValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TabClosed tabClosed = (TabClosed) o;

        if (!tabId.equals(tabClosed.tabId)) return false;
        if (!amountPaid.equals(tabClosed.amountPaid)) return false;
        if (!orderValue.equals(tabClosed.orderValue)) return false;
        return tipValue.equals(tabClosed.tipValue);
    }

    @Override
    public int hashCode() {
        int result = tabId.hashCode();
        result = 31 * result + amountPaid.hashCode();
        result = 31 * result + orderValue.hashCode();
        result = 31 * result + tipValue.hashCode();
        return result;
    }
}

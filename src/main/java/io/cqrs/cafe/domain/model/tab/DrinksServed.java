package io.cqrs.cafe.domain.model.tab;

import io.cqrs.cafe.domain.model.DomainEvent;

import java.util.Date;
import java.util.List;

public class DrinksServed implements DomainEvent {

    private Date occurredOn;
    private String tabId;
    private List<Integer> menuNumbers;

    DrinksServed(String tabId, List<Integer> menuNumbers) {
        this.occurredOn = new Date();
        this.tabId = tabId;
        this.menuNumbers = menuNumbers;
    }

    @Override
    public Date occurredOn() {
        return null;
    }

    public String getTabId() {
        return tabId;
    }

    public List<Integer> getMenuNumbers() {
        return menuNumbers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrinksServed that = (DrinksServed) o;

        if (!tabId.equals(that.tabId)) return false;
        return menuNumbers.equals(that.menuNumbers);
    }

    @Override
    public int hashCode() {
        int result = tabId.hashCode();
        result = 31 * result + menuNumbers.hashCode();
        return result;
    }
}

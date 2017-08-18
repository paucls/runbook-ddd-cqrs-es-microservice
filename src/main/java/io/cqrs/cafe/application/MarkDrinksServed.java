package io.cqrs.cafe.application;

import java.util.List;

public class MarkDrinksServed {

    private final String tabId;
    private final List<Integer> menuNumbers;

    public MarkDrinksServed(String tabId, List<Integer> menuNumbers) {
        this.tabId = tabId;
        this.menuNumbers = menuNumbers;
    }

    public String getTabId() {
        return tabId;
    }

    public List<Integer> getMenuNumbers() {
        return menuNumbers;
    }
}

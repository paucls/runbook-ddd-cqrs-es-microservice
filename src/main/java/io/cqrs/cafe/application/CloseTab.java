package io.cqrs.cafe.application;

public class CloseTab {
    private String tabId;
    private Double amountPaid;

    public CloseTab(String tabId, Double amountPaid) {
        this.tabId = tabId;
        this.amountPaid = amountPaid;
    }

    public String getTabId() {
        return tabId;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }
}

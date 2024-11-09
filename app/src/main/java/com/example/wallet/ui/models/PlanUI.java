package com.example.wallet.ui.models;

import java.util.Date;

public class PlanUI {
    private String id;
    private String name;
    private String description;
    private String paymentDeadline;
    private String targetAmount;
    public PlanUI() {}

    public PlanUI(String id, String name, String description, String paymentDeadline, String targetAmount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.paymentDeadline = paymentDeadline;
        this.targetAmount = targetAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentDeadline() {
        return paymentDeadline;
    }

    public void setPaymentDeadline(String paymentDeadline) {
        this.paymentDeadline = paymentDeadline;
    }

    public String getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(String targetAmount) {
        this.targetAmount = targetAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

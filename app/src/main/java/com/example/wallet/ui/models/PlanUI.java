package com.example.wallet.ui.models;

import java.util.Date;

public class PlanUI {
    private String name;
    private String description;
    private String paymentDeadline;
    private String targetAmount;
    public PlanUI() {}

    public PlanUI(String name, String description, String paymentDeadline, String targetAmount) {
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
}

package com.example.wallet.ui.models;

public class AccountMovementUI {
    private final String id;
    private final String amount;
    private final String date;
    private final String idPlan;
    private boolean isCheck;
    public AccountMovementUI(String id, String amount, String date, String idPlan) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.idPlan = idPlan;
        this.isCheck = false;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getIdPlan() {
        return idPlan;
    }

    public boolean isCheck() {
        return isCheck;
    }
    public void setCheck(boolean check) {
        isCheck = check;
    }
    public String getId() {
        return id;
    }
}

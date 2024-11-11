package com.example.wallet.ui.models;

public class AccountMovementUI {
    private final String id;
    private final String amount;
    private final String date;
    private final String idPlan;
    private boolean isCheck;
    private TypeAccountMovement typeAccountMovement;
    public AccountMovementUI(String id, String amount, String date, String idPlan, TypeAccountMovement typeAccountMovement) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.idPlan = idPlan;
        this.typeAccountMovement = typeAccountMovement;
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

    public TypeAccountMovement getTypeAccountMovement() {
        return typeAccountMovement;
    }

    public void setTypeAccountMovement(TypeAccountMovement typeAccountMovement) {
        this.typeAccountMovement = typeAccountMovement;
    }
}

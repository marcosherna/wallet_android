package com.example.wallet.ui.models;

public class AccountMovementUI {
    private String id;
    private String amount;
    private String date;
    private String idPlan;
    private boolean isCheck;
    private TypeAccountMovement typeAccountMovement;
    private Long autoIncrementId; // Agregar este campo

    public AccountMovementUI() {
        this.isCheck = false;
    }

    public AccountMovementUI(String id, String amount, String date, String idPlan, TypeAccountMovement typeAccountMovement) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.idPlan = idPlan;
        this.typeAccountMovement = typeAccountMovement;
        this.isCheck = false;
    }

    public Long getAutoIncrementId() {
        return autoIncrementId;
    }

    public void setAutoIncrementId(Long autoIncrementId) {
        this.autoIncrementId = autoIncrementId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAmount() { return amount; }
    public String getDate() { return date; }
    public String getIdPlan() { return idPlan; }
    public boolean isCheck() { return isCheck; }
    public void setCheck(boolean check) { isCheck = check; }
    public TypeAccountMovement getTypeAccountMovement() { return typeAccountMovement; }
    public void setTypeAccountMovement(TypeAccountMovement typeAccountMovement) {
        this.typeAccountMovement = typeAccountMovement;
    }
}

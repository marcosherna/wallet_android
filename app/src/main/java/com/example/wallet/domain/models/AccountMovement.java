package com.example.wallet.domain.models;

import java.util.Date;

public class AccountMovement extends Entity {

    public enum Type {
        EXPENSE,
        REVENUE
    }

    private Float amount;
    private Date date;
    private Type typeMovements;
    private String idPlan;
    private Plan plan;
    private Long autoIncrementId; // Nuevo campo para el ID autoincremental

    // Constructor vacío requerido por Firestore
    public AccountMovement() {
    }

    public AccountMovement(Type type, String idPlan, Float amount){
        this.typeMovements = type;
        this.idPlan = idPlan;
        this.amount = amount;
        this.date = new Date();
    }

    // Getters y Setters del nuevo campo
    public Long getAutoIncrementId() {
        return autoIncrementId;
    }

    public void setAutoIncrementId(Long autoIncrementId) {
        this.autoIncrementId = autoIncrementId;
    }

    // Getters y Setters originales
    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Type getTypeMovements() {
        return typeMovements;
    }

    public void setTypeMovements(Type typeMovements) {
        this.typeMovements = typeMovements;
    }

    public String getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(String idPlan) {
        this.idPlan = idPlan;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }
}

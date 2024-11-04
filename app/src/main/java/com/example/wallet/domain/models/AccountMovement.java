package com.example.wallet.domain.models;

import java.util.Date;

public class AccountMovement extends Entity {

    public enum Type {
        EXPENSE,
        REVENUE
    }


    private Float amount;
    private Date date;
    private Type type;
    private String idPlan;
    private Plan plan;

    public AccountMovement(Type type, String idPlan, Float amount){
        this.type = type;
        this.idPlan = idPlan;
        this.amount = amount;
        this.date = new Date();
    }


    public Float getAmount() { return  this.amount; }
    public void setAmount(Float amount){
        this.amount = amount;
    }

    public Date getDate() { return  this.date; }
    public void setDate(Date date){
        this.date = date;
    }

    public Type getTypeMovements() { return  this.type; }
    public void setTypeMovements(Type type){
        this.type = type;
    }

    public String getIdPlan() { return  this.idPlan; }
    public void setIdPlan(String idPlan){
        this.idPlan = idPlan;
    }

    public Plan getPlan() { return  this.plan; }
    public void setPlan(Plan plan){
        this.plan = plan;
    }


}

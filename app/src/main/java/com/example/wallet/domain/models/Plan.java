package com.example.wallet.domain.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Plan extends Entity{

    public enum Status {
        COMPLETE,
        IN_PROGRESS,
    }
    private final String id;
    private String name;
    private String description;
    private Date paymentDeadline;
    private Float targetAmount;
    private Status status;
    private final List<AccountMovement> movements;

    public Plan(String id, String name, String description, Date paymentDeadline, Float targetAmount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.paymentDeadline = paymentDeadline;
        this.targetAmount = targetAmount;
        this.status = Status.IN_PROGRESS;
        this.movements = new ArrayList<>();
    }

    public String getId() { return this.id; }

    public String getName() { return this.name; }
    public void setName(String name){
        this.name = name;
    }

    public String getDescription() { return this.description; }
    public void setDescription(String description){
        this.description = description;
    }

    public Date getPaymentDeadline() { return this.paymentDeadline; }
    public void setPaymentDeadline(Date paymentDeadline){
        this.paymentDeadline = paymentDeadline;
    }

    public Float getTargetAmount() { return this.targetAmount; }
    public void getTargetAmount(Float targetAmount){
        this.targetAmount = targetAmount;
    }

    public Status getStatus() {
        this.status = this.getCurrentAmount() >= this.targetAmount? Status.COMPLETE : Status.IN_PROGRESS;
        return this.status;
    }


    public void addMovement(AccountMovement movement){
        if (movement.getIdPlan().equals(this.id)) {
            this.movements.add(movement);
        }
    }

    public Float sumAmountToTypeAccount(AccountMovement.Type type){
        return this.movements.stream()
                .filter( m -> m.getTypeMovements() == type)
                .map(AccountMovement::getAmount)
                .reduce(0.0f, Float::sum);
    }

    public Float getCurrentAmount(){
        float amountExpense;
        float amountRevenue;
        amountExpense = this.sumAmountToTypeAccount(AccountMovement.Type.EXPENSE);
        amountRevenue = this.sumAmountToTypeAccount(AccountMovement.Type.REVENUE);
        return amountRevenue - amountExpense;
    }

    public List<AccountMovement> getMovements(){
        return this.movements;
    }

    public Float getRate(){
        float rate = 0;
        float currentAmount = this.getCurrentAmount();
        if (this.targetAmount != null && this.targetAmount > 0) {
            rate = (currentAmount / this.targetAmount) * 100;
        }
        return rate;
    }


}

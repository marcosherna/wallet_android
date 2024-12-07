package com.example.wallet.domain.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Plan extends Entity {

    public enum Status {
        COMPLETE,
        IN_PROGRESS,
    }

    private String id;
    private String name;
    private String description;
    private Date paymentDeadline;
    private Float targetAmount;
    private Status status;
    private List<AccountMovement> movements;
    private Long autoIncrementId; // Campo para el ID autoincremental

    // Constructor vacío requerido por Firestore
    public Plan() {
        this.movements = new ArrayList<>();
    }

    public Plan(String id, String name, String description, Date paymentDeadline, Float targetAmount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.paymentDeadline = paymentDeadline;
        this.targetAmount = targetAmount;
        this.status = Status.IN_PROGRESS;
        this.movements = new ArrayList<>();
    }

    public Long getAutoIncrementId() {
        return this.autoIncrementId;
    }

    public void setAutoIncrementId(Long autoIncrementId) {
        this.autoIncrementId = autoIncrementId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public Date getPaymentDeadline() {
        return this.paymentDeadline;
    }

    public void setPaymentDeadline(Date paymentDeadline){
        this.paymentDeadline = paymentDeadline;
    }

    public Float getTargetAmount() {
        return this.targetAmount;
    }

    public void setTargetAmount(Float targetAmount){
        this.targetAmount = targetAmount;
    }

    public Status getStatus() {
        this.status = this.getCurrentAmount() >= this.targetAmount ? Status.COMPLETE : Status.IN_PROGRESS;
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Float getCurrentAmount(){
        float amountExpense = this.sumAmountToTypeAccount(AccountMovement.Type.EXPENSE);
        float amountRevenue = this.sumAmountToTypeAccount(AccountMovement.Type.REVENUE);
        return amountRevenue - amountExpense;
    }

    public Float getRate(){
        float rate = 0;
        float currentAmount = this.getCurrentAmount();
        if (this.targetAmount != null && this.targetAmount > 0) {
            rate = (currentAmount / this.targetAmount) * 100;
            rate = rate == 0f ? 100 : rate;
        }
        return rate;
    }

    public List<AccountMovement> getMovements(){
        return this.movements;
    }

    public void setMovements(List<AccountMovement> movements) {
        this.movements = movements;
    }

    public void addMovement(AccountMovement movement){
        if (movement.getIdPlan().equals(this.id)) {
            this.movements.add(movement);
        }
    }

    public Float sumAmountToTypeAccount(AccountMovement.Type type){
        float sum = 0f;
        for (AccountMovement m : this.movements) {
            if (m.getTypeMovements() == type) {
                sum += m.getAmount();
            }
        }
        return sum;
    }

    public Float getTotalExpended(){
        return sumAmountToTypeAccount(AccountMovement.Type.EXPENSE);
    }

    public Float getTotalRevenue(){
        return sumAmountToTypeAccount(AccountMovement.Type.REVENUE);
    }

    public Float getPercentage(){
        return this.getRate();
    }

    public void setCurrentAmount(Float currentAmount) {
        // Este método no es necesario implementar lógicamente, ya que el currentAmount se calcula dinámicamente
    }

    public void setRate(Float rate) {
        // Igual que el anterior, el rate se calcula dinámicamente, no hace falta setearlo
    }
}

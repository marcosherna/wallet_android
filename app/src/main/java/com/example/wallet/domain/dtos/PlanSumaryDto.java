package com.example.wallet.domain.dtos;

public class PlanSumaryDto {
    private final String id;
    private final String idPlan;
    private final String groupDescription;
    private final String totalExpended;
    private final String totalRevenue;
    private final String percentage;
    private final String total;
    private final String plazo;
    private final String status;

    public PlanSumaryDto(String id, String idPlan, String groupDescription, String totalExpended, String totalRevenue, String percentage, String total, String plazo, String status) {
        this.id = id;
        this.idPlan = idPlan;
        this.groupDescription = groupDescription;
        this.totalExpended = totalExpended;
        this.totalRevenue = totalRevenue;
        this.percentage = percentage;
        this.total = total;
        this.plazo = plazo;
        this.status = status;
    }
    public String getId() {
        return id;
    }

    public String getIdPlan() {
        return idPlan;
    }

    public String getTotalExpended() {
        return totalExpended;
    }

    public String getTotalRevenue() {
        return totalRevenue;
    }

    public String getPercentage() {
        return percentage;
    }

    public String getTotal(){
        return this.total;
    }

    public String getGroupDescription(){
        return this.groupDescription;
    }
    public String getPlazo() {
        return plazo;
    }

    public String getStatus() {
        return status;
    }



}

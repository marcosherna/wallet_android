package com.example.wallet.ui.models;

public class PlanSummaryUI {
    private final String id;
    private final String idPlan;
    private final String groupDescription;
    private final String totalExpended;
    private final String totalRevenue;
    private final String percentage;
    private final String total;
    private final String term;
    private final String status;

    public PlanSummaryUI(String id, String idPlan, String groupDescription, String totalExpended, String totalRevenue, String percentage, String total, String term, String status) {
        this.id = id;
        this.idPlan = idPlan;
        this.groupDescription = groupDescription;
        this.totalExpended = totalExpended;
        this.totalRevenue = totalRevenue;
        this.percentage = percentage;
        this.total = total;
        this.term = term;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getIdPlan() {
        return idPlan;
    }

    public String getGroupDescription() {
        return groupDescription;
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

    public String getTotal() {
        return total;
    }

    public String getTerm() {
        return term;
    }

    public String getStatus() {
        return status;
    }
}

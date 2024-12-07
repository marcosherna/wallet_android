package com.example.wallet.ui.models;

public class PlanSummaryUI {
    private String someField; // Campo adicional si es necesario
    private String idPlan;
    private String namePlan;
    private String description;
    private String totalExpended;
    private String totalRevenue;
    private String percentage;
    private String total;
    private String term;
    private String status;

    public PlanSummaryUI() {
    }

    public PlanSummaryUI(String someField, String idPlan, String namePlan, String description,
                         String totalExpended, String totalRevenue, String percentage,
                         String total, String term, String status) {
        this.someField = someField;
        this.idPlan = idPlan;
        this.namePlan = namePlan;
        this.description = description;
        this.totalExpended = totalExpended;
        this.totalRevenue = totalRevenue;
        this.percentage = percentage;
        this.total = total;
        this.term = term;
        this.status = status;
    }

    // Getters y Setters

    public String getSomeField() {
        return someField;
    }

    public void setSomeField(String someField) {
        this.someField = someField;
    }

    public String getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(String idPlan) {
        this.idPlan = idPlan;
    }

    public String getNamePlan() {
        return namePlan;
    }

    public void setNamePlan(String namePlan) {
        this.namePlan = namePlan;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotalExpended() {
        return totalExpended;
    }

    public void setTotalExpended(String totalExpended) {
        this.totalExpended = totalExpended;
    }

    public String getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(String totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Método adicional para obtener la descripción del grupo.
     * Aquí puedes definir qué campo representa la descripción del grupo.
     * Por ejemplo, podrías usar el nombre del plan.
     */
    public String getGroupDescription() {
        return this.namePlan; // O cualquier otro campo que consideres adecuado
    }

    @Override
    public String toString() {
        return "PlanSummaryUI{" +
                "idPlan='" + idPlan + '\'' +
                ", namePlan='" + namePlan + '\'' +
                ", description='" + description + '\'' +
                ", totalExpended='" + totalExpended + '\'' +
                ", totalRevenue='" + totalRevenue + '\'' +
                ", percentage='" + percentage + '\'' +
                ", total='" + total + '\'' +
                ", term='" + term + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

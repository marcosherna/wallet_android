package com.example.wallet.ui.mappers;

import com.example.wallet.domain.models.AccountMovement;
import com.example.wallet.domain.models.Plan;
import com.example.wallet.ui.models.AccountMovementUI;
import com.example.wallet.ui.models.PlanSummaryUI;
import com.example.wallet.ui.models.PlanUI;
import com.example.wallet.ui.models.TypeAccountMovement;
import com.example.wallet.utils.DateFormatHelper;

import java.util.Date;

public class Mapper {

    public static AccountMovementUI AMovementToUI(AccountMovement movement) {
        if (movement == null) {
            return null;
        }

        // Formatear la fecha de Date a String
        String dateFormatted;
        try {
            dateFormatted = DateFormatHelper.format(movement.getDate(), "EEEE d 'de' MMMM 'del' yyyy");
        } catch (Exception e) {
            // Manejar el error de formateo, asignar una representación por defecto
            dateFormatted = movement.getDate().toString();
        }

        // Mapeo del tipo de movimiento
        TypeAccountMovement typeUI;
        if (movement.getTypeMovements() == AccountMovement.Type.EXPENSE) {
            typeUI = TypeAccountMovement.EXPENSE;
        } else {
            typeUI = TypeAccountMovement.REVENUE;
        }

        return new AccountMovementUI(
                movement.getId(),
                String.valueOf(movement.getAmount()),
                dateFormatted, // Ahora es String
                movement.getIdPlan(),
                typeUI
        );
    }

    public static PlanSummaryUI PlanToSummaryUI(Plan plan) {
        if (plan == null) {
            return null;
        }

        // Formatear la fecha de Date a String
        String termFormatted;
        try {
            termFormatted = DateFormatHelper.format(plan.getPaymentDeadline(), "EEEE d 'de' MMMM 'del' yyyy");
        } catch (Exception e) {
            // Manejar el error de formateo, asignar una representación por defecto
            termFormatted = plan.getPaymentDeadline().toString();
        }

        // Determinar el estado como String
        String status = (plan.getStatus() == Plan.Status.IN_PROGRESS) ? "En Proceso" : "Completo";

        return new PlanSummaryUI(
                "", // someField, asignar vacío ya que no se especifica
                plan.getId(), // idPlan
                plan.getName(), // namePlan
                plan.getDescription(), // description
                plan.getTotalExpended() != null ? String.valueOf(plan.getTotalExpended()) : "0", // totalExpended
                plan.getTotalRevenue() != null ? String.valueOf(plan.getTotalRevenue()) : "0", // totalRevenue
                plan.getPercentage() != null ? String.valueOf(plan.getPercentage()) + "%" : "0%", // percentage
                plan.getTargetAmount() != null ? String.valueOf(plan.getTargetAmount()) : "0", // total (usando targetAmount si es equivalente)
                termFormatted, // term
                status // status
        );
    }


    public static Plan PlanUIToPlan(PlanUI planUI) {
        if (planUI == null) {
            return null;
        }

        // Parsear la fecha de String a Date
        Date paymentDeadline;
        try {
            paymentDeadline = DateFormatHelper.parseDate(planUI.getPaymentDeadline());
        } catch (Exception e) {
            // Manejar el error de parseo, asignar una fecha por defecto
            paymentDeadline = new Date();
        }

        // Parsear targetAmount de String a Float
        Float targetAmount;
        try {
            targetAmount = Float.parseFloat(planUI.getTargetAmount());
        } catch (NumberFormatException e) {
            targetAmount = 0.0f; // Valor por defecto
        }

        Plan plan = new Plan(
                planUI.getId(),
                planUI.getName(),
                planUI.getDescription(),
                paymentDeadline,
                targetAmount
        );

        // Otros campos como 'status' se calcularán automáticamente en el getter de 'status'

        return plan;
    }


    public static PlanUI PlanToUI(Plan plan) {
        if (plan == null) {
            return null;
        }

        // Formatear la fecha de Date a String
        String paymentDeadlineFormatted;
        try {
            paymentDeadlineFormatted = DateFormatHelper.format(plan.getPaymentDeadline(), "EEEE d 'de' MMMM 'del' yyyy");
        } catch (Exception e) {
            // Manejar el error de formateo, asignar una representación por defecto
            paymentDeadlineFormatted = plan.getPaymentDeadline().toString();
        }

        // Formatear targetAmount de Float a String
        String targetAmountStr = plan.getTargetAmount() != null ? String.valueOf(plan.getTargetAmount()) : "0";

        return new PlanUI(
                plan.getId(),
                plan.getName(),
                plan.getDescription(),
                paymentDeadlineFormatted,
                targetAmountStr
        );
    }
}

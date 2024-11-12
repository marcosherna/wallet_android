package com.example.wallet.ui.mappers;

import com.example.wallet.domain.models.AccountMovement;
import com.example.wallet.domain.models.Plan;
import com.example.wallet.ui.models.AccountMovementUI;
import com.example.wallet.ui.models.PlanSummaryUI;
import com.example.wallet.ui.models.PlanUI;
import com.example.wallet.ui.models.TypeAccountMovement;
import com.example.wallet.utils.DateFormatHelper;

public class Mapper {
    public static AccountMovementUI AMovementToUI(AccountMovement movement){
        String dateFormat = DateFormatHelper.format(movement.getDate(), "dd-MM-yyyy");
        return new AccountMovementUI(
                movement.getId(),
                movement.getAmount().toString(),
                dateFormat,
                movement.getIdPlan(), // TODO: refactorizar el tipo
                movement.getTypeMovements() == AccountMovement.Type.EXPENSE ? TypeAccountMovement.EXPENSE: TypeAccountMovement.REVENUE
        );
    }

    public static PlanUI PlanToUI(Plan plan){
        String dateFormat = DateFormatHelper.format(plan.getPaymentDeadline(), "dd-MM-yyyy");
        return new PlanUI(plan.getId(),
                plan.getName(),
                plan.getDescription(),
                dateFormat,
                plan.getTargetAmount().toString());
    }

    public static PlanSummaryUI PlanToSummaryUI(Plan plan){

        Float totalExpense = plan.sumAmountToTypeAccount(AccountMovement.Type.EXPENSE);
        Float totalRevenue = plan.sumAmountToTypeAccount(AccountMovement.Type.REVENUE);

        return  new PlanSummaryUI("",
                plan.getId(),
                plan.getName(),
                plan.getDescription(),
                totalExpense.toString(),
                totalRevenue.toString(),
                plan.getRate().toString()  + "%",
                plan.getCurrentAmount().toString(),
                DateFormatHelper.format(plan.getPaymentDeadline(), "EEEE d 'de' MMMM 'del' yyyy"),
                (plan.getStatus() == Plan.Status.IN_PROGRESS ?
                        "En Proceso" : "Completo") );
    }
}

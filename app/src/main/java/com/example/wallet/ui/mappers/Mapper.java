package com.example.wallet.ui.mappers;

import com.example.wallet.domain.models.AccountMovement;
import com.example.wallet.domain.models.Plan;
import com.example.wallet.ui.models.AccountMovementUI;
import com.example.wallet.ui.models.PlanUI;
import com.example.wallet.utils.DateFormatHelper;

public class Mapper {
    public static AccountMovementUI AMovementToUI(AccountMovement movement){
        String dateFormat = DateFormatHelper.format(movement.getDate(), "dd-MM-yyyy");
        return new AccountMovementUI(
                movement.getId(),
                movement.getAmount().toString(),
                dateFormat,
                movement.getIdPlan());
    }

    public static PlanUI PlanToUI(Plan plan){
        String dateFormat = DateFormatHelper.format(plan.getPaymentDeadline(), "dd-MM-yyyy");
        return new PlanUI(plan.getId(),
                plan.getName(),
                plan.getDescription(),
                dateFormat,
                plan.getTargetAmount().toString());
    }
}

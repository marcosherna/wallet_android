package com.example.wallet.ui.mappers;

import com.example.wallet.domain.models.AccountMovement;
import com.example.wallet.ui.models.AccountMovementUI;
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
}

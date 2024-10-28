package com.example.wallet.domain.fake.date;

import com.example.wallet.domain.models.AccountMovement;
import com.example.wallet.domain.models.Entity;
import com.example.wallet.domain.models.Plan;

import java.util.ArrayList;
import java.util.Date;


public class LocalContext {
    public static ArrayList<Entity> context = new ArrayList<>();

    public static void setup(){
        Plan plan1 =  new Plan("", "test", "Este es un plan para test", new Date(), 400f);
        plan1.addMovement(new AccountMovement(AccountMovement.Type.EXPENSE, plan1.getId(), 100f));
        plan1.addMovement(new AccountMovement(AccountMovement.Type.REVENUE, plan1.getId(), 100f));
        context.add(plan1);
    }

}

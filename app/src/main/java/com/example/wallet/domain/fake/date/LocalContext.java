package com.example.wallet.domain.fake.date;

import com.example.wallet.domain.models.AccountMovement;
import com.example.wallet.domain.models.Entity;
import com.example.wallet.domain.models.Plan;

import java.util.ArrayList;
import java.util.Date;


public class LocalContext {
    public static ArrayList<Entity> context = new ArrayList<>();

    public static void setup(){
        context.clear();
        Plan plan1 =  new Plan("1", "test", "Este es un plan para test", new Date(), 800f);

        AccountMovement movement1 = new AccountMovement(AccountMovement.Type.REVENUE, plan1.getId(), 100f);
        AccountMovement movement2 = new AccountMovement(AccountMovement.Type.EXPENSE, plan1.getId(), 200f);

        Plan plan2 = new Plan("2", "test2", "Este es un plan para test", new Date(), 400f);
        AccountMovement movement3 = new AccountMovement(AccountMovement.Type.REVENUE, plan2.getId(), 200f);
        AccountMovement movement4 = new AccountMovement(AccountMovement.Type.EXPENSE, plan2.getId(), 200f);

        context.add(plan1);
        context.add(plan2);
        context.add(movement1);
        context.add(movement2);

        context.add(movement3);
        context.add(movement4);


    }

}

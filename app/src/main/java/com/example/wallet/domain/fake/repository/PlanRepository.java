package com.example.wallet.domain.fake.repository;


import com.example.wallet.domain.fake.date.LocalContext;
import com.example.wallet.domain.models.AccountMovement;
import com.example.wallet.domain.models.Plan;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PlanRepository extends BaseRepository<Plan>{
    public PlanRepository(){
        super(LocalContext.context);
    }

    @Override
    public Plan get(String id) {
        return super.filterByClass(Plan.class).stream()
                .filter(p -> p.getId().equals(id))
                .peek(plan -> {

                    ArrayList<AccountMovement> movements = super.filterByClass(AccountMovement.class).stream()
                            .filter(movement -> movement.getIdPlan().equals(plan.getId()))
                            .collect(Collectors.toCollection(ArrayList::new));

                    plan.getMovements().clear();
                    movements.forEach(plan::addMovement);

                }).findFirst().orElse(null);
    }

    @Override
    public ArrayList<Plan> getAll() {
        return super.filterByClass(Plan.class).stream()
                .peek(plan -> {
                    ArrayList<AccountMovement> movements = super.filterByClass(AccountMovement.class).stream()
                            .filter(movement -> movement.getIdPlan().equals(plan.getId()))
                            .collect(Collectors.toCollection(ArrayList::new));

                    plan.getMovements().clear();
                    movements.forEach(plan::addMovement);
                })
                .collect(Collectors.toCollection(ArrayList::new));

    }

}

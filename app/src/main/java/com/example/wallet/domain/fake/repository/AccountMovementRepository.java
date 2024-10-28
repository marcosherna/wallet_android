package com.example.wallet.domain.fake.repository;

import com.example.wallet.domain.fake.date.LocalContext;
import com.example.wallet.domain.models.AccountMovement;
import com.example.wallet.domain.models.Plan;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class AccountMovementRepository extends BaseRepository<AccountMovement> {
    public AccountMovementRepository(){
        super(LocalContext.context);
    }

    @Override
    public AccountMovement get(String id) {
        return super.filterByClass(AccountMovement.class).stream()
                .filter( acm -> acm.getId().equals(id))
                .peek(acm -> {
                    super.filterByClass(Plan.class).stream()
                            .filter( p -> p.getId().equals(acm.getIdPlan()))
                            .findFirst()
                            .ifPresent( p -> {
                                acm.setIdPlan(p.getId());
                                acm.setPlan(p);
                            });

                }).findFirst().orElse(null);
    }

    @Override
    public ArrayList<AccountMovement> getAll() {
        return super.filterByClass(AccountMovement.class).stream()
                .peek(acm -> {
                    super.filterByClass(Plan.class).stream()
                            .filter( p -> p.getId().equals(acm.getIdPlan()))
                            .findFirst()
                            .ifPresent( p -> {
                                acm.setIdPlan(p.getId());
                                acm.setPlan(p);
                            });

                }).collect(Collectors.toCollection(ArrayList::new));
    }
}

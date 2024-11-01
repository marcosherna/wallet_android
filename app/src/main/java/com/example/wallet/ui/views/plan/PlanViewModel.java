package com.example.wallet.ui.views.plan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.domain.dtos.PlanSumaryDto;
import com.example.wallet.domain.fake.repository.PlanRepository;
import com.example.wallet.domain.models.AccountMovement;
import com.example.wallet.domain.models.Plan;
import com.example.wallet.utils.DateFormatHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlanViewModel extends ViewModel {
    PlanRepository planRepository;
    final MutableLiveData<HashMap<String, List<PlanSumaryDto>>> planstWithSumary;
    public LiveData<HashMap<String, List<PlanSumaryDto>>> getPlansWithSumary(){ return this.planstWithSumary; }


    public PlanViewModel() {
        this.planstWithSumary = new MutableLiveData<>();
        this.planRepository = new PlanRepository();
        this.transformDataToDto();
    }

    public void transformDataToDto(){
        if (this.planRepository != null){
            ArrayList<Plan> plans = this.planRepository.getAll();
            HashMap<String, List<PlanSumaryDto>> _plansWithSumary = new HashMap<>();

            plans.forEach(plan -> {
                Float totalExpense = plan.sumAmountToTypeAccount(AccountMovement.Type.EXPENSE);
                Float totalRevenue = plan.sumAmountToTypeAccount(AccountMovement.Type.REVENUE);

                PlanSumaryDto planSumaryDto = new PlanSumaryDto("",
                        plan.getId(),
                        plan.getDescription(),
                        totalExpense.toString(),
                        totalRevenue.toString(),
                        plan.getRate().toString()  + "%",
                        plan.getCurrentAmount().toString(),
                        DateFormatHelper.format(plan.getPaymentDeadline(), "EEEE d 'de' MMMM 'del' yyyy"),
                        (plan.getStatus() == Plan.Status.IN_PROGRESS ?
                                "En Proceso" : "Completo") );

                _plansWithSumary.put(plan.getName(), Collections.singletonList(planSumaryDto));
            });

            this.planstWithSumary.setValue(_plansWithSumary);

        }
    }


}
package com.example.wallet.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.wallet.R;
import com.example.wallet.domain.dtos.PlanSumaryDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PlanExpandableListAdapter extends BaseExpandableListAdapter {

    Context context;

    public PlanExpandableListAdapter(Context context, List<String> titlesPlans, HashMap<String, List<PlanSumaryDto>> plansWithSumary) {
        this.context = context;
        this.titlesPlans = titlesPlans;
        this.plansWithSumary = plansWithSumary;
    }

    List<String> titlesPlans;
    HashMap<String, List<PlanSumaryDto>> plansWithSumary;

    public PlanExpandableListAdapter(Context context){
        this.titlesPlans = new ArrayList<>();
        this.plansWithSumary = new HashMap<>();
        this.context = context;
    }

    public void setTitlesPlans(List<String> titlesPlans){
        this.titlesPlans = titlesPlans;
    }

    public void setPlansWithSumary(HashMap<String, List<PlanSumaryDto>> plansWithSumary){
        this.plansWithSumary = plansWithSumary;
    }

    @Override
    public int getGroupCount() {
        return this.titlesPlans != null ? this.titlesPlans.size() :0 ;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<PlanSumaryDto> children = this.plansWithSumary.get(this.titlesPlans.get(groupPosition));
        return children != null ? children.size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.titlesPlans.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.plansWithSumary.get(this.titlesPlans.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupDescription) {
        return groupDescription;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String title = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater
                    .inflate(R.layout.expandable_list_header, parent, false);
        }
        TextView listTitleTextView = convertView.findViewById(R.id.tv_title_plan);
        listTitleTextView.setText(title);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final PlanSumaryDto planSumaryDto = (PlanSumaryDto) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater)this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater
                    .inflate(R.layout.expandable_list_item, parent, false);
        }

        TextView tvPlanDescription = convertView.findViewById(R.id.tv_planDescription);
        TextView tvRevenuedPlan = convertView.findViewById(R.id.tv_revenued_plan);
        TextView tvStatusPlan = convertView.findViewById(R.id.tv_status_plan);
        TextView tvExpendedPlan = convertView.findViewById(R.id.tv_expended_plan);
        TextView tvTotalPricePlan = convertView.findViewById(R.id.tv_total_price_plan);
        TextView tvPlazoPlan = convertView.findViewById(R.id.tv_plazo_plan);
        TextView tvPercentagePlan = convertView.findViewById(R.id.tv_percentage_plan);

        tvPlanDescription.setText(planSumaryDto.getGroupDescription());
        tvRevenuedPlan.setText(planSumaryDto.getTotalRevenue());
        tvExpendedPlan.setText(planSumaryDto.getTotalExpended());
        tvTotalPricePlan.setText(planSumaryDto.getTotal());
        tvPlazoPlan.setText(planSumaryDto.getPlazo());
        tvPercentagePlan.setText(planSumaryDto.getPercentage());
        tvStatusPlan.setText(planSumaryDto.getStatus());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

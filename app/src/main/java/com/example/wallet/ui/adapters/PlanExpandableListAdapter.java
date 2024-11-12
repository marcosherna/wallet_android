package com.example.wallet.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wallet.R;
import com.example.wallet.ui.models.PlanSummaryUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PlanExpandableListAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> titlesPlans;
    HashMap<String, List<PlanSummaryUI>> plansWithSummary;
    boolean hasActions = false;

    OnClickDialog listenerPositiveClick;
    OnClickDialog listenerNegativeClick;

    public PlanExpandableListAdapter(Context context, boolean hasActions){
        this.titlesPlans = new ArrayList<>();
        this.plansWithSummary = new HashMap<>();
        this.context = context;
        this.hasActions = hasActions;
    }

    public void setTitlesPlans(List<String> titlesPlans){
        this.titlesPlans = titlesPlans;
    }

    public void setPlansWithSummary(HashMap<String, List<PlanSummaryUI>> plansWithSummary){
        if(this.plansWithSummary != null){
            this.plansWithSummary = plansWithSummary;
            notifyDataSetChanged();
        }
    }

    public void setOnListenerPositiveClick(OnClickDialog listener){
        this.listenerPositiveClick = listener;
    }

    public void setOnListenerNegativeClick(OnClickDialog listener){
        this.listenerNegativeClick = listener;
    }

    public interface OnClickDialog{
        void OnClick(PlanSummaryUI planSummaryUI);
    }

    @Override
    public int getGroupCount() {
        return this.titlesPlans != null ? this.titlesPlans.size() :0 ;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<PlanSummaryUI> children = this.plansWithSummary.get(this.titlesPlans.get(groupPosition));
        return children != null ? children.size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.titlesPlans.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Objects.requireNonNull(this.plansWithSummary.get(this.titlesPlans.get(groupPosition)))
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
        final PlanSummaryUI planSummaryUI = (PlanSummaryUI) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater)this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater
                    .inflate(R.layout.expandable_list_item, parent, false);
        }

        int visibility = hasActions ? View.VISIBLE: View.GONE;
        convertView.findViewById(R.id.actionsLayout).setVisibility(visibility);

        TextView tvPlanDescription = convertView.findViewById(R.id.tv_planDescription);
        TextView tvRevenuedPlan = convertView.findViewById(R.id.tv_revenued_plan);
        TextView tvStatusPlan = convertView.findViewById(R.id.tv_status_plan);
        TextView tvExpendedPlan = convertView.findViewById(R.id.tv_expended_plan);
        TextView tvTotalPricePlan = convertView.findViewById(R.id.tv_total_price_plan);
        TextView tvTermPlan = convertView.findViewById(R.id.tv_plazo_plan);
        TextView tvPercentagePlan = convertView.findViewById(R.id.tv_percentage_plan);

        // actions buttons
        ImageButton negativeButton = convertView.findViewById(R.id.btnDelete);
        ImageButton positiveButton = convertView.findViewById(R.id.btnEdit);

        tvPlanDescription.setText(planSummaryUI.getGroupDescription());
        tvRevenuedPlan.setText(planSummaryUI.getTotalRevenue());
        tvExpendedPlan.setText(planSummaryUI.getTotalExpended());
        tvTotalPricePlan.setText(planSummaryUI.getTotal());
        tvTermPlan.setText(planSummaryUI.getTerm());
        tvPercentagePlan.setText(planSummaryUI.getPercentage());
        tvStatusPlan.setText(planSummaryUI.getStatus());


        positiveButton.setOnClickListener( __ -> {
            if (this.listenerPositiveClick != null){
                this.listenerPositiveClick.OnClick(planSummaryUI);
            }
        });

        negativeButton.setOnClickListener( __ -> {
            if (this.listenerNegativeClick != null){
                this.listenerNegativeClick.OnClick(planSummaryUI);
            }
        });

        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

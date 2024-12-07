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

    private final Context context;
    private List<String> listTitlesPlans;
    private HashMap<String, List<PlanSummaryUI>> listPlansWithSummary;
    private OnListenerNegativeClick listenerNegativeClick;
    private OnListenerPositiveClick listenerPositiveClick;

    public PlanExpandableListAdapter(Context context, boolean hasActions){
        this.listTitlesPlans = new ArrayList<>();
        this.listPlansWithSummary = new HashMap<>();
        this.context = context;
    }

    public void setTitlesPlans(List<String> titlesPlans){
        this.listTitlesPlans = titlesPlans;
        notifyDataSetChanged();
    }

    public void setPlansWithSummary(HashMap<String, List<PlanSummaryUI>> plansWithSummary){
        this.listPlansWithSummary = plansWithSummary;
        notifyDataSetChanged();
    }

    public void setOnListenerNegativeClick(OnListenerNegativeClick listener) {
        this.listenerNegativeClick = listener;
    }

    public void setOnListenerPositiveClick(OnListenerPositiveClick listener) {
        this.listenerPositiveClick = listener;
    }

    @Override
    public int getGroupCount() {
        return this.listTitlesPlans != null ? this.listTitlesPlans.size() :0 ;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<PlanSummaryUI> children = this.listPlansWithSummary.get(this.listTitlesPlans.get(groupPosition));
        return children != null ? children.size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listTitlesPlans.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Objects.requireNonNull(this.listPlansWithSummary.get(this.listTitlesPlans.get(groupPosition)))
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

    // Vista del Grupo
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

    // Vista del Niño
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final PlanSummaryUI planSummaryUI = (PlanSummaryUI) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater)this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater
                    .inflate(R.layout.expandable_list_item, parent, false);
        }

        // Configurar la visibilidad de las acciones
        convertView.findViewById(R.id.actionsLayout).setVisibility(View.VISIBLE);

        TextView tvPlanDescription = convertView.findViewById(R.id.tv_planDescription);
        TextView tvRevenuedPlan = convertView.findViewById(R.id.tv_revenued_plan);
        TextView tvStatusPlan = convertView.findViewById(R.id.tv_status_plan);
        TextView tvExpendedPlan = convertView.findViewById(R.id.tv_expended_plan);
        TextView tvTotalPricePlan = convertView.findViewById(R.id.tv_total_price_plan);
        TextView tvTermPlan = convertView.findViewById(R.id.tv_plazo_plan);
        TextView tvPercentagePlan = convertView.findViewById(R.id.tv_percentage_plan);

        // Configurar los TextViews con datos del plan
        if (planSummaryUI.getGroupDescription() != null) {
            tvPlanDescription.setText(planSummaryUI.getGroupDescription());
        } else {
            tvPlanDescription.setText("Nombre no disponible");
        }

        tvRevenuedPlan.setText(planSummaryUI.getTotalRevenue() != null ? planSummaryUI.getTotalRevenue() : "0");
        tvExpendedPlan.setText(planSummaryUI.getTotalExpended() != null ? planSummaryUI.getTotalExpended() : "0");
        tvTotalPricePlan.setText(planSummaryUI.getTotal() != null ? planSummaryUI.getTotal() : "0");
        tvTermPlan.setText(planSummaryUI.getTerm() != null ? planSummaryUI.getTerm() : "Sin plazo");
        tvPercentagePlan.setText(planSummaryUI.getPercentage() != null ? planSummaryUI.getPercentage() : "0%");
        tvStatusPlan.setText(planSummaryUI.getStatus() != null ? planSummaryUI.getStatus() : "Estado desconocido");

        // Configurar los botones de acción
        ImageButton negativeButton = convertView.findViewById(R.id.btnDelete);
        ImageButton positiveButton = convertView.findViewById(R.id.btnEdit);

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

    // Interfaces para manejar eventos de clic
    public interface OnListenerNegativeClick {
        void OnClick(PlanSummaryUI planSummaryUI);
    }

    public interface OnListenerPositiveClick {
        void OnClick(PlanSummaryUI planSummaryUI);
    }
}

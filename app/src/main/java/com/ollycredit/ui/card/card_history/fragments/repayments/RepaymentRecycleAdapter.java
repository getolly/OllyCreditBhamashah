package com.ollycredit.ui.card.card_history.fragments.repayments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ollycredit.R;

/**
 * Created by ch8n on 24/7/17.
 */

public class RepaymentRecycleAdapter extends RecyclerView.Adapter<RepaymentRecycleAdapter.RepaymentViewHolder> {


    private Context context;

    public RepaymentRecycleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RepaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_repayment_history_list, parent, false);
        RepaymentViewHolder holder = new RepaymentViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RepaymentViewHolder holder, int position) {
        //todo interacions
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RepaymentViewHolder extends ViewHolder {

        private TextView repayDateTitle;
        private TextView repayDateTime;
        private TextView repayStatus;
        private TextView repayCost;
        private TextView repayPenalty;

        public RepaymentViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View itemView) {
            repayDateTitle = (TextView) itemView.findViewById(R.id.rv_repay_date);
            repayDateTime = (TextView) itemView.findViewById(R.id.rv_repay_datetime);
            repayStatus = (TextView) itemView.findViewById(R.id.rv_repay_status);
            repayCost = (TextView) itemView.findViewById(R.id.rv_repay_cost);
            repayPenalty = (TextView) itemView.findViewById(R.id.rv_repay_penalty);
        }
    }
}

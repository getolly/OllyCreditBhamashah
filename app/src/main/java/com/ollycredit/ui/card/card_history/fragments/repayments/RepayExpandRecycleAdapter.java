package com.ollycredit.ui.card.card_history.fragments.repayments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ollycredit.R;
import com.ollycredit.api.model.RepaymentFetchedData;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ch8n on 24/7/17.
 */

public class RepayExpandRecycleAdapter extends RecyclerView.Adapter<RepayExpandRecycleAdapter.RepaymentViewHolder> {


    private Context context;
    RepaymentFetchedData bill = new RepaymentFetchedData(), repayment= new RepaymentFetchedData(),
            due= new RepaymentFetchedData();

    public RepayExpandRecycleAdapter(Context context) {
        this.context = context;
    }

    public void setdata(RepaymentFetchedData bill, RepaymentFetchedData repayment, RepaymentFetchedData due) {
        this.bill = bill;
        this.repayment = repayment;
        this.due = due;
    }

    @Override
    public RepaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_expanable_rvitem_repayment, parent, false);
        RepaymentViewHolder holder = new RepaymentViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RepaymentViewHolder holder, int position) {
        //todo interacions
        switch (position) {
            case 0:
                holder.expandIcon.setImageResource(R.drawable.ic_bill);
                //holder.expandTitle.setText("Make a repayment until 5th of the month");
                holder.expandTitle.setText(bill.getName());
                holder.expandBody.setText(bill.getContent());
                holder.iconText.setVisibility(View.INVISIBLE);
                break;

            case 1:
                holder.expandIcon.setImageResource(R.drawable.ic_repayment);
                //holder.expandTitle.setText("Repay min 40% of the amount");
                holder.expandTitle.setText(repayment.getName());
                holder.expandBody.setText(repayment.getContent());
                holder.iconText.setVisibility(View.INVISIBLE);
                break;
            case 2:
                holder.expandIcon.setImageResource(R.drawable.ic_minimum);
                //holder.expandTitle.setText("Repay min 40% of the amount");
                holder.expandTitle.setText(due.getName());
                holder.expandBody.setText(due.getContent());
                holder.iconText.setVisibility(View.INVISIBLE);
                break;
        }

        //holder.expandBody.setText("You can make a repayment earlier but no later than 5th of the next month to avoid late payment fee");

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class RepaymentViewHolder extends ViewHolder {

        private ImageView expandIcon;
        private TextView expandTitle;
        private TextView expandBody;
        @BindView(R.id.tv_icon_data)
        TextView iconText;

        public RepaymentViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void initViews(View itemView) {

            expandTitle = (TextView) itemView.findViewById(R.id.expan_title_tv);
            expandBody = (TextView) itemView.findViewById(R.id.expan_body_tv);
            expandIcon = (ImageView) itemView.findViewById(R.id.expan_icon);

        }
    }
}

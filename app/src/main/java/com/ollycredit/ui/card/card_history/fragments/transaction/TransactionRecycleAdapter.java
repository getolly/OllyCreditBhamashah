package com.ollycredit.ui.card.card_history.fragments.transaction;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ollycredit.R;
import com.ollycredit.api.model.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ch8n on 24/7/17.
 */

public class TransactionRecycleAdapter extends RecyclerView.Adapter<TransactionRecycleAdapter.TransactionViewHolder> {


    private Context context;
    private List<Transaction> transactionList;


    public TransactionRecycleAdapter(Context context, ArrayList<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_transaction_history_list, parent, false);
        TransactionViewHolder holder = new TransactionViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        //todo interacions
        holder.transTitle.setText(transactionList.get(position).getMerchant().split("   ")[0]);
        holder.transDateTime.setText(transactionList.get(position).getDate());
        String amountString = context.getString(R.string.rs_symbol) + transactionList.get(position).getAmount().toString();
        holder.transAmount.setText(amountString);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class TransactionViewHolder extends ViewHolder {

        private TextView transTitle;
        private TextView transDateTime;
        private TextView transAmount;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View itemView) {
            transTitle = (TextView) itemView.findViewById(R.id.rv_trans_title);
            transDateTime = (TextView) itemView.findViewById(R.id.rv_trans_datetime);
            transAmount = (TextView) itemView.findViewById(R.id.rv_trans_amount);
        }
    }
}

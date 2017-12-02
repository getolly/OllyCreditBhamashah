package com.ollycredit.ui.repayments.store_cards;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ollycredit.R;
import com.payu.india.Model.CardStatus;
import com.payu.india.Model.StoredCard;
import com.payu.india.Payu.PayuConstants;
import com.payu.payuui.SdkuiUtil.SdkUIConstants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ch8n on 22/11/17.
 */

public class StoreCardListAdapter extends BaseAdapter {

    private final HashMap<String, CardStatus> mValueAddedHashMap;
    private final HashMap<String, String> mOneClickCardTokens;
    private ArrayList<StoredCard> storedCards;
    private Context context;
    private StoredCardSelected storedCardSelected;


    public StoreCardListAdapter(Context context,
                                ArrayList<StoredCard> storedCards,
                                HashMap<String, CardStatus> valueAddedHashMap,
                                HashMap<String, String> oneClickCardTokens) {
        this.storedCards = storedCards;
        this.context = context;
        storedCardSelected = (StoredCardSelected) context;
        mValueAddedHashMap = valueAddedHashMap;
        mOneClickCardTokens = oneClickCardTokens;
    }

    @Override
    public int getCount() {
        if (storedCards == null) {
            return 0;
        }
        return storedCards.size();

    }

    @Override
    public Object getItem(int i) {
        return storedCards.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.item_list_stored_card, viewGroup, false);

        TextView tvStoreCard = view.findViewById(R.id.tv_credit_card);

        tvStoreCard.setText(storedCards.get(position).getMaskedCardNumber());
        tvStoreCard.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                Bundle mBundle = new Bundle();
                mBundle.putParcelable(PayuConstants.STORED_CARD, storedCards.get(position));
                String bankStatus;

                if (mValueAddedHashMap != null
                        && mValueAddedHashMap.get(storedCards.get(position).getCardBin()) != null
                        && mValueAddedHashMap.get(storedCards.get(position).getCardBin()).getStatusCode() == 0) {

                    bankStatus = storedCards.get(position).getIssuingBank() + " is temporarily down";

                } else {

                    bankStatus = "";
                }
                mBundle.putString(SdkUIConstants.ISSUING_BANK_STATUS, bankStatus);
                mBundle.putInt(SdkUIConstants.POSITION, position);
                mBundle.putSerializable(PayuConstants.ONE_CLICK_CARD_TOKENS, mOneClickCardTokens);
                storedCardSelected.getCardSelected(mBundle);
            }
        });

        return view;
    }

    public interface StoredCardSelected {
        void getCardSelected(Bundle storedCardBundle);
    }
}

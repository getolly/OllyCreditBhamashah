package com.ollycredit.ui.repayments.net_banking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.ollycredit.R;
import com.payu.india.Model.PaymentParams;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NetBankingFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.image_button_axis)
    ImageButton axisImageButton;

    @BindView(R.id.image_button_hdfc)
    ImageButton hdfcImageButton;

    @BindView(R.id.image_button_citi)
    ImageButton citiImageButton;

    @BindView(R.id.image_button_sbi)
    ImageButton sbiImageButton;

    @BindView(R.id.image_button_icici)
    ImageButton iciciImageButton;


    private String bankcode;
    private Spinner spinnerNetbanking;
    private PaymentParams mPaymentParams;
    private ArrayAdapter<String> mAdapter;
    private View rootView;


    private Bundle mBundle;

    public static NetBankingFragment newInstance() {
        return new NetBankingFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_net_banking, container, false);

        ButterKnife.bind(this, rootView);


        axisImageButton.setOnClickListener(this);
        hdfcImageButton.setOnClickListener(this);
        sbiImageButton.setOnClickListener(this);
        iciciImageButton.setOnClickListener(this);

        mPaymentParams = new PaymentParams();


        return rootView;
    }


    @Override
    public void onClick(View view) {
        axisImageButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        hdfcImageButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        sbiImageButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        iciciImageButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        int id = view.getId();

        if (id == R.id.image_button_axis) {
            axisImageButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cyan_main));
            mPaymentParams.setBankCode("AXIB");
        } else if (id == R.id.image_button_hdfc) {
            hdfcImageButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cyan_main));
            mPaymentParams.setBankCode("HDFB");
        } else if (id == R.id.image_button_sbi) {
            sbiImageButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cyan_main));
            mPaymentParams.setBankCode("SBIB");
        } else if (id == R.id.image_button_icici) {
            iciciImageButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cyan_main));
            mPaymentParams.setBankCode("ICIB");
        }
    }

    public PaymentParams getPaymentParams() {
        return mPaymentParams;
    }
}

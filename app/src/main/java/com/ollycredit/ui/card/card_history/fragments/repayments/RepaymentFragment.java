package com.ollycredit.ui.card.card_history.fragments.repayments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.RepaymentFetchedData;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.base.BaseFragment;
import com.ollycredit.ui.card.card_history.repay_credit.amount.RepayAmountActivity;
import com.ollycredit.utils.GlobalConstants;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Payu.Payu;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepaymentFragment extends BaseFragment implements RepaymentView {


    @BindView(R.id.trans_payback)
    View transPaybackView;

    @BindView(R.id.ll_minimum_due_amt)
    LinearLayout llMinimumDueAmt;


    @BindView(R.id.nsv_repayment)
    NestedScrollView nsvScrollTo;

    @BindView(R.id.layout_repay_rv_content)
    LinearLayout repayContentLayout;

    @BindView(R.id.rv_repaymenthistory)
    RecyclerView repaymentHistoryRV;

    RecyclerView repaymentExpandRV;

    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    RepaymentPresenter repaymentPresenter;

    //payback
    private TextView paybackDaysTextView;
    private TextView paybackCreditTextView;
    private Button paybackButton;

    private ImageView viewRupeeSymbol;
    private ImageButton paybackExpandImageButton;
    @BindView(R.id.payback_notify_color)
    LinearLayout paybackNotifyColorFramelayout;

    private LinearLayoutManager mLayoutManager;
    private RepaymentRecycleAdapter mAdapter;
    private RepayExpandRecycleAdapter expandAdapter;
    PaymentParams mPaymentParams;

    //repayment Amount from server
    //boolean getRepaymentAmount=false;
    String amountDue, amountUsed;
    boolean isdefualted = false;
    int daysleft;
    private TextView paybackCreditMinimumTextView;
    private String minAmountDue;


    public RepaymentFragment() {
        // Required empty public constructor
    }

    private boolean isExpanded = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_repayment, container, false);
        ButterKnife.bind(this, itemView);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);

        repaymentPresenter.attachView(this);

        setUp();

        //todo content goes here
        paybackExpandImageButton.setVisibility(View.GONE);


        paybackExpandImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_REPAYMENT,
                                GlobalConstants.ACTION_TOGGLE,
                                GlobalConstants.LABLE_REPAYMENT_RULES);

                ExpandingView();
            }
        });


        return itemView;
    }

    private void ExpandingView() {
        paybackExpandImageButton.setRotation(paybackExpandImageButton.getRotation() + 180);
        if (isExpanded) {
            repaymentExpandRV.setVisibility(View.GONE);
            isExpanded = false;
        } else {
            repaymentExpandRV.setVisibility(View.VISIBLE);
            isExpanded = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...

        OllyCreditApplication.getInstance().trackScreenView(this.getClass().getSimpleName());
        repaymentPresenter.getrepayment(preferencesHelper.getToken());
        repaymentPresenter.getRepaymentDisplayData();

    }


    @Override
    protected void setUp() {

        //payback views init
        paybackDaysTextView = (TextView) transPaybackView.findViewById(R.id.tv_payback_days);
        paybackCreditTextView = (TextView) transPaybackView.findViewById(R.id.tv_payback_credit);
        paybackCreditMinimumTextView = (TextView) transPaybackView.findViewById(R.id.tv_payback_min_credit);
        paybackButton = (Button) transPaybackView.findViewById(R.id.btn_payback);
        paybackExpandImageButton = (ImageButton) transPaybackView.findViewById(R.id.ib_payback_expand);
        viewRupeeSymbol = (ImageView) transPaybackView.findViewById(R.id.viewRupeeSymbol);

        repaymentExpandRV = (RecyclerView) transPaybackView.findViewById(R.id.repay_expand);

        mLayoutManager = new LinearLayoutManager(getActivity());
        repaymentExpandRV.setLayoutManager(mLayoutManager);
        expandAdapter = new RepayExpandRecycleAdapter(getActivity());
        repaymentExpandRV.setAdapter(expandAdapter);

        repaymentExpandRV.setNestedScrollingEnabled(false);
        repaymentHistoryRV.setNestedScrollingEnabled(false);

        repaymentExpandRV.setVisibility(View.VISIBLE);
        paybackExpandImageButton.setRotation(paybackExpandImageButton.getRotation() + 180);

        mPaymentParams = new PaymentParams();
        Payu.setInstance(getContext());

        paybackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_REPAYMENT,
                                GlobalConstants.ACTION_GOTO,
                                GlobalConstants.LABLE_TO_REPAY_AMOUNT);


                Intent intent = new Intent(getContext(), RepayAmountActivity.class);
                intent.putExtra("amountdue", amountDue);
                intent.putExtra("amountused", amountUsed);
                intent.putExtra("amountMin", minAmountDue);
                intent.putExtra("isdefualted", isdefualted);
                intent.putExtra("dayleft", daysleft);
                getContext().startActivity(intent);

                nsvScrollTo.scrollTo(0, 0);

                repaymentExpandRV.setNestedScrollingEnabled(false);

                //getRepaymenttime();

            }
        });
    }


    private void getRepaymenttime() {


        daysleft = preferencesHelper.getBillCounter();
        if (daysleft < 10) {
            SpannableString spannableString = new SpannableString(9 - daysleft + " day[s] left to repay credit");
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 6, 0);
            paybackDaysTextView.setText(spannableString);
            paybackNotifyColorFramelayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green_main));
        } else if (daysleft == 9) {
            daysleft = 0;
            SpannableString spannableString = new SpannableString(daysleft + " day[s] left to repay credit");
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 6, 0);
            paybackDaysTextView.setText(spannableString);
            paybackNotifyColorFramelayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green_main));
        } else {
            SpannableString spannableString = new SpannableString("Pay immediately");
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, spannableString.length(), 0);
            paybackDaysTextView.setText(spannableString);
            paybackNotifyColorFramelayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red_error));
        }


    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void getReqFailed(int failedCode, String message) {

    }

    @Override
    public void getRepaymentSuccess(ResponseModel responseModel) {
        hideProgressDialog();

        amountUsed = responseModel.getAmountUsed();
        amountDue = responseModel.getAmountDue();
        //minAmountDue = String.valueOf((Double.valueOf(amountDue) * 40) / 100);
        minAmountDue = responseModel.getMinAmountDue().toString();
        isdefualted = responseModel.getIsDefaulted();

        //flag to prevent to access to next activity without successful Repayment amount
        // getRepaymentAmount=true;

        int billCounter = preferencesHelper.getBillCounter();


        if (billCounter < 0) {
            paybackNotifyColorFramelayout.setVisibility(View.GONE);
            llMinimumDueAmt.setVisibility(View.GONE);
            paybackCreditTextView.setText(String.valueOf(Double.valueOf(responseModel.
                    getAmountDue())+ Double.valueOf(responseModel.getAmountUsed())));
        } else if (billCounter > 10) {
            paybackCreditTextView.setText(amountDue);

            paybackNotifyColorFramelayout.setBackgroundColor(getResources().getColor(R.color.red_error));
            viewRupeeSymbol.setColorFilter(ContextCompat.getColor(getContext(), R.color.red_error));
            paybackCreditTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.red_error));
        } else {
            paybackCreditTextView.setText(amountDue);
            paybackNotifyColorFramelayout.setBackgroundColor(getResources().getColor(R.color.green_main));
            viewRupeeSymbol.setColorFilter(ContextCompat.getColor(getContext(), R.color.blue_text));
            paybackCreditTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_text));
        }

        paybackCreditMinimumTextView.setText(minAmountDue);
        getRepaymenttime();

//        paybackButton.setEnabled(true);

    }

    private String search(String key, List<RepaymentFetchedData> list) {
        for (RepaymentFetchedData repaymentFetchedData : list) {
            if (repaymentFetchedData.getName().equals(key))
                return repaymentFetchedData.getContent();
        }
        return null;
    }

    @Override
    public void getRepaymentFailed(int failedCode, String message) {

    }

    @Override
    public void getRepaymentDataSuccess(ResponseModel responseModel) {
        List<RepaymentFetchedData> displayDataList = responseModel.getRepaymentDisplayDataList();
        RepaymentFetchedData one = new RepaymentFetchedData();
        one.setName(search("due_text_title", displayDataList));
        one.setContent(search("due_text_content", displayDataList));
        RepaymentFetchedData two = new RepaymentFetchedData();
        two.setName(search("percentage_text_title", displayDataList));
        two.setContent(search("percentage_text_content", displayDataList));
        RepaymentFetchedData three = new RepaymentFetchedData();
        three.setName(search("minimum_due_text_title", displayDataList));
        three.setContent(search("minimum_due_text_content", displayDataList));
        expandAdapter.setdata(one, two, three);
        expandAdapter.notifyDataSetChanged();
    }


}

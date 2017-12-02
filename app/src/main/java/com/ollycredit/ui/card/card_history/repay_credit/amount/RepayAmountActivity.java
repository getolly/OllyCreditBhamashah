package com.ollycredit.ui.card.card_history.repay_credit.amount;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.repayments.RepaymentActivity;
import com.ollycredit.utils.GlobalConstants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class RepayAmountActivity extends BaseActivity {

    @BindView(R.id.cv_payback_container)
    CardView cvPayBackContainer;

    @BindView(R.id.ll_days_left)
    LinearLayout llDaysLeft;

    @BindView(R.id.tv_total_credit_spent)
    TextView tvTotalCreditSpentText;

    @BindView(R.id.tv_payback_amount_spent)
    TextView tvAmountSpend;

    @BindView(R.id.tv_payback_amount_daysleft)
    TextView tvDaysLeft;

    @BindView(R.id.tv_payback_amount_minimum)
    TextView tvMiniAmountTitle;


    @BindView(R.id.til_payback_amount)
    TextInputLayout tilPayBackAmount;

    @BindView(R.id.et_payback_amount)
    EditText etPayBackAmount;

    @BindView(R.id.tv_payback_amount_range)
    TextView tvPayBackAmountRange;

    @BindView(R.id.toolbar_repayamount)
    Toolbar navToolbar;

    @BindView(R.id.ab_repay_amount)
    AppBarLayout appBarLayout;

    @Inject
    PreferencesHelper preferencesHelper;

    @BindView(R.id.iv_total_amt)
    RadioButton rbCTotalAmt;

    @BindView(R.id.iv_min_amt)
    RadioButton rbMinAmt;

    @BindView(R.id.iv_custom_amt)
    RadioButton rbCustomAmt;

    @BindView(R.id.tv_total_amt)
    TextView tvTotalAmount;

    @BindView(R.id.tv_min_amt)
    TextView tvMinAmount;

    @BindView(R.id.cv_min)
    CardView cvMinAmount;


    String amountDue, amountUsed, viewAmount;

    boolean isDefualted;
    int daysleft;
    String minAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_repay);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        setUp();
    }


    @Override
    protected void onResume() {
        super.onResume();
        OllyCreditApplication.getInstance().trackScreenView(this.getClass().getSimpleName());
    }

    @Override
    protected void setUp() {

        setSupportActionBar(navToolbar);

        tvDaysLeft = (TextView) findViewById(R.id.tv_payback_amount_daysleft);
        tvMiniAmountTitle = (TextView) findViewById(R.id.tv_payback_amount_minimum);
        tilPayBackAmount = (TextInputLayout) findViewById(R.id.til_payback_amount);
        cvPayBackContainer = (CardView) findViewById(R.id.cv_payback_container);
        etPayBackAmount = (EditText) findViewById(R.id.et_payback_amount);
        tvPayBackAmountRange = (TextView) findViewById(R.id.tv_payback_amount_range);

        amountDue = getIntent().getStringExtra("amountdue");
        amountUsed = getIntent().getStringExtra("amountused");
        minAmount = getIntent().getStringExtra("amountMin");
        isDefualted = getIntent().getBooleanExtra("isdefualted", true);
        daysleft = getIntent().getIntExtra("dayleft", 0);

        int billCounter = preferencesHelper.getBillCounter();
        if (billCounter < 0) {
            cvPayBackContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            tvAmountSpend.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvTotalCreditSpentText.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvTotalCreditSpentText.setText("Total credit used");
            llDaysLeft.setVisibility(View.GONE);
            cvMinAmount.setVisibility(View.GONE);
            viewAmount = String.valueOf(Double.parseDouble(amountUsed)+Double.parseDouble(amountDue));
        } else if (billCounter <= 10) {
            viewAmount = amountDue;
            cvMinAmount.setVisibility(View.VISIBLE);
            tvTotalCreditSpentText.setText("Total credit spent");
            cvPayBackContainer.setBackgroundColor(getResources().getColor(R.color.green_main));
        } else {
            viewAmount = amountDue;
            cvMinAmount.setVisibility(View.VISIBLE);
            tvTotalCreditSpentText.setText("Total credit spent");
            cvPayBackContainer.setBackgroundColor(getResources().getColor(R.color.red_error));
        }

        tvAmountSpend.setText("\u20B9 " + viewAmount);
        getRepaymenttime();

        //minAmount = (Double.parseDouble(viewAmount) * 40) / 100;
        //minAmount = (Double.parseDouble(viewAmount) * 40) / 100;
        tvPayBackAmountRange.setText("Between (\u20B9" + minAmount + ") - " +
                "(\u20B9" + viewAmount + ")");
        tvTotalAmount.setText("\u20B9 " + viewAmount);
        tvMinAmount.setText("\u20B9 " + minAmount);

    }

    private void getRepaymenttime() {


        daysleft = preferencesHelper.getBillCounter();
        if (daysleft < 10) {
            SpannableString spannableString = new SpannableString(9 - daysleft + " day[s] left to repay credit");
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 6, 0);
            tvDaysLeft.setText(spannableString);
        } else if (daysleft == 9) {
            daysleft = 0;
            SpannableString spannableString = new SpannableString(daysleft + " day[s] left to repay credit");
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 6, 0);
            tvDaysLeft.setText(spannableString);
        } else {
            SpannableString spannableString = new SpannableString("Pay immediately");
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, spannableString.length(), 0);
            tvDaysLeft.setText(spannableString);
        }


    }


    @OnClick({R.id.iv_total_amt, R.id.cv_total})
    public void ivTotal() {
        rbCTotalAmt.setChecked(true);
        rbCustomAmt.setChecked(false);
        rbMinAmt.setChecked(false);
    }

    @OnClick({R.id.iv_min_amt, R.id.cv_min})
    public void ivMin(View view) {

        rbCustomAmt.setChecked(false);
        rbCTotalAmt.setChecked(false);
        rbMinAmt.setChecked(true);


    }

    @OnClick({R.id.iv_custom_amt, R.id.cv_custom, R.id.et_payback_amount})
    @OnTextChanged(R.id.et_payback_amount)
    public void ivCustom() {
        rbCTotalAmt.setChecked(false);
        rbCustomAmt.setChecked(true);
        rbMinAmt.setChecked(false);
    }

    @OnClick(R.id.btn_proceed)
    public void onProceedClicked() {

        Double maxPayAmount=0.0;

        if(preferencesHelper.getBillCounter()<0){
            maxPayAmount=Double.parseDouble(amountUsed)+Double.parseDouble(amountDue);
        }
        else if(preferencesHelper.getBillCounter()>=0) {
            maxPayAmount=Double.parseDouble(amountDue);
        }

        String amount;
        if (rbCTotalAmt.isChecked()) {
            amount = tvTotalAmount.getText().toString().split(" ")[1];
        } else if (rbMinAmt.isChecked()) {
            amount = tvMinAmount.getText().toString().split(" ")[1];
        } else if (rbCustomAmt.isChecked()) {

            amount = etPayBackAmount.getText().toString();

            if (amount.isEmpty()) {
                Toast.makeText(this, "Please enter amount.", Toast.LENGTH_SHORT).show();
                etPayBackAmount.requestFocus();
                return;
            }

            Log.i("f", "onProceedClicked: "+minAmount);
            Log.i("f", "onProceedClicked: "+amount);
            Log.i("f", "onProceedClicked: "+maxPayAmount);
            Log.i("f", "onProceedClicked: "+preferencesHelper.getBillCounter());
            Log.i("f", "onProceedClicked: "+amountDue);
            Log.i("f", "onProceedClicked: "+viewAmount);
            if (Double.parseDouble(amount) < Double.parseDouble(minAmount) || Double.parseDouble(amount) > maxPayAmount){
                Toast.makeText(this, "Amount should be Between \u20B9 " + minAmount + " and " +
                        "\u20B9 " + viewAmount + "", Toast.LENGTH_SHORT).show();
                etPayBackAmount.requestFocus();
                return;
            }

        } else {
            Toast.makeText(this, "Please choose a payment option", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(RepayAmountActivity.this, RepaymentActivity.class);
        intent.putExtra("viewamount", viewAmount);
        intent.putExtra("actualpay", amount);
        startActivity(intent);
    }

    private boolean isValid(EditText editText) {
        if (TextUtils.isEmpty(editText.getText().toString().trim())) {
            editText.setError(getString(R.string.invalid_entry));
            return false;
        }
        return true;
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_REPAYMENT,
                GlobalConstants.ACTION_BACK,
                GlobalConstants.LABLE_TO_REPAYMENT
        );
    }

}

package com.ollycredit.ui.card.verify.debit.debit_register;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ollycredit.BuildConfig;
import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.PayUModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.dialogs.CreditIncDialog;
import com.ollycredit.utils.helpers.HelperClass;
import com.ollycredit.utils.helpers.TextFormatHelper;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.payuui.Activity.PayUBaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ollycredit.utils.GlobalConstants.CATEGORY_VERIFY_DOCUMENTS;

public class VerifyBankActivity extends BaseActivity implements VerifyBankView {

    @BindView(R.id.layout_bank_reg_avail)
    LinearLayout layoutBankRegAvail;

    @BindView(R.id.layout_bank_reg_error)
    LinearLayout layoutBankRegError;

    @BindView(R.id.fl_loadingview)
    FrameLayout loadingView;


    @BindView(R.id.tv_verify_bank_title)
    TextView tvTitle;

    @BindView(R.id.toolbar_bank_reg)
    Toolbar navToolbar;

    @BindView(R.id.btn_bank_reg_verify)
    Button verifyButton;

    @BindView(R.id.ctb_bank)
    CollapsingToolbarLayout ctbToolbar;

    PaymentParams mPaymentParams;
    PayUModel payUModel;
    private PayuConfig payuConfig;
    private String merchantKey, userCredentials;
    private static String successUrl = BuildConfig.BASE_URL + "api/payment/success";
    private static String failureUrl = BuildConfig.BASE_URL + "api/payment/fail";


    // Used when generating hash from SDK
    private PayUChecksum checksum;

    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    VerifyBankPresenter verifyBankPresenter;

    private String amount = "3500";
    private BroadcastReceiver networkReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_register);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        //appointKycPresenter.attachView(this);

        setUp();
    }

    @OnClick(R.id.btn_bank_reg_verify)
    public void verifyButtonClicked() {


        OllyCreditApplication.getInstance().trackEvent(
                CATEGORY_VERIFY_DOCUMENTS,
                GlobalConstants.ACTION_VERIFY,
                GlobalConstants.LABLE_VERIFY_DEBIT
        );

        mPaymentParams = new PaymentParams();
        payUModel = new PayUModel();
        Payu.setInstance(this);

        loadingView.setVisibility(View.VISIBLE);
//        showProgressDialog("Please wait...");
        makePayment();


    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideErrorConnection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        OllyCreditApplication.getInstance().trackScreenView(this.getClass().getSimpleName());
    }

    @Override
    protected void setUp() {

        verifyBankPresenter.attachView(this);
        setSupportActionBar(navToolbar);
        ctbToolbar.setTitle(navToolbar.getTitle());
        ctbToolbar.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(VerifyBankActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }
            }
        };

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_VERIFY_DOCUMENTS,
                GlobalConstants.ACTION_BACK,
                GlobalConstants.LABLE_TO_INCREASE_LIMIT
        );
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }


    private void makePayment() {


        merchantKey = "gtKFFx";
        String email = "momen@getolly.com";
        //userCredentials = "default";
        userCredentials = merchantKey + ":" + email;

        //server olly
        payUModel.setKey(merchantKey);
        payUModel.setAmount("1");
        payUModel.setProductinfo("Repayment");
        payUModel.setFirstname("momen");
        payUModel.setEmail(email);
        payUModel.setTxnid("" + System.currentTimeMillis());
        payUModel.setUdf1(String.valueOf(preferencesHelper.getUserId()));
        payUModel.setUdf2("debit_card_link");
        payUModel.setUdf3("udf3");
        payUModel.setUdf4("udf4");
        payUModel.setUdf5("udf5");
        payUModel.setUserCredentials(userCredentials == null ? PayuConstants.DEFAULT : userCredentials);

        mPaymentParams.setKey(payUModel.getKey());
        mPaymentParams.setAmount(payUModel.getAmount());
        mPaymentParams.setProductInfo(payUModel.getProductinfo());
        mPaymentParams.setFirstName(payUModel.getFirstname());
        mPaymentParams.setEmail(payUModel.getEmail());
        mPaymentParams.setTxnId(payUModel.getTxnid());
        mPaymentParams.setSurl(successUrl);
        mPaymentParams.setFurl(failureUrl);
        mPaymentParams.setUdf1(payUModel.getUdf1());
        mPaymentParams.setUdf2(payUModel.getUdf2());
        mPaymentParams.setUdf3(payUModel.getUdf3());
        mPaymentParams.setUdf4(payUModel.getUdf4());
        mPaymentParams.setUdf5(payUModel.getUdf5());
        mPaymentParams.setUserCredentials(payUModel.getUserCredentials());


        // for offer_key
        if (null != mPaymentParams.getOfferKey()) {
            mPaymentParams.setOfferKey(mPaymentParams.getOfferKey());
        }
        System.out.print("hii" + new Gson().toJson(mPaymentParams));

        verifyBankPresenter.getHash(payUModel);
    }


    @Override
    public void getHashSuccess(ResponseModel responseModel) {

        //hideProgressDialog();
        loadingView.setVisibility(View.GONE);
        //mPaymentParams.setHash(responseModel.getPmtHash());
        PayuHashes payuHashes = new PayuHashes();
        payuHashes.setVasForMobileSdkHash(responseModel.getVasForMobileSdkHash());
        payuHashes.setPaymentRelatedDetailsForMobileSdkHash(responseModel.getPaymentRelatedDetailsForMobileSdkHash());
        payuHashes.setPaymentHash(responseModel.getPaymentHash());
        payuHashes.setDeleteCardHash(responseModel.getDeleteUserCardHash());
        payuHashes.setStoredCardsHash(responseModel.getGetUserCardsHash());
        payuHashes.setEditCardHash(responseModel.getEditUserCardHash());
        payuHashes.setSaveCardHash(responseModel.getSaveUserCardHash());
        payuHashes.setCheckOfferStatusHash(responseModel.getCheckOfferStatusHash());

        payuConfig = new PayuConfig();
        payuConfig.setEnvironment(PayuConstants.STAGING_ENV);
        Intent intent = new Intent(this, PayUBaseActivity.class);
        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
        intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);
        startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }


    @Override
    public void veriBankFailed(int failedCode, String message) {

        showDialogBox("Verification failed");
        loadingView.setVisibility(View.GONE);
        //hideProgressDialog();
    }

    @Override
    public void getUserSuccess(ResponseModel responseModel) {

        //hideProgressDialog();
        loadingView.setVisibility(View.GONE);

        if (responseModel.getCreditLimitFlags().getIsCardLinked() == 1) {

            amount = TextFormatHelper.indianRupeesFormat((double) (responseModel.getUser().getCreditLimit()));

            CreditIncDialog creditIncDialog = new CreditIncDialog(this, amount, R.drawable.img_happy);
            creditIncDialog.setCancelable(false);
            creditIncDialog.show();


            preferencesHelper.putInt(GlobalConstants.PREF_PAN_VERIFIED, 1);
            //preferencesHelper.putString(GlobalUtils.PREF_BAL, responseModel.getUser().get());
            //preferencesHelper.putString(GlobalUtils.PREF_USED, "\u20B9 " + (responseModel.getUser().getCreditLimit() - Double.parseDouble(responseModel.getUser().ge())));
            preferencesHelper.putLong(GlobalConstants.PREF_MAX, responseModel.getUser().getMaxCreditLimit());
            preferencesHelper.putLong(GlobalConstants.PREF_C_LIMIT, responseModel.getUser().getCreditLimit());
        } else {
            layoutBankRegError.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                showDialogBox("Please wait...");

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        verifyBankPresenter.getUserInfo(preferencesHelper.getToken());

                    }
                }, 2000);


            }
            if (resultCode == Activity.RESULT_CANCELED) {


                //Write your code if there's no result
            }
        }
    }//onActivi


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.feedback_icon);

        if (menuItem != null) {
            HelperClass.tintMenuIcon(this, menuItem, R.color.white);
        }

        return true;
    }

}

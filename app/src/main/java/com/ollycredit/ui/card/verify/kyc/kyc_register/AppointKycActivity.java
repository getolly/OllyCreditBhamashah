package com.ollycredit.ui.card.verify.kyc.kyc_register;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.kyc.KycRequestModel;
import com.ollycredit.api.model.kyc.KycResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.card.verify.kyc.appoint_confirm.AppointConfirmActivity;
import com.ollycredit.ui.card.verify.kyc.appoint_time.AppointTimeActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.EventBusHelper;
import com.ollycredit.utils.helpers.HelperClass;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointKycActivity extends BaseActivity implements AppointKycView {

    @BindView(R.id.toolbar_kyc)
    Toolbar navToolbar;

    @BindView(R.id.ctb_kyc)
    CollapsingToolbarLayout ctbToolbar;

    @BindView(R.id.btn_kyc_register)
    Button submitRegister;

    @BindView(R.id.fl_loadingview)
    FrameLayout loadingView;

    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    AppointKycPresenter appointKycPresenter;

    private KycRequestModel kycRequestModel;
    private BroadcastReceiver networkReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_kyc);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        appointKycPresenter.attachView(this);

        setUp();

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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.btn_kyc_register)
    public void validateOnclick() {
        //showProgressDialog();
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_CARD_HISTORY,
                GlobalConstants.ACTION_GOTO,
                GlobalConstants.LABLE_TO_CONFIRM_EKYC
        );
        loadingView.setVisibility(View.VISIBLE);
        kycRequestModel = new KycRequestModel();
        kycRequestModel.setToken(preferencesHelper.getToken());
        appointKycPresenter.getAppointInfo(kycRequestModel);

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
        setSupportActionBar(navToolbar);
        ctbToolbar.setTitle(navToolbar.getTitle());
        ctbToolbar.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(AppointKycActivity.this);
                if (isOnline){
                    hideErrorConnection();
                }else {
                    showErrorConnection();
                }
            }
        };

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void reqSuccess(KycResponseModel responseModel) {
        preferencesHelper.saveToken(responseModel.getToken());

        kycRequestModel.setToken(responseModel.getToken());
        kycRequestModel.setAppointKYCDateOne(responseModel.getAppointment().getDate1());
        kycRequestModel.setAppointKYCDateTwo(responseModel.getAppointment().getDate2());
        kycRequestModel.setAppointKYCTime(responseModel.getAppointment().getTime());
        kycRequestModel.setAppointKYCAddressOne(responseModel.getAppointment().getAddress1());
        kycRequestModel.setAppointKYCAddressTwo(responseModel.getAppointment().getAddress2());
        kycRequestModel.setAppointKYCPhone(responseModel.getAppointment().getMobileNum());
        kycRequestModel.setAppointKYCPin(String.valueOf(responseModel.getAppointment().getPincodeId()));

        EventBus.getDefault().postSticky(new EventBusHelper(kycRequestModel));

        //hideProgressDialog();
        loadingView.setVisibility(View.GONE);

        startActivity(new Intent(AppointKycActivity.this, AppointConfirmActivity.class));
        finish();
    }

    @Override
    public void reqFailed(int errorCode, String message) {
        //hideProgressDialog();
        loadingView.setVisibility(View.GONE);
        if (errorCode == GlobalConstants.NETWORK_REQUEST_CALL_FAILED) {
            showDialogBox(message);
        }

        if (errorCode == GlobalConstants.NETWORK_REQUEST_APPOINT_KYC) {
            EventBus.getDefault().postSticky(new EventBusHelper(new KycRequestModel()));
            startActivity(new Intent(AppointKycActivity.this, AppointTimeActivity.class));
            finish();
        }

        if (errorCode == GlobalConstants.NETWORK_REQUEST_VERIFY_OTP) {
            showDialogBox(message);
        }


    }

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

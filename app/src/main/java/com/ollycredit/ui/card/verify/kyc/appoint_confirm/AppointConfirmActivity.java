package com.ollycredit.ui.card.verify.kyc.appoint_confirm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.kyc.KycRequestModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.card.card_home.CardHomeActivity;
import com.ollycredit.ui.card.verify.kyc.appoint_time.AppointTimeActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.EventBusHelper;
import com.ollycredit.utils.helpers.HelperClass;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointConfirmActivity extends BaseActivity implements AppointConfirmView {


    @BindView(R.id.tv_appoint_confirm_date)
    TextView dateTextText;
    @BindView(R.id.tv_appoint_confirm_time)
    TextView timeTextText;
    @BindView(R.id.tv_appoint_confirm_location)
    TextView locationTextText;
    @BindView(R.id.tv_appoint_confirm_phone)
    TextView phoneTextText;

    @BindView(R.id.btn_appoint_confirm_reschedule)
    TextView rescheduleButton;
    @BindView(R.id.btn_appoint_confirm_contactolly)
    TextView contactOllyButton;

    @BindView(R.id.ctb_kyc)
    CollapsingToolbarLayout ctbToolbar;


    @BindView(R.id.toolbar_appoint_confirm)
    Toolbar navToolbar;

    @Inject
    AppointConfirmPresenter appointConfirmPresenter;

    @Inject
    PreferencesHelper preferencesHelper;

    KycRequestModel kycRequestModel;
    private BroadcastReceiver networkReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_appoint_confirm);
        getActivityComponent().inject(AppointConfirmActivity.this);
        ButterKnife.bind(AppointConfirmActivity.this);
        appointConfirmPresenter.attachView(this);
        setUp();

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(networkReceiver);
    }


    @Override
    protected void onResume() {
        super.onResume();
        OllyCreditApplication.getInstance().trackScreenView(this.getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideErrorConnection();
    }

    @OnClick(R.id.btn_appoint_confirm_reschedule)
    public void OnReshedule() {

        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_VERIFY_DOCUMENTS,
                GlobalConstants.ACTION_BUTTON_CLICKED,
                GlobalConstants.LABLE_TO_CONFIRM_EKYC
        );

        kycRequestModel.setReschedule(true);
        EventBus.getDefault().postSticky(new EventBusHelper(kycRequestModel));
        startActivity(new Intent(AppointConfirmActivity.this, AppointTimeActivity.class));
        finish();
    }

    @OnClick(R.id.btn_appoint_confirm_contactolly)
    public void OnContactOlly() {
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_VERIFY_DOCUMENTS,
                GlobalConstants.ACTION_BUTTON_CLICKED,
                GlobalConstants.LABLE_TO_CUSTOMER_CARE
        );

        Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void setUp() {
        setSupportActionBar(navToolbar);
        ctbToolbar.setTitle(navToolbar.getTitle());
        ctbToolbar.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);
        showLoadingView();

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(AppointConfirmActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }
            }
        };

    }

    public void validateOnclick() {
        startActivity(new Intent(this, CardHomeActivity.class));
        finish();
    }

    @Subscribe(priority = 1, threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventBusHelper eventBusHelper) {
        kycRequestModel = eventBusHelper.getKycRequestModel();
        Log.e("Create pin", new Gson().toJson(kycRequestModel));
        updateFields();

    }

    private void updateFields() {

        String finalDate = kycRequestModel.getAppointKYCDateOne() + " - " + kycRequestModel.getAppointKYCDateTwo();

        dateTextText.setText(finalDate);
        timeTextText.setText(kycRequestModel.getAppointKYCTime());
        locationTextText.setText(kycRequestModel.getAppointKYCAddressOne());
        phoneTextText.setText(kycRequestModel.getAppointKYCPhone());

        hideLoadingView();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_VERIFY_DOCUMENTS,
                GlobalConstants.ACTION_BACK,
                GlobalConstants.LABLE_TO_START_EKYC
        );
    }



    @Override
    public boolean onSupportNavigateUp() {
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_VERIFY_DOCUMENTS,
                GlobalConstants.ACTION_BACK,
                GlobalConstants.LABLE_TO_START_EKYC
        );
        validateOnclick();
        return true;
    }

    @Override
    public void showProgress() {

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


    @Override
    public void hideProgress() {

    }
}

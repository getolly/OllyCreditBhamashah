package com.ollycredit.ui.card.verify.kyc.appoint_address;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.gson.Gson;
import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.api.model.kyc.KycRequestModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.card.verify.kyc.appoint_confirm.AppointConfirmActivity;
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

public class AppointAddressActivity extends BaseActivity implements AppointAddressView, OnFocusChangeListener {


    @BindView(R.id.til_appoint_pin)
    TextInputLayout tilPin;
    @BindView(R.id.til_appoint_address_one)
    TextInputLayout tilAddressMain;
    @BindView(R.id.til_appoint_address_two)
    TextInputLayout tilAddressOpt;
    @BindView(R.id.til_appoint_address_contact)
    TextInputLayout tilContactNo;


    @BindView(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;


    @BindView(R.id.edit_appoint_address_pin)
    EditText pinEditText;

    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScrollView;
    @BindView(R.id.edit_appoint_address_one)
    EditText addressOneEditText;
    @BindView(R.id.edit_appoint_address_two)
    EditText addressTwoEditText;
    @BindView(R.id.edit_appoint_address_contact)
    EditText contactEditText;

    @BindView(R.id.btn_appoint_address_submit)
    FloatingActionButton submitFAB;

    @BindView(R.id.toolbar_appoint_address)
    Toolbar navToolbar;

    @BindView(R.id.ctb_kyc)
    CollapsingToolbarLayout ctbToolbar;


    @Inject
    AppointAddressPresenter appointAddressPresenter;

    @Inject
    PreferencesHelper preferencesHelper;

    private KycRequestModel kycRequestModel;
    private BroadcastReceiver networkReceiver;
    private EditText[] fieldEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_address_kyc);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        appointAddressPresenter.attachView(this);
        setUp();

    }

    public void showFABprogress(EditText[] inputFields) {
        for (EditText field : inputFields) {
            field.setEnabled(false);
        }
        fabProgressCircle.show();
    }

    public void hideFABprogress(EditText[] inputFields) {
        for (EditText field : inputFields) {
            field.setEnabled(true);
        }
        fabProgressCircle.hide();
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

    @Subscribe(priority = 1, threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventBusHelper eventBusHelper) {
        kycRequestModel = eventBusHelper.getKycRequestModel();
        Log.e("Create pin", new Gson().toJson(kycRequestModel));
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideErrorConnection();
    }

    @Override
    protected void setUp() {
        setSupportActionBar(navToolbar);
        ctbToolbar.setTitle(navToolbar.getTitle());
        ctbToolbar.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);
        pinEditText.setOnFocusChangeListener(this);
        addressOneEditText.setOnFocusChangeListener(this);
        contactEditText.setOnFocusChangeListener(this);

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(AppointAddressActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        OllyCreditApplication.getInstance().trackScreenView(this.getClass().getSimpleName());
    }

    @OnClick(R.id.btn_appoint_address_submit)
    public void validateOnclick() {

        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_VERIFY_DOCUMENTS,
                GlobalConstants.ACTION_BUTTON_CLICKED,
                GlobalConstants.LABLE_VERIFY_EKYC
        );

        fieldEditText = new EditText[]{pinEditText, addressOneEditText, contactEditText};
        TextInputLayout[] TextInputs = {tilPin, tilAddressMain, tilContactNo};

        if (validate(fieldEditText, TextInputs)) {

            showFABprogress(fieldEditText);

            String pinString = pinEditText.getText().toString().trim();
            String addOneString = addressOneEditText.getText().toString().trim();
            String addTwoString = addressTwoEditText.getText().toString().trim();
            String contactString = contactEditText.getText().toString().trim();

            preferencesHelper.putBoolean(GlobalConstants.PREF_EKYC_SCHEDULED, true);
            kycRequestModel.setToken(preferencesHelper.getToken());
            kycRequestModel.setAppointKYCPin(pinString);
            kycRequestModel.setAppointKYCAddressOne(addOneString);
            kycRequestModel.setAppointKYCAddressTwo("not available");
            kycRequestModel.setAppointKYCPhone(contactString);

            Log.e("appoint time", new Gson().toJson(kycRequestModel));

            fabProgressCircle.show();
            if (kycRequestModel.isReschedule()) {
                appointAddressPresenter.updateAppointment(kycRequestModel);
            } else {
                appointAddressPresenter.createAppointment(kycRequestModel);
            }


        }

    }


    private boolean validate(EditText[] editTexts, TextInputLayout[] inputLayouts) {

        for (int i = 0; i < editTexts.length; i++) {


            if (editTexts[i].getText().toString().trim().isEmpty()) {
                inputLayouts[i].setError(getString(R.string.empty_field));
                return false;
            }

            if (editTexts[i].getTag().toString().equalsIgnoreCase("pin")) {

                if (editTexts[i].getText().toString().trim().length() != 6) {
                    inputLayouts[i].setError(getString(R.string.invalid_entry));
                    return false;
                }

            }

            if (editTexts[i].getTag().toString().contains("contact")) {
                if (editTexts[i].getText().toString().trim().length() != 10) {
                    inputLayouts[i].setError(getString(R.string.invalid_entry));
                    return false;
                }
            }

            inputLayouts[i].setError(null);
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_VERIFY_DOCUMENTS,
                GlobalConstants.ACTION_BACK,
                GlobalConstants.LABLE_TO_APPOINT_EKYC_DATETIME
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }


    @Override
    public void reqSuccess(ResponseModel responseModel) {
        hideFABprogress(fieldEditText);

        preferencesHelper.saveToken(responseModel.getToken());
        kycRequestModel.setToken(responseModel.getToken());
        EventBus.getDefault().postSticky(new EventBusHelper(kycRequestModel));
        startActivity(new Intent(AppointAddressActivity.this, AppointConfirmActivity.class));
        finish();
    }

    @Override
    public void reqFailed(int errorCode, String message) {
        hideFABprogress(fieldEditText);
        if (errorCode == GlobalConstants.NETWORK_REQUEST_CALL_FAILED) {
            showDialogBox(message);
        }

        if (errorCode == GlobalConstants.NETWORK_REQUEST_APPOINT_KYC) {
            showDialogBox(message);
        }

        if (errorCode == GlobalConstants.NETWORK_REQUEST_VERIFY_OTP) {
            showDialogBox(message);
        }


    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.edit_appoint_address_pin:
                if (pinEditText.hasFocus()) {
                    nestedScrollView.scrollTo(0, addressOneEditText.getTop());
                    tilPin.setError(null);
                }
                if (!hasFocus) {
                    EditText[] editTexts = {pinEditText};
                    TextInputLayout[] inputLayouts = {tilPin};
                    validate(editTexts, inputLayouts);
                }
                break;
            case R.id.edit_appoint_address_one:
                if (addressOneEditText.hasFocus()) {
                    nestedScrollView.scrollTo(0, contactEditText.getBottom());
                    tilAddressMain.setError(null);
                }
                if (!hasFocus) {
                    EditText[] editTexts = {addressOneEditText};
                    TextInputLayout[] inputLayouts = {tilAddressMain};
                    validate(editTexts, inputLayouts);
                }
                break;
            case R.id.edit_appoint_address_contact:
                if (contactEditText.hasFocus()) {
                    tilContactNo.setError(null);
                }
                if (!hasFocus) {
                    EditText[] editTexts = {contactEditText};
                    TextInputLayout[] inputLayouts = {tilContactNo};
                    validate(editTexts, inputLayouts);
                }
                break;

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

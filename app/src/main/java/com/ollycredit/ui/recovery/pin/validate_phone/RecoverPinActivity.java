package com.ollycredit.ui.recovery.pin.validate_phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.gson.Gson;
import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.RequestModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.recovery.pin.validate_user.ValidateUserActivity;
import com.ollycredit.utils.DialogInteractor.OtpDialogInteractor;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.dialogs.ChangeOtpPhoneDialog;
import com.ollycredit.utils.dialogs.OTPVerifyDialog;
import com.ollycredit.utils.helpers.EventBusHelper;
import com.ollycredit.utils.helpers.HelperClass;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecoverPinActivity extends BaseActivity
        implements RecoverPinView,
        ChangeOtpPhoneDialog.NewNumbCallback,
        OTPVerifyDialog.OTPVerifyResult {


    @BindView(R.id.edit_recover_pin_userPhone)
    EditText etUserPhone;

    @BindView(R.id.toolbar_recover_pin)
    Toolbar navToolbar;

    @BindView(R.id.fab_recover_pin_submitNumb)
    FloatingActionButton recoverPinFAB;

    @BindView(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;


    @BindView(R.id.tilPhoneTwo)
    TextInputLayout tilPhoneTwo;

    @BindView(R.id.til_phone)
    TextInputLayout tilPhone;

    @BindView(R.id.iv_phone)
    ImageView ivPhone;


    @Inject
    RecoverPinPresenter recoverPinPresenter;

    @Inject
    PreferencesHelper preferencesHelper;

    private String phoneNumb;
    private RequestModel mRequestModel;
    private BroadcastReceiver smsReceiver;

    private OTPVerifyDialog otpDialog;
    private OtpDialogInteractor otpDialogInteractor;
    private BroadcastReceiver networkReceiver;
    private EditText[] editTexts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_pin);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        recoverPinPresenter.attachView(this);

        setUp();
    }

    @Override
    protected void onDestroy() {
        otpDialog = null;
        super.onDestroy();
    }


    @Override
    protected void setUp() {

        otpDialog = new OTPVerifyDialog(this);
        otpDialogInteractor = otpDialog;

        setSupportActionBar(navToolbar);
        navToolbar.setTitle(" ");

        mRequestModel = new RequestModel();

        etUserPhone.requestFocus();

        etUserPhone.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateInput(etUserPhone.getText().toString().trim());
                }
            }
        });

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(RecoverPinActivity.this);
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
//        OllyCreditApplication.getInstance().trackEvent(
//                GlobalConstants.CATEGORY_ONBOARDING,
//                GlobalConstants.ACTION_GOTO,
//                GlobalConstants.LABLE_TO_HOME
//        );
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private boolean validateInput(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            tilPhoneTwo.setError(getString(R.string.empty_field));
            tilPhone.setError("         ");
            ivPhone.setColorFilter(ContextCompat.getColor(this, R.color.red_error));
            return false;
        }

        if (phoneNumber.length() != 10) {
            tilPhoneTwo.setError("Please enter a valid 10 digit phone number");
            tilPhone.setError("         ");
            ivPhone.setColorFilter(ContextCompat.getColor(this, R.color.red_error));
            return false;
        }

        tilPhoneTwo.setError(null);
        tilPhone.setError(null);
        ivPhone.setColorFilter(ContextCompat.getColor(this, R.color.cyan_main));
        return true;
    }


    @OnClick(R.id.fab_recover_pin_submitNumb)
    public void onFabPressed() {

        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_RECOVER_CREDENTIALS,
                GlobalConstants.ACTION_RESET,
                GlobalConstants.LABLE_RECOVER_PIN
        );
        phoneNumb = etUserPhone.getText().toString().trim();

        if (validateInput(phoneNumb)) {

            editTexts = new EditText[]{etUserPhone};
            //todo validation phone number
            showFABprogress(editTexts);
            sendPhoneValidationReq(phoneNumb);

        }


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


    private void sendPhoneValidationReq(String phone) {

        showFABprogress(editTexts);
        this.phoneNumb = phone;
        mRequestModel.setPhone("+91" + phone);
        //TODO : check for existing phone
        recoverPinPresenter.checkUser(mRequestModel);

    }


    private void showVerifyDialog(String phoneNumb) {

        this.phoneNumb = phoneNumb;
        otpDialog.setCancelable(false);
        otpDialog.show();
        otpDialogInteractor.sendPhoneNo(phoneNumb);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        LocalBroadcastManager.getInstance(this).registerReceiver(smsReceiver, new IntentFilter(GlobalConstants.OTP_ACTION_VALUE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideErrorConnection();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(networkReceiver);
        LocalBroadcastManager.getInstance(this).registerReceiver(smsReceiver, new IntentFilter(GlobalConstants.OTP_ACTION_VALUE));
    }

    @Subscribe(priority = 1, threadMode = ThreadMode.MAIN, sticky = true)
    public void onSmsReciveEvent(String otp) {
        otpDialogInteractor.sendOtp(otp);
        otpDialogInteractor.btnOtpReset();

    }


    @Override
    public void isOTPValid(String otp) {

        showFABprogress(editTexts);
        mRequestModel.setPin(otp);
        Log.e("request model ", new Gson().toJson(mRequestModel));
        recoverPinPresenter.verifyOTP(mRequestModel);
    }

    @Override
    public void resetNumber(String result) {
        sendPhoneValidationReq(result);
    }

    @Override
    public void resentOTPReq(String phoneNumb) {
        sendPhoneValidationReq(phoneNumb);
    }

    @Override
    public void otpReqSuccess(ResponseModel responseModel) {
        showFABprogress(editTexts);
        recoverPinPresenter.getUserInfo(responseModel.getToken());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.feedback_icon);

        if (menuItem != null) {
            HelperClass.tintMenuIcon(this, menuItem, R.color.colorPrimary);
        }

        return true;
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }


    @Override
    public void ifNewUser(ResponseModel responseModel) {

        fabProgressCircle.hide();
        if (responseModel.getStatus().equals(GlobalConstants.NETWORK_RESPONSE_FAILED)) {
            preferencesHelper.saveToken(responseModel.getToken());
            mRequestModel.setToken(responseModel.getToken());
            mRequestModel.setNewLoginflag(true);

            recoverPinPresenter.loginOldUser(mRequestModel);

        } else {
            hideFABprogress(editTexts);
            showDialogBox("We don't have an account associated with this number.");
        }

    }


    @Override
    public void reqFailed(int failedCode, String message) {
        hideFABprogress(editTexts);
        if (failedCode == GlobalConstants.NETWORK_REQUEST_CALL_FAILED) {
            showDialogBox(message);
        }
        if (failedCode == GlobalConstants.NETWORK_REQUEST_CREATE_USER) {
            showDialogBox(message);
        }

        if (failedCode == GlobalConstants.NETWORK_REQUEST_VERIFY_OTP) {
            showDialogBox(message);
        }
    }


    @Override
    public void loginReqSuccess(ResponseModel responseModel) {


        showVerifyDialog(phoneNumb);
        hideFABprogress(editTexts);
        preferencesHelper.saveToken(responseModel.getToken());
        mRequestModel.setToken(responseModel.getToken());

    }

    @Override
    public void getUserSuccess(ResponseModel responseModel) {


        hideFABprogress(editTexts);
        Log.e("otp verify", new Gson().toJson(responseModel));
        Intent i = new Intent(RecoverPinActivity.this, ValidateUserActivity.class);
        preferencesHelper.putString(GlobalConstants.PREF_USER_NAME_KEY, responseModel.getUser().getFirstName() + " " + responseModel.getUser().getLastName());
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("token", responseModel.getToken());
        preferencesHelper.saveToken(responseModel.getToken());
        finish();

        EventBus.getDefault().postSticky(new EventBusHelper(mRequestModel));
        startActivity(i);
    }

}

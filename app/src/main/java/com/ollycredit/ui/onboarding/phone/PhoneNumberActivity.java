package com.ollycredit.ui.onboarding.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.RequestModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.onboarding.landing_page.LandingPage;
import com.ollycredit.ui.onboarding.signup.create_pin.CreatePinActivity;
import com.ollycredit.ui.onboarding.signup.user_details.step_one.UserRegistrationActivity;
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

import static com.ollycredit.utils.GlobalConstants.APP_FLOW_CLASSIC;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_OPERATIONAL;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_PREPAID;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_PRE_USER;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_SELECT;
import static com.ollycredit.utils.GlobalConstants.HACKATHON_APP_FLOW;

public class PhoneNumberActivity extends BaseActivity implements PhoneView, ChangeOtpPhoneDialog.
        NewNumbCallback, OTPVerifyDialog.OTPVerifyResult {

    @BindView(R.id.text_phone_progress)
    TextView progressTitleTextView;

    @BindView(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;

    @BindView(R.id.edit_phone_userPhone)
    EditText userPhoneEditText;

    @BindView(R.id.tilPhoneTwo)
    TextInputLayout tilPhoneTwo;

    @BindView(R.id.toolbar_phone)
    Toolbar navToolbar;

    @BindView(R.id.til_phone)
    TextInputLayout tilPhone;

    @BindView(R.id.iv_phone)
    ImageView ivPhone;

    @Inject
    PhonePresenter phonePresenter;

    @Inject
    PreferencesHelper preferencesHelper;

    @GlobalConstants.AppFlow
    private int appFlow;

    private OtpDialogInteractor otpDialogInteractor;

    private String phoneNumb;
    private OTPVerifyDialog otpDialog;
    private RequestModel mRequestModel;
    private boolean newUser = false;
    private boolean newloginFlag = true;
    private BroadcastReceiver smsReceiver;
    private BroadcastReceiver networkReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        getActivityComponent().inject(this);
        ButterKnife.bind(PhoneNumberActivity.this);
        phonePresenter.attachView(this);

        setUp();
    }

    @Override
    protected void setUp() {

        otpDialog = new OTPVerifyDialog(this);
        otpDialogInteractor = otpDialog;

        String progress = "<font color='#1E2F4D'>1/</font>";
        String outof = "<font color='#728290'>2</font>";
        progressTitleTextView.setText(Html.fromHtml(progress + outof));

        setSupportActionBar(navToolbar);
        mRequestModel = new RequestModel();

        userPhoneEditText.requestFocus();

        userPhoneEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tilPhoneTwo.setError(null);
                    tilPhone.setError(null);
                    tilPhone.setHintTextAppearance(R.style.TilHintAppearanceCorrect);
                }
                if (!hasFocus) {
                    validateInput(userPhoneEditText.getText().toString().trim());
                }
            }
        });
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(PhoneNumberActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }
            }
        };
    }


    @Override
    protected void onDestroy() {
        otpDialog = null;
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        LocalBroadcastManager.getInstance(this).registerReceiver(smsReceiver,
                new IntentFilter(GlobalConstants.OTP_ACTION_VALUE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver);
        unregisterReceiver(networkReceiver);
    }


    @Subscribe(priority = 1, threadMode = ThreadMode.MAIN, sticky = true)
    public void onSmsReciveEvent(String otp) {
        otpDialogInteractor.sendOtp(otp);
        otpDialogInteractor.btnOtpReset();
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
    public void onBackPressed() {
        Intent intent = new Intent(this, LandingPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.fab_phone_submitNumb)
    public void phoneButtonClicked() {
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_ONBOARDING,
                GlobalConstants.ACTION_GOTO,
                GlobalConstants.LABLE_TO_USER_DETAILS
        );

        phoneNumb = userPhoneEditText.getText().toString().trim();

        if (validateInput(phoneNumb)) {
            fabProgressCircle.show();
            userPhoneEditText.setEnabled(false);
            sendPhoneValidationReq(phoneNumb);
        }
    }

    private void sendPhoneValidationReq(String phone) {

        this.phoneNumb = phone;
        mRequestModel.setPhone("+91" + phone);
        phonePresenter.getFlow("+91" + phoneNumb);

    }

    @Override
    public void reqGetFlowSuccess(ResponseModel responseModel) {

        appFlow = responseModel.getUserFlow();

        //appflow 10
        appFlow = GlobalConstants.HACKATHON_APP_FLOW;

        preferencesHelper.putInt(GlobalConstants.USER_APP_FLOW, GlobalConstants.HACKATHON_APP_FLOW);

        switch (appFlow) {
            case APP_FLOW_PRE_USER:
            case APP_FLOW_CLASSIC:
            case APP_FLOW_SELECT:
            case APP_FLOW_PREPAID:
                phonePresenter.registerUser(mRequestModel);
                break;

            case HACKATHON_APP_FLOW:
                phonePresenter.registerUser(mRequestModel);
                break;

            case APP_FLOW_OPERATIONAL:
                mRequestModel.setNewLoginflag(newloginFlag);
                phonePresenter.loginUser(mRequestModel);
                break;
            default:
                showDialogBox("Your phone number is not listed in any organization");
                fabProgressCircle.hide();
                userPhoneEditText.setEnabled(true);
        }
    }

    @Override
    public void reqRegisterUserSuccess(ResponseModel responseModel) {

        if (responseModel.getMessage().equalsIgnoreCase("Your phone number is not listed in any " +
                "organization")) {
            showDialogBox(responseModel.getMessage());
        } else if (responseModel.getMessage().contains("exists")) {
            preferencesHelper.saveToken(responseModel.getToken());
            mRequestModel.setToken(responseModel.getToken());
            mRequestModel.setNewLoginflag(newloginFlag);
            phonePresenter.loginOldUser(mRequestModel);
            newUser = false;
        } else if (responseModel.getMessage().equalsIgnoreCase("success")) {
            preferencesHelper.saveToken(responseModel.getToken());
            mRequestModel.setToken(responseModel.getToken());
            showVerifyDialog(phoneNumb);
            newUser = true;
        } else {
            showDialogBox(responseModel.getMessage());
        }
    }

    @Override
    public void loginReqSuccess(ResponseModel responseModel) {
        showVerifyDialog(phoneNumb);
        userPhoneEditText.setFocusable(true);
        preferencesHelper.saveToken(responseModel.getToken());
        mRequestModel.setToken(responseModel.getToken());
    }


    @Override
    public void reqLoginSuccess(ResponseModel responseModel) {
        preferencesHelper.saveToken(responseModel.getToken());
        mRequestModel.setToken(responseModel.getToken());
        showVerifyDialog(phoneNumb);
    }

    private void showVerifyDialog(String phoneNumb) {

        fabProgressCircle.hide();
        userPhoneEditText.setEnabled(true);
        otpDialog.setCancelable(false);
        if (!otpDialog.isShowing()) {
            otpDialog.show();
        }
        otpDialogInteractor.sendPhoneNo(phoneNumb);
        this.phoneNumb = phoneNumb;

    }


    private boolean validateInput(String phoneNumb) {

        String red_error = "#F5414A";
        if (TextUtils.isEmpty(phoneNumb)) {
            tilPhoneTwo.setError(getString(R.string.empty_field));
            tilPhone.setError("         ");
            tilPhone.setHintTextAppearance(R.style.TilHintAppearanceError);
            tilPhone.setErrorTextAppearance(R.style.TilErrorAppearanceError);
            ivPhone.setColorFilter(Color.parseColor(red_error));
            return false;
        }

        if (phoneNumb.length() != 10) {
            tilPhoneTwo.setError(getString(R.string.invalid_entry));
            tilPhone.setError("         ");
            tilPhone.setHintTextAppearance(R.style.TilHintAppearanceError);
            tilPhone.setErrorTextAppearance(R.style.TilErrorAppearanceError);
            ivPhone.setColorFilter(Color.parseColor(red_error));
            return false;
        }

        tilPhoneTwo.setError(null);
        tilPhone.setError(null);
        String cyan_main = "#00aad4";
        ivPhone.setColorFilter(Color.parseColor(cyan_main));
        tilPhone.setHintTextAppearance(R.style.TilHintAppearanceCorrect);

        return true;
    }


    @Override
    public void isOTPValid(String otp) {
        fabProgressCircle.show();
        userPhoneEditText.setEnabled(false);
        mRequestModel.setPin(otp);

        switch (appFlow) {
            case APP_FLOW_PRE_USER:
            case APP_FLOW_PREPAID:
            case APP_FLOW_CLASSIC:
            case APP_FLOW_SELECT:
                if (newUser) {
                    phonePresenter.verifyPhoneOTPForRegister(mRequestModel);
                } else {
                    phonePresenter.verifyPhoneOTPForLogin(mRequestModel);
                }
                break;
            case HACKATHON_APP_FLOW:
                //phonePresenter.verifyPhoneOTPForLogin(mRequestModel);
                Intent i;
                fabProgressCircle.hide();
                i = new Intent(PhoneNumberActivity.this, AccountNumberActivity.class);

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(i);
                break;


            case APP_FLOW_OPERATIONAL:
                phonePresenter.verifyPhoneOTPForLogin(mRequestModel);
                break;
        }
    }


    @Override
    public void reqOTPVerificationSuccess(ResponseModel responseModel) {
        userPhoneEditText.setEnabled(true);

        otpDialogInteractor.otpcorrect();
        mRequestModel.setToken(responseModel.getToken());
        preferencesHelper.saveToken(responseModel.getToken());

        switch (appFlow) {
            case APP_FLOW_PRE_USER:
            case APP_FLOW_PREPAID:
            case APP_FLOW_CLASSIC:
            case APP_FLOW_SELECT:
                fabProgressCircle.hide();
                Intent i;
                if (newUser)
                    i = new Intent(PhoneNumberActivity.this, UserRegistrationActivity.class);
                else
                    i = new Intent(PhoneNumberActivity.this, ValidateUserActivity.class);

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                EventBus.getDefault().postSticky(new EventBusHelper(mRequestModel));
                finish();
                startActivity(i);
                break;

            case HACKATHON_APP_FLOW:

                fabProgressCircle.hide();
                i = new Intent(PhoneNumberActivity.this, AccountNumberActivity.class);

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(i);

                break;
            case APP_FLOW_OPERATIONAL:
                phonePresenter.getCardActiveStatus(responseModel.getToken());
                break;
        }
    }

    @Override
    public void reqCardActiveSucess(ResponseModel responseModel) {

        fabProgressCircle.hide();
        preferencesHelper.putBoolean(GlobalConstants.PREF_IS_CARD_ACTIVE, responseModel.
                getCardActive());
        Intent i = new Intent(PhoneNumberActivity.this, CreatePinActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        EventBus.getDefault().postSticky(new EventBusHelper(mRequestModel));
        finish();
        startActivity(i);
    }


    @Override
    public void resentOTPReq(String phoneNumb) {
        sendPhoneValidationReq(phoneNumb);
    }

    @Override
    public void resetNumber(String result) {
        sendPhoneValidationReq(result);
    }

    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void reqFailed(int errorCode, String message) {
        fabProgressCircle.hide();
        userPhoneEditText.setEnabled(true);

        if (errorCode == GlobalConstants.NETWORK_REQUEST_CALL_FAILED) {
            showDialogBox(message);
        }

        if (errorCode == GlobalConstants.NETWORK_REQUEST_CREATE_USER) {
        }

        if (errorCode == GlobalConstants.USER_NOT_EXIST) {
            showDialogBox(message);
        }

        if (errorCode == GlobalConstants.NETWORK_TOKEN_EXPIRE) {
            newloginFlag = false;
            showDialogBox("Your Session has Expired please try again");
        }

        if (errorCode == GlobalConstants.NETWORK_REQUEST_VERIFY_OTP) {
            otpDialogInteractor.otpIncorrect();
        }
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

}

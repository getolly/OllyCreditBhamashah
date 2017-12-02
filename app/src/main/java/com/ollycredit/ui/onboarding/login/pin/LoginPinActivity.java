package com.ollycredit.ui.onboarding.login.pin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.RequestModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.card.card_home.CardHomeActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.custom_views.PasscodeNumberView;
import com.ollycredit.utils.dialogs.UpdateDialog;
import com.ollycredit.utils.helpers.EventBusHelper;
import com.ollycredit.utils.helpers.HelperClass;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginPinActivity extends BaseActivity
        implements View.OnClickListener, LoginPinView, PasscodeNumberView.PassCodeListener {


    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.passcode_view)
    PasscodeNumberView passcodeNumberView;

    @Inject
    LoginPinPresenter loginPinPresenter;

    @Inject
    PreferencesHelper preferencesHelper;

    private String userPin;
    private ResponseModel responseModel;
    private RequestModel requestModel;
    private BroadcastReceiver networkReceiver;
    private EditText[] inputField;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        loginPinPresenter.attachView(this);
        setUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForUpdate();
        OllyCreditApplication.getInstance().trackScreenView(this.getClass().getSimpleName());
//        OllyCreditApplication.getInstance().trackEvent(
//                GlobalConstants.CATEGORY_ONBOARDING,
//                GlobalConstants.ACTION_GOTO,
//                GlobalConstants.LABLE_TO_HOME
//        );
    }

    private void checkForUpdate() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            loginPinPresenter.checkForUpdate(pInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(CardHomeActivity.class.getSimpleName(), e.toString());
        }

    }

    @Override
    public void showUpdateDialog(String msg) {
        UpdateDialog updateDialog = new UpdateDialog(this, msg);
        updateDialog.setCancelable(false);
        updateDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideErrorConnection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    protected void setUp() {

        passcodeNumberView.setPassCodeListener(this);
        requestModel = new RequestModel();

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(LoginPinActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }

            }
        };
    }

    @Override
    public void passcodeEntered(String passcode) {
        swipeRefresh.setRefreshing(true);
        Log.d("mytag", passcode);
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_ONBOARDING,
                GlobalConstants.ACTION_GOTO,
                GlobalConstants.LABLE_TO_HOME
        );

        userPin = passcodeNumberView.getPasscode();
        if (TextUtils.isEmpty(userPin)) {
            showDialogBox(getString(R.string.empty_field));
            return;
        }

        if (userPin.length() < 4) {
            showDialogBox("Please enter your 4 digit Olly pin...");
            passcodeNumberView.clearFields();
            return;
        }

        requestModel.setPhone(preferencesHelper.getString(GlobalConstants.PREF_USER_NUMB_KEY,
                GlobalConstants.PREF_DEFAULT_STRING_VALUE));
        requestModel.setNewLoginflag(false);

        loginPinPresenter.loginOldUser(requestModel);

    }

    @Subscribe(priority = 1, threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventBusHelper eventBusHelper) {
        responseModel = eventBusHelper.getResponseModel();
        //Log.e("user details1", new Gson().toJson(responseModel));
        requestModel = new RequestModel();
        requestModel.setToken(responseModel.getToken());
        showProgressDialog();

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

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


    public void showFABprogress(EditText[] inputFields) {
        for (EditText field : inputFields) {
            field.setEnabled(false);
        }
        swipeRefresh.setRefreshing(true);
//        fabProgressCircle.show();
    }

    public void hideFABprogress(EditText[] inputFields) {
      swipeRefresh.setRefreshing(false);
//        fabProgressCircle.hide();
    }

    @Override
    public void loginReqSuccess(ResponseModel responseModel) {

        preferencesHelper.saveToken(responseModel.getToken());
        RequestModel pinRequestModel = new RequestModel();
        pinRequestModel.setToken(preferencesHelper.getToken());
        pinRequestModel.setPin(userPin);

        loginPinPresenter.checkPin(pinRequestModel);

    }

    @Override
    public void loginReqFailed(int failedCode, String message) {
        hideFABprogress(inputField);
        if (failedCode == GlobalConstants.NETWORK_REQUEST_CALL_FAILED) {
            showDialogBox(message);
        }
        if (failedCode == GlobalConstants.NETWORK_REQUEST_LOGIN_USER) {
            showDialogBox(message);
        }

    }

    @Override
    public void pinReqSuccess(ResponseModel responseModel) {

        loginPinPresenter.getUserInfo(responseModel.getToken());


    }

    @Override
    public void pinReqFailed(int failedCode, String message) {
        hideFABprogress(inputField);
        showDialogBox("PIN is incorrect");
        passcodeNumberView.clearFields();
    }

    @Override
    public void getUserSuccess(ResponseModel responseModel) {

        swipeRefresh.setRefreshing(false);

        preferencesHelper.putString(GlobalConstants.PREF_USER_NUMB_KEY, responseModel.getUser().getMobileNum());
        preferencesHelper.putString(GlobalConstants.PREF_USER_EMAIL_KEY, responseModel.getUser().getEmail());
        preferencesHelper.putString(GlobalConstants.PREF_USER_NAME_KEY, responseModel.getUser().getFirstName() + " " + responseModel.getUser().getLastName());
        preferencesHelper.putString(GlobalConstants.PREF_USER_PAN_KEY, String.valueOf(responseModel.getUser().getPanNum()));

        preferencesHelper.saveToken(responseModel.getToken());
        preferencesHelper.setUserId(responseModel.user.getId());

        preferencesHelper.putInt(GlobalConstants.PREF_PAN_VERIFIED, responseModel.getCreditLimitFlags().getIsPanNumberLinked());
        preferencesHelper.putInt(GlobalConstants.PREF_DEBIT_VERIFIED, responseModel.getCreditLimitFlags().getIsCardLinked());
        preferencesHelper.putInt(GlobalConstants.PREF_EKYC_VERIFIED, responseModel.getCreditLimitFlags().getIsKycLinked());
        preferencesHelper.putInt(GlobalConstants.PREF_REPAYED_VERIFIED, responseModel.getCreditLimitFlags().getHasSuccessfulPayments());

        preferencesHelper.putString(GlobalConstants.PREF_USER_CREDIT_LIMIT, String.valueOf(responseModel.getUser().getCreditLimit()));
        preferencesHelper.putString(GlobalConstants.PREF_PAN_LIMIT, responseModel.getCreditLimitAmounts().getPanNumberLinked());
        preferencesHelper.putString(GlobalConstants.PREF_DEBIT_LIMIT, responseModel.getCreditLimitAmounts().getCardLinked());
        preferencesHelper.putString(GlobalConstants.PREF_EKYC_LIMIT, responseModel.getCreditLimitAmounts().getKycLinked());
        preferencesHelper.putString(GlobalConstants.PREF_REPAYED_LIMIT, responseModel.getCreditLimitAmounts().getSuccessfulPayments());

        hideFABprogress(inputField);
        Intent i;

        preferencesHelper.putInt(GlobalConstants.PREF_EMAIL_VERIFY, responseModel.getUser().getEmailValidated());
//        if (responseModel.getUser().getEmailValidated() == 1) {
        i = new Intent(this, CardHomeActivity.class);

        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();

        startActivity(i);
    }
}

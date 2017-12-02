package com.ollycredit.ui.recovery.pin.validate_password;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
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
import com.ollycredit.api.model.RequestModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.recovery.password.send_recover_mail.RecoverPasswordActivity;
import com.ollycredit.ui.recovery.pin.set_pin.ResetPinActivity;
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


public class RecoverPasswordDetailsActivity extends BaseActivity implements RecoverPasswordDetailsView {

    @BindView(R.id.edit_recover_pin_details_userPhone)
    EditText passwordEditText;

    @BindView(R.id.til_password)
    TextInputLayout tilPassword;

    @BindView(R.id.toolbar_recover_pin)
    Toolbar navToolbar;

    @BindView(R.id.fab_recover_pin_details_submitNumb)
    FloatingActionButton submitDetailFAB;

    @BindView(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;


    RequestModel mRequestModel;

    @Inject
    RecoverPasswordDetailsPresenter recoverPasswordDetailsPresenter;

    @Inject
    PreferencesHelper preferencesHelper;
    private BroadcastReceiver networkReceiver;
    private EditText[] inputFields;
    private String token;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recover_pin_details);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);

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


    @OnClick(R.id.fab_recover_pin_details_submitNumb)
    public void onClickSubmitFAB() {

        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_RECOVER_CREDENTIALS,
                GlobalConstants.ACTION_VERIFY,
                GlobalConstants.LABLE_TO_VERIFY_PHONE
        );


        String password = passwordEditText.getText().toString().trim();
        inputFields = new EditText[]{passwordEditText};
        TextInputLayout[] tilLayouts = {tilPassword};

        if (validate(inputFields, tilLayouts)) {
            showFABprogress(inputFields);
            RequestModel requestModel = new RequestModel();
            requestModel.setPassword(password);
            Log.e("recover", preferencesHelper.getToken());
            token =preferencesHelper.getToken();
            requestModel.setToken(preferencesHelper.getToken());
            recoverPasswordDetailsPresenter.verifyPassword(requestModel);

        }

    }


    private boolean validate(EditText[] editTexts, TextInputLayout[] inputLayouts) {

        for (int i = 0; i < editTexts.length; i++) {

            if (editTexts[i].getText().toString().trim().isEmpty()) {
                inputLayouts[i].setError(getString(R.string.empty_field));
                return false;
            }

            if (editTexts[i].getTag().toString().equalsIgnoreCase("password")) {

                if (editTexts[i].getText().toString().trim().length() < 6) {
                    inputLayouts[i].setError(getString(R.string.invalid_entry));
                    return false;
                }

            }
            if (editTexts[i].getTag().toString().equalsIgnoreCase("password")) {

                if (editTexts[i].getText().toString().trim().length() < 6) {
                    inputLayouts[i].setError(getString(R.string.invalid_entry));
                    return false;
                }

            }

            inputLayouts[i].setError(null);

        }
        return true;
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

    @OnClick(R.id.btn_recover_pin_details_forgotPin)
    public void onClickForgotPasswordButton() {
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_RECOVER_CREDENTIALS,
                GlobalConstants.ACTION_VERIFY,
                GlobalConstants.LABLE_TO_RECOVER_PIN
        );

        startActivity(new Intent(RecoverPasswordDetailsActivity.this, RecoverPasswordActivity.class));
        finish();
    }


    @Subscribe(priority = 1, threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventBusHelper eventBusHelper) {
        mRequestModel = eventBusHelper.getRequestModel();
        Log.e("user details1", new Gson().toJson(mRequestModel));
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
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideErrorConnection();
    }


    @Override
    protected void setUp() {
        setSupportActionBar(navToolbar);
        navToolbar.setTitle(" ");

        recoverPasswordDetailsPresenter.attachView(this);
        passwordEditText.requestFocus();

        passwordEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tilPassword.setError(null);
                }
            }
        });
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(RecoverPasswordDetailsActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
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
    public void reqSuccess(ResponseModel responseModel) {

        hideFABprogress(inputFields);

        if (responseModel.getStatus().equals(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {
            Intent intent = new Intent(RecoverPasswordDetailsActivity.this, ResetPinActivity.class);
            intent.putExtra("token", responseModel.getToken());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intent);

            EventBus.getDefault().postSticky(new EventBusHelper(mRequestModel));

        } else if (responseModel.getStatus().equals(GlobalConstants.NETWORK_RESPONSE_FAILED)) {
            showDialogBox("Invalid password. Please try again with valid password.");
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


    @Override
    public void reqFailed(int failedCode, String message) {

        hideFABprogress(inputFields);
    }
}

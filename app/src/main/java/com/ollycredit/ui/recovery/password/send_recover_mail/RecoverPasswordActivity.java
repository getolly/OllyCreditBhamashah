package com.ollycredit.ui.recovery.password.send_recover_mail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.recovery.customer_care.RecoverByPhoneCallMainActivity;
import com.ollycredit.ui.recovery.password.RecoverPasswordCompleteActivity;
import com.ollycredit.utils.GlobalConstants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecoverPasswordActivity extends BaseActivity implements RecoverPasswordView, OnClickListener {


    @BindView(R.id.edit_rec_pass_firstName)
    EditText firstNameEditText;

    @BindView(R.id.edit_rec_pass_lastName)
    EditText lastNameEditText;

    @BindView(R.id.edit_rec_pass_email)
    EditText emailEditText;

    @BindView(R.id.pinview_rec_pass_pin)
    View otpView;

    @BindView(R.id.toolbar_recover_pass)
    Toolbar navToolbar;

    @BindView(R.id.fab_rec_pin_submitNumb)
    FloatingActionButton submitNumbFAB;

    @BindView(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;

    @BindView(R.id.btn_rec_pass_forgotPin)
    Button recovPinButton;


    @BindView(R.id.btn_rec_pass_send)
    Button btnSendMail;

    @Inject
    RecoverPasswordPresenter recoverPasswordPresenter;

    @Inject
    PreferencesHelper preferencesHelper;

    private EditText pinOne;
    private EditText pinTwo;
    private EditText pinThree;
    private EditText pinFour;
    private String lastOtp;
    private BroadcastReceiver networkReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        recoverPasswordPresenter.attachView(this);

        initPinView();
        setUp();
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


    private void initPinView() {

        pinOne = (EditText) otpView.findViewById(R.id.pin_one);
        pinTwo = (EditText) otpView.findViewById(R.id.pin_two);
        pinThree = (EditText) otpView.findViewById(R.id.pin_three);
        pinFour = (EditText) otpView.findViewById(R.id.pin_four);


        pinOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    pinTwo.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        pinTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    pinThree.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    pinOne.requestFocus();
                }
            }
        });

        pinThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    pinFour.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    pinTwo.requestFocus();
                }
            }
        });

        pinFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    pinThree.requestFocus();
                }
            }
        });
    }

    @OnClick(R.id.btn_rec_pass_send)
    public void onClickSendMail() {

        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_RECOVER_CREDENTIALS,
                GlobalConstants.ACTION_BUTTON_CLICKED,
                GlobalConstants.LABLE_RECOVER_EMAIL
        );

        showLoadingView();
        recoverPasswordPresenter.sendRecoverMail(preferencesHelper.getToken());

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


    public String getOtpFromPinView() {

        lastOtp = (pinOne.getText().toString()
                + pinTwo.getText().toString()
                + pinThree.getText().toString()
                + pinFour.getText().toString()).trim();

        Log.e("dialog final otp", lastOtp);

        return lastOtp;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @OnClick(R.id.fab_rec_pin_submitNumb)
    public void onPressSubmitFAB(View v) {
        fabProgressCircle.show();
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String pin = getOtpFromPinView();

        if (TextUtils.isEmpty(firstName) ||
                TextUtils.isEmpty(lastName) ||
                TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(pin)) {
            firstNameEditText.setError(getString(R.string.empty_field));
            lastNameEditText.setError(getString(R.string.empty_field));
            emailEditText.setError(getString(R.string.empty_field));
            showDialogBox("please enter a valid 4 digit pin");
            return;
        }


        if (firstName.length() < 3 || lastName.length() < 3 || email.length() < 3 || pin.length() < 4) {
            firstNameEditText.setError(getString(R.string.invalid_entry));
            lastNameEditText.setError(getString(R.string.invalid_entry));
            showDialogBox("please enter a valid 4 digit pin");
            emailEditText.setError(getString(R.string.invalid_entry));
            return;
        }

        fabProgressCircle.hide();

        startActivity(new Intent(RecoverPasswordActivity.this, RecoverPasswordCompleteActivity.class));
        finish();
    }


    @OnClick(R.id.btn_rec_pass_forgotPin)
    public void onRecPinButtonPressed(View v) {
        startActivity(new Intent(RecoverPasswordActivity.this, RecoverByPhoneCallMainActivity.class));
        finish();
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
    protected void setUp() {

        setSupportActionBar(navToolbar);
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(RecoverPasswordActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }
            }
        };


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void reqFailed(int failedCode, String message) {
        hideLoadingView();
        showDialogBox(message);
    }

    @Override
    public void reqSuccess(ResponseModel responseModel) {
        hideLoadingView();
        startActivity(new Intent(this, RecoverPasswordCompleteActivity.class));
        finish();
    }
}

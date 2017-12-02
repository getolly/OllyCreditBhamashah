package com.ollycredit.ui.onboarding.login.password;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.RequestModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.recovery.password.send_recover_mail.RecoverPasswordActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.HelperClass;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginPasswordActivity extends BaseActivity implements LoginPaswordView {

    @BindView(R.id.edit_login_pass_password)
    EditText passwordEditText;

    @BindView(R.id.btn_login_pass_forgotPin)
    Button forgotPasswordButton;

    @BindView(R.id.fab_login_password_submitNumb)
    FloatingActionButton submitNumberFAB;

    @BindView(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;


    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    LoginPasswordPresenter loginPasswordPresenter;

    private String userPassword;
    private ResponseModel responseModel;
    private RequestModel requestModel;
    private BroadcastReceiver networkReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        setUp();
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


    @OnClick({
            R.id.btn_login_pass_forgotPin,
            R.id.fab_login_password_submitNumb
    })
    public void onClickButton(View v) {
        switch (v.getId()) {
            case R.id.btn_login_pass_forgotPin:
                OllyCreditApplication.getInstance().trackEvent(
                        GlobalConstants.CATEGORY_ONBOARDING,
                        GlobalConstants.ACTION_GOTO,
                        GlobalConstants.LABLE_TO_RECOVER_PASSWORD
                );

                startActivity(new Intent(LoginPasswordActivity.this, RecoverPasswordActivity.class));
                finish();
                break;
            case R.id.fab_login_password_submitNumb:

                OllyCreditApplication.getInstance().trackEvent(
                        GlobalConstants.CATEGORY_ONBOARDING,
                        GlobalConstants.ACTION_BUTTON_CLICKED,
                        GlobalConstants.LABLE_RECOVER_PASSWORD
                );

                userPassword = passwordEditText.getText().toString();
                fabProgressCircle.show();
                if (TextUtils.isEmpty(userPassword)) {
                    showDialogBox(getString(R.string.empty_field));
                    return;
                }


//              Todo verify pin get user details and pass to dashboard


                break;
        }

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

        loginPasswordPresenter.attachView(this);
        requestModel = new RequestModel();
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(LoginPasswordActivity.this);
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


}

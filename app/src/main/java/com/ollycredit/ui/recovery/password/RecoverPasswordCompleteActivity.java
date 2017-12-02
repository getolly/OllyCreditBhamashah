package com.ollycredit.ui.recovery.password;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Button;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.onboarding.login.pin.LoginPinActivity;
import com.ollycredit.utils.GlobalConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecoverPasswordCompleteActivity extends BaseActivity {

    @BindView(R.id.btn_reset_pass_back_login)
    Button btnLogin;

    private BroadcastReceiver networkReceiver;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password_complete);
        ButterKnife.bind(this);
        setUp();
    }

    @OnClick(R.id.btn_reset_pass_back_login)
    public void onClikedButton() {
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_RECOVER_CREDENTIALS,
                GlobalConstants.ACTION_GOTO,
                GlobalConstants.LABLE_TO_LOGIN_PIN
        );
        startActivity(new Intent(this, LoginPinActivity.class));
        finish();
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
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);

        hideErrorConnection();

    }


    @Override
    protected void setUp() {
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(RecoverPasswordCompleteActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }
            }
        };


    }
}

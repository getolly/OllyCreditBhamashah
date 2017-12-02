package com.ollycredit.ui.recovery.pin.confirm;

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

public class PinResetConfirmActivity extends BaseActivity {

    @BindView(R.id.btn_back_login)
    Button backToLoginButton;
    private BroadcastReceiver networkReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_reset_confirm);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);

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

    @OnClick(R.id.btn_back_login)
    public void onClick() {
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_RECOVER_CREDENTIALS,
                GlobalConstants.ACTION_GOTO,
                GlobalConstants.LABLE_TO_LOGIN_PIN
        );
        startActivity(new Intent(this, LoginPinActivity.class));
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
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(PinResetConfirmActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }
            }
        };

    }
}

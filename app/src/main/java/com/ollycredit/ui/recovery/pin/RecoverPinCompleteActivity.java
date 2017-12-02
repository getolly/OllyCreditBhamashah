package com.ollycredit.ui.recovery.pin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.onboarding.phone.PhoneNumberActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecoverPinCompleteActivity extends BaseActivity {


    @BindView(R.id.btn_back_login)
    Button backtologinButton;
    private BroadcastReceiver networkReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_pin_complete);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        setUp();
    }

    @OnClick(R.id.btn_back_login)
    public void onClikedButton(View view) {
        startActivity(new Intent(RecoverPinCompleteActivity.this, PhoneNumberActivity.class));
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
    protected void setUp() {
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(RecoverPinCompleteActivity.this);
                if (isOnline){
                    hideErrorConnection();
                }else {
                    showErrorConnection();
                }
            }
        };

    }
}

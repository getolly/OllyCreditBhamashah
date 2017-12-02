package com.ollycredit.ui.recovery.customer_care;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.utils.GlobalConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecoverByPhoneCallMainActivity extends BaseActivity {

    @BindView(R.id.btn_contact_care)
    Button contactCareButton;

    @BindView(R.id.btn_back_login)
    Button backToLoginButton;
    private BroadcastReceiver networkReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_by_phone_call_main);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        setUp();

    }

    @OnClick({
            R.id.btn_contact_care,
            R.id.btn_back_login
    })
    public void onButtonClicked(){
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_RECOVER_CREDENTIALS,
                GlobalConstants.ACTION_BUTTON_CLICKED,
                GlobalConstants.LABLE_TO_CUSTOMER_CARE
        );
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideErrorConnection();
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
                boolean isOnline = isOnline(RecoverByPhoneCallMainActivity.this);
                if (isOnline){
                    hideErrorConnection();
                }else {
                    showErrorConnection();
                }
            }
        };

    }
}

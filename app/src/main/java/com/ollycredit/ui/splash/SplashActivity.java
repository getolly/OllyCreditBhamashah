package com.ollycredit.ui.splash;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.onboarding.landing_page.LandingPage;
import com.ollycredit.ui.onboarding.login.pin.LoginPinActivity;
import com.ollycredit.utils.GlobalConstants;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity {


    private String defaultNumber;


    @Inject
    PreferencesHelper preferencesHelper;
    private BroadcastReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getActivityComponent().inject(this);

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


    @Override
    protected void onPause() {
        super.onPause();
    }



    @Override
    protected void setUp() {

        defaultNumber = preferencesHelper.getString(GlobalConstants.PREF_USER_NUMB_KEY, GlobalConstants.PREF_DEFAULT_STRING_VALUE);

        Timer splashTimer = new Timer();
        splashTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                jump();
            }
        }, GlobalConstants.SPLASH_SCREEN_TIME_OUT);

    }

    private void jump() {

        if (defaultNumber.equalsIgnoreCase(GlobalConstants.PREF_DEFAULT_STRING_VALUE)) {
            startActivity(new Intent(SplashActivity.this, LandingPage.class));
            this.finish();
        } else {
            startActivity(new Intent(SplashActivity.this, LoginPinActivity.class));
            this.finish();
        }

    }
}

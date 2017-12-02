package com.ollycredit.ui.onboarding.landing_page;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.onboarding.phone.PhoneNumberActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.HelperClass;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LandingPage extends BaseActivity {


    @BindView(R.id.landing_edittext)
    EditText phoneEditText;

    private BroadcastReceiver networkReceiver;

    @Inject
    PreferencesHelper preferencesHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

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
                if (intent.getExtras() != null) {

                    if (isOnline(LandingPage.this)) {
                        hideErrorConnection();
                    } else {
                        showErrorConnection();
                    }
                }
            }
        };

        checkForPermission();
        phoneEditText.setFocusable(false);
        phoneEditText.setClickable(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        OllyCreditApplication.getInstance().trackScreenView(this.getClass().getSimpleName());
    }

    @OnClick(R.id.landing_edittext)
    protected void onLayoutClicked() {


        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_ONBOARDING,
                GlobalConstants.ACTION_GOTO,
                GlobalConstants.LABLE_TO_VERIFY_PHONE
        );

        Intent i = new Intent(LandingPage.this, PhoneNumberActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}

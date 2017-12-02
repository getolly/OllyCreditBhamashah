package com.ollycredit.ui.onboarding.signup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ollycredit.R;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.utils.helpers.HelperClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReadTermsConditionActivity extends BaseActivity {


    @BindView(R.id.btn_terms_read)
    Button termsConditionButton;
    private BroadcastReceiver networkReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);

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


    @OnClick(R.id.btn_terms_read)
    public void onPressReadTermsCondition(View v) {
//         Intent i = new Intent(ReadTermsConditionActivity.this, TCPage1Activity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(i);
//        finish();
//        TermsAndConditionsDialog termsAndConditionsDialog = new TermsAndConditionsDialog(ReadTermsConditionActivity.this);
//        termsAndConditionsDialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        startActivity(new Intent(ReadTermsConditionActivity.this, LandingPage.class));
        finish();
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
    protected void setUp() {
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(ReadTermsConditionActivity.this);
                if (isOnline){
                    hideErrorConnection();
                }else {
                    showErrorConnection();
                }
            }
        };


    }


//    @Override
//    public void onResult(boolean result) {
//        if (result) {
//            Toast.makeText(this, "Conditions Accepted!", Toast.LENGTH_SHORT).show();
//        }
//    }
}

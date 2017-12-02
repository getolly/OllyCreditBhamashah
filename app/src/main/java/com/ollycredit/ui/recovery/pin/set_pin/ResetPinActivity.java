package com.ollycredit.ui.recovery.pin.set_pin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.gson.Gson;
import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.RequestModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.card.card_home.CardHomeActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.custom_views.PasscodeNumberView;
import com.ollycredit.utils.helpers.EventBusHelper;
import com.ollycredit.utils.helpers.HelperClass;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ollycredit.R.id.toolbar_pin;

public class ResetPinActivity extends BaseActivity implements View.OnClickListener, ResetPinView {

    @BindView(R.id.text_in_progress)
    TextView progressTitleTextView;

    @BindView(R.id.passcode_view)
    PasscodeNumberView passcodeNumberView;

    @BindView(toolbar_pin)
    Toolbar navToolbar;

    @BindView(R.id.fab_pin_submitPin)
    FloatingActionButton submitNewPinFAB;

    @BindView(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;

    private RequestModel mRequestModel;

    @Inject
    ResetPinPresenter resetPinPresenter;

    @Inject
    PreferencesHelper preferencesHelper;

    String token;
    private BroadcastReceiver networkReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_pin);

        getActivityComponent().inject(this);
        ButterKnife.bind(ResetPinActivity.this);
        resetPinPresenter.attachView(this);

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



    @OnClick(R.id.fab_pin_submitPin)
    public void onPressSubmitFAB(View v) {
        String newPin = passcodeNumberView.getPasscode();
        if (TextUtils.isEmpty(newPin)) {
            passcodeNumberView.setError(getString(R.string.empty_field));
            return;
        }

        if (newPin.length() < 4) {
            passcodeNumberView.setError(getString(R.string.empty_field));
            passcodeNumberView.clearFields();
            return;
        }

        showFABprogress();

        RequestModel requestModel = new RequestModel();
        requestModel.setToken(token);
        requestModel.setPin(newPin);
        resetPinPresenter.choosePin(requestModel);



    }

    public void showFABprogress() {

        fabProgressCircle.show();
    }

    public void hideFABprogress() {

        fabProgressCircle.hide();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

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
    }


    @Override
    protected void onPause() {
        super.onPause();
        hideErrorConnection();
    }


    @Override
    protected void setUp() {

        String progress = "<font color='#1E2F4D'>3/</font>";
        String outof = "<font color='#728290'>3</font>";
        progressTitleTextView.setText(Html.fromHtml(progress + outof));

        setSupportActionBar(navToolbar);

        token = getIntent().getStringExtra("token");

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(ResetPinActivity.this);
                if (isOnline){
                    hideErrorConnection();
                }else {
                    showErrorConnection();
                }
            }
        };


    }

    @Subscribe(priority = 1, threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventBusHelper eventBusHelper) {
        mRequestModel = eventBusHelper.getRequestModel();
        Log.e("Create pin", new Gson().toJson(mRequestModel));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_pin:
                onBackPressed();
                break;
        }

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }



    @Override
    public void reqSuccess(ResponseModel responseModel) {
        //Toast.makeText(this, "Pin change successful!", Toast.LENGTH_SHORT).show();
        Log.e("reset pin : user pin",new Gson().toJson(responseModel));
        resetPinPresenter.getUserInfo(responseModel.getToken());

    }

    @Override
    public void reqFailed(int errorCode,String message) {

        hideFABprogress();

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage(message)
                .setTitle("Error")
                .setNeutralButton("Ok", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();


        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
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
    public void getUserSuccess(ResponseModel responseModel) {


        Log.e("reset pin : user info",new Gson().toJson(responseModel));
        preferencesHelper.putString(GlobalConstants.PREF_USER_NUMB_KEY, responseModel.getUser().getMobileNum());
        preferencesHelper.putString(GlobalConstants.PREF_USER_EMAIL_KEY, responseModel.getUser().getEmail());
        preferencesHelper.putString(GlobalConstants.PREF_USER_NAME_KEY, responseModel.getUser().getFirstName()+" "+responseModel.getUser().getLastName());


        preferencesHelper.saveToken(responseModel.getToken());
        preferencesHelper.setUserId(responseModel.getUser().getId());

        preferencesHelper.putInt(GlobalConstants.PREF_EMAIL_VERIFIED,responseModel.getUser().getEmailValidated());
        preferencesHelper.putInt(GlobalConstants.PREF_PAN_VERIFIED,responseModel.getCreditLimitFlags().getIsPanNumberLinked());
        preferencesHelper.putInt(GlobalConstants.PREF_DEBIT_VERIFIED,responseModel.getCreditLimitFlags().getIsCardLinked());
        preferencesHelper.putInt(GlobalConstants.PREF_EKYC_VERIFIED,responseModel.getCreditLimitFlags().getIsKycLinked());
        preferencesHelper.putInt(GlobalConstants.PREF_REPAYED_VERIFIED, responseModel.getCreditLimitFlags().getHasSuccessfulPayments());

        hideFABprogress();

        Intent i = new Intent(ResetPinActivity.this, CardHomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        finish();
        startActivity(i);

    }
}

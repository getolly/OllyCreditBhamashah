package com.ollycredit.ui.onboarding.signup.create_pin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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

import static com.ollycredit.utils.GlobalConstants.APP_FLOW_CLASSIC;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_OPERATIONAL;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_PREPAID;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_PRE_USER;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_SELECT;
import static com.ollycredit.utils.GlobalConstants.HACKATHON_APP_FLOW;

public class CreatePinActivity extends BaseActivity implements View.OnClickListener, CreatePinView {

    @BindView(R.id.text_in_progress)
    TextView progressTitleTextView;

    @BindView(R.id.passcode_view)
    PasscodeNumberView passcodeNumberView;

    @BindView(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;

    @BindView(R.id.fab_pin_submitPin)
    FloatingActionButton submitNewPinFAB;

    private RequestModel mRequestModel;

    @Inject
    CreatePinPresenter createPinPresenter;

    @Inject
    PreferencesHelper preferencesHelper;

    private BroadcastReceiver networkReceiver;
    private EditText[] inputFields;

    private int appFlow;

    private boolean isFooterNumbReq;

    private boolean isCardActive;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_pin);

        getActivityComponent().inject(this);
        ButterKnife.bind(CreatePinActivity.this);
        createPinPresenter.attachView(this);
        isFooterNumbReq = getIntent().getBooleanExtra("number_progress_needed", true);
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

    @Override
    protected void onPause() {
        super.onPause();
        hideErrorConnection();
    }


    public void showFABprogress(EditText[] inputFields) {

        fabProgressCircle.show();
    }

    public void hideFABprogress(EditText[] inputFields) {

        fabProgressCircle.hide();
    }


    @OnClick(R.id.fab_pin_submitPin)
    public void onPressSubmitFAB() {
        String newPin = passcodeNumberView.getPasscode();

        if (TextUtils.isEmpty(newPin)) {
            passcodeNumberView.setError(getString(R.string.empty_field));
            return;
        }

        if (newPin.length() != 4) {
            passcodeNumberView.setError(getString(R.string.invalid_entry));
            passcodeNumberView.clearFields();
            return;
        }

        showFABprogress(inputFields);

        mRequestModel.setToken(preferencesHelper.getToken());
        mRequestModel.setPin(newPin);
        switch (appFlow) {
            case APP_FLOW_PRE_USER:
            case APP_FLOW_PREPAID:
            case APP_FLOW_CLASSIC:
            case APP_FLOW_SELECT:
                createPinPresenter.createPin(mRequestModel);
                break;

            case HACKATHON_APP_FLOW:
                createPinPresenter.changePin(mRequestModel);
                break;
            case APP_FLOW_OPERATIONAL:
                if (isCardActive) {
                    createPinPresenter.changePin(mRequestModel);
                } else {
                    createPinPresenter.createPin(mRequestModel);
                }
                break;
        }
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
    protected void setUp() {

        isCardActive = preferencesHelper.getBoolean(GlobalConstants.PREF_IS_CARD_ACTIVE, true);
        appFlow = preferencesHelper.getInt(GlobalConstants.USER_APP_FLOW, 0);
        appFlow = HACKATHON_APP_FLOW;

        if (isFooterNumbReq) {
            String progress = "<font color='#FFFFFF'>3/</font>";
            String outof = "<font color='#FFFFFF'>3</font>";
            if (appFlow == GlobalConstants.APP_FLOW_PRE_USER) {
                progress = "<font color='#FFFFFF'>3/</font>";
                outof = "<font color='#FFFFFF'>3</font>";
            } else if (appFlow == GlobalConstants.APP_FLOW_OPERATIONAL) {
                progress = "<font color='#FFFFFF'>2/</font>";
                outof = "<font color='#FFFFFF'>2</font>";
            }
            progressTitleTextView.setText(Html.fromHtml(progress + outof));
        } else {
            progressTitleTextView.setVisibility(View.INVISIBLE);
            //overiding flow of user login to "update" the login for already exisiting user
            appFlow = GlobalConstants.APP_FLOW_OPERATIONAL;
        }


        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(CreatePinActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }
            }
        };


        EventBusHelper eventBusHelper = EventBus.getDefault().removeStickyEvent(EventBusHelper.class);
        mRequestModel = eventBusHelper.getRequestModel();


    }

    @Subscribe(priority = 1, threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventBusHelper eventBusHelper) {
//        mRequestModel = eventBusHelper.getRequestModel();
//        Log.e("Create pin", new Gson().toJson(mRequestModel));
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
        preferencesHelper.saveToken(responseModel.getToken());
        preferencesHelper.putBoolean(GlobalConstants.PREF_USER_ONBOARDED, true);
        createPinPresenter.getUserInfo(responseModel.getToken());


    }

    @Override
    public void getUserSuccess(ResponseModel responseModel) {

        Log.e("userinfo", new Gson().toJson(responseModel));

        preferencesHelper.putString(GlobalConstants.PREF_USER_NUMB_KEY, responseModel.getUser().getMobileNum());
        preferencesHelper.putString(GlobalConstants.PREF_USER_EMAIL_KEY, responseModel.getUser().getEmail());
        preferencesHelper.putString(GlobalConstants.PREF_USER_NAME_KEY, responseModel.getUser().getFirstName() + " " + responseModel.getUser().getLastName());

        preferencesHelper.saveToken(responseModel.getToken());
        preferencesHelper.setUserId(responseModel.user.getId());
        int isEmailValid = responseModel.getUser().getEmailValidated();

        preferencesHelper.putInt(GlobalConstants.PREF_PAN_VERIFIED, responseModel.getCreditLimitFlags().getIsPanNumberLinked());
        preferencesHelper.putInt(GlobalConstants.PREF_DEBIT_VERIFIED, responseModel.getCreditLimitFlags().getIsCardLinked());
        preferencesHelper.putInt(GlobalConstants.PREF_EKYC_VERIFIED, responseModel.getCreditLimitFlags().getIsKycLinked());
        preferencesHelper.putInt(GlobalConstants.PREF_REPAYED_VERIFIED, responseModel.getCreditLimitFlags().getHasSuccessfulPayments());

        hideFABprogress(inputFields);
        Intent i;

//        if (isEmailValid == new ViewPagerHelper().DOC_VERIFIED) {
        i = new Intent(CreatePinActivity.this, CardHomeActivity.class);
//        } else {
//            i = new Intent(CreatePinActivity.this, VerifyEmailActivity.class);
//        }

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


        startActivity(i);
        finish();

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
    public void reqFailed(int errorCode, String message) {
        hideFABprogress(inputFields);


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
}

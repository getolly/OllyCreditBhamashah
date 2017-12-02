package com.ollycredit.ui.onboarding.signup.user_details.step_two;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.gson.Gson;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.RequestModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.onboarding.signup.create_pin.CreatePinActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.dialogs.TermsAndConditionsDialog;
import com.ollycredit.utils.helpers.EventBusHelper;
import com.ollycredit.utils.helpers.HelperClass;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserDetailsTwoActivity extends BaseActivity implements View.OnClickListener,
        UserDetailsTwoView {

    @BindView(R.id.text_user_progress)
    TextView progressTitleTextView;

    @BindView(R.id.tv_user_welcome)
    TextView tv_user_welcome;

    @BindView(R.id.tv_details_tc)
    TextView tvTermsCondition;
    @BindView(R.id.tv_scollto)
    TextView tvScrollTo;

    @BindView(R.id.tv_user_welcome_msg)
    TextView tv_user_welcome_msg;

    @BindView(R.id.sv_userdetailtwo)
    ScrollView svUserDetail;

    @BindView(R.id.cbTermsCondition)
    CheckBox cbTermsCondition;

    @BindView(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;

//    @BindView(R.id.edit_user_password)
//    EditText passwordEditText;

    @BindView(R.id.toolbar_user)
    Toolbar navToolbar;


    @BindView(R.id.fab_user_submitDetails)
    FloatingActionButton submitDetailsFAB;

    public final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$");


    private RequestModel mRequestModel;

    private String name;
    private String cyan_main = "#00aad4";

    @Inject
    UserDetailsTwoPresenter userDetailsTwoPresenter;

    @Inject
    PreferencesHelper preferencesHelper;
    private BroadcastReceiver networkReceiver;
    private EditText[] editTexts;
    private String fname;
    private String lname;
    private String dob;

    private int onboardingFlow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        userDetailsTwoPresenter.attachView(this);


        fname = getIntent().getStringExtra("fname");
        lname = getIntent().getStringExtra("lname");
        dob = getIntent().getStringExtra("dob");


        String welcome = "Hi " + fname + " " + lname;
        tv_user_welcome.setText(welcome);
        setUp();
    }


    @Override
    protected void onPause() {
        super.onPause();
        hideErrorConnection();
    }

    @OnClick(R.id.fab_user_submitDetails)
    public void onPressSubmitFAB(View v) {
        //String password = passwordEditText.getText().toString().trim();

        if (cbTermsCondition.isChecked()) {
            fabProgressCircle.show();
            mRequestModel = new RequestModel();
            mRequestModel.setName(name);
            mRequestModel.setToken(preferencesHelper.getToken());
            Intent i = new Intent(UserDetailsTwoActivity.this, CreatePinActivity.class);
            i.putExtra("flow", onboardingFlow);
            EventBus.getDefault().postSticky(new EventBusHelper(mRequestModel));
            startActivity(i);
        } else {
            Toast.makeText(this, "Please Check Terms and Conditions", Toast.LENGTH_SHORT).show();
        }


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


    public void showFABprogress(EditText[] inputFields) {
        for (EditText field : inputFields) {
            field.setEnabled(false);
        }
        fabProgressCircle.show();
    }

    public void hideFABprogress(EditText[] inputFields) {

        Log.e("hideFABprogress", new Gson().toJson(inputFields));

        for (EditText field : inputFields) {
            field.setEnabled(true);
        }
        fabProgressCircle.hide();
    }


    private boolean validate(EditText[] editTexts, TextInputLayout[] inputLayouts) {

        for (int i = 0; i < editTexts.length; i++) {

            if (editTexts[i].getText().toString().trim().isEmpty()) {
                inputLayouts[i].setError(getString(R.string.empty_field));
                return false;
            }

//            if (editTexts[i].getTag().toString().equalsIgnoreCase("password")) {
//
//                if (editTexts[i].getText().toString().trim().length() < 6) {
//                    inputLayouts[i].setError(getString(R.string.invalid_entry));
//                    return false;
//                }
//
//            }

            if (editTexts[i].getTag().toString().equalsIgnoreCase("email")) {

                if (!validateEmail(editTexts[i].getText().toString().trim())) {
                    inputLayouts[i].setError(getString(R.string.invalid_entry));
                    return false;
                }

            }


            if (editTexts[i].getText().toString().trim().length() < 3) {
                inputLayouts[i].setError(getString(R.string.invalid_entry));
                return false;
            }

            inputLayouts[i].setError(null);

        }
        return true;
    }

    @OnClick(R.id.tv_details_tc)
    public void onTCclicked() {
        TermsAndConditionsDialog dailogTAC = new TermsAndConditionsDialog();
        dailogTAC.show(
                getSupportFragmentManager(), "terms_condition_dialog");
    }


    public boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    protected void setUp() {

        onboardingFlow = preferencesHelper.getInt(GlobalConstants.USER_APP_FLOW, 0);

        if (onboardingFlow == GlobalConstants.APP_FLOW_PRE_USER) {

            String progress = "<font color='#1E2F4D'>3/</font>";
            String outof = "<font color='#728290'>4</font>";
            progressTitleTextView.setText(Html.fromHtml(progress + outof));

//            userDetailsTwoPresenter.getUserInfo(preferencesHelper.getToken());


        } else if (onboardingFlow == GlobalConstants.APP_FLOW_OPERATIONAL) {
                        userDetailsTwoPresenter.getUserInfo(preferencesHelper.getToken());

            String progress = "<font color='#1E2F4D'>2/</font>";
            String outof = "<font color='#728290'>3</font>";
            progressTitleTextView.setText(Html.fromHtml(progress + outof));

        }


        String termsConditionOne = "By signing up with Olly I agree\nto ";
        String termsConditionTwo = "Terms and Condition";

        SpannableString spannableString = new SpannableString(termsConditionOne + termsConditionTwo);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(cyan_main)), termsConditionOne.length(), (termsConditionOne + termsConditionTwo).length(), 0);
        tvTermsCondition.setText(spannableString);

        setSupportActionBar(navToolbar);

        //passwordEditText.setOnFocusChangeListener(this);

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(UserDetailsTwoActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }
            }
        };


    }


//    @Override
//    public void onBackPressed() {
//        EventBus.getDefault().postSticky(new EventBusHelper(mRequestModel));
//        super.onBackPressed();
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Subscribe(priority = 1, threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventBusHelper eventBusHelper) {
    }


    @Override
    public void onClick(View v) {
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void reqSuccess(ResponseModel responseModel) {

        Log.e("userInfo", new Gson().toJson(responseModel));

        if (onboardingFlow == GlobalConstants.APP_FLOW_PRE_USER) {
//            hideFABprogress(editTexts);
//            emailEditText.setEnabled(true);
//            fabProgressCircle.hide();
//            //Toast.makeText(this, "Details Sent!", Toast.LENGTH_SHORT).show();
//
//            Intent i = new Intent(UserDetailsTwoActivity.this, CreatePinActivity.class);
//
//            mRequestModel.setToken(responseModel.getToken());
//            preferencesHelper.saveToken(responseModel.getToken());
//            EventBus.getDefault().postSticky(new EventBusHelper(mRequestModel));
//
//            startActivity(i);

            fabProgressCircle.hide();

            tv_user_welcome.setVisibility(View.GONE);
            tv_user_welcome_msg.setVisibility(View.VISIBLE);

            hideLoadingView();
//            mRequestModel.setToken(responseModel.getToken());
            preferencesHelper.saveToken(responseModel.getToken());


        } else if (onboardingFlow == GlobalConstants.APP_FLOW_OPERATIONAL) {

            fabProgressCircle.hide();
            //req success
            String firstName = responseModel.getUser().getFirstName().substring(0, 1).toUpperCase() + responseModel.getUser().getFirstName().substring(1);
            String lastName = responseModel.getUser().getLastName().substring(0, 1).toUpperCase() + responseModel.getUser().getLastName().substring(1);

            String welcome = "Hi " + firstName + " " + lastName;

            tv_user_welcome.setText(welcome);

            tv_user_welcome_msg.setVisibility(View.VISIBLE);

            hideLoadingView();
//            mRequestModel.setToken(responseModel.getToken());
            preferencesHelper.saveToken(responseModel.getToken());

        }


    }

    @Override
    public void reqFailed(int errorCode, String message) {
        fabProgressCircle.hide();
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

package com.ollycredit.ui.user_profile.user_details;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.card.verify.debit.debit_register.VerifyBankActivity;
import com.ollycredit.ui.card.verify.kyc.kyc_register.AppointKycActivity;
import com.ollycredit.ui.card.verify.pan.VerifyPanActivity;
import com.ollycredit.ui.user_profile.change_password.ChangePasswordActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.HelperClass;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ollycredit.utils.GlobalConstants.APP_FLOW_CLASSIC;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_OPERATIONAL;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_PREPAID;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_PRE_USER;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_SELECT;
import static com.ollycredit.utils.GlobalConstants.LABLE_TO_RESET_PASSWORD;

public class UserProfileActivity extends BaseActivity implements UserProfileView {

    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    UserProfilePresenter userProfilePresenter;


    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;

    @BindView(R.id.no_pre_user_layout)
    LinearLayout no_pre_user_layoutserPhone;

    @BindView(R.id.iv_user_resend_email)
    ImageView ivResendEmail;

    @BindView(R.id.tv_user_email)
    TextView tvUserEmail;


    @BindView(R.id.tv_user_pan)
    TextView tvUserPAN;

    @BindView(R.id.tv_user_pan_status)
    TextView tvPANstatus;

    @BindView(R.id.tv_user_kyc)
    TextView tvUserKyc;

    @BindView(R.id.tv_user_kyc_status)
    TextView tvKycStatus;

    @BindView(R.id.tv_user_email_status)
    TextView tvEmailStatus;

    @BindView(R.id.tv_user_debit_status)
    TextView tvdebitStatus;

    @BindView(R.id.tv_user_debit)
    TextView tvUserDebit;

    @BindView(R.id.fl_change_password)
    FrameLayout flChangePassword;

    @BindView(R.id.fl_user_bank)
    FrameLayout flVerifyDebit;
    @BindView(R.id.fl_user_pan)
    FrameLayout flVerifyPan;
    @BindView(R.id.fl_user_kyc)
    FrameLayout flVerifyKYC;

    @BindView(R.id.appbar_user_profile)
    AppBarLayout userProfileAppbar;


    @BindView(R.id.ctb_user_profile)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.toolbar_user_profile)
    Toolbar toolbar;
    private String green_main = "#11B96C";
    private String cyan_main = "#00aad4";
    private String red_error = "#F5414A";
    private int appFlow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        userProfilePresenter.attachView(this);
        setUp();
    }

    @OnClick(R.id.fl_change_password)
    public void changePassword() {
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_USER_PROFILE,
                GlobalConstants.ACTION_GOTO,
                LABLE_TO_RESET_PASSWORD
        );
        startActivity(new Intent(this, ChangePasswordActivity.class));
    }


    @Override
    protected void setUp() {
        setSupportActionBar(toolbar);
        appFlow = preferencesHelper.getInt(GlobalConstants.USER_APP_FLOW, 0);

        switch (appFlow) {
            case APP_FLOW_PRE_USER:
            case APP_FLOW_PREPAID:
            case APP_FLOW_CLASSIC:
            case APP_FLOW_SELECT:
                no_pre_user_layoutserPhone.setVisibility(View.VISIBLE);
                break;
            case APP_FLOW_OPERATIONAL:
                no_pre_user_layoutserPhone.setVisibility(View.GONE);
                break;
        }


        collapsingToolbarLayout.setTitle(toolbar.getTitle());
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);

        showLoadingView();
        userProfilePresenter.getUserInfo(preferencesHelper.getToken());
    }

    @Override
    public void getUserSuccess(ResponseModel responseModel) {
        hideLoadingView();
        String completeName = preferencesHelper.getString(GlobalConstants.PREF_USER_NAME_KEY, "");
        String firstName = completeName.split(" ")[0].substring(0, 1).toUpperCase() + completeName.split(" ")[0].substring(1);
        String lastName = completeName.split(" ")[1].substring(0, 1).toUpperCase() + completeName.split(" ")[1].substring(1);

        String username = firstName + "\n" + lastName;

        collapsingToolbarLayout.setTitle(username);
        tvUserPhone.setText(responseModel.getUser().getMobileNum().toString());
        tvUserEmail.setText(responseModel.getUser().getEmail());


        boolean terminante = false;


        if (responseModel.getUser().getEmailValidated() == 1) {
            tvEmailStatus.setTextColor(ContextCompat.getColor(this, R.color.green_main));
            tvEmailStatus.setText("Verified");
            ivResendEmail.setVisibility(View.GONE);
        } else {
            tvEmailStatus.setText("Verify Email");
            tvEmailStatus.setTextColor(ContextCompat.getColor(this, R.color.red_error));
            ivResendEmail.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    OllyCreditApplication.getInstance().trackEvent(
                            GlobalConstants.CATEGORY_USER_PROFILE,
                            GlobalConstants.ACTION_GOTO,
                            GlobalConstants.LABLE_VERIFY_EMAIL
                    );


                    userProfilePresenter.resendEmail(preferencesHelper.getToken());
                }
            });
        }


        if (responseModel.getCreditLimitFlags().getIsPanNumberLinked() == 0) {

            tvUserPAN.setTextColor(ContextCompat.getColor(this, R.color.blue_text));
            tvPANstatus.setText("Link your PAN");
            tvPANstatus.setTextColor(Color.parseColor(cyan_main));

            flVerifyPan.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    OllyCreditApplication.getInstance().trackEvent(
                            GlobalConstants.CATEGORY_USER_PROFILE,
                            GlobalConstants.ACTION_GOTO,
                            GlobalConstants.LABLE_VERIFY_PAN
                    );

                    startActivity(new Intent(UserProfileActivity.this, VerifyPanActivity.class));
                    finish();
                }
            });
            terminante = true;


        } else if (responseModel.getCreditLimitFlags().getIsPanNumberLinked() == 1) {
            String pan = String.valueOf(responseModel.getUser().getPanNum());
            tvUserPAN.setText("PAN card (" + pan + ")");
            tvUserPAN.setTextColor(ContextCompat.getColor(this, R.color.blue_text));
            tvPANstatus.setText("Verified");
            tvPANstatus.setTextColor(Color.parseColor(green_main));
            flVerifyPan.setOnClickListener(null);

        }

        if (terminante) {
            return;
        }

        if (responseModel.getCreditLimitFlags().getIsCardLinked() == 0) {
            tvUserDebit.setTextColor(ContextCompat.getColor(this, R.color.blue_text));
            tvdebitStatus.setText("Link bank Account");
            tvdebitStatus.setTextColor(Color.parseColor(cyan_main));
            flVerifyDebit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    OllyCreditApplication.getInstance().trackEvent(
                            GlobalConstants.CATEGORY_USER_PROFILE,
                            GlobalConstants.ACTION_GOTO,
                            GlobalConstants.LABLE_VERIFY_DEBIT
                    );

                    startActivity(new Intent(UserProfileActivity.this, VerifyBankActivity.class));
                    finish();
                }
            });
            terminante = true;

        } else if (responseModel.getCreditLimitFlags().getIsCardLinked() == 1) {
            tvUserDebit.setTextColor(ContextCompat.getColor(this, R.color.blue_text));
            tvdebitStatus.setText("Verified");
            tvdebitStatus.setTextColor(Color.parseColor(green_main));
            flVerifyDebit.setOnClickListener(null);
        }

        if (terminante) {
            return;
        }


        if (responseModel.getCreditLimitFlags().getIsKycLinked() == 0) {

            tvKycStatus.setText("Submit E-Kyc");
            tvUserKyc.setTextColor(ContextCompat.getColor(this, R.color.blue_text));
            tvKycStatus.setTextColor(ContextCompat.getColor(this, R.color.cyan_main));
            flVerifyKYC.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    OllyCreditApplication.getInstance().trackEvent(
                            GlobalConstants.CATEGORY_USER_PROFILE,
                            GlobalConstants.ACTION_GOTO,
                            GlobalConstants.LABLE_VERIFY_EKYC
                    );

                    startActivity(new Intent(UserProfileActivity.this, AppointKycActivity.class));
                    finish();
                }
            });


        } else if (responseModel.getCreditLimitFlags().getIsKycLinked() == 1) {
            tvKycStatus.setText("Verified");
            tvUserKyc.setTextColor(ContextCompat.getColor(this, R.color.blue_text));
            tvKycStatus.setTextColor(ContextCompat.getColor(this, R.color.green_main));
            flVerifyKYC.setOnClickListener(null);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        OllyCreditApplication.getInstance().trackScreenView(this.getClass().getSimpleName());
//        OllyCreditApplication.getInstance().trackEvent(
//                GlobalConstants.CATEGORY_ONBOARDING,
//                GlobalConstants.ACTION_GOTO,
//                GlobalConstants.LABLE_TO_HOME
//        );
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
    public void reqFailed(int failedCode, String message) {
        hideLoadingView();
        showDialogBox(message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().postSticky("object.class");
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_USER_PROFILE,
                GlobalConstants.ACTION_BACK,
                GlobalConstants.LABLE_TO_HOME
        );
    }

    @Override
    public void resendEmail(ResponseModel responseModel) {
        Toast.makeText(this, "Verification mail sent!", Toast.LENGTH_SHORT).show();
    }
}

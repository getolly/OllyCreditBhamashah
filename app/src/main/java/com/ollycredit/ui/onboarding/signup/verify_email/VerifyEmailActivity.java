package com.ollycredit.ui.onboarding.signup.verify_email;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.card.card_home.CardHomeActivity;
import com.ollycredit.utils.GlobalConstants;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class VerifyEmailActivity extends BaseActivity implements VerifyEmailView {


    @BindView(R.id.btn_verify_email_already_confirm)
    Button btnAlreadyConfirm;

    @BindView(R.id.btn_verify_email_resend_edit_mail)
    Button btnEmailResend;

    @BindView(R.id.card_img_mask)
    ImageView cardImage;

    @BindView(R.id.tv_user_email)
    TextView tvUserEmail;

    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    VerifyEmailPresenter verifyEmailPresenter;

    private BroadcastReceiver networkReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        verifyEmailPresenter.attachView(this);
        setUp();
    }

    @OnClick(R.id.btn_verify_email_already_confirm)
    public void onClick(View view) {

        showProgressDialog("Please wait ...");

        verifyEmailPresenter.getUserInfo(preferencesHelper.getToken());


    }

    @Override
    public void onBackPressed() {

        finish();


    }


    @OnClick(R.id.btn_verify_email_resend_edit_mail)
    public void resendEmail() {
        verifyEmailPresenter.resendEmail(preferencesHelper.getToken());
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

        tvUserEmail.setText("Your Card is now being generated. Meanwhile check your inbox and verify your email ID \n("+preferencesHelper.getString(GlobalConstants.PREF_USER_EMAIL_KEY,"")+")");

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(VerifyEmailActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }
            }
        };


        Picasso.with(this).load(R.mipmap.card_image_1024)
                .transform(new CropCircleTransformation()).into((ImageView) findViewById(R.id.card_img_mask));
    }


    @Override
    public void reqFailed(int failedCode, String message) {

        hideProgressDialog();
        showDialogBox(message);
    }

    @Override
    public void getUserSuccess(ResponseModel responseModel) {
        hideProgressDialog();
        if (responseModel.getUser().getEmailValidated() == 1) {
            Intent i;

            i = new Intent(this, CardHomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
            finish();


        } else {

            showDialogBox("Email not verified");
        }
    }

    @Override
    public void resendEmailSuccess(ResponseModel responseModel) {
        Toast.makeText(this, "mail sent!", Toast.LENGTH_SHORT).show();
    }
}

package com.ollycredit.ui.card.verify.pan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.RequestModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.dialogs.CreditIncDialog;
import com.ollycredit.utils.helpers.HelperClass;
import com.ollycredit.utils.helpers.TextFormatHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerifyPanActivity extends BaseActivity implements VerifyPanView {

    @BindView(R.id.tv_verify)
    TextView verifytextView;

    @BindView(R.id.iv_pancard)
    ImageView ivPanIcon;

    @BindView(R.id.til_pannumber)
    TextInputLayout tilPan;

    @BindView(R.id.et_verify_pannumb)
    EditText panEditText;
    @BindView(R.id.fab_verify_pan)
    FloatingActionButton fabSubmitPan;
    @BindView(R.id.appbar_verifypan)
    AppBarLayout appBarVerify;
    @BindView(R.id.toolbar_verifypan)
    Toolbar toolBarVerify;

    @BindView(R.id.fl_loadingview)
    FrameLayout loaingView;
    @BindView(R.id.layot_verify_error)
    LinearLayout layoutVerifyError;
    @Inject
    VerifyPanPresenter verifyPanPresenter;
    @Inject
    PreferencesHelper preferencesHelper;

    @BindView(R.id.ctb_pan)
    CollapsingToolbarLayout ctbToolbar;
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScrollView;

    @BindView(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;


    boolean isExpanded = true;

    private String panNumb;
    private String amount = "2500";
    private BroadcastReceiver networkReceiver;
    private EditText[] editTexts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_pan);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        verifyPanPresenter.attachView(this);
        setUp();


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

    public void showFABprogress(EditText[] inputFields) {
        for (EditText field : inputFields) {
            field.setEnabled(false);
        }
        fabProgressCircle.show();
    }

    public void hideFABprogress(EditText[] inputFields) {
        for (EditText field : inputFields) {
            field.setEnabled(true);
        }
        fabProgressCircle.hide();
    }



    @Override
    protected void setUp() {

        setSupportActionBar(toolBarVerify);
        ctbToolbar.setTitle(toolBarVerify.getTitle());
        ctbToolbar.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);
        styleEditText();

        panEditText.requestFocus();

        //todo on edittext focus show keyboard then strink toolbar

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(VerifyPanActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        OllyCreditApplication.getInstance().trackScreenView(this.getClass().getSimpleName());
    }

    private void styleEditText() {

        tilPan.setErrorEnabled(true);// edit text desc msg
        tilPan.setErrorTextAppearance(R.style.AppTheme_TextNormalAppearance);
        tilPan.setHintTextAppearance(R.style.AppTheme_TextCorrectAppearance);
        tilPan.setError("Please enter PAN which belongs to you as we will be validating it");//msg
        ivPanIcon.setColorFilter(ContextCompat.getColor(VerifyPanActivity.this, R.color.blue_text));

        panEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (panEditText.hasFocus()) {
                    ivPanIcon.setColorFilter(ContextCompat.getColor(VerifyPanActivity.this, R.color.cyan_main));
                    tilPan.setHintTextAppearance(R.style.AppTheme_TextCorrectAppearance);
                    ivPanIcon.setColorFilter(ContextCompat.getColor(VerifyPanActivity.this, R.color.cyan_main));
                    nestedScrollView.scrollTo(0, panEditText.getBottom());
                }

                if (!panEditText.hasFocus()) {

                    tilPan.setErrorTextAppearance(R.style.AppTheme_TextNormalAppearance);
                    tilPan.setHintTextAppearance(R.style.AppTheme_TextCorrectAppearance);
                    tilPan.setError("Please enter PAN which belongs to you as we will be validating it");//msg
                    ivPanIcon.setColorFilter(ContextCompat.getColor(VerifyPanActivity.this, R.color.blue_text));
                    validate(panEditText.getText().toString());
                }
            }
        });


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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.fab_verify_pan)
    public void validateOnclick() {
        panNumb = panEditText.getText().toString().trim();
        panEditText.clearFocus();

        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_VERIFY_DOCUMENTS,
                GlobalConstants.ACTION_BUTTON_CLICKED,
                GlobalConstants.LABLE_VERIFY_PAN
        );

        if (validate(panNumb)) {

            editTexts = new EditText[]{panEditText};

            showFABprogress(editTexts);
//            loaingView.setVisibility(View.VISIBLE);
            HelperClass.hideKeyboard(this);
            //showProgressDialog("Verifying...");
            RequestModel requestModel = new RequestModel();
            requestModel.setToken(preferencesHelper.getToken());
            requestModel.setPan(panNumb);
            requestModel.setFname(preferencesHelper.getString(GlobalConstants.PREF_USER_NAME_KEY, "").split(" ")[0]);
            requestModel.setLast(preferencesHelper.getString(GlobalConstants.PREF_USER_NAME_KEY, "").split(" ")[1]);
            verifyPanPresenter.verifPan(requestModel);

        }


    }

    private boolean validate(String panNumb) {
        if (TextUtils.isEmpty(panNumb)) {
            tilPan.setError(getString(R.string.empty_field) + ", please enter PAN which belongs to you");

            ivPanIcon.setColorFilter(ContextCompat.getColor(VerifyPanActivity.this, R.color.red_error));
            tilPan.setErrorTextAppearance(R.style.AppTheme_TextErrorAppearance);
            tilPan.setHintTextAppearance(R.style.AppTheme_TextErrorAppearance);
            return false;
        }

        if (panNumb.trim().length() != 10) {
            tilPan.setError("Enter valid 10-digit PAN number");
            ivPanIcon.setColorFilter(ContextCompat.getColor(VerifyPanActivity.this, R.color.red_error));
            tilPan.setErrorTextAppearance(R.style.AppTheme_TextErrorAppearance);
            tilPan.setHintTextAppearance(R.style.AppTheme_TextErrorAppearance);
            return false;
        }

        ivPanIcon.setColorFilter(ContextCompat.getColor(VerifyPanActivity.this, R.color.cyan_main));
        tilPan.setHintTextAppearance(R.style.AppTheme_TextCorrectAppearance);
        tilPan.setError("Please enter PAN which belongs to you as we will be validating it");
        return true;
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void panVerifyReq(final ResponseModel responseModel) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                verifyPanPresenter.getUserInfo(responseModel.getToken());

            }
        }, 5000);


    }


    @Override
    public void reqFailed(int failedCode, String message) {
        //hideProgressDialog();
        hideFABprogress(editTexts);
        //loaingView.setVisibility(View.GONE);
        panEditText.requestFocus();
        if (failedCode == GlobalConstants.NETWORK_REQUEST_CALL_FAILED) {
            showDialogBox(message);
        }

        if (failedCode == GlobalConstants.NETWORK_REQUEST_VERIFY_PAN) {
            showDialogBox(message);
        }

        if (failedCode == GlobalConstants.NETWORK_REQUEST_VERIFY_USER_PAN) {
            showDialogBox(message);
        }


    }

    @Override
    public void userInfoReq(ResponseModel responseModel) {

        amount = TextFormatHelper.indianRupeesFormat((double) (responseModel.getUser().getCreditLimit()));
        //hideProgressDialog();
        hideFABprogress(editTexts);
        //loaingView.setVisibility(View.GONE);
        panEditText.requestFocus();
        if (responseModel.getCreditLimitFlags().getIsPanNumberLinked() == 1) {
            CreditIncDialog creditIncDialog = new CreditIncDialog(this, amount, R.drawable.img_happy);
            creditIncDialog.setCancelable(false);
            creditIncDialog.show();

            preferencesHelper.putInt(GlobalConstants.PREF_PAN_VERIFIED, 1);
            //preferencesHelper.putString(GlobalUtils.PREF_BAL, responseModel.getUser().get());
            //preferencesHelper.putString(GlobalUtils.PREF_USED, "\u20B9 " + (responseModel.getUser().getCreditLimit() - Double.parseDouble(responseModel.getUser().ge())));
            preferencesHelper.putLong(GlobalConstants.PREF_MAX, responseModel.getUser().getMaxCreditLimit());
            preferencesHelper.putLong(GlobalConstants.PREF_C_LIMIT, responseModel.getUser().getCreditLimit());
        } else {
            //showDialogBox("PAN Number is not valid");
            tilPan.setError("Please enter PAN which belongs to you as we will be validating it");
            tilPan.setErrorTextAppearance(R.style.AppTheme_TextNormalAppearance);
            tilPan.setHintTextAppearance(R.style.AppTheme_TextCorrectAppearance);
            ivPanIcon.setColorFilter(ContextCompat.getColor(VerifyPanActivity.this, R.color.cyan_main));
            verifytextView.setVisibility(View.GONE);
            layoutVerifyError.setVisibility(View.VISIBLE);
        }

    }
}

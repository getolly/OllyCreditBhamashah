package com.ollycredit.ui.user_profile.change_password;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.ChangePassResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.utils.GlobalConstants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity implements ChangePasswordView {


    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    ChangePasswordPresenter changePasswordPresenter;

    @BindView(R.id.appbar_user_profile)
    AppBarLayout userProfileAppbar;


    @BindView(R.id.ctb_user_profile)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.toolbar_user_profile)
    Toolbar toolbar;

    @BindView(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;

    @BindView(R.id.fab_user_submitPassword)
    FloatingActionButton fabSubmitPassword;


    @BindView(R.id.til_user_current_password)
    TextInputLayout tilCurrentPassword;

    @BindView(R.id.til_user_new_password)
    TextInputLayout tilNewPassword;


    @BindView(R.id.et_user_current_password)
    EditText et_currentPassword;

    @BindView(R.id.et_user_new_password)
    EditText et_newPassword;


    EditText[] inputFlields;
    TextInputLayout[] tilList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        changePasswordPresenter.attachView(this);
        setUp();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    protected void setUp() {
        setSupportActionBar(toolbar);

        collapsingToolbarLayout.setTitle(toolbar.getTitle());
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);
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

    @OnClick(R.id.fab_user_submitPassword)
    public void onFABclicked() {

        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_USER_PROFILE,
                GlobalConstants.ACTION_RESET,
                GlobalConstants.LABLE_RESET_PASSWORD
        );

        String oldPass = et_currentPassword.getText().toString().trim();
        String newPass = et_newPassword.getText().toString().trim();

        inputFlields = new EditText[]{et_currentPassword, et_newPassword};
        tilList = new TextInputLayout[]{tilCurrentPassword, tilNewPassword};


        if (validate(inputFlields, tilList)) {
            showFABprogress(inputFlields);
            ChangePassResponseModel mRequestModel = new ChangePassResponseModel();

            mRequestModel.setOldPassword(oldPass);
            mRequestModel.setNewPassword(newPass);
            mRequestModel.setToken(preferencesHelper.getToken());
            changePasswordPresenter.changeUserPassword(mRequestModel);

        }

    }

    private boolean validate(EditText[] editTexts, TextInputLayout[] inputLayouts) {

        for (int i = 0; i < editTexts.length; i++) {

            if (editTexts[i].getText().toString().trim().isEmpty()) {
                inputLayouts[i].setError(getString(R.string.empty_field));
                return false;
            }


            if (editTexts[i].getText().toString().trim().length() < 6) {
                inputLayouts[i].setError(getString(R.string.invalid_entry));
                return false;

            }

            inputLayouts[i].setError(null);

        }
        return true;
    }


    @Override
    public void reqFailed(int failedCode, String message) {
        showDialogBox(message);
        hideFABprogress(inputFlields);
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
    public void reqSuccess(ChangePassResponseModel responseModel) {

        Toast.makeText(this, "Password Changed", Toast.LENGTH_SHORT).show();
        finish();
    }
}

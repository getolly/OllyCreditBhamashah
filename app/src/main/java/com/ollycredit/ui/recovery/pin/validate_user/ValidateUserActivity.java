package com.ollycredit.ui.recovery.pin.validate_user;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.pattern.MaskPattern;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.onboarding.signup.create_pin.CreatePinActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.HelperClass;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ValidateUserActivity extends BaseActivity implements ValidateUserView, OnFocusChangeListener {

    @BindView(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;

    @BindView(R.id.edit_valuser_firstname)
    EditText firstNameEditText;
    @BindView(R.id.edit_valuser_adhar)
    EditText aadharOrPanEditText;
    @BindView(R.id.edit_valuser_lastname)
    EditText lastNameEditText;
    @BindView(R.id.edit_valuser_dob)
    EditText dobEditText;

    @BindView(R.id.til_firstname)
    TextInputLayout firstnameTIL;
    @BindView(R.id.til_lastname)
    TextInputLayout lastnameTIL;
    @BindView(R.id.til_dob)
    TextInputLayout dobTIL;
    @BindView(R.id.til_pan_or_adhar)
    TextInputLayout aadharOrPanTIL;


    @BindView(R.id.iv_calender_dob)
    ImageView iv_calender_dob;


    @BindView(R.id.toolbar_valuser)
    Toolbar navToolbar;

    @BindView(R.id.fab_valuser_submit)
    FloatingActionButton submitDetailsFAB;

    @Inject
    ValidateUserPresenter validateUserPresenter;

    @Inject
    PreferencesHelper preferencesHelper;

    private BroadcastReceiver networkReceiver;
    private EditText[] editTexts;
    private String shouldbeFirstName;
    private String shouldbeLastName;
    private String shouldbeDOB;
    private String shouldbeAadhar;
    private String shouldbePAN;
    private DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validte_user);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        validateUserPresenter.attachView(this);
        setUp();
    }

    @Override
    protected void setUp() {

        navToolbar.setTitle(" ");
        setSupportActionBar(navToolbar);

        firstNameEditText.setOnFocusChangeListener(this);
        lastNameEditText.setOnFocusChangeListener(this);
        dobEditText.setOnFocusChangeListener(this);
        aadharOrPanEditText.setOnFocusChangeListener(this);


        SimpleMaskFormatter smf = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(dobEditText, smf);
        dobEditText.addTextChangedListener(mtw);


        MaskPattern mp03 = new MaskPattern("[0-3]");
        MaskPattern mp09 = new MaskPattern("[0-9]");
        MaskPattern mp01 = new MaskPattern("[0-1]");

        MaskFormatter mf = new MaskFormatter("[0-3][0-9]/[0-1][0-9]/[0-9][0-9][0-9][0-9]");

        mf.registerPattern(mp01);
        mf.registerPattern(mp03);
        mf.registerPattern(mp09);

        dobEditText.addTextChangedListener(new MaskTextWatcher(dobEditText, mf));


        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(ValidateUserActivity.this);
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
            HelperClass.tintMenuIcon(this, menuItem, R.color.colorPrimary);
        }

        return true;
    }


    @Override
    public void reqSuccess(ResponseModel responseModel) {

//        shouldbeFirstName = responseModel.getUser().getFirstName();
//        shouldbeLastName = responseModel.getUser().getLastName();
        shouldbeDOB = String.valueOf(responseModel.getUser().getDateOfBirth());
//        shouldbePAN = String.valueOf(responseModel.getUser().getPanNum());
        //shouldbeAadhar = String.valueOf(responseModel.getUser().getPanNum()); ;

//        String firstName = firstNameEditText.getText().toString().trim();
//        String lastName = lastNameEditText.getText().toString().trim();
        String dateOfBirth = dobEditText.getText().toString().trim();
        //String aadharOrPan = lastNameEditText.getText().toString().trim();


        Log.e("shouldbe", shouldbeDOB);
        preferencesHelper.saveToken(responseModel.getToken());

        StringBuilder shouldbeYear = new StringBuilder();
        shouldbeYear.append(shouldbeDOB.charAt(0));
        shouldbeYear.append(shouldbeDOB.charAt(1));
        shouldbeYear.append(shouldbeDOB.charAt(2));
        shouldbeYear.append(shouldbeDOB.charAt(3));


        StringBuilder dobYear = new StringBuilder();
        dobYear.append(dateOfBirth.charAt(6));
        dobYear.append(dateOfBirth.charAt(7));
        dobYear.append(dateOfBirth.charAt(8));
        dobYear.append(dateOfBirth.charAt(9));

        StringBuilder shouldbeMonth = new StringBuilder();
        shouldbeMonth.append(shouldbeDOB.charAt(5));
        shouldbeMonth.append(shouldbeDOB.charAt(6));

        StringBuilder dobMonth = new StringBuilder();
        dobMonth.append(dateOfBirth.charAt(3));
        dobMonth.append(dateOfBirth.charAt(4));

        StringBuilder shouldbeDay = new StringBuilder();
        shouldbeDay.append(shouldbeDOB.charAt(8));
        shouldbeDay.append(shouldbeDOB.charAt(9));

        StringBuilder dobDay = new StringBuilder();
        dobDay.append(dateOfBirth.charAt(0));
        dobDay.append(dateOfBirth.charAt(1));

        Log.e("shouldbe", shouldbeYear + " " + shouldbeMonth + " " + shouldbeDay);
        Log.e("dobUser", dobYear + " " + dobMonth + " " + dobDay);

        if (shouldbeYear.toString().equals(dobYear.toString())) {
            if (shouldbeMonth.toString().equals(dobMonth.toString())) {
                if (shouldbeDay.toString().equals(dobDay.toString())) {
                    Intent i = new Intent(ValidateUserActivity.this, CreatePinActivity.class);
                    i.putExtra("number_progress_needed", false);
                    //Overiding flow to update the pincode using PUT and not POSTing the pincode
                    i.putExtra("flow", GlobalConstants.APP_FLOW_OPERATIONAL);
                    startActivity(i);
                } else {
                    hideFABprogress(editTexts);
                    showDialogBox("Your details doesn't match with the details we have");
                }

            } else {
                hideFABprogress(editTexts);
                showDialogBox("Your details doesn't match with the details we have");
            }

        } else {
            hideFABprogress(editTexts);
            showDialogBox("Your details doesn't match with the details we have");
        }


    }

    @Override
    public void reqFailed(int errorCode, String message) {
        //hideFABprogress(editTexts);
        hideFABprogress(editTexts);
        hideLoadingView();
        if (message.toLowerCase().contains("index")){
           message = "Invalid date, please try again";
        }
        showDialogBox(message);
    }


    @OnClick(R.id.iv_calender_dob)
    public void onCalenderClicked() {

        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_RECOVER_CREDENTIALS,
                GlobalConstants.ACTION_GOTO,
                GlobalConstants.LABLE_TO_RECOVER_PIN
        );

        final Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                //newCalendar.set(startYear, startMonth, startDate);
                String finaldate1 = HelperClass.converPickerDate(dayOfMonth, monthOfYear, year);
                dobEditText.setText(finaldate1);
                dobTIL.setError(null);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideErrorConnection();
        fabProgressCircle.hide();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.fab_valuser_submit)
    public void onPressSubmitFAB(View v) {

        //editTexts = new EditText[]{firstNameEditText, lastNameEditText, dobEditText, aadharOrPanEditText};
        editTexts = new EditText[]{dobEditText};
        //TextInputLayout[] inputLayouts = {firstnameTIL, lastnameTIL, dobTIL, aadharOrPanTIL};
        TextInputLayout[] inputLayouts = {dobTIL};


        if (validate(editTexts, inputLayouts)) {

            validateUserPresenter.getUserInfo(preferencesHelper.getToken());
            showFABprogress(editTexts);

        }


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

    private boolean validate(EditText[] editTexts, TextInputLayout[] inputLayouts) {

        for (int i = 0; i < editTexts.length; i++) {

            if (editTexts[i].getTag().toString().equals("dob")) {

                if (editTexts[i].getText().toString().trim().isEmpty()) {
                    inputLayouts[i].setError(getString(R.string.empty_field));
                    return false;
                }

                if (editTexts[i].getText().toString().trim().length() < 8) {
                    inputLayouts[i].setError(getString(R.string.invalid_entry));
                    return false;
                }

                inputLayouts[i].setError(null);

            }


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
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.edit_valuser_firstname:
                if (firstNameEditText.hasFocus()) {
                    firstnameTIL.setError(null);

                }
                if (!hasFocus) {
                    if (firstNameEditText.getText().toString().trim().isEmpty() || firstNameEditText.getText().toString().trim().length() == 0) {
                        setTILError(firstNameEditText.getTag().toString(), getString(R.string.empty_field));
                        return;
                    }
                    if (firstNameEditText.getText().toString().trim().length() < 3) {
                        setTILError(firstNameEditText.getTag().toString(), getString(R.string.invalid_entry));
                        return;
                    }
                    firstnameTIL.setError(null);

                }

                break;

            case R.id.edit_valuser_lastname:
                if (lastNameEditText.hasFocus()) {
                    // removeTILError(lastNameEditText.getTag().toString());

                }
                if (!hasFocus) {
                    if (lastNameEditText.getText().toString().trim().isEmpty() || lastNameEditText.getText().toString().trim().length() == 0) {
                        setTILError(lastNameEditText.getTag().toString(), getString(R.string.empty_field));
                        return;
                    }
                    if (lastNameEditText.getText().toString().trim().length() < 3) {
                        setTILError(lastNameEditText.getTag().toString(), getString(R.string.invalid_entry));
                        return;
                    }
                    lastnameTIL.setError(null);
                }

                break;

            case R.id.edit_valuser_dob:
//                if (hasFocus) {
//                    showDateDiloagBox();
//                }

                if (hasFocus) {

                    //removeTILError(dobEditText.getTag().toString());

                }

                if (!hasFocus) {
                    if (dobEditText.getText().toString().trim().isEmpty() || dobEditText.getText().toString().trim().length() == 0) {
                        setTILError(dobEditText.getTag().toString(), getString(R.string.empty_field));
                        return;
                    }
                    if (dobEditText.getText().toString().trim().length() < 10) {
                        setTILError(dobEditText.getTag().toString(), getString(R.string.invalid_entry));
                        return;
                    }
                    dobTIL.setError(null);
                }
                break;

            case R.id.edit_valuser_adhar:
//                if (hasFocus) {
//                    showDateDiloagBox();
//                }

                if (hasFocus) {
                    //svUserDetail.scrollTo(0, tvScrollTo.getBottom());

                    //removeTILError(dobEditText.getTag().toString());

                }

                if (!hasFocus) {
                    if (aadharOrPanEditText.getText().toString().trim().isEmpty() || dobEditText.getText().toString().trim().length() == 0) {
                        setTILError(dobEditText.getTag().toString(), getString(R.string.empty_field));
                        return;
                    }
                    aadharOrPanTIL.setError(null);
                }
                break;
        }

    }


    private void setTILError(String tag, String message) {
        TextInputLayout[] TextInputs = {firstnameTIL, lastnameTIL, dobTIL, aadharOrPanTIL};

        for (TextInputLayout inputLayout : TextInputs) {
            if (inputLayout.getTag().toString().equalsIgnoreCase(tag)) {
                inputLayout.setErrorEnabled(true);
                // inputLayout.setHintTextAppearance(R.style.AppTheme_TextErrorAppearance);
                inputLayout.setError(message);
            }
        }

    }


    private void removeTILError(String tag) {

        TextInputLayout[] TextInputs = {firstnameTIL, lastnameTIL, dobTIL, aadharOrPanTIL};

        for (TextInputLayout inputLayout : TextInputs) {
            if (inputLayout.getTag().toString().equalsIgnoreCase(tag)) {
                // inputLayout.setErrorEnabled(false);
                inputLayout.setHintTextAppearance(R.style.AppTheme_TextCorrectAppearance);
                inputLayout.setError(null);
            }
        }

    }


}

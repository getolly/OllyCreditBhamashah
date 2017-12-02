package com.ollycredit.ui.onboarding.signup.user_details.step_one;

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
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.pattern.MaskPattern;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.gson.Gson;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.RequestModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.onboarding.signup.create_pin.CreatePinActivity;
import com.ollycredit.utils.DateUtils;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.EventBusHelper;
import com.ollycredit.utils.helpers.HelperClass;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserRegistrationActivity extends BaseActivity implements OnFocusChangeListener,
        UserRegistrationView {

    @BindView(R.id.text_user_progress)
    TextView progressTitleTextView;

    @BindView(R.id.iv_calender_dob)
    ImageView ivCalender;

    @BindView(R.id.sv_userdetailone)
    ScrollView svUserDetail;

    @BindView(R.id.tv_scollto)
    TextView tvScrollTo;

    @BindView(R.id.edit_detail_one_firstname)
    EditText firstNameEditText;

    @BindView(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;

    @BindView(R.id.edit_detail_one_lastname)
    EditText lastNameEditText;

    @BindView(R.id.edit_detail_one_dob)
    EditText dobEditText;

    @BindView(R.id.edit_user_email)
    EditText emailEditText;

    @BindView(R.id.et_verify_pannumb)
    EditText panNumbEditText;

    @BindView(R.id.til_firstname)
    TextInputLayout firstnameTIL;

    @BindView(R.id.til_lastname)
    TextInputLayout lastnameTIL;

    @BindView(R.id.til_dob)
    TextInputLayout dobTIL;

    @BindView(R.id.til_detail_email)
    TextInputLayout emailTIL;

    @BindView(R.id.til_pannumber)
    TextInputLayout panNumberTIL;

    @BindView(R.id.toolbar_details_one)
    Toolbar navToolbar;

    @BindView(R.id.fab_user_submitDetails)
    FloatingActionButton submitDetailsFAB;

    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    UserRegistrationPresenter userRegistrationPresenter;

    @GlobalConstants.AppFlow
    private int appFlow;

    private RequestModel mRequestModel;
    private DatePickerDialog datePickerDialog;
    private String finaldate;
    int year_dif;
    private BroadcastReceiver networkReceiver;

    public final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$");
    private EditText[] inputFields;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        userRegistrationPresenter.attachView(this);

        setUp();

    }

    @Override
    @SuppressWarnings("ResourceType")
    protected void setUp() {

        appFlow = preferencesHelper.getInt(GlobalConstants.USER_APP_FLOW, 0);

        setupScreen();

        showLoadingView();

        userRegistrationPresenter.reqAutoFill(preferencesHelper.getToken());

        firstNameEditText.setOnFocusChangeListener(this);
        lastNameEditText.setOnFocusChangeListener(this);
        dobEditText.setOnFocusChangeListener(this);

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
                boolean isOnline = isOnline(UserRegistrationActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }
            }
        };


    }

    private void setupScreen() {

        if (appFlow == GlobalConstants.APP_FLOW_PREPAID) {
            panNumberTIL.setVisibility(View.GONE);
        }

        String progress = "<font color='#1E2F4D'>2/</font>";
        String outof = "<font color='#728290'>3</font>";
        progressTitleTextView.setText(Html.fromHtml(progress + outof));

        setSupportActionBar(navToolbar);


    }

    @OnClick(R.id.iv_calender_dob)
    public void onCalenderClicked() {

        final Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                String finaldate1 = HelperClass.converPickerDate(dayOfMonth, monthOfYear, year);
                dobEditText.setText(finaldate1);
                dobTIL.setError(null);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

    @OnClick(R.id.fab_user_submitDetails)
    public void onPressSubmitFAB(View v) {

        inputFields = new EditText[]{firstNameEditText, lastNameEditText, dobEditText, emailEditText, panNumbEditText};

        if (validateInputs()) {

            showFABprogress(inputFields);

            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String dob = dobEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();

            try {
                Date initDate = new SimpleDateFormat("dd/MM/yyyy").parse(dob);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                dob = formatter.format(initDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            preferencesHelper.saveToken(mRequestModel.getToken());

            mRequestModel.setFname(firstName);
            mRequestModel.setLast(lastName);
            mRequestModel.setDob(dob);
            mRequestModel.setEmail(email);

            Log.d("mytag", "going");

            if (appFlow == GlobalConstants.APP_FLOW_PREPAID) {
                Log.d("mytag", "onbaorindg");
                userRegistrationPresenter.completeRegistration(mRequestModel);
            } else {
                Log.d("mytag", "nooo");
                String pan = panNumbEditText.getText().toString().trim();
                mRequestModel.setPan(pan);
                userRegistrationPresenter.checkPanStatus(mRequestModel.getToken(), true,
                        mRequestModel);
            }

        }

    }

    private boolean validateInputs() {

        finaldate = dobEditText.getText().toString();

        if (dobEditText.length() == 10) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = new Date();
            try {
                startDate = df.parse(finaldate);

                df.setLenient(false);

                Calendar calendar = new GregorianCalendar();
                calendar.setTime(startDate);
                int year = calendar.get(Calendar.YEAR);
                year_dif = 2017 - year;

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        dobTIL.setError(null);
        firstnameTIL.setError(null);
        lastnameTIL.setError(null);

        ArrayList<EditText> editTexts = new ArrayList<>();
        editTexts.add(firstNameEditText);
        editTexts.add(lastNameEditText);
        editTexts.add(dobEditText);
        editTexts.add(emailEditText);

        if (appFlow != GlobalConstants.APP_FLOW_PREPAID) {
            editTexts.add(panNumbEditText);
        }

        for (EditText editText : editTexts) {

            // removeTILError(editText.getTag().toString());

            if (editText.getText().toString().trim().isEmpty() || editText.getText().toString().trim().length() == 0) {
                setTILError(editText.getTag().toString(), getString(R.string.empty_field));

                return false;
            }


            if (!editText.getTag().toString().equalsIgnoreCase("dob")) {
                if (editText.getText().toString().trim().length() < 3) {
                    setTILError(editText.getTag().toString(), getString(R.string.invalid_entry));

                    return false;
                }
            } else if (editText.getTag().toString().equalsIgnoreCase("dob")) {
                if (finaldate.isEmpty() || finaldate.length() == 0) {
                    setTILError(editText.getTag().toString(), getString(R.string.empty_field));

                }
                if (finaldate.length() < 10 || !isDateValid(finaldate)) {
                    setTILError(editText.getTag().toString(), "Invalid date");

                    return false;
                }
                if (year_dif < 18 || year_dif > 65) {
                    setTILError(editText.getTag().toString(), "Age is not valid");

                    return false;
                }

            }

            if (editText.getTag().toString().equalsIgnoreCase("email")) {

                if (!validateEmail(editText.getText().toString().trim())) {
                    setTILError(editText.getTag().toString(), "Invalid email");
                    return false;
                }

            }

            if (editText.getTag().toString().equalsIgnoreCase("pan")) {

                if (editText.getText().toString().length() != 10) {
                    setTILError(editText.getTag().toString(), "Invalid PAN number");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void reqCompleteSuccess(ResponseModel responseModel) {

        Intent intent = new Intent(UserRegistrationActivity.this, CreatePinActivity.class);
        intent.putExtra("fname", mRequestModel.getFname());
        intent.putExtra("lname", mRequestModel.getLast());
        intent.putExtra("dob", mRequestModel.getDob());
        startActivity(intent);
    }


    @Override
    public void reqAutoFillSuccess(ResponseModel responseModel) {

        hideLoadingView();
        firstNameEditText.setText(responseModel.getPreuser().getFirstName());
        lastNameEditText.setText(responseModel.getPreuser().getLastName());
        dobEditText.setText(responseModel.getPreuser().getDob() == null ? "" : DateUtils.
                convertDatePattern(responseModel.getPreuser().getDob(), "dd/MM/yyyy"));
        emailEditText.setText(responseModel.getPreuser().getEmail());


    }

    @Override
    public void reqPanSuccess(ResponseModel responseModel) {

        preferencesHelper.saveToken(responseModel.getToken());
        userRegistrationPresenter.completeRegistration(mRequestModel);
    }

    @Override
    public void reqPanAlreadyVerified(ResponseModel responseModel) {
        preferencesHelper.saveToken(responseModel.getToken());
        userRegistrationPresenter.completeRegistration(mRequestModel);
    }

    @Override
    public void reqUserInfoSuccess(ResponseModel responseModel) {
        hideFABprogress(inputFields);
        preferencesHelper.saveToken(responseModel.getToken());
        if (responseModel.getCreditLimitFlags().getIsPanNumberLinked() == 1) {
            userRegistrationPresenter.completeRegistration(mRequestModel);
        } else {
            panNumberTIL.setError("PAN number invalid/your details doesn't match ");
        }

    }

    public static boolean isDateValid(String date) {
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

    private void setTILError(String tag, String message) {
        TextInputLayout[] TextInputs = {firstnameTIL, lastnameTIL, dobTIL, panNumberTIL, emailTIL};

        for (TextInputLayout inputLayout : TextInputs) {
            if (inputLayout.getTag().toString().equalsIgnoreCase(tag)) {
                inputLayout.setErrorEnabled(true);
                // inputLayout.setHintTextAppearance(R.style.AppTheme_TextErrorAppearance);
                inputLayout.setError(message);
            }
        }

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
        fabProgressCircle.hide();
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

    @Subscribe(priority = 1, threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventBusHelper eventBusHelper) {
        mRequestModel = eventBusHelper.getRequestModel();
        Log.e("user details1", new Gson().toJson(mRequestModel));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.edit_detail_one_firstname:
                if (firstNameEditText.hasFocus()) {
                    svUserDetail.scrollTo(0, tvScrollTo.getBottom());
                    firstnameTIL.setError(null);
                    //removeTILError(firstNameEditText.getTag().toString());

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

            case R.id.edit_detail_one_lastname:
                if (lastNameEditText.hasFocus()) {
                    svUserDetail.scrollTo(0, tvScrollTo.getBottom());
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

            case R.id.edit_detail_one_dob:
//                if (hasFocus) {
//                    showDateDiloagBox();
//                }

                if (hasFocus) {
                    svUserDetail.scrollTo(0, tvScrollTo.getBottom());

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
        }


    }

    @Override
    public void reqFailed(int failedCode, String message) {
        hideFABprogress(inputFields);
        showDialogBox(message);
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



}

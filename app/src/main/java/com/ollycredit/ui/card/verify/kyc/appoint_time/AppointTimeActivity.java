package com.ollycredit.ui.card.verify.kyc.appoint_time;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.gson.Gson;
import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.kyc.KycRequestModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.card.verify.kyc.appoint_address.AppointAddressActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.EventBusHelper;
import com.ollycredit.utils.helpers.HelperClass;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointTimeActivity extends BaseActivity implements AppointTimeView {


    @BindView(R.id.edit_appoint_date_from)
    EditText etDateFrom;

    @BindView(R.id.iv_calender)
    ImageView ivDate;

    @BindView(R.id.iv_clock)
    ImageView ivClock;

    @BindView(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;


    @BindView(R.id.til_appoint_date_from)
    TextInputLayout tilDateFrom;
    @BindView(R.id.til_appoint_time)
    TextInputLayout tilTime;

    @BindView(R.id.til_appoint_date_to)
    TextInputLayout tilDateTo;

    @BindView(R.id.edit_appoint_date_to)
    EditText etDateTo;

    @BindView(R.id.ctb_kyc)
    CollapsingToolbarLayout ctbToolbar;


    @BindView(R.id.edit_appoint_time)
    EditText etTime;

    @BindView(R.id.fab_appoint_submit)
    FloatingActionButton submitFAB;

    @BindView(R.id.toolbar_appoint)
    Toolbar navToolbar;

    @Inject
    AppointTimePresenter appointTimePresenter;

    @Inject
    PreferencesHelper preferencesHelper;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private String finaltime = GlobalConstants.DEFAULT_VALUE;

    private KycRequestModel requestModel;
    private String finaldate1 = GlobalConstants.DEFAULT_VALUE;
    private String finaldate2 = GlobalConstants.DEFAULT_VALUE;
    private String progress;
    private int startDate = 0;
    private int endDate = 0;
    private int startMonth = 0;
    private int startYear = 0;
    private BroadcastReceiver networkReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_time);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        appointTimePresenter.attachView(this);
        setUp();

    }

    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideErrorConnection();
    }


    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(networkReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        OllyCreditApplication.getInstance().trackScreenView(this.getClass().getSimpleName());
    }

    @Override
    protected void setUp() {
        setSupportActionBar(navToolbar);
        ctbToolbar.setTitle(navToolbar.getTitle());
        ctbToolbar.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(AppointTimeActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }
            }
        };

    }

    @OnClick(R.id.edit_appoint_date_from)
    public void showDateFrom() {
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_VERIFY_DOCUMENTS,
                GlobalConstants.ACTION_BUTTON_CLICKED,
                GlobalConstants.LABLE_APPOINT_EKYC_DATETIME
        );
        fromDateDiloagBox();
    }


    @OnClick(R.id.edit_appoint_date_to)
    public void showDateTO() {
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_VERIFY_DOCUMENTS,
                GlobalConstants.ACTION_BUTTON_CLICKED,
                GlobalConstants.LABLE_APPOINT_EKYC_DATETIME
        );
        if (startDate == 0) {
            Toast.makeText(AppointTimeActivity.this, "please select From date first", Toast.LENGTH_SHORT).show();
        } else {
            toDateDiloagBox();
        }
    }

    @OnClick(R.id.edit_appoint_time)
    public void showTime() {
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_VERIFY_DOCUMENTS,
                GlobalConstants.ACTION_BUTTON_CLICKED,
                GlobalConstants.LABLE_APPOINT_EKYC_DATETIME
        );
        showTimeDiloagBox();
    }

    @OnClick(R.id.fab_appoint_submit)
    public void validateOnclick() {

        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_VERIFY_DOCUMENTS,
                GlobalConstants.ACTION_BUTTON_CLICKED,
                GlobalConstants.LABLE_TO_APPOINT_EKYC_ADDRESS
        );
        if (validate()) {
            requestModel.setAppointKYCDateOne(finaldate1);
            requestModel.setAppointKYCDateTwo(finaldate2);

            requestModel.setAppointKYCTime(finaltime);

            Log.e("appoint time", new Gson().toJson(requestModel));

            EventBus.getDefault().postSticky(new EventBusHelper(requestModel));
            startActivity(new Intent(AppointTimeActivity.this, AppointAddressActivity.class));
            finish();


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



    private boolean validate() {

        if (finaldate1.contains(GlobalConstants.DEFAULT_VALUE)) {
            tilDateFrom.setHintTextAppearance(R.style.AppTheme_TextErrorAppearance);
            ivDate.setColorFilter(ContextCompat.getColor(this, R.color.red_error));
            return false;
        }

        if (finaldate2.contains(GlobalConstants.DEFAULT_VALUE)) {
            tilDateTo.setHintTextAppearance(R.style.AppTheme_TextErrorAppearance);
            ivDate.setColorFilter(ContextCompat.getColor(this, R.color.red_error));
            return false;
        }

        if (finaltime.contains(GlobalConstants.DEFAULT_VALUE)) {
            tilTime.setHintTextAppearance(R.style.AppTheme_TextErrorAppearance);
            ivClock.setColorFilter(ContextCompat.getColor(this, R.color.red_error));
            return false;
        }

        return true;
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

    private void fromDateDiloagBox() {

        final Calendar newCalendar = Calendar.getInstance();
        tilDateFrom.setHintTextAppearance(R.style.AppTheme_TextNormalAppearance);
        ivDate.setColorFilter(ContextCompat.getColor(this, R.color.gray_main_alpha_50));
        datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                startDate = dayOfMonth;
                startMonth = monthOfYear;
                startYear = year;
                //newCalendar.set(startYear, startMonth, startDate);
                finaldate1 = HelperClass.converPickerDate(dayOfMonth, monthOfYear, year);
                etDateFrom.setText(finaldate1);
                etDateFrom.setError(null);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        newCalendar.add(Calendar.DAY_OF_YEAR, 3);
        datePickerDialog.getDatePicker().setMinDate(newCalendar.getTimeInMillis());

        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }


    private void toDateDiloagBox() {
        ivDate.setColorFilter(ContextCompat.getColor(this, R.color.gray_main_alpha_50));
        final Calendar newCalendar = Calendar.getInstance();
        tilDateTo.setHintTextAppearance(R.style.AppTheme_TextNormalAppearance);
        datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                //newCalendar.set(year, monthOfYear, dayOfMonth);
                endDate = dayOfMonth;
                finaldate2 = HelperClass.converPickerDate(dayOfMonth, monthOfYear, year);
                etDateTo.setText(finaldate2);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        newCalendar.set(Calendar.MONTH, startMonth);
        newCalendar.set(Calendar.DAY_OF_MONTH, startDate + 3);
        newCalendar.set(Calendar.YEAR, startYear);
        datePickerDialog.getDatePicker().setMinDate(newCalendar.getTimeInMillis());
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

    private void showTimeDiloagBox() {
        ivClock.setColorFilter(ContextCompat.getColor(this, R.color.gray_main_alpha_50));
        final Calendar newCalenderTime = Calendar.getInstance();
        tilTime.setHintTextAppearance(R.style.AppTheme_TextNormalAppearance);
        timePickerDialog = new TimePickerDialog(this, new OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //finaltime = HelperClass.converPickerTime(hourOfDay, minute);
                if (String.valueOf(minute).length() == 1) {
                    finaltime = hourOfDay + ":0" + minute;
                } else {
                    finaltime = hourOfDay + ":" + minute;
                }

                // newCalenderTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                //newCalenderTime.set(Calendar.MINUTE, minute);
                etTime.setText(finaltime);
                //finaltime = String.valueOf(newCalenderTime.getTimeInMillis());
            }
        }, newCalenderTime.get(Calendar.HOUR_OF_DAY), newCalenderTime.get(Calendar.MINUTE), false);

        timePickerDialog.setCancelable(false);
        timePickerDialog.show();
    }

    @Subscribe(priority = 1, threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventBusHelper eventBusHelper) {
        requestModel = eventBusHelper.getKycRequestModel();
        Log.e("Create pin", new Gson().toJson(requestModel));


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_VERIFY_DOCUMENTS,
                GlobalConstants.ACTION_BACK,
                GlobalConstants.LABLE_TO_START_EKYC
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}

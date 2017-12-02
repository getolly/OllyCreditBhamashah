package com.ollycredit.ui.onboarding.phone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

import com.google.gson.Gson;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.RequestModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.api.model.bahama.UserAccDetail;
import com.ollycredit.api.model.bahama.UserPersonalDetails;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.onboarding.signup.create_pin.CreatePinActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.EventBusHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountNumberActivity extends BaseActivity implements AccountNumberView {

    @BindView(R.id.edit_account_number)
    EditText et_account_number;

    @BindView(R.id.toolbar_phone)
    Toolbar navToolbar;


    @Inject
    AccountNumberPresenter accountNumberPresenter;

    @Inject
    PreferencesHelper preferencesHelper;
    private RequestModel requestModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_number);

        getActivityComponent().inject(this);
        ButterKnife.bind(AccountNumberActivity.this);
        accountNumberPresenter.attachView(this);

    }

    @Override
    public boolean onNavigateUp() {
        return super.onNavigateUp();
    }

    @Override
    protected void setUp() {

        setSupportActionBar(navToolbar);


    }

    @OnClick(R.id.fab_accountsubmit)
    public void onClick() {
        String accountNumb = et_account_number.getText().toString().trim();
        //showLoadingView();

        showProgressDialog();
//        accountNumberPresenter.getBramUserDetails("https://apitest.sewadwaar.rajasthan.gov.in/app/live/Service/Bhamashah/hofAndMember/" + "1067-X8Q6-15116" + "?client_id=ad7288a4-7764-436d-a727-783a977f1fe1");
        accountNumberPresenter.getBramAccountDetails("https://apitest.sewadwaar.rajasthan.gov.in/app/live/Service/getAccountDetails/" + accountNumb + "?client_id=ad7288a4-7764-436d-a727-783a977f1fe1");
    }


    @Override
    public void getBramAccountDetails(List<UserAccDetail> bramAccDetailsModel) {

        Log.e("getBramAccountDetails: ", new Gson().toJson(bramAccDetailsModel));

        preferencesHelper.getString(GlobalConstants.PREF_ADHAR, String.valueOf(bramAccDetailsModel.get(0).getAadharId()));
        preferencesHelper.getString(GlobalConstants.PREF_AccountNumber, bramAccDetailsModel.get(0).getAccountNumber());
        preferencesHelper.getString(GlobalConstants.PREF_getBhamashahId, bramAccDetailsModel.get(0).getBhamashahId());

        requestModel = new RequestModel();
        //store docs
        requestModel.setAadhaar_num(String.valueOf(bramAccDetailsModel.get(0).getAadharId()));
        requestModel.setPan_num("PLEASE_IGNORE_PAN");

        accountNumberPresenter.getBramUserDetails("https://apitest.sewadwaar.rajasthan.gov.in/app/live/Service/Bhamashah/hofAndMember/" + bramAccDetailsModel.get(0).getFamilyId() + "?client_id=ad7288a4-7764-436d-a727-783a977f1fe1");

    }

    @Override
    public void getBramUserDetails(UserPersonalDetails bhamaUserDetailsModel) {

        // hideLoadingView();
        Log.e("getBramUserDetails: ", new Gson().toJson(bhamaUserDetailsModel));
        preferencesHelper.getString(GlobalConstants.PREF_BAMA_DOB, bhamaUserDetailsModel.getHofDetails().getdOB());
        preferencesHelper.getString(GlobalConstants.PREF_BAMA_FIRST_NAME, bhamaUserDetailsModel.getHofDetails().getnAMEENG().split(" ")[0]);
        preferencesHelper.getString(GlobalConstants.PREF_BAMA_LAST_NAME, bhamaUserDetailsModel.getHofDetails().getnAMEENG().split(" ")[1]);


        requestModel.setToken(preferencesHelper.getToken());
        requestModel.setEmail("doesntMatter@DontCare.com");
        requestModel.setFname(bhamaUserDetailsModel.getHofDetails().getnAMEENG().split(" ")[0]);
        requestModel.setLast(bhamaUserDetailsModel.getHofDetails().getnAMEENG().split(" ")[1]);
        requestModel.setDob(String.valueOf(bhamaUserDetailsModel.getHofDetails().getdOB()));
        requestModel.setOrgId(2);


        //personal details
        requestModel.setAddress(bhamaUserDetailsModel.getHofDetails().getaDDRESS());
        requestModel.setPincode(bhamaUserDetailsModel.getHofDetails().getpINCODE());
        requestModel.setCity(bhamaUserDetailsModel.getHofDetails().getbLOCKCITY());
        requestModel.setState(bhamaUserDetailsModel.getHofDetails().getdISTRICT());
        requestModel.setResidence_date(String.valueOf(bhamaUserDetailsModel.getHofDetails().getdOB()));


        //professional details
        requestModel.setDesignation("Garrib-Hu-Please-Deydo-Cash");
        requestModel.setMonthly_income(String.valueOf(bhamaUserDetailsModel.getHofDetails().getiNCOMEDESCENG()).split("-")[0]);


        accountNumberPresenter.getRegisterCompleted(requestModel);

    }

    @Override
    public void getComplRegSuccess(ResponseModel responseModel) {
        responseModel.setToken(responseModel.getToken());
        accountNumberPresenter.getPersonalDetails(requestModel);
    }

    @Override
    public void PersonalDetails(ResponseModel responseModel) {
        responseModel.setToken(responseModel.getToken());
        accountNumberPresenter.getProffesionalDetails(requestModel);
    }

    @Override
    public void ProffesionalDetails(ResponseModel responseModel) {
        responseModel.setToken(responseModel.getToken());
        accountNumberPresenter.getDocumentStored(requestModel);
    }

    @Override
    public void DocumentStored(ResponseModel responseModel) {

        hideProgressDialog();
        Intent intent = new Intent(AccountNumberActivity.this, CreatePinActivity.class);
        EventBus.getDefault().postSticky(new EventBusHelper(requestModel));
        intent.putExtra("fname", requestModel.getFname());
        intent.putExtra("lname", requestModel.getLast());
        intent.putExtra("dob", requestModel.getDob());
        startActivity(intent);
    }
}

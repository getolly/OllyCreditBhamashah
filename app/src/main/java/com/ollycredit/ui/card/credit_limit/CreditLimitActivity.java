package com.ollycredit.ui.card.credit_limit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.HelperClass;
import com.ollycredit.utils.helpers.TextFormatHelper;
import com.ollycredit.utils.helpers.ViewPagerHelper;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CreditLimitActivity extends BaseActivity implements CreditLimitView {


    @BindView(R.id.rv_credit_limit)
    RecyclerView creditLimitRV;

    @BindView(R.id.ctb_limit)
    CollapsingToolbarLayout ctbLimit;

    @BindView(R.id.view_limit_progress)
    View limitProgressView;

    @BindView(R.id.toolbar_cardLimit)
    Toolbar navToolbar;

    @BindView(R.id.fl_loadingview)
    FrameLayout loadingView;


    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @BindString(R.string.rs_symbol)
    String rs_symbol;


    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    CreditLimitPresenter creditLimitPresenter;

    CreditLimitRecycleAdapter mAdapter;

    int seq_num = 0;
    private String token;

    private String cyan_light = "#00e5ff";
    private BroadcastReceiver networkReceiver;
    private Boolean isEkycScheduled;


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void reqSucces(ResponseModel responseModel) {

        //hideProgressDialog();
        loadingView.setVisibility(View.GONE);
        appBarLayout.setExpanded(true);
        //todo content goes here
        creditLimitVG = new CreditLimitViewsGroup(limitProgressView);

        preferencesHelper.putString(GlobalConstants.PREF_USER_CREDIT_LIMIT, String.valueOf(responseModel.getUser().getCreditLimit()));

        updateToolBar();


        creditLimitVG.availableCreditTV.setText(String.valueOf(TextFormatHelper.indianRupeesFormat(Double.valueOf(responseModel.getUser().getCreditLimit()))));
        creditLimitVG.totalCreditTV.setText(rs_symbol + TextFormatHelper.indianRupeesFormat(Double.valueOf(responseModel.getUser().getMaxCreditLimit())));

        double progress = (responseModel.getUser().getCreditLimit() * 100) / responseModel.getUser().getMaxCreditLimit();

        creditLimitVG.creditProgressBar.setProgress((int) progress);

        String progressOneString = "You are eligible to " + (int) progress + "% of available Olly Credit";

        SpannableString spannableString = new SpannableString(progressOneString);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(cyan_light)), 20, 21 + String.valueOf((int) progress).length(), 0);

        creditLimitVG.percentageAvailTV.setText(spannableString);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        creditLimitRV.setLayoutManager(mLayoutManager);

        creditLimitRV.setNestedScrollingEnabled(false);
        int onboardingFlow = preferencesHelper.getInt(GlobalConstants.USER_APP_FLOW, 0);
        mAdapter = new CreditLimitRecycleAdapter(
                this, onboardingFlow,
                new ViewPagerHelper().getMoreCreditTitles(),
                new ViewPagerHelper().getMoreCreditStatus(),
                new ViewPagerHelper().getMoreCreditIconsIds(),
//                seq_num,
                responseModel.getCreditLimitAmounts(),
                responseModel.getCreditLimitFlags(),
                responseModel.getUser().getCreditLimit(),
                isEkycScheduled
        );

        creditLimitRV.setAdapter(mAdapter);

    }

    private void updateToolBar() {

        String userCreditLimit = TextFormatHelper.indianRupeesFormat(Double.parseDouble(preferencesHelper.getString(GlobalConstants.PREF_USER_CREDIT_LIMIT, "0")));

        ctbLimit.setTitle(rs_symbol + userCreditLimit + " Credit Limit");
    }

    @Override
    public void reqFail(int failedCode, String message) {

        //hideProgressDialog();
        loadingView.setVisibility(View.GONE);

        if (failedCode == GlobalConstants.NETWORK_REQUEST_CALL_FAILED) {
            showDialogBox(message);
        }
        if (failedCode == GlobalConstants.NETWORK_REQUEST_CREDIT_LIMIT_VALUES) {
            showDialogBox(message);
            showDialogBox(message);
        }

    }


    public class CreditLimitViewsGroup {
        @BindView(R.id.tv_credit_available)
        TextView availableCreditTV;
        @BindView(R.id.tv_credit_total)
        TextView totalCreditTV;
        @BindView(R.id.tv_credit_percent_avail)
        TextView percentageAvailTV;
        @BindView(R.id.pb_credit_limitprogress)
        ProgressBar creditProgressBar;

        CreditLimitViewsGroup(View limitProgressView) {
            //bind butterknief with - object of nested & included layout
            ButterKnife.bind(this, limitProgressView);
        }
    }

    private CreditLimitViewsGroup creditLimitVG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_limit);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        creditLimitPresenter.attachView(this);

        setUp();
    }

    @Override
    protected void setUp() {

        setSupportActionBar(navToolbar);
        appBarLayout.setExpanded(false);

        updateToolBar();

        ctbLimit.setCollapsedTitleTypeface(Typeface.createFromAsset(getAssets(), "fonts/muli-semibold.ttf"));
        ctbLimit.setExpandedTitleTextAppearance(R.style.HiddenAppear);
        ctbLimit.setCollapsedTitleTextAppearance(R.style.TextColorWhite);


        isEkycScheduled = preferencesHelper.getBoolean(GlobalConstants.PREF_EKYC_SCHEDULED, false);
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(CreditLimitActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }
            }
        };


    }

    @Override
    protected void onPause() {
        super.onPause();
        hideErrorConnection();
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onResume() {
        super.onResume();

        OllyCreditApplication.getInstance().trackScreenView(this.getClass().getSimpleName());
        loadingView.setVisibility(View.VISIBLE);
        appBarLayout.setExpanded(true);
//        showProgressDialog();
        token = preferencesHelper.getToken();

        creditLimitPresenter.getLimitValues(token);
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_INCREASE_LIMIT,
                GlobalConstants.ACTION_BACK,
                GlobalConstants.LABLE_TO_HOME
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        creditLimitVG = null;

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

}

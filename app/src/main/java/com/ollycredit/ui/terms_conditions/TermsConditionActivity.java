package com.ollycredit.ui.terms_conditions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.EventBusHelper;
import com.ollycredit.utils.helpers.ViewPagerHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TermsConditionActivity extends BaseActivity {

    @BindView(R.id.view_terms_condition)
    View viewTermsConds;

    @BindView(R.id.toolbar_terms)
    Toolbar navToolbar;


    private ViewPager vpTermsConds;
    //private TabLayout tlNavTabs;
    private ProgressBar pbTCIndiacator;
    private int progress = 0;
    private ViewPagerHelper pagerHelper;

    private float x1, x2;
    private final int MIN_DISTANCE = 150;
    private BroadcastReceiver networkReceiver;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition2);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        initViews();
        setUp();

    }

    @Override
    protected void onPause() {
        super.onPause();
        hideErrorConnection();
    }

    private void initViews() {
        vpTermsConds = (ViewPager) viewTermsConds.findViewById(R.id.vp_terms_condition);
        pbTCIndiacator = (ProgressBar) viewTermsConds.findViewById(R.id.pb_tc_increment);
        // tlNavTabs = (TabLayout) viewTermsConds.findViewById(R.id.navbar_tabs);
        Button btnAgree = (Button) viewTermsConds.findViewById(R.id.btn_tc_agree);
        btnAgree.setText("Next");
        btnAgree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                OllyCreditApplication.getInstance().trackEvent(
                        GlobalConstants.CATEGORY_TERMS_CONDITIONS,
                        GlobalConstants.ACTION_BUTTON_CLICKED,
                        GlobalConstants.LABLE_TO_NEXT_TC
                );

                onClickNext();
            }
        });
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

        setSupportActionBar(navToolbar);

        pagerHelper = new ViewPagerHelper();

        TermsConditionPagerAdapter tacPagerAdapter = new TermsConditionPagerAdapter(
                getSupportFragmentManager(),
                pagerHelper.getTcCsPageFragments()
        );

        vpTermsConds.setAdapter(tacPagerAdapter);
//        tlNavTabs.setupWithViewPager(vpTermsConds);
//        tlNavTabs.clearOnTabSelectedListeners();
        updateViewPagerFragments();

        vpTermsConds.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                progress = position;
                updateViewPagerFragments();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(TermsConditionActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }
            }
        };

    }

    private void updateViewPagerFragments() {
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            pbTCIndiacator.setProgress(progress + 1, true);
        } else {
            pbTCIndiacator.setProgress(progress + 1);
        }

        // new ViewPagerHelper().setPagerIndiacatorOfTC(tlNavTabs);
        // tlNavTabs.getTabAt(progress).setIcon(R.drawable.indicator_viewpager_active);
        EventBus.getDefault().postSticky(new EventBusHelper(progress));
        vpTermsConds.setCurrentItem(progress);
    }


    public void onClickNext() {
        progress++;
        if (progress == 5) {
            progress = 0;
        }
        updateViewPagerFragments();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_TERMS_CONDITIONS,
                GlobalConstants.ACTION_BACK,
                GlobalConstants.LABLE_TO_HOME
        );
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

}

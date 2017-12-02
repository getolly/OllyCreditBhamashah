package com.ollycredit.ui.card.card_history;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.navigation.NavDrawerFragment;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.HelperClass;
import com.ollycredit.utils.helpers.ViewPagerHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardHistoryActivity extends BaseActivity {

    @BindView(R.id.toolbar_cardhistory)
    Toolbar navToolbar;

    @BindView(R.id.vp_cardhistory)
    ViewPager cardHistoryViewPager;

    @BindView(R.id.navbar_tabs)
    TabLayout navTabLayout;
    private BroadcastReceiver networkReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_history);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        setUp();

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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_CARD_HISTORY,
                GlobalConstants.ACTION_BACK,
                GlobalConstants.LABLE_TO_HOME
        );
        return true;
    }

    @Override
    protected void setUp() {
        setSupportActionBar(navToolbar);

        CardHistoryPagerAdapter pagerAdapter = new CardHistoryPagerAdapter(
                getSupportFragmentManager(),
                this,
                new ViewPagerHelper().getCardHistoryPageTitles(),
                new ViewPagerHelper().getCardHistoryPageFragments()
        );

        cardHistoryViewPager.setAdapter(pagerAdapter);
        navTabLayout.setupWithViewPager(cardHistoryViewPager);

        cardHistoryViewPager.setCurrentItem(getIntent().getIntExtra("position", 0), true);

        cardHistoryViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        OllyCreditApplication.getInstance().trackEvent(
                                GlobalConstants.CATEGORY_CARD_HISTORY,
                                GlobalConstants.ACTION_SWIPE,
                                GlobalConstants.LABLE_TO_TRANSACTION
                        );
                        break;
                    case 1:
                        OllyCreditApplication.getInstance().trackEvent(
                                GlobalConstants.CATEGORY_CARD_HISTORY,
                                GlobalConstants.ACTION_SWIPE,
                                GlobalConstants.LABLE_TO_REPAYMENT
                        );
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(CardHistoryActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }
            }
        };


        //setUpNavDrawer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        OllyCreditApplication.getInstance().trackScreenView(this.getClass().getSimpleName());
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

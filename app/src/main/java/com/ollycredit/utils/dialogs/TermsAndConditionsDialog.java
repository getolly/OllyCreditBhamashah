package com.ollycredit.utils.dialogs;

import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.ui.terms_conditions.TermsConditionPagerAdapter;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.EventBusHelper;
import com.ollycredit.utils.helpers.ViewPagerHelper;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by abdulaziz on 12/27/16.
 */

public class TermsAndConditionsDialog extends DialogFragment implements View.OnClickListener {


    private View viewTermsConds;
    private ViewPager vpTermsConds;
    //private TabLayout tlNavTabs;
    private int progress = 0;
    private ProgressBar pbTCindiacator;
    private ViewPagerHelper pagerHelper;

    private View itemView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OllyCreditApplication
                .getInstance()
                .trackScreenView(this.getClass().getSimpleName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        itemView = inflater.inflate(R.layout.terms_and_conditions_dialog_layout, container);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getDialog().setCancelable(false);
        getDialog().getWindow().setGravity(Gravity.CENTER);


        return itemView;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.85);
        int screenHeight = (int) (metrics.heightPixels * 0.85);

        getDialog().getWindow().setLayout(screenWidth, screenHeight);
        setUp();

    }

    public TermsAndConditionsDialog() {
    }


    private void setUp() {

        viewTermsConds = (View) itemView.findViewById(R.id.view_tc_dialog);
        vpTermsConds = (ViewPager) viewTermsConds.findViewById(R.id.vp_terms_condition);
        //tlNavTabs = (TabLayout) viewTermsConds.findViewById(R.id.navbar_tabs);
        pbTCindiacator = (ProgressBar) viewTermsConds.findViewById(R.id.pb_tc_increment);
        Button btnAgree = (Button) viewTermsConds.findViewById(R.id.btn_tc_agree);

        btnAgree.setOnClickListener(this);


        pagerHelper = new ViewPagerHelper();

        TermsConditionPagerAdapter tacPagerAdapter = new TermsConditionPagerAdapter(
                getChildFragmentManager(),
                pagerHelper.getTcCsPageFragments()
        );

        vpTermsConds.setAdapter(tacPagerAdapter);
//        tlNavTabs.setupWithViewPager(vpTermsConds);
//        tlNavTabs.clearOnTabSelectedListeners();
        updateViewPagerFragments();

        vpTermsConds.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    private void updateViewPagerFragments() {

        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            pbTCindiacator.setProgress(progress + 1, true);
        } else {
            pbTCindiacator.setProgress(progress + 1);
        }
        //new ViewPagerHelper().setPagerIndiacatorOfTC(tlNavTabs);
        //tlNavTabs.getTabAt(progress).setIcon(R.drawable.indicator_viewpager_active);
        EventBus.getDefault().postSticky(new EventBusHelper(progress));
        vpTermsConds.setCurrentItem(progress);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_tc_agree:

                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_DILAOG,
                                GlobalConstants.ACTION_NEXT,
                                GlobalConstants.LABLE_DIALOG_TERMS_CONDITIONS);

                progress++;
                if (progress < 5) {
                    updateViewPagerFragments();
                } else {
                    this.dismiss();
                }
                break;

        }
    }
}
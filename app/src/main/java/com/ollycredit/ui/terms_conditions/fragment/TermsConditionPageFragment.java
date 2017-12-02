package com.ollycredit.ui.terms_conditions.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.base.BaseFragment;
import com.ollycredit.utils.helpers.EventBusHelper;
import com.ollycredit.utils.helpers.ViewPagerHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermsConditionPageFragment extends BaseFragment {


    @BindView(R.id.tc_heading)
    TextView tvTCHeading;

    @BindView(R.id.tc_policy_content)
    TextView tvTCBody;

    @BindView(R.id.tc_background)
    ImageView ivTCBackground;


    private int position;

    private ViewPagerHelper pagerHelper;

    public TermsConditionPageFragment() {
        // Required empty public constructor
    }



    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        OllyCreditApplication.getInstance().trackScreenView(this.getClass().getSimpleName());
//        OllyCreditApplication.getInstance().trackEvent(
//                GlobalConstants.CATEGORY_ONBOARDING,
//                GlobalConstants.ACTION_GOTO,
//                GlobalConstants.LABLE_TO_HOME
//        );
    }


    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(priority = 1, threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventBusHelper eventBusHelper) {

        position = eventBusHelper.getViewPagerPosition();
        Log.e("viewpager pos :", new Gson().toJson(position));

        setContent(position);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View itemView = inflater.inflate(R.layout.fragment_tc_page, container, false);
        ButterKnife.bind(this, itemView);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);

        setUp();

        return itemView;
    }

    @Override
    protected void setUp() {
        pagerHelper = new ViewPagerHelper();
    }

    public void setContent(int position) {

        tvTCHeading.setText(pagerHelper.getTsCsTitle().get(position));
        tvTCBody.setText(pagerHelper.getTsCsPolicy().get(position));
        ivTCBackground.setBackgroundColor(Color.parseColor(pagerHelper.getBackgroundColors().get(position)));

    }
}

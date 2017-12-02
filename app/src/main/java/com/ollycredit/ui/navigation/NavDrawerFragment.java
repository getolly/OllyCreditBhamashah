package com.ollycredit.ui.navigation;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.base.BaseFragment;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.ViewPagerHelper;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ollycredit.utils.GlobalConstants.APP_FLOW_CLASSIC;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_OPERATIONAL;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_PREPAID;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_PRE_USER;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_SELECT;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavDrawerFragment extends BaseFragment implements NavRecycleAdapter.NavDrawer {

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout mDrawerLayout;


    @BindView(R.id.rv_nav)
    RecyclerView navRV;

    @BindView(R.id.nav_username)
    TextView usernameTextView;


    @BindView(R.id.nav_credit_task_title)
    TextView creditTitleTextView;

    @BindView(R.id.nav_credit_task_desc)
    TextView creditDescTextView;

    @BindView(R.id.nav_credit_image)
    ImageView creditImageView;

    @BindView(R.id.nav_credit_task_layout)
    FrameLayout creditBonusFrameLayout;

    private NavRecycleAdapter mAdapter;

    @Inject
    PreferencesHelper preferencesHelper;

    @BindString(R.string.rs_symbol)
    String rsSymbol;

    private int appFlow;

    Typeface typeface_semi, typeface_reg, typeface_bold;


    public NavDrawerFragment() {
        // Required empty public constructor
    }

    public void setUpDrawer(int FragId, DrawerLayout drawerLayout, Toolbar toolbar) {

        mDrawerLayout = drawerLayout;

        drawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                drawerLayout,
                toolbar,
                R.string.open_nav,
                R.string.close_nav) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

        };

        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();


    }


    @Override
    public void onResume() {
        super.onResume();
        getContent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_nav_drawer, container, false);
        ButterKnife.bind(this, itemView);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);

        setUp();

        return itemView;
    }

    @Override
    protected void setUp() {


        appFlow = preferencesHelper.getInt(GlobalConstants.USER_APP_FLOW, 0);

        typeface_reg = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Muli-Regular.ttf");
        typeface_semi = Typeface.createFromAsset(getActivity().getAssets(), "fonts/muli-semibold.ttf");
        typeface_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/muli-bold.ttf");

        usernameTextView.setTypeface(typeface_reg);
        creditTitleTextView.setTypeface(typeface_semi);
        creditDescTextView.setTypeface(typeface_semi);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        navRV.setLayoutManager(mLayoutManager);


        switch (appFlow) {
            case APP_FLOW_PRE_USER:
            case APP_FLOW_CLASSIC:
            case APP_FLOW_SELECT:
            case APP_FLOW_PREPAID:
                mAdapter = new NavRecycleAdapter(
                        getContext(),
                        new ViewPagerHelper().getNavDrawerTitles(),
                        new ViewPagerHelper().getNavDrawerIconIds(),
                        new ViewPagerHelper().getNavDrawerTags(),
                        NavDrawerFragment.this,
                        appFlow
                );
                break;
            case APP_FLOW_OPERATIONAL:
                mAdapter = new NavRecycleAdapter(
                        getContext(),
                        new ViewPagerHelper().getNavDrawerTitlesPre(),
                        new ViewPagerHelper().getNavDrawerIconIdsPre(),
                        new ViewPagerHelper().getNavDrawerTagsPre(),
                        NavDrawerFragment.this,
                        appFlow
                );
                break;
            default:
        }


        navRV.setAdapter(mAdapter);

    }

    private void getContent() {

        String completeName = preferencesHelper.getString(GlobalConstants.PREF_USER_NAME_KEY, "");
        String firstName = completeName.split(" ")[0].substring(0, 1).toUpperCase() + completeName.split(" ")[0].substring(1);
        String lastName = completeName.split(" ")[1].substring(0, 1).toUpperCase() + completeName.split(" ")[1].substring(1);


        String username = firstName + "\n" + lastName;

        int panVerified = preferencesHelper.getInt(GlobalConstants.PREF_PAN_VERIFIED, 0);
        int debitStatus = preferencesHelper.getInt(GlobalConstants.PREF_DEBIT_VERIFIED, 0);
        int kycStatus = preferencesHelper.getInt(GlobalConstants.PREF_EKYC_VERIFIED, 0);
        int repayStatus = preferencesHelper.getInt(GlobalConstants.PREF_REPAYED_VERIFIED, 0);

        String panLimit = preferencesHelper.getString(GlobalConstants.PREF_PAN_LIMIT, "500");
        String debitLimit = preferencesHelper.getString(GlobalConstants.PREF_DEBIT_LIMIT, "0");
        String kycLimit = preferencesHelper.getString(GlobalConstants.PREF_EKYC_LIMIT, "0");
        String repayLimit = preferencesHelper.getString(GlobalConstants.PREF_REPAYED_LIMIT, "0");


        if (!TextUtils.isEmpty(username)) {
            usernameTextView.setText(username);
        }


        switch (appFlow) {
            case APP_FLOW_PRE_USER:
            case APP_FLOW_CLASSIC:
            case APP_FLOW_SELECT:
            case APP_FLOW_PREPAID:
//                if (panVerified == 0) {
//                    setContent(
//                            "Verify your PAN card",
//                            "Get +" + rsSymbol + TextFormatHelper.indianRupeesFormat(Double.valueOf(panLimit)) + " of credit",
//                            new ViewPagerHelper().getNavNextVerifyIconsIds().get(0),
//                            VerifyPanActivity.class,
//                            GlobalConstants.LABLE_VERIFY_PAN);
//
//                } else if (debitStatus == 0) {
//                    setContent(
//                            "Verify your debit card",
//                            "Get +" + rsSymbol + TextFormatHelper.indianRupeesFormat(Double.valueOf(debitLimit)) + " of credit",
//                            new ViewPagerHelper().getNavNextVerifyIconsIds().get(1),
//                            VerifyBankActivity.class,
//                            GlobalConstants.LABLE_VERIFY_DEBIT);
//
//                } else if (kycStatus == 0) {
//                    setContent(
//                            "Verify your KYC",
//                            "Get +" + rsSymbol + TextFormatHelper.indianRupeesFormat(Double.valueOf(kycLimit)) + " of credit",
//                            new ViewPagerHelper().getNavNextVerifyIconsIds().get(2),
//                            AppointKycActivity.class,
//                            GlobalConstants.LABLE_VERIFY_EKYC);
//
//                } else if (repayStatus == 0) {
//                    setContent(
//                            "Make two successful repayments",
//                            "Get +" + rsSymbol + TextFormatHelper.indianRupeesFormat(Double.valueOf(repayLimit)) + " of credit",
//                            new ViewPagerHelper().getNavNextVerifyIconsIds().get(0),
//                            CreditLimitActivity.class,
//                            GlobalConstants.LABLE_VERIFY_REPAYMENT);
//
//                } else {
//                    creditBonusFrameLayout.setVisibility(View.GONE);
//                }
                creditBonusFrameLayout.setVisibility(View.GONE);
                break;
            case APP_FLOW_OPERATIONAL:
                creditBonusFrameLayout.setVisibility(View.GONE);
                break;
            default:
        }


    }

    void setContent(String title, String desc, int iconId, final Class activityName, final String LABLE) {
        creditTitleTextView.setText(title);
        creditDescTextView.setText(desc);
        creditImageView.setImageResource(iconId);

        creditBonusFrameLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                OllyCreditApplication.getInstance().trackEvent(
                        GlobalConstants.CATEGORY_NAV_DRAWER,
                        GlobalConstants.ACTION_GOTO,
                        LABLE
                );
                startActivity(new Intent(getActivity(), activityName));
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAdapter = null;
    }

    @Override
    public void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }
}

package com.ollycredit.ui.terms_conditions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by ch8n on 11/8/17.
 */

public class TermsConditionPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;

    public TermsConditionPagerAdapter(FragmentManager fm, List<Fragment> tcCsPageFragments) {
        super(fm);
        fragmentList = tcCsPageFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}

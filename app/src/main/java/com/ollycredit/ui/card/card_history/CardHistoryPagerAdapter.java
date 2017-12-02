package com.ollycredit.ui.card.card_history;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by ch8n on 24/7/17.
 */

public class CardHistoryPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private List<String> titleList;
    private List<Fragment> fragmentList;


    public CardHistoryPagerAdapter(FragmentManager fm, Context context, List<String> titleList, List<Fragment> fragmentList) {
        super(fm);
        this.context = context;
        this.titleList = titleList;
        this.fragmentList = fragmentList;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);

    }
}

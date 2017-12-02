package com.ollycredit.ui.card.card_history.fragments.repayments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;

/**
 * Created by ch8n on 4/8/17.
 */

public class ExpandableRepaymentView extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> Iconids;
    private ArrayList<String> Titles;
    private ArrayList<String> contents;

    public ExpandableRepaymentView(Context context, ArrayList<String> Iconids, ArrayList<String> Titles, ArrayList<String> contents) {
        this.context = context;
        this.Iconids = Iconids;
        this.Titles = Titles;
        this.contents = contents;
    }

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}

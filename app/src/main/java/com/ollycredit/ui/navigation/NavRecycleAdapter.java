package com.ollycredit.ui.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.ui.card.card_history.CardHistoryActivity;
import com.ollycredit.ui.card.credit_limit.CreditLimitActivity;
import com.ollycredit.ui.help.HelpActivity;
import com.ollycredit.ui.navigation.NavRecycleAdapter.NavigationViewHolder;
import com.ollycredit.ui.terms_conditions.LegalTermsConditionActivity;
import com.ollycredit.ui.terms_conditions.PrivatePolicyActivity;
import com.ollycredit.ui.terms_conditions.TermsConditionActivity;
import com.ollycredit.ui.user_profile.user_details.UserProfileActivity;
import com.ollycredit.utils.GlobalConstants;

import java.util.List;

/**
 * Created by ch8n on 24/7/17.
 */

public class NavRecycleAdapter extends RecyclerView.Adapter<NavigationViewHolder> {


    private Context context;
    private List<String> navDrawerTitles;
    private List<String> navDrawerTags;
    private List<Integer> navDrawerIconIds;
    private NavDrawer navDrawer;
    private String LABLE;
    private int userType;


    public NavRecycleAdapter(Context context, List<String> navDrawerTitles, List<Integer> navDrawerIconIds, List<String> navDrawerTags, NavDrawerFragment navDrawerFragment, int userType) {
        this.context = context;
        this.navDrawerTitles = navDrawerTitles;
        this.navDrawerIconIds = navDrawerIconIds;
        this.navDrawerTags = navDrawerTags;
        navDrawer = navDrawerFragment;
        this.userType = userType;

    }

    @Override
    public NavigationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_nav_list, parent, false);
        NavigationViewHolder holder = new NavigationViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(NavigationViewHolder holder, int position) {
        //todo interacions

        holder.navIcon.setImageResource(navDrawerIconIds.get(position));
        holder.navTitle.setText(navDrawerTitles.get(position));
        holder.navItemLayout.setTag(navDrawerTags.get(position));
        holder.navItemLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Class IntentClass = null;

                switch (v.getTag().toString()) {
                    case "limit":
                        IntentClass = CreditLimitActivity.class;
                        LABLE = GlobalConstants.LABLE_TO_INCREASE_LIMIT;
                        break;
                    case "profile":
                        IntentClass = UserProfileActivity.class;
                        LABLE = GlobalConstants.LABLE_TO_USER_PROFILE;
                        break;
                    case "repayment":
                        LABLE = GlobalConstants.LABLE_TO_REPAYMENT;
                        Intent intent = new Intent(context, CardHistoryActivity.class);
                        intent.putExtra("position", 1);
                        context.startActivity(intent);
                        navDrawer.closeDrawer();
                        return;
//                    case "terms":
//                        IntentClass = TermsConditionActivity.class;
//                        LABLE = GlobalConstants.LABLE_TO_TERMS_CONDITIONS;
//                        break;
                    case "legal":
                        IntentClass = LegalTermsConditionActivity.class;
                        LABLE = GlobalConstants.LABLE_TO_LEGAL_TERMS;
                        break;
                    case "private":
                        IntentClass = PrivatePolicyActivity.class;
                        LABLE = GlobalConstants.LABLE_TO_PRIVATE_TERMS;
                        break;
                    case "help":
                        IntentClass = HelpActivity.class;
                        LABLE = GlobalConstants.LABLE_TO_HELP;
                        break;
                }

                OllyCreditApplication.getInstance().trackEvent(
                        GlobalConstants.CATEGORY_NAV_DRAWER,
                        GlobalConstants.ACTION_GOTO,
                        LABLE
                );

                if (IntentClass != null) {
                    context.startActivity(new Intent(context, IntentClass));
                } else {
                    Toast.makeText(context, "Coming soon...", Toast.LENGTH_SHORT).show();
                }

                navDrawer.closeDrawer();
            }
        });




    }

    interface NavDrawer {
        void closeDrawer();
    }

    @Override
    public int getItemCount() {
        return navDrawerTitles.size();
    }

    public class NavigationViewHolder extends ViewHolder {

        private ImageView navIcon;
        private TextView navTitle;
        private LinearLayout navItemLayout;

        public NavigationViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View itemView) {
            navIcon = (ImageView) itemView.findViewById(R.id.nav_icon);
            navTitle = (TextView) itemView.findViewById(R.id.nav_title);
            navItemLayout = (LinearLayout) itemView.findViewById(R.id.nav_item);
        }
    }
}

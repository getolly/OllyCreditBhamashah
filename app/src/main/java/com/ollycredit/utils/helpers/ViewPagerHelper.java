package com.ollycredit.utils.helpers;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import com.ollycredit.R;
import com.ollycredit.ui.card.card_history.fragments.repayments.RepaymentFragment;
import com.ollycredit.ui.card.card_history.fragments.transaction.TransactionFragment;
import com.ollycredit.ui.terms_conditions.fragment.TermsConditionPageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ch8n on 24/7/17.
 */

public class ViewPagerHelper {

    //===================Card History functions===============//

    public List<String> getCardHistoryPageTitles() {
        List<String> tabtiles = new ArrayList<String>();
        tabtiles.add("TRANSACTIONS");
        tabtiles.add("REPAYMENTS");
        return tabtiles;
    }

    public List<Fragment> getCardHistoryPageFragments() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new TransactionFragment());
        fragmentList.add(new RepaymentFragment());
        return fragmentList;
    }


    //===================Navigation functions===============//

    public List<String> getNavDrawerTitles() {
        List<String> navtiles = new ArrayList<String>();
        navtiles.add("Your credit limit");
        navtiles.add("User profile");
        navtiles.add("Credit repayment");
//        navtiles.add("Olly terms & conditions");
        navtiles.add("Help");
        navtiles.add("Olly legal terms");
        navtiles.add("Olly private policy");
        return navtiles;
    }

    public List<String> getNavDrawerTags() {
        List<String> navtags = new ArrayList<String>();
        navtags.add("limit");
        navtags.add("profile");
        navtags.add("repayment");
//        navtags.add("terms");
        navtags.add("help");
        navtags.add("legal");
        navtags.add("private");
        return navtags;
    }

    public List<Integer> getNavDrawerIconIds() {
        List<Integer> navIcons = new ArrayList<Integer>();

        navIcons.add(R.drawable.ic_creditlimit);
        navIcons.add(R.drawable.ic_profile);
        navIcons.add(R.drawable.ic_repayment);
        navIcons.add(R.drawable.ic_help);
//        navIcons.add(R.drawable.ic_termsconditions);
        navIcons.add(R.drawable.ic_legal);
        navIcons.add(R.drawable.ic_policy);

        return navIcons;
    }

    public List<Integer> getNavNextVerifyIconsIds() {
        List<Integer> creditIcons = new ArrayList<Integer>();

        creditIcons.add(R.drawable.ic_add_pan);
        creditIcons.add(R.drawable.ic_add_bank);
        creditIcons.add(R.drawable.ic_add_kyc);
        creditIcons.add(R.drawable.ic_repayment);

        return creditIcons;
    }

    //===================Navigation functions pre-user ===============//

    public List<String> getNavDrawerTitlesPre() {
        List<String> navtiles = new ArrayList<String>();
        navtiles.add("User profile");
        navtiles.add("Credit repayment");
//        navtiles.add("Olly terms & conditions");
        navtiles.add("Help");
        navtiles.add("Olly legal terms");
        navtiles.add("Olly private policy");
        return navtiles;
    }

    public List<String> getNavDrawerTagsPre() {
        List<String> navtags = new ArrayList<String>();
        navtags.add("profile");
        navtags.add("repayment");
//        navtags.add("terms");
        navtags.add("help");
        navtags.add("legal");
        navtags.add("private");
        return navtags;
    }

    public List<Integer> getNavDrawerIconIdsPre() {
        List<Integer> navIcons = new ArrayList<Integer>();

        navIcons.add(R.drawable.ic_profile);
        navIcons.add(R.drawable.ic_repayment);
//        navIcons.add(R.drawable.ic_termsconditions);
        navIcons.add(R.drawable.ic_help);
        navIcons.add(R.drawable.ic_legal);
        navIcons.add(R.drawable.ic_policy);

        return navIcons;
    }

    public List<Integer> getNavNextVerifyIconsIdsPre() {
        List<Integer> creditIcons = new ArrayList<Integer>();

        creditIcons.add(R.drawable.ic_add_pan);
        creditIcons.add(R.drawable.ic_add_bank);
        creditIcons.add(R.drawable.ic_add_kyc);
        creditIcons.add(R.drawable.ic_repayment);

        return creditIcons;
    }



    //====================CreditLimit functions===========//

    public int DOC_VERIFIED = 1;
    public int DOC_UNVERIFIED = 0;

    public List<String> getMoreCreditTitles() {
        List<String> credittiles = new ArrayList<String>();
        credittiles.add("Share your PAN card number");
        credittiles.add("Verify your bank account");
        credittiles.add("Provide us with your e-KYCs");
        credittiles.add("Make 2 repayments in time");
        return credittiles;
    }


    public List<String> getMoreCreditStatus() {
        List<String> credittiles = new ArrayList<String>();
        credittiles.add("LINK YOUR PAN");
        credittiles.add("LINK BANK ACCOUNT");
        credittiles.add("SUBMIT E-KYCs");
        credittiles.add("MAKE REPAYMENT");
        return credittiles;
    }


    public List<Integer> getMoreCreditIconsIds() {
        List<Integer> creditIcons = new ArrayList<Integer>();

        creditIcons.add(R.drawable.ic_pancard);
        creditIcons.add(R.drawable.ic_debitcard);
        creditIcons.add(R.drawable.ic_kyc_new);
        creditIcons.add(R.drawable.ic_repayment);

        return creditIcons;
    }

    //===================terms Condition functions===============//

    public List<String> getTsCsTitle() {
        List<String> tcTitles = new ArrayList<String>();

        tcTitles.add("You the user, acknowledge and accept that:");
        tcTitles.add("You the user, acknowledge and accept that:");
        tcTitles.add("You the user, are informed and agree that:");
        tcTitles.add("You the user, are informed and agree that:");
        tcTitles.add("You the user, are acknowledge and confirm that:");

        return tcTitles;
    }

    public List<SpannableString> getTsCsPolicy() {
        List<SpannableString> tcPolicys = new ArrayList<SpannableString>();

        SpannableString spannableString =
                new SpannableString("You have completed the age of 18 years.\n\nYou are not disqualified from entering into a binding legal contract.\n\nYou have valid bank account.");
        tcPolicys.add(spannableString);

        spannableString
                = new SpannableString("The billing cycle for the Olly Wallet and/or Card shall be from 1st to 31st of each month");
        tcPolicys.add(spannableString);

        spannableString
                = new SpannableString("You will be charged an interest of 0% for the first 90 days, when such sums of monies become due.\n\nAfter the expiry of this period of 90 days, you shall be liable to pay an interest of 2.5%, on a monthly basis.");
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                35, 36, 0);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                52, 58, 0);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                185, 188, 0);
        tcPolicys.add(spannableString);

        spannableString
                = new SpannableString("You are liable to pay the complete amount of monies that may have been charged by you to the Olly Wallet and/or Card.\n\nIn the event of your inability to pay the entire amount of money due, you shall make payment of a minimum amount which shall not be less than 40% of the money due, for the respective billing.");
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                212, 261, 0);
        tcPolicys.add(spannableString);


        spannableString
                = new SpannableString("In the event of your failure to pay the complete amount of monies due, you may be classified as a wilful defaulter.");
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                98, 114, 0);
        tcPolicys.add(spannableString);

        return tcPolicys;
    }

    public List<Fragment> getTcCsPageFragments() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new TermsConditionPageFragment());
        fragmentList.add(new TermsConditionPageFragment());
        fragmentList.add(new TermsConditionPageFragment());
        fragmentList.add(new TermsConditionPageFragment());
        fragmentList.add(new TermsConditionPageFragment());
        return fragmentList;
    }


    public List<String> getBackgroundColors() {
        List<String> colorList = new ArrayList<>();
        colorList.add("#614A9A");
        colorList.add("#5365D3");
        colorList.add("#2B838D");
        colorList.add("#5FAB91");
        colorList.add("#A95086");
        return colorList;
    }


    public void setPagerIndiacatorOfTC(TabLayout tabLayout) {
        tabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        tabLayout.getTabAt(0).setIcon(R.drawable.indicator_viewpager);
        tabLayout.getTabAt(1).setIcon(R.drawable.indicator_viewpager);
        tabLayout.getTabAt(2).setIcon(R.drawable.indicator_viewpager);
        tabLayout.getTabAt(3).setIcon(R.drawable.indicator_viewpager);
        tabLayout.getTabAt(4).setIcon(R.drawable.indicator_viewpager);
    }


}

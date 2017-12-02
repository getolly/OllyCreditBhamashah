package com.ollycredit.ui.terms_conditions;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.HelperClass;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivatePolicyActivity extends BaseActivity {

    @BindView(R.id.toolbar_legal)
    Toolbar navToolbar;

    @BindView(R.id.tv_content)
    TextView tv_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_policy_condition);


        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        setUp();

    }

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
    protected void setUp() {

        setSupportActionBar(navToolbar);

        tv_content.setText(Html.fromHtml("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<p>Welcome to Olly Credit owned by Millennial Card Technologies Private Limited, (“Our”, “Us”, “We”, “Olly”, “Website”,\n" +
                "    “Site”). This Privacy Policy (the “Privacy Policy”) along with the Terms of Use provides the terms and conditions\n" +
                "    governing Your use of this Website. At [insert company website], We value Your trust & respect Your privacy. Unless\n" +
                "    otherwise defined in this Privacy Policy, terms used in this Privacy Policy have the same meanings as in Our Terms\n" +
                "    and Conditions at [insert company website] and/or [insert company application]. This Privacy Policy provides You\n" +
                "    with details about the manner in which Your data is collected, stored & used by Us. You are advised to read this\n" +
                "    Privacy Policy carefully. By visiting Our Website and/or Application You expressly give Us consent to use & disclose\n" +
                "    Your personal information in accordance with this Privacy Policy. If You do not agree to the terms of the policy,\n" +
                "    please do not use or access Our Website.</p>\n" +
                "\n" +
                "<p>The expressions “You”, “Your” or “User”, whenever the context so requires, for the purposes of this Privacy Policy,\n" +
                "    shall mean any natural or legal person who may create by registration, membership account on this Website and/or\n" +
                "    Application or agree to avail Our Services through this Website and/or Application, or otherwise access Our Website\n" +
                "    and/or Application.\n" +
                "    This Privacy Policy is published by Us, in compliance of:</p>\n" +
                "    Section 43A of the Information Technology Act, 2000; and\n" +
                "    Regulation 4 of the Information Technology (Reasonable Security Practices and Procedures and Sensitive Personal\n" +
                "        Information) Rules, 2011\n" +
                "    \n" +
                "\n" +
                "\n" +
                "    \n" +
                "        <h3>1. General</h3>\n" +
                "        <p style=\"font-weight: normal;\">We will not sell, share or rent Your personal information to any third party or use Your email address/mobile\n" +
                "            number\n" +
                "            for unsolicited emails and/or SMS. Any emails and/or SMS sent by Us will only be in connection with the\n" +
                "            provision of\n" +
                "            agreed Services & products and this Privacy Policy. To carry out general marketing We would be sending out\n" +
                "            emails to\n" +
                "            users registered on Our Website and/or Application (or by any means and in any media, including, but not\n" +
                "            limited\n" +
                "            to,\n" +
                "            on Our Website and/or Application or through Our merchants or in advertisements / marketing campaigns / any\n" +
                "            form\n" +
                "            of\n" +
                "            audio or visual media or websites). We reserve the right to communicate Your personal information to any\n" +
                "            third\n" +
                "            party\n" +
                "            that makes a legally-compliant request for its disclosure.\n" +
                "        </p>\n" +
                "    \n" +
                "\n" +
                "    \n" +
                "        <h3>2.Collection of Information</h3>\n" +
                "        <p style=\"font-weight: normal;\">In order to provide Our Services, We may collect the following types of information:\n" +
                "            User information – When You use the Services, We automatically receive and record information from Your\n" +
                "            device and,\n" +
                "            applicable browser. Such information may include Your IP address, cookie information, software and hardware\n" +
                "            attributes,\n" +
                "            contacts and unique device ID. Most of the information We collect during Your use of Our Website and/or\n" +
                "            Application,\n" +
                "            such as Your device and hardware IDs and device type, the content of Your request, and basic usage stats\n" +
                "            about Your\n" +
                "            device and general use of Our Services does not by itself identify You to Us, though it may be unique or\n" +
                "            consist of\n" +
                "            or\n" +
                "            contain information that You consider personal. Specifically We collect, retain and use information\n" +
                "            collected,\n" +
                "            retained\n" +
                "            and shared through Facebook Platform, Twitter, Google and YouTube, according to the applicable terms and\n" +
                "            conditions\n" +
                "            of\n" +
                "            their privacy policies.\n" +
                "            User Id – In order to avail the services, you shall have to sign up on the Website and/or Application.\n" +
                "            Use history and preferences – We save Your query terms as well as the experience/category selected, these\n" +
                "            may\n" +
                "            associated\n" +
                "            with the unique User ID of Your device. We also save Your preferences as recorded through Your use of the\n" +
                "            Services\n" +
                "            and\n" +
                "            use them for personalization features.\n" +
                "            Cookies – When You use Our Services, We may send one or more cookies. We use cookies to improve the quality\n" +
                "            of Our\n" +
                "            Service by storing User preferences and tracking User information. Certain of Our products and services\n" +
                "            allow You to\n" +
                "            download and/or personalize the content You receive from Us. For these products and services, We will record\n" +
                "            information\n" +
                "            about Your preferences, along with any information You provide yourself.\n" +
                "        </p>\n" +
                "    \n" +
                "\n" +
                "    <h3>3.Use of Information</h3>\n" +
                "        <p style=\"font-weight: normal;\">The Personal Information We collect allows Us to keep You posted on Our latest announcements, upcoming\n" +
                "            events,\n" +
                "            including\n" +
                "            confirmations, security alerts and support and administrative messages. It also helps Us to improve Our\n" +
                "            Services. If\n" +
                "            You\n" +
                "            do not want to be on Our mailing list, You can opt out at any time by updating Your preferences on the\n" +
                "            Website.\n" +
                "            From time to time, We may use Your Personal Information to send important notices, such as communications\n" +
                "            and\n" +
                "            changes to\n" +
                "            Our terms, conditions and policies. Since, this information is important to Your interaction with Us, You\n" +
                "            cannot opt\n" +
                "            out\n" +
                "            of receiving these communications.\n" +
                "            We may also use Personal Information for internal purposes such as data analysis and research to improve Our\n" +
                "            Services,\n" +
                "            products and customer communication.\n" +
                "            We collect Non-Personally Identifiable Information about Our users generally to improve features and\n" +
                "            functionality\n" +
                "            of\n" +
                "            the Website. We may analyze trends through aggregated demographic and psychographic offers to users. We may\n" +
                "            also\n" +
                "            share\n" +
                "            this Non-Personally Identifiable Information with Our partners to allow them to assist Us in delivering\n" +
                "            tailored\n" +
                "            advertisements, promotions, deals, discounts, and other offers to You when You use the Services. We collect\n" +
                "            information\n" +
                "            from You when You participate in sweepstakes, games or other promotions offered on Our Website.\n" +
                "            We may also collect information about You through other methods, including research surveys or information\n" +
                "            We obtain\n" +
                "            from third parties, including verification services, data services, as well as public sources.\n" +
                "        </p>\n" +
                "\n" +
                "    <h3>4.Sharing of Personal Information</h3>\n" +
                "        <p style=\"font-weight: normal;\">We only share personal information with other companies or individuals in the following limited\n" +
                "            circumstances:\n" +
                "            We may share with third parties the location of Your device in order to improve and personalize Your\n" +
                "            experience of\n" +
                "            the\n" +
                "            Services.\n" +
                "            We may share with third parties certain pieces of non-personal information, such as the number of Users who\n" +
                "            used a\n" +
                "            particular service, users who clicked on a particular advertisement or who skipped a particular\n" +
                "            advertisement, as\n" +
                "            well\n" +
                "            as similar aggregated information. Such information does not identify You individually.\n" +
                "            We may share with third parties’ Unique Device ID. While such information does not by itself identify You to\n" +
                "            us, it\n" +
                "            may\n" +
                "            be unique or consist of or contain information that You consider personal, however such information does not\n" +
                "            identify\n" +
                "            You individually.\n" +
                "            We provide such information to trusted businesses or persons for the purpose of processing personal\n" +
                "            information on\n" +
                "            Our\n" +
                "            behalf. We require that these parties agree to process such information based on Our instructions and in\n" +
                "            compliance\n" +
                "            with\n" +
                "            this Privacy Policy and any other appropriate confidentiality and security measures.\n" +
                "            Advertisers: We allow advertisers and/or merchant partners (“Advertisers”) to choose the demographic\n" +
                "            information of\n" +
                "            users who will see their advertisements and/or promotional offers and You agree that We may provide any of\n" +
                "            the\n" +
                "            information We have collected from You in non-personally identifiable form to an Advertiser, in order for\n" +
                "            that\n" +
                "            Advertiser to select the appropriate audience for those advertisements and/or offers.\n" +
                "            If You sign through a third party social networking site or service, Your list of “friends” from that site\n" +
                "            or\n" +
                "            service\n" +
                "            may be automatically imported to the website and or service. Again, We do not control the policies and\n" +
                "            practices of\n" +
                "            any\n" +
                "            other third party site or service.\n" +
                "        </p>\n" +
                "\n" +
                "    \n" +
                "        <h3>5.Security</h3>\n" +
                "        <p style=\"font-weight: normal;\">The security of Your personal information is important to Us. We take precautions, including administrative,\n" +
                "            technical\n" +
                "            and physical measures, to safeguard Your personal information against loss, theft and misuse, as well as\n" +
                "            against\n" +
                "            unauthorized access, disclosure, alteration and destruction. When You enter sensitive information on Our\n" +
                "            registration We\n" +
                "            encrypt that information.\n" +
                "            This Website has various electronic, procedural and physical security measures in place to protect the loss,\n" +
                "            misuse\n" +
                "            and\n" +
                "            alteration of information, or any accidental loss, destruction or damage to data. When You submit Your\n" +
                "            information\n" +
                "            via\n" +
                "            the Website, Your information is protected through Our security systems.\n" +
                "        </p>\n" +
                "    \n" +
                "    \n" +
                "        <h3>6.Accessing Third Party Websites</h3>\n" +
                "        <p style=\"font-weight: normal;\">\n" +
                "            Website, mobile site and mobile app may contain links to other websites. Please note that when you click on\n" +
                "            one of\n" +
                "            these\n" +
                "            links, you are entering another website for which [insert company name] has no responsibility. We encourage\n" +
                "            you to\n" +
                "            read\n" +
                "            the privacy statements of all such sites as their policies may be materially different from this Privacy\n" +
                "            Policy. Of\n" +
                "            course, you are solely responsible for maintaining the secrecy of your passwords, and your membership\n" +
                "            account\n" +
                "            information. Please be very careful, responsible, and alert with this information, especially whenever\n" +
                "            you're\n" +
                "            online.\n" +
                "        </p>\n" +
                "    \n" +
                "    \n" +
                "        <h3>7.Protection of Children</h3>\n" +
                "        <p style=\"font-weight: normal;\">\n" +
                "\n" +
                "            If You are a User under the age of 18 (“Child User”), please ensure that You have adequate consent from Your\n" +
                "            parent\n" +
                "            or\n" +
                "            guardian to access Services in accordance with the present Privacy Policy. If You are the parent or guardian\n" +
                "            of a\n" +
                "            Child\n" +
                "            User, please advise Your child of the risks of posting personal information on Our Services or other online\n" +
                "            services,\n" +
                "            and that any information posted may be used by third parties without restriction. Notwithstanding anything\n" +
                "            contained\n" +
                "            herein, We shall always presume that every User has adequate consent and free-will to access Services and to\n" +
                "            enter\n" +
                "            into\n" +
                "            a legally enforceable contract with Us.\n" +
                "            We also reserve the right to remove, at Our sole discretion, any content or material posted by a User which\n" +
                "            may be\n" +
                "            of\n" +
                "            defamatory or sexual nature or offensive to other Users in any manner, and take appropriate legal action\n" +
                "            against\n" +
                "            such\n" +
                "            User in accordance with Our Terms of Use\n" +
                "        </p>\n" +
                "    \n" +
                "\n" +
                "    \n" +
                "        <h3>8.Cookies</h3>\n" +
                "        <p style=\"font-weight: normal;\">\n" +
                "            A cookie is a chunk of data that is sent to the Website from a server and stored on the hard drive of Your\n" +
                "            computer\n" +
                "            and/or mobile (“Cookies”). A session Cookie disappears automatically after You close Your browser. A\n" +
                "            persistent\n" +
                "            Cookie\n" +
                "            remains after You close Your browser and may be used by Your browser on subsequent visits to the Services.\n" +
                "            We may\n" +
                "            use\n" +
                "            Cookies related information to:</p>\n" +
                "            remember Your data and/or personal information so that You will not have to re-enter\n" +
                "                it during Your visit or the next time You access the Services;\n" +
                "            \n" +
                "            provide customized third-party advertisements, content, and information;\n" +
                "            monitor the effectiveness of third-party marketing campaigns;\n" +
                "            monitor aggregate site usage metrics such as total number of visitors and pages viewed; and\n" +
                "            track Your entries, submissions,and status in any promotions or other activities.\n" +
                "        >\n" +
                "        <p style=\"font-weight: normal;\">We may allow third-party service providers, like advertisers, to place and read their own Cookies, web\n" +
                "        beacons, and\n" +
                "        similar technologies to collect Data and/or Personal Information through Services. This data and/or personal\n" +
                "        information\n" +
                "        are collected directly and automatically by these third parties, and We do not participate in these data\n" +
                "        transmissions\n" +
                "        and these third-party cookies are not covered under this Privacy Policy.\n" +
                "        </p>\n" +
                "    \n" +
                "\n" +
                "    \n" +
                "        <h3>9.Changes to the Privacy Policy</h3>\n" +
                "        <p style=\"font-weight: normal;\">\n" +
                "            We reserve the right to change the Privacy Policy at Our sole discretion. We will inform Users of any such\n" +
                "            change by\n" +
                "            Us\n" +
                "            posting the updated Privacy Policy on Our Website. We encourage You to review this Privacy Policy regularly\n" +
                "            for any\n" +
                "            such\n" +
                "            changes. Your continued use of the Services will be subject to the then-current Privacy Policy.\n" +
                "        </p>\n" +
                "    \n" +
                "\n" +
                "    \n" +
                "        <h3>10.Compliance with Laws and Law Enforcement</h3>\n" +
                "        <p style=\"font-weight: normal;\">\n" +
                "            We will cooperate with government and law enforcement officials and private parties to enforce and comply\n" +
                "            with the\n" +
                "            law.\n" +
                "            We reserve the right to track IP addresses for the purposes of fraud prevention, and to release IP addresses\n" +
                "            to\n" +
                "            legal\n" +
                "            authorities. We will disclose information about You to government or law enforcement officials or private\n" +
                "            parties\n" +
                "            when\n" +
                "            We believe reasonably necessary to comply with law, to protect the property and rights of Olly or a third\n" +
                "            party, to\n" +
                "            protect the safety of the public or any person, or to prevent or stop activity We may consider to be, or to\n" +
                "            pose a\n" +
                "            risk\n" +
                "            of being, any illegal, unethical or legally actionable activity.\n" +
                "        </p>\n" +
                "    \n" +
                "\n" +
                "    \n" +
                "        <h3>11.Email Notifications</h3>\n" +
                "        <p style=\"font-weight: normal;\">\n" +
                "            You may be contacted, by email or other means; for example, We may send You promotional offer on behalf of\n" +
                "            other\n" +
                "            businesses, or communicate with You about Your use of the service. Also, We may receive a confirmation when\n" +
                "            You open\n" +
                "            an\n" +
                "            email from us. This confirmation helps us improve Our service. If You do not want to receive email or other\n" +
                "            mail\n" +
                "            from\n" +
                "            us, please indicate Your preference by clicking on the “Unsubscribe” link at the bottom of the email or\n" +
                "            changing\n" +
                "            Your\n" +
                "            email settings on Your account settings page. Please note that if You do not want to receive legal notices\n" +
                "            from us,\n" +
                "            those legal notices will still govern Your use of the service, and You are responsible for reviewing such\n" +
                "            legal\n" +
                "            notices\n" +
                "            for changes.\n" +
                "        </p>\n" +
                "    \n" +
                "\n" +
                "    \n" +
                "        <h3>12.Contact Information</h3>\n" +
                "        <p style=\"font-weight: normal;\">\n" +
                "            If You have any questions or concerns with respect to this Terms of Use or the Website or any information\n" +
                "            contained\n" +
                "            on\n" +
                "            thereon, You may contact Us by writing to Us at [insert company contact details]. These Terms of Use\n" +
                "            supersede any\n" +
                "            previous versions.\n" +
                "        </p>\n" +
                "    \n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>"));


    }

    @Override
    public boolean onSupportNavigateUp() {
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_TERMS_CONDITIONS,
                GlobalConstants.ACTION_BACK,
                GlobalConstants.LABLE_TO_HOME
        );
        return super.onSupportNavigateUp();
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

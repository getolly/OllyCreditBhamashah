package com.ollycredit.ui.help;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.model.RepaymentFetchedData;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.HelperClass;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpActivity extends BaseActivity implements HelpView {

    @Inject
    HelpPresenter helpPresenter;

    @BindView(R.id.tv_email)
    TextView tvEmail;

    @BindView(R.id.tv_phone)
    TextView tvPhone;

    @BindView(R.id.toolbar_help)
    Toolbar toolbar;

    private String email, phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        helpPresenter.attachView(this);
        setUp();
    }

    @Override
    protected void setUp() {
        phone = "+919643266205";
        email = "support@getolly.com";
        setSupportActionBar(toolbar);
        helpPresenter.fetchHelp();
        showLoadingView();
    }

    @Override
    public void helpDetails(ResponseModel responseModel) {
        hideLoadingView();
        email = search("help_email", responseModel.getRepaymentDisplayDataList());
        phone = search("help_phone", responseModel.getRepaymentDisplayDataList());
        tvEmail.setText(email);
        tvPhone.setText(phone);
    }

    private String search(String key, List<RepaymentFetchedData> list) {
        for (RepaymentFetchedData repaymentFetchedData : list) {
            if (repaymentFetchedData.getName().equals(key))
                return repaymentFetchedData.getContent();
        }
        return null;
    }

    @OnClick(R.id.ff_email)
    public void emailClicked() {
        ShareCompat.IntentBuilder.from(this)
                .setType("message/rfc822")
                .addEmailTo(email)
                .setSubject("Help")
                .startChooser();
    }

    @OnClick(R.id.ff_phone)
    public void phoneClicked() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    @Override
    public void reqFailed(int code, String msg) {
        hideLoadingView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().postSticky("object.class");
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_USER_PROFILE,
                GlobalConstants.ACTION_BACK,
                GlobalConstants.LABLE_TO_HOME
        );
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

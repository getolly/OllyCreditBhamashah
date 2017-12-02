package com.ollycredit.ui.repayments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.ollycredit.R;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.repayments.debit_card.DebitCardFragment;
import com.ollycredit.utils.GlobalConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentMethodActivity extends BaseActivity {

    @BindView(R.id.toolbar_repayment)
    Toolbar navToolbar;

    @GlobalConstants.PayMethod
    private int payMethod;

    @BindView(R.id.tv_payment)
    TextView tvPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        ButterKnife.bind(this);
        setSupportActionBar(navToolbar);

        setUp();
    }

    @SuppressWarnings("ResourceType")
    @Override
    protected void setUp() {
        payMethod = getIntent().getIntExtra("paymethod", GlobalConstants.DEFAULT);
        tvPayment.setText(getIntent().getStringExtra("amount"));
        switch (payMethod) {
            case GlobalConstants.PAY_METHOD_DEBIT:
                getSupportActionBar().setTitle("Add a new Card");

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_base, DebitCardFragment.newInstance(),
                                "debit card")
                        .commit();
                break;
        }

    }
}

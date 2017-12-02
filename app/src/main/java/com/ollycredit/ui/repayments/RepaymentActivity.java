package com.ollycredit.ui.repayments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.ollycredit.BuildConfig;
import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.PayUModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.api.remote.DataManager;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.card.card_home.CardHomeActivity;
import com.ollycredit.ui.repayments.store_cards.StoreCardListAdapter.StoredCardSelected;
import com.ollycredit.ui.repayments.store_cards.StoreCardsFragment;
import com.ollycredit.ui.repayments.store_cards.StoreCardsFragment.StoredCardStatus;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.PayUHashGenerator.PayuHashGeneratorPresenter;
import com.ollycredit.utils.PayUHashGenerator.PayuHashGeneratorView;
import com.ollycredit.utils.dialogs.CreditCardDialog;
import com.ollycredit.utils.dialogs.CreditCardDialog.CreditCardDialogCallback;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PostData;
import com.payu.india.Model.StoredCard;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.india.PostParams.PaymentPostParams;
import com.payu.payuui.Activity.PaymentsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RepaymentActivity extends BaseActivity implements PayuHashGeneratorView,
        StoredCardSelected, CreditCardDialogCallback, StoredCardStatus {

    @BindView(R.id.toolbar_repayment)
    Toolbar navToolbar;

    @BindView(R.id.tv_payment_type)
    TextView tvPaymentType;

    @Inject
    DataManager dataManager;

    @Inject
    PayuHashGeneratorPresenter presenter;

    @Inject
    PreferencesHelper preferencesHelper;

    @BindView(R.id.tv_helpline)
    TextView tvHelpline;

    private String actuallpayed, totalamount;
    private PaymentParams mPaymentParams;
    private PayUModel payUModel;
    private static String successUrl = BuildConfig.BASE_URL + "api/payment/success";
    private static String failureUrl = BuildConfig.BASE_URL + "api/payment/fail";
    private String merchantKey, userCredentials;
    @GlobalConstants.PayMethod
    private int paymentMethod;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment_ccdc);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);
        setUp();
        Payu.setInstance(getApplicationContext());

    }


    @Override
    protected void setUp() {

        setSupportActionBar(navToolbar);

        tvPaymentType.setText("PAYMENT");
        getTimeRange();
        totalamount = getIntent().getStringExtra("viewamount");
        actuallpayed = getIntent().getStringExtra("actualpay");
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        payUModel = new PayUModel();

        payUModel.setUserCredentials(userCredentials == null ? PayuConstants.DEFAULT :
                userCredentials);

        StoreCardsFragment storeCardsFragment = StoreCardsFragment.newInstance(payUModel);
        storeCardsFragment.setStoredCardStatus(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.saved_card_layout, storeCardsFragment,
                        "saved cards")
                .commit();
        paymentMethod = GlobalConstants.DEFAULT;
    }

    private void getTimeRange() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.HOUR_OF_DAY, 10);
        startCalendar.set(Calendar.MINUTE, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.HOUR_OF_DAY, 18);
        endCalendar.set(Calendar.MINUTE, 0);
        long currentTime = System.currentTimeMillis();
        if (startCalendar.getTimeInMillis() <= currentTime &&
                endCalendar.getTimeInMillis() >= currentTime) {
            tvHelpline.setText(Html.fromHtml("Facing issues in Repayment? Call us on " +
                    "<font color='#232c83'>8448010559</font>"));
        }
    }

    @OnClick(R.id.tv_helpline)
    public void helplineClicked() {
        String phone = "8448010559";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    @OnClick(R.id.cv_add_new_card)
    public void setPaymentMethodDebit() {
        tvPaymentType.setText("DEBIT CARD");
        paymentMethod = GlobalConstants.PAY_METHOD_DEBIT;
        startPaymentMethodActivity();
    }

    @OnClick(R.id.cv_net_banking)
    public void setPaymentMethodNetBank() {
        paymentMethod = GlobalConstants.PAY_METHOD_NETBANK;
        tvPaymentType.setText("NET BANKING");
    }

    @Override
    public void getCardSelected(Bundle storedCardBundle) {
        paymentMethod = GlobalConstants.PAY_METHOD_SAVED_CARDS;
        CreditCardDialog creditCardDialog = new CreditCardDialog(this, storedCardBundle);
        creditCardDialog.show();
    }

    @Override
    public void getCardDetails(String cvv, Bundle cardBundle) {
        paymentMethod = GlobalConstants.PAY_METHOD_SAVED_CARDS;
        StoredCard mStoredCard = cardBundle.getParcelable(PayuConstants.STORED_CARD);
        mStoredCard.setCvv(cvv);

        mPaymentParams.setCardToken(mStoredCard.getCardToken());
        mPaymentParams.setNameOnCard(mStoredCard.getNameOnCard());
        mPaymentParams.setCardName(mStoredCard.getCardName());
        mPaymentParams.setExpiryMonth(mStoredCard.getExpiryMonth());
        mPaymentParams.setExpiryYear(mStoredCard.getExpiryYear());
        mPaymentParams.setCvv(mStoredCard.getCvv());

        makePayment();
    }

    @Override
    public void getCardStored() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void startPaymentMethodActivity() {
        Intent intent = new Intent(RepaymentActivity.this, PaymentMethodActivity.class);
        intent.putExtra("paymethod", paymentMethod);
        intent.putExtra("amount", actuallpayed);
        startActivityForResult(intent, GlobalConstants.PAYMENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GlobalConstants.PAYMENT_REQUEST_CODE) {
            if (resultCode == GlobalConstants.PAYMENT_PROCEED) {
                mPaymentParams = data.getParcelableExtra("payment_params");
                makePayment();
            }
        }

        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (data != null) {
                try {
                    JSONObject jsonObject = new JSONObject(data.getStringExtra(PayuConstants.
                            PAYU_RESPONSE));
                    if (jsonObject.has(PayuConstants.STATUS) && jsonObject.getString(PayuConstants
                            .STATUS).compareToIgnoreCase("success") == 0) {
                        new AlertDialog.Builder(this)
                                .setCancelable(false)
                                .setMessage("Transaction Successfull")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent i = new Intent(getApplicationContext(), CardHomeActivity.class);

                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        finish();

                                        startActivity(i);
                                    }
                                })
                                .create()
                                .show();
                    } else {
                        Toast.makeText(this, "Some error occurred during the transcation, please " +
                                "try again", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void makePayment() {
        mPaymentParams.setKey(payUModel.getKey());
        mPaymentParams.setAmount(payUModel.getAmount());
        mPaymentParams.setProductInfo(payUModel.getProductinfo());
        mPaymentParams.setFirstName(payUModel.getFirstname());
        mPaymentParams.setEmail(payUModel.getEmail());
        mPaymentParams.setTxnId(payUModel.getTxnid());
        mPaymentParams.setSurl(successUrl);
        mPaymentParams.setFurl(failureUrl);
        mPaymentParams.setUdf1(payUModel.getUdf1());
        mPaymentParams.setUdf2(payUModel.getUdf2());
        mPaymentParams.setUdf3(payUModel.getUdf3());
        mPaymentParams.setUdf4(payUModel.getUdf4());
        mPaymentParams.setUdf5(payUModel.getUdf5());
        mPaymentParams.setUserCredentials(payUModel.getUserCredentials());

        // for offer_key
        if (null != mPaymentParams.getOfferKey()) {
            mPaymentParams.setOfferKey(mPaymentParams.getOfferKey());
        }

        presenter.getHash(payUModel);
    }

    @Override
    public void getReqFailed(int networkRequestCallFailed, String message) {
        showDialogBox(message);
    }

    @Override
    public void getHashSuccess(ResponseModel responseModel) {
        hideProgressDialog();
        mPaymentParams.setHash(responseModel.getPaymentHash());


        try {

            PostData postData = new PostData();

            switch (paymentMethod) {
                case GlobalConstants.PAY_METHOD_DEBIT:
                    postData = new PaymentPostParams(mPaymentParams, PayuConstants.CC).
                            getPaymentPostParams();
                    break;
                case GlobalConstants.PAY_METHOD_NETBANK:
                    postData = new PaymentPostParams(mPaymentParams, PayuConstants.NB).
                            getPaymentPostParams();
                    break;
                case GlobalConstants.PAY_METHOD_UPI:
                    postData = new PaymentPostParams(mPaymentParams, PayuConstants.UPI).
                            getPaymentPostParams();
                    break;
                case GlobalConstants.PAY_METHOD_SAVED_CARDS:
                    postData = new PaymentPostParams(mPaymentParams, PayuConstants.CC).
                            getPaymentPostParams();
            }

            if (postData.getCode() == PayuErrors.NO_ERROR) {

                PayuConfig payuConfig = new PayuConfig();

                if (BuildConfig.BASE_URL.contains("ec2"))
                    payuConfig.setEnvironment(PayuConstants.STAGING_ENV);
                else
                    payuConfig.setEnvironment(PayuConstants.PRODUCTION_ENV);

                payuConfig.setData(postData.getResult());
                Intent intent = new Intent(this, PaymentsActivity.class);
                intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
                startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
            } else {
                Toast.makeText(this, postData.getResult(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_REPAYMENT,
                GlobalConstants.ACTION_BACK,
                GlobalConstants.LABLE_TO_REPAY_AMOUNT
        );
    }
}

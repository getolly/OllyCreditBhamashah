package com.ollycredit.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.pattern.MaskPattern;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.gson.Gson;
import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.utils.CardUtils;
import com.ollycredit.utils.GlobalConstants;
import com.payu.india.Model.StoredCard;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuUtils;
import com.payu.payuui.SdkuiUtil.SdkUIConstants;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;


/**
 * Created by ch8n
 */

public class CreditCardDialog extends Dialog implements View.OnClickListener {


//    @BindView(R.id.et_expiry)
//    EditText etExpiryDate;

    private Bundle cardBundle;
    private HashMap<String, String> oneClickCardTokens = null;
    private int position = 0;
    private boolean oneClickPayment = false;
    private String issuingBankStatus = "";
    private StoredCard mStoredCard = null;

    private CreditCardDialog.CreditCardDialogCallback cardDialogCallback;

    private Context baseContext;

    private DisplayMetrics metrics;
    private int dialogWidth;
    private int dialogheight;

    private View creditCardView;
    private ImageView cardImage;

    private EditText ccEtCreditCard;
//    private TextView tvCardBrand;
    private EditText et_expiry_month;
    private EditText et_expiry_year;
    private EditText et_card_cvv;
    private EditText et_name_on_card;
    private CheckBox cb_enable_oneclick_payment;
    private boolean isNewCard = false;
    private Button btn_repayment;
    private Bundle newCardBundle;
    private String issuer;
    private PayuUtils payuUtils;
    private StringBuilder result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.layout_dialog_credit_card);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ButterKnife.bind(this);
        init();

        OllyCreditApplication
                .getInstance()
                .trackScreenView(this.getClass().getSimpleName());

        setUp();

    }


    private void init() {

        creditCardView = findViewById(R.id.credit_card_view);
        ccEtCreditCard = creditCardView.findViewById(R.id.et_card_number);
//        tvCardBrand = creditCardView.findViewById(R.id.cardBrand);
        et_expiry_month = creditCardView.findViewById(R.id.et_expiry_month);
        et_expiry_year = creditCardView.findViewById(R.id.et_expiry_year);
        et_card_cvv = creditCardView.findViewById(R.id.et_card_cvv);
        et_name_on_card = creditCardView.findViewById(R.id.et_name_on_card);
        cb_enable_oneclick_payment = creditCardView.findViewById(R.id.cb_enable_oneclick_payment);
        btn_repayment = creditCardView.findViewById(R.id.btn_repayment);
        cardImage = creditCardView.findViewById(R.id.iv_cardType);

        metrics = baseContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        //dialogWidth = (9 * width) / 10;
        dialogWidth = width;
        dialogheight = LinearLayout.LayoutParams.WRAP_CONTENT;

        this.getWindow().setLayout(dialogWidth, dialogheight);
        this.setCancelable(true);

    }

    private void setUp() {
        btn_repayment.setOnClickListener(this);
        et_card_cvv.requestFocus();
        payuUtils = new PayuUtils();

//        etExpiryDate.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int before, int count, int after) {
//                if (before == 1 && count == 2 && s.charAt(s.length()-1) != '/') {
//                    etExpiryDate.setText(etExpiryDate.getText().toString() + "/");
//                }
//                if (etExpiryDate.getText().toString().toCharArray().length < 3) {
//                    etExpiryDate.setText(etExpiryDate.getText().toString().replace("/", ""));
//                }
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        }); 191072  card pin1704

        ccEtCreditCard.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                String charSequence = s.toString().replaceAll("-", "");

                if (charSequence.length() > 6) { // to confirm rupay card we need min 6 digit.
                    if (null == issuer) {
                        issuer = payuUtils.getIssuer(charSequence.toString().replace(" ", ""));
                    }
                    if (issuer != null && issuer.length() > 1) {
                        cardImage.setImageResource(CardUtils.getIssuerImage(issuer));

                    }
                }
            }
        });


        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNNN-NNNN-NNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(ccEtCreditCard, smf);
        ccEtCreditCard.addTextChangedListener(mtw);


        MaskPattern mp091 = new MaskPattern("[0-9]");


        MaskFormatter mf =
                new MaskFormatter("[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]");

        mf.registerPattern(mp091);

        ccEtCreditCard.addTextChangedListener(new MaskTextWatcher(ccEtCreditCard, mf));
//


        ccEtCreditCard.setEnabled(false);
        et_expiry_month.setEnabled(false);
        et_expiry_year.setEnabled(false);
        et_name_on_card.setEnabled(false);
        cb_enable_oneclick_payment.setVisibility(View.GONE);
//        tvCardBrand.setText(mStoredCard.getCardBrand());
        ccEtCreditCard.setText(mStoredCard.getMaskedCardNumber());
        et_expiry_month.setText(mStoredCard.getExpiryMonth());
        et_expiry_year.setText(mStoredCard.getExpiryYear());
        et_name_on_card.setText(mStoredCard.getCardName());
        btn_repayment.setVisibility(View.VISIBLE);


    }


    public CreditCardDialog(Context context,
                            Bundle cardStoredBundle) {
        super(context);
        cardDialogCallback = (CreditCardDialogCallback) context;
        baseContext = context;
        cardBundle = cardStoredBundle;
        if (!isNewCard) {
            mStoredCard = cardStoredBundle.getParcelable(PayuConstants.STORED_CARD);
            issuingBankStatus = cardStoredBundle.getString(SdkUIConstants.ISSUING_BANK_STATUS);
            oneClickPayment = cardStoredBundle.getBoolean(PayuConstants.ONE_CLICK_PAYMENT);
            position = cardStoredBundle.getInt(SdkUIConstants.POSITION);
            oneClickCardTokens = (HashMap<String, String>) cardStoredBundle.getSerializable(PayuConstants.ONE_CLICK_CARD_TOKENS);
        } else {
            cardBundle = cardStoredBundle;
        }
    }


    public interface CreditCardDialogCallback {

        void getCardDetails(String cvv, Bundle cardStoredBundle);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.btn_repayment:

                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_DILAOG,
                                GlobalConstants.ACTION_RESEND_OTP,
                                GlobalConstants.LABLE_DIALOG_REPAYMENT);


                if (!isNewCard) {

                    String cvv = et_card_cvv.getText().toString().trim();
                    if (!TextUtils.isEmpty(cvv)) {

                        PayuUtils mPayuUtils = new PayuUtils();
                        boolean isCvvValid = false;

                        Log.e("cardBin", mStoredCard.getCardBin());

                        if (mPayuUtils.validateCvv(mStoredCard.getCardBin(), cvv) && !cvv.equals("")) {
                            isCvvValid = true;
                        }

                        if (isCvvValid) {
                            cardDialogCallback.getCardDetails(cvv, cardBundle);
                            dismiss();
                        } else {
                            showDialog("Error", "CVV is wrong");
                        }

                    } else {
                        showDialog("Error", "Please enter CVV");
                    }

                } else {
                    String creditCardNumber = ccEtCreditCard.getText().toString();
                    String expMonth = et_expiry_month.getText().toString();
                    String exYear = et_expiry_year.getText().toString();
                    String cardOwnerName = et_name_on_card.getText().toString();
                    String cardCvv = et_card_cvv.getText().toString();
                    boolean saveCard = cb_enable_oneclick_payment.isChecked();

                    if (!TextUtils.isEmpty(creditCardNumber)) {
                        cardBundle.putString("card_numb", creditCardNumber);
                    } else {
                        ccEtCreditCard.setError("Error");
                        return;
                    }
                    if (!TextUtils.isEmpty(expMonth)) {
                        cardBundle.putString("exp_month", expMonth);
                    } else {
                        et_expiry_month.setError("Error");
                        return;
                    }
                    if (!TextUtils.isEmpty(exYear)) {
                        cardBundle.putString("exp_year", exYear);
                    } else {
                        et_expiry_year.setError("Error");
                        return;
                    }
                    if (!TextUtils.isEmpty(cardOwnerName)) {
                        cardBundle.putString("card_owner", cardOwnerName);
                    } else {
                        et_name_on_card.setError("Error");
                        return;
                    }
                    if (!TextUtils.isEmpty(cardCvv)) {
                        cardBundle.putString("card_cvv", cardCvv);
                    } else {
                        et_name_on_card.setError("Error");
                        return;
                    }


                    cardBundle.putBoolean("save_card", saveCard);

                    dismiss();

                }


//                Toast.makeText(baseContext, "Repayment clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    public void showDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setMessage(message)
                .setTitle(title)
                .setNeutralButton("Ok", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog.show();
    }

}
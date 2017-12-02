package com.ollycredit.ui.repayments.debit_card;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.pattern.MaskPattern;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.base.BaseFragment;
import com.ollycredit.utils.CardUtils;
import com.ollycredit.utils.GlobalConstants;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Payu.PayuUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DebitCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DebitCardFragment extends BaseFragment {

    @BindView(R.id.et_card_number)
    EditText etCardNumber;

    @BindView(R.id.iv_cardType)
    ImageView ivCardImage;

    @BindView(R.id.et_expiry_month)
    EditText etExpiryMonth;

    @BindView(R.id.et_expiry_year)
    EditText etExpiryYear;

    @BindView(R.id.et_card_cvv)
    EditText etCardCVV;

    @BindView(R.id.et_name_on_card)
    EditText etNameOnCard;

    @BindView(R.id.til_card_numb)
    TextInputLayout tilCardNumb;

    @BindView(R.id.til_exp_month)
    TextInputLayout tilExpMonth;

    @BindView(R.id.til_exp_year)
    TextInputLayout tilExpYear;

    @BindView(R.id.til_exp_cvv)
    TextInputLayout tilExpCvv;

    @BindView(R.id.til_card_user_name)
    TextInputLayout tilCardUserName;


    private String issuer;
    private PayuUtils payuUtils;

    @BindView(R.id.cb_enable_oneclick_payment)
    CheckBox cbEnableOneClickPayment;

    @Inject
    PreferencesHelper preferencesHelper;

    private View rootView;

    public static DebitCardFragment newInstance() {
        return new DebitCardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_debit_card, container, false);

        // ivCardImage = rootView.findViewById(R.id.iv_card_img);

        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        setUp();

        return rootView;
    }

    @Override
    protected void setUp() {
        payuUtils = new PayuUtils();
        etCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String charSequence = s.toString().replaceAll("-", "");
                if (charSequence.length() > 6) { // to confirm rupay card we need min 6 digit.
                    if (null == issuer) {
                        issuer = payuUtils.getIssuer(charSequence);
                    }
                    if (issuer != null && issuer.length() > 1) {
                        ivCardImage.setImageResource(CardUtils.getIssuerImage(issuer));

                    }
                }

                if (charSequence.length() == 16) {
                    etExpiryMonth.requestFocus();
                }
            }
        });

        MaskPattern mp091 = new MaskPattern("[0-9]");
        MaskFormatter mf =
                new MaskFormatter("[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-" +
                        "[0-9][0-9][0-9][0-9]");
        mf.registerPattern(mp091);
        etCardNumber.addTextChangedListener(new MaskTextWatcher(etCardNumber, mf));
    }

    @OnFocusChange(R.id.et_card_number)
    public void onFocusChangeCardNumber(View view, boolean hasFocus) {

        String cardNumbWithDash = etCardNumber.getText().toString().trim();

        if (!hasFocus) {
            String cardNumber = cardNumbWithDash.replaceAll("-", "");
            if (TextUtils.isEmpty(cardNumber) || cardNumber.length() < 14) {
                tilCardNumb.setError("Invalid");
            } else {
                tilCardNumb.setError(null);
            }
        }
    }

    @OnTextChanged(R.id.et_expiry_month)
    public void onTextChangedExpiryMonth(CharSequence s) {
        if (s.length() == 2)
            etExpiryYear.requestFocus();
    }

    @OnFocusChange(R.id.et_expiry_month)
    public void onFocusChangeExpMonth(View view, boolean hasFocus) {

        if (!hasFocus) {
            String expDate = etExpiryMonth.getText().toString().trim();

            if (TextUtils.isEmpty(expDate) || expDate.length() != 2) {
                tilExpMonth.setError("Invalid");
            } else {
                tilExpMonth.setError(null);
            }
        }

    }

    @OnTextChanged(R.id.et_expiry_year)
    public void onTextChangedExipryYear(CharSequence s) {
        if (s.length() == 2)
            etCardCVV.requestFocus();
    }

    @OnFocusChange(R.id.et_expiry_year)
    public void onFocusChangeExpYear(View view, boolean hasFocus) {

        if (!hasFocus) {
            String expDate = etExpiryYear.getText().toString().trim();

            if (TextUtils.isEmpty(expDate) || expDate.length() != 2) {
                tilExpYear.setError("Invalid");
            } else {
                tilExpYear.setError(null);
            }
        }


    }

    @OnTextChanged(R.id.et_card_cvv)
    public void onTextChangedCVV(CharSequence s) {
        if (s.length() == 3)
            etNameOnCard.requestFocus();
    }

    @OnFocusChange(R.id.et_card_cvv)
    public void onFocusChangeCardCvv(View view, boolean hasFocus) {

        if (!hasFocus) {
            String expDate = etCardCVV.getText().toString().trim();

            if (TextUtils.isEmpty(expDate) || expDate.length() != 3) {
                tilExpCvv.setError("Invalid");
            } else {
                tilExpCvv.setError(null);
            }
        }

    }

    @OnFocusChange(R.id.et_name_on_card)
    public void onFocusChangeNameOnCard(View view, boolean hasFocus) {
        if (!hasFocus) {
            String expDate = etNameOnCard.getText().toString().trim();

            if (TextUtils.isEmpty(expDate) || expDate.length() < 2) {
                tilCardUserName.setError("Invalid");
            } else {
                tilCardUserName.setError(null);
            }
        }

    }


    @OnClick(R.id.btn_repayment)
    public void getPaymentParams() {

        if (isValid()) {
            PaymentParams paymentParams = new PaymentParams();
            paymentParams.setCardNumber(etCardNumber.getText().toString().replaceAll("-", ""));
            paymentParams.setCardName(etNameOnCard.getText().toString());
            paymentParams.setNameOnCard(etNameOnCard.getText().toString());
            paymentParams.setExpiryMonth(etExpiryMonth.getText().toString());// MM
            paymentParams.setExpiryYear("20"+etExpiryYear.getText().toString());// YY
            paymentParams.setCvv( etCardCVV.getText().toString());
            paymentParams.setEnableOneClickPayment(cbEnableOneClickPayment.isChecked() ? 1 : 0);
            paymentParams.setStoreCard(1);
            Intent intent = new Intent();
            intent.putExtra("payment_params", paymentParams);
            getActivity().setResult(GlobalConstants.PAYMENT_PROCEED, intent);
            getActivity().finish();
        }


    }


    private boolean isValid() {

        //13-16 credit card length with dash 15 - 19
        String cardNumbWithDash = etCardNumber.getText().toString().trim();

        String cardNumber = cardNumbWithDash.replaceAll("-", "");
        if (TextUtils.isEmpty(cardNumber) || cardNumber.length() < 14) {
            tilCardNumb.setError("Invalid");
            return false;
        }


        String expDate = etExpiryMonth.getText().toString().trim();

        if (TextUtils.isEmpty(expDate) || expDate.length() < 1) {
            tilExpMonth.setError("Invalid");
            return false;
        }

        String expYear = etExpiryYear.getText().toString().trim();

        if (TextUtils.isEmpty(expYear) || expYear.length() != 2) {
            tilExpYear.setError("Invalid");
            return false;
        }

        String cardCvv = etCardCVV.getText().toString().trim();

        if (TextUtils.isEmpty(cardCvv) || cardCvv.length() < 2) {
            tilExpCvv.setError("Invalid");
            return false;
        }

        String nameOnCard = etNameOnCard.getText().toString().trim();

        if (TextUtils.isEmpty(nameOnCard) || nameOnCard.length() < 2) {
            tilCardUserName.setError("Invalid");
            return false;
        }

        tilCardNumb.setError(null);
        tilExpMonth.setError(null);
        tilExpYear.setError(null);
        tilExpCvv.setError(null);
        tilCardUserName.setError(null);

        return true;
    }


}

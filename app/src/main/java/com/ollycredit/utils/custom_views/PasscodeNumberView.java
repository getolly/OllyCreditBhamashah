package com.ollycredit.utils.custom_views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.ui.onboarding.login.pin.LoginPinActivity;
import com.ollycredit.ui.recovery.pin.validate_phone.RecoverPinActivity;
import com.ollycredit.utils.GlobalConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilpreet on 24/10/17.
 */

public class PasscodeNumberView extends LinearLayout implements PasscodeTextView.PassCodeListener {

    @BindView(R.id.pv_passcode)
    PasscodeTextView passCodeView;

    @BindView(R.id.tv_error)
    TextView tvError;

    private PassCodeListener passCodeListener;

    public void setPassCodeListener(PassCodeListener passCodeListener) {
        this.passCodeListener = passCodeListener;
    }

    public PasscodeNumberView(Context context) {
        super(context);
        init(context);
    }

    public PasscodeNumberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PasscodeNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_passcode_number, this, true);
        ButterKnife.bind(this, view);
        passCodeView.setPassCodeListener(this);
    }

    @OnClick(R.id.btn_one)
    public void clickedOne() {
        passCodeView.enterCode("1");
        clearErrorMsg();
    }

    @OnClick(R.id.btn_two)
    public void clickedTwo() {
        passCodeView.enterCode("2");
        clearErrorMsg();
    }

    @OnClick(R.id.btn_three)
    public void clickedThree() {
        passCodeView.enterCode("3");
        clearErrorMsg();
    }

    @OnClick(R.id.btn_four)
    public void clickedFour() {
        passCodeView.enterCode("4");
        clearErrorMsg();
    }

    @OnClick(R.id.btn_five)
    public void clickedFive() {
        passCodeView.enterCode("5");
        clearErrorMsg();
    }

    @OnClick(R.id.btn_six)
    public void clickedSix() {
        passCodeView.enterCode("6");
        clearErrorMsg();
    }

    @OnClick(R.id.btn_seven)
    public void clickedSeven() {
        passCodeView.enterCode("7");
        clearErrorMsg();
    }

    @OnClick(R.id.btn_eight)
    public void clickedEight() {
        passCodeView.enterCode("8");
        clearErrorMsg();
    }

    @OnClick(R.id.btn_nine)
    public void clickedNine() {
        passCodeView.enterCode("9");
        clearErrorMsg();
    }

    @OnClick(R.id.btn_zero)
    public void clickedZero() {
        passCodeView.enterCode("0");
        clearErrorMsg();
    }

    @OnClick(R.id.backspace)
    public void backspace() {
        passCodeView.backSpace();
    }

    @OnClick(R.id.btn_signin_forgotPin)
    public void forgotPin() {
        OllyCreditApplication.getInstance().trackEvent(
                GlobalConstants.CATEGORY_ONBOARDING,
                GlobalConstants.ACTION_GOTO,
                GlobalConstants.LABLE_TO_RECOVER_PIN
        );

        getContext().startActivity(new Intent(getContext(), RecoverPinActivity.class));
    }

    public String getPasscode() {
        return passCodeView.getPasscode();
    }

    public void clearFields() {
        passCodeView.clearPasscodeField();
    }

    public void setError(String msg) {
        tvError.setText(msg);
    }

    private void clearErrorMsg() {
        tvError.setText("");
    }

    @Override
    public void passCodeEntered(String passcode) {
        if (passCodeListener != null)
            passCodeListener.passcodeEntered(passcode);
    }

    public interface PassCodeListener {
        public void passcodeEntered(String passcode);
    }
}

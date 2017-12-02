package com.ollycredit.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.utils.DialogInteractor.OtpDialogInteractor;
import com.ollycredit.utils.GlobalConstants;


/**
 * Created by ch8n
 */

public class OTPVerifyDialog extends Dialog implements View.OnClickListener, OtpDialogInteractor {


    private OTPVerifyDialog.OTPVerifyResult otpVerifyResult;

    private TextView changePhoneTextView;

    private Button resentOTPButton;
    private Button submitOTPButton;

    private View otpVerifyView;

    private String phone = "";
    private Context baseContext;

    private DisplayMetrics metrics;
    private int dialogWidth;
    private int dialogheight;

    private View pinView;
    private EditText pinOne;
    private EditText pinTwo;
    private EditText pinThree;
    private EditText pinFour;
    private TextView tvCountDown;
    private TextInputLayout til_otpview;


    private String finalOtp;
    private Button btnOtpResend;
    private CountDownTimer countdown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.otp_dialog_layout);
        init();

        OllyCreditApplication
                .getInstance()
                .trackScreenView(this.getClass().getSimpleName());

        setUp();

    }

    private void fillOtpView(String finalOtp) {

        pinOne.setText(String.valueOf(finalOtp.charAt(0)));
        pinOne.getBackground().clearColorFilter();
        pinTwo.setText(String.valueOf(finalOtp.charAt(1)));
        pinTwo.getBackground().clearColorFilter();
        pinThree.setText(String.valueOf(finalOtp.charAt(2)));
        pinThree.getBackground().clearColorFilter();
        pinFour.setText(String.valueOf(finalOtp.charAt(3)));
        pinFour.getBackground().clearColorFilter();
        til_otpview.setError(null);
    }

    @Override
    public void sendOtp(String otp) {
        fillOtpView(otp);
        Log.e("otpview", otp);
    }

    @Override
    public void sendPhoneNo(String phone) {
        this.phone = phone;
        phone = "+91" + this.phone;

        String changeNumber = "\t\tCHANGE NUMBER";

        SpannableString spannablestrPhone = new SpannableString(phone + changeNumber);

        spannablestrPhone.setSpan(new ForegroundColorSpan(Color.parseColor("#1E2F4D")), 0, phone.length(), 0);
        spannablestrPhone.setSpan(new StyleSpan(Typeface.BOLD), 0, phone.length(), 0);
        spannablestrPhone.setSpan(new ForegroundColorSpan(Color.parseColor("#728290")), phone.length() + 1, spannablestrPhone.length(), 0);
        spannablestrPhone.setSpan(new RelativeSizeSpan(0.7f), phone.length() + 1, spannablestrPhone.length(), 0);

        changePhoneTextView.setText(spannablestrPhone);

        Log.e("otpview", phone);
    }

    @Override
    public void otpIncorrect() {
        otpViewError("Otp incorrect!");
    }

    @Override
    public void otpcorrect() {
        this.dismiss();
    }

    @Override
    public void btnOtpReset() {
        btnOtpResend.setVisibility(View.VISIBLE);
        //btnOtpResend.setText("Otp received!");
        //btnOtpResend.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_main));

        btnOtpResend.setText("Resend OTP");
        btnOtpResend.setTextColor(ContextCompat.getColor(getContext(), R.color.cyan_main));
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//            btnOtpResend.setText("Resend OTP");
        //btnOtpResend.setTextColor(ContextCompat.getColor(getContext(), R.color.cyan_main));
//            }
//        }, 2000);
    }


    private void init() {
        changePhoneTextView = (TextView) findViewById(R.id.text_otp_dialog_phoneEdit);
        resentOTPButton = (Button) findViewById(R.id.btn_otp_dialog_resendOtp);
        submitOTPButton = (Button) findViewById(R.id.btn_otp_dialog_submit);
        til_otpview = (TextInputLayout) findViewById(R.id.til_otpview);


        pinView = findViewById(R.id.pinview_otp_verify);
        pinOne = (EditText) pinView.findViewById(R.id.pin_one);
        pinTwo = (EditText) pinView.findViewById(R.id.pin_two);
        pinThree = (EditText) pinView.findViewById(R.id.pin_three);
        pinFour = (EditText) pinView.findViewById(R.id.pin_four);


        otpVerifyView = findViewById(R.id.resendOtpView);
        btnOtpResend = (Button) otpVerifyView.findViewById(R.id.btn_otp_dialog_resendOtp);
        tvCountDown = (TextView) otpVerifyView.findViewById(R.id.tv_countdown);


        metrics = baseContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        //dialogWidth = (9 * width) / 10;
        dialogWidth = width;
        dialogheight = LinearLayout.LayoutParams.WRAP_CONTENT;

        this.getWindow().setLayout(dialogWidth, dialogheight);


    }

    private void setUp() {

        resentOTPButton.setOnClickListener(this);
        submitOTPButton.setOnClickListener(this);
        changePhoneTextView.setOnClickListener(this);


        countdown = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

                if (millisUntilFinished / 1000 < 10) {
                    tvCountDown.setText("00:0" + millisUntilFinished / 1000);
                } else {
                    tvCountDown.setText("00:" + millisUntilFinished / 1000);
                }


                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                btnOtpResend.setVisibility(View.VISIBLE);
            }

        };


        pinOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    pinTwo.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        pinTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    pinThree.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    pinOne.requestFocus();
                }
            }
        });

        pinThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    pinFour.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    pinTwo.requestFocus();
                }
            }
        });

        pinFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    pinThree.requestFocus();
                }
            }
        });
    }

    public OTPVerifyDialog(Context context) {
        super(context);
        otpVerifyResult = (OTPVerifyResult) context;
        baseContext = context;
    }


    public String getOtpFromPinView() {

        finalOtp = (pinOne.getText().toString()
                + pinTwo.getText().toString()
                + pinThree.getText().toString()
                + pinFour.getText().toString()).trim();

        Log.e("dialog final otp", finalOtp);

        return finalOtp;
    }


    public interface OTPVerifyResult {

        void isOTPValid(String otp);

        void resentOTPReq(String phoneNumb);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.btn_otp_dialog_resendOtp:

                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_DILAOG,
                                GlobalConstants.ACTION_RESEND_OTP,
                                GlobalConstants.LABLE_DIALOG_OTP_VERIFY);


                btnOtpResend.setVisibility(View.INVISIBLE);


                countdown.start();


                otpVerifyResult.resentOTPReq(phone);

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        btnOtpResend.setVisibility(View.VISIBLE);
//                        Toast.makeText(baseContext,"You haven't received any OTP please try again...", Toast.LENGTH_SHORT).show();
//                    }
//                }, 60000);
                break;
            case R.id.text_otp_dialog_phoneEdit:

                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_DILAOG,
                                GlobalConstants.ACTION_EDIT_PHONE,
                                GlobalConstants.LABLE_DIALOG_OTP_VERIFY);

                ChangeOtpPhoneDialog changeOtpPhoneDialog = new ChangeOtpPhoneDialog(baseContext);
                changeOtpPhoneDialog.setCancelable(false);
                changeOtpPhoneDialog.show();

                //otpVerifyResult.changeNumberRequest();


//                OTPVerifyDialog.this.dismiss();
                break;

            case R.id.btn_otp_dialog_submit:

                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_DILAOG,
                                GlobalConstants.ACTION_ACCEPT,
                                GlobalConstants.LABLE_DIALOG_OTP_VERIFY);

                String otp = getOtpFromPinView();

                if (otp.length() != 4) {
                    //showDialog("Please enter a 4 digit valid pin...");
                    otpViewError("Please enter a 4 digit valid pin...");
                    //pinFour.getBackground().clearColorFilter();
                    return;
                }
                otpVerifyResult.isOTPValid(otp);
                break;
        }
    }

    private void otpViewError(String errorMsg) {
        til_otpview.setError(errorMsg);
        pinOne.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.red_error), Mode.SRC_IN);
        pinTwo.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.red_error), Mode.SRC_IN);
        pinThree.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.red_error), Mode.SRC_IN);
        pinFour.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.red_error), Mode.SRC_IN);
    }

    public void showDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setMessage(message)
                .setTitle("Error")
                .setNeutralButton("Ok", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        alertDialog.show();
    }

}
package com.ollycredit.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.utils.GlobalConstants;

/**
 * Created by ch8n
 */

public class ChangeOtpPhoneDialog extends Dialog implements View.OnClickListener {


    private ChangeOtpPhoneDialog.NewNumbCallback numbCallback;

    private EditText newPhoneNumbEditText;

    private Button cancelButton;
    private Button submitButton;
    private ImageView ivPhone;

    private String phone;
    private Context baseContext;
    private TextInputLayout tilChangephone;

    private DisplayMetrics metrics;
    private int dialogWidth;
    private int dialogheight;

    private String cyan_main = "#00aad4";
    private String red_error = "#F5414A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.layout_dialog_new_number);

        init();

        OllyCreditApplication
                .getInstance()
                .trackScreenView(this.getClass().getSimpleName());

        cancelButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        newPhoneNumbEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) baseContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(newPhoneNumbEditText, InputMethodManager.SHOW_FORCED);
                }
            }
        });

        newPhoneNumbEditText.requestFocus();

    }

    private void init() {

        newPhoneNumbEditText = (EditText) findViewById(R.id.edit_dialog_newNumb);
        cancelButton = (Button) findViewById(R.id.dialog_cancel_button);
        submitButton = (Button) findViewById(R.id.dialog_ok_button);
        ivPhone = (ImageView) findViewById(R.id.iv_phone);
        tilChangephone = (TextInputLayout) findViewById(R.id.tilPhoneTwo);

        metrics = baseContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;


        //this.getWindow().setLayout((9 * width) / 10, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogWidth = width;
        dialogheight = LinearLayout.LayoutParams.WRAP_CONTENT;

        this.getWindow().setLayout(dialogWidth, dialogheight);


    }

    public ChangeOtpPhoneDialog(Context context) {
        super(context);
        numbCallback = (NewNumbCallback) context;
        baseContext = context;
    }


    public interface NewNumbCallback {
        void resetNumber(String result);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.dialog_cancel_button:
                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_DILAOG,
                                GlobalConstants.ACTION_CANCEL,
                                GlobalConstants.LABLE_DIALOG_CHANGE_PHONE);

                this.dismiss();
                break;

            case R.id.dialog_ok_button:

                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_DILAOG,
                                GlobalConstants.ACTION_ACCEPT,
                                GlobalConstants.LABLE_DIALOG_CHANGE_PHONE);


                tilChangephone.setErrorEnabled(true);

                if (newPhoneNumbEditText.getText().toString().isEmpty() || newPhoneNumbEditText.getText().toString().trim().length() != 10) {
                    tilChangephone.setError("Please enter valid phone number");
                    tilChangephone.getEditText().setHighlightColor(Color.parseColor(red_error));
                    tilChangephone.getEditText().setHintTextColor(Color.parseColor(red_error));
                    ivPhone.setColorFilter(Color.parseColor(red_error));
                    return;
                }

                tilChangephone.setErrorEnabled(false);
                tilChangephone.getEditText().setHighlightColor(Color.parseColor(cyan_main));
                tilChangephone.getEditText().setHintTextColor(Color.parseColor(cyan_main));

                phone = newPhoneNumbEditText.getText().toString().trim();
                numbCallback.resetNumber(phone);
                ChangeOtpPhoneDialog.this.dismiss();
                break;
        }
    }

}
package com.ollycredit.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.HelperClass;

/**
 * Created by ch8n
 */

public class ChangeEmailDialog extends Dialog implements View.OnClickListener {


    private EmailCallback emailCallback;

    private EditText nEmailEditText;

    private Button cancelButton;
    private Button submitButton;

    private String email;
    private Context baseContext;



    private DisplayMetrics metrics;
    private int dialogWidth;
    private int dialogheight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.layout_dialog_change_email);

        init();

        OllyCreditApplication
                .getInstance()
                .trackScreenView(this.getClass().getSimpleName());

        cancelButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        nEmailEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) baseContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(nEmailEditText, InputMethodManager.SHOW_FORCED);
                }
            }
        });

        nEmailEditText.requestFocus();

    }

    private void init() {

        nEmailEditText = (EditText) findViewById(R.id.edit_dialog_email);
        cancelButton = (Button) findViewById(R.id.dialog_cancel_button);
        submitButton = (Button) findViewById(R.id.dialog_ok_button);

        metrics = baseContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;


        //this.getWindow().setLayout((9 * width) / 10, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogWidth = width;
        dialogheight = LinearLayout.LayoutParams.WRAP_CONTENT;

        this.getWindow().setLayout(dialogWidth, dialogheight);


    }

    public ChangeEmailDialog(Context context) {
        super(context);
        emailCallback = (EmailCallback) context;
        baseContext = context;
    }


    public interface EmailCallback {
        void changeEmail(String result);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.dialog_cancel_button:
                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_DILAOG,
                                GlobalConstants.ACTION_CANCEL,
                                GlobalConstants.LABLE_DIALOG_CHANGE_EMAIL);
                this.dismiss();
                break;

            case R.id.dialog_ok_button:

                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_DILAOG,
                                GlobalConstants.ACTION_ACCEPT,
                                GlobalConstants.LABLE_DIALOG_CHANGE_EMAIL);

                if (nEmailEditText.getText().toString().isEmpty() || HelperClass.isValidEmail(nEmailEditText.getText().toString())) {
                    nEmailEditText.setError("Please enter valid email number");
                    return;
                }

                email = nEmailEditText.getText().toString().trim();
                emailCallback.changeEmail(email);
                ChangeEmailDialog.this.dismiss();
                break;
        }
    }

}
package com.ollycredit.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;


/**
 * Created by ch8n
 */

public class UserFeedbackSendDialog extends Dialog implements View.OnClickListener {

    private Context baseContext;
    private DisplayMetrics metrics;
    private int dialogWidth;
    private int dialogheight;
    private Button gotItButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.user_feedback_send_dialog);
        init();
        OllyCreditApplication
                .getInstance()
                .trackScreenView(this.getClass().getSimpleName());
        setUp();

    }

    private void init() {
        metrics = baseContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        //dialogWidth = (9 * width) / 10;
        dialogWidth = width;
        dialogheight = LinearLayout.LayoutParams.WRAP_CONTENT;
        gotItButton = (Button) findViewById(R.id.got_it_button);

        this.getWindow().setLayout(dialogWidth, dialogheight);
    }

    private void setUp() {

        /*getMoreButton.setOnClickListener(this);
        dissmissButton.setOnClickListener(this);
        creditIncTextView.setText("You just have increased your credit limit by " + baseContext.getString(R.string.rs_symbol) + amount + " !");
*/
        gotItButton.setOnClickListener(this);
    }

    public UserFeedbackSendDialog(Context context) {
        super(context);
        baseContext = context;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.got_it_button:
                UserFeedbackSendDialog.this.dismiss();
                break;

        }
    }

    public void showDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setMessage(message)
                .setTitle("User Feedback")
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
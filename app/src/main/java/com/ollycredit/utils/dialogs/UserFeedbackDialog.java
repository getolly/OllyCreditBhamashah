package com.ollycredit.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.utils.GlobalConstants;


/**
 * Created by ch8n
 */

public class UserFeedbackDialog extends Dialog implements View.OnClickListener {

    private Context baseContext;
    private TextView textView;
    private DisplayMetrics metrics;
    private int dialogWidth;
    private int dialogheight;
    private ImageView screenshotView;
    private Button caneclButton;
    private Button sendFeedBackButton;
    private Bitmap image;
    private EditText feedbackEditText;

    OnMyDialogResult mDialogResult; // the callback


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.user_feedback_dialog);
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
        //screenshotView = (ImageView) findViewById(R.id.image_user_feedback);
        caneclButton = (Button) findViewById(R.id.button_cancel_user_feedback);
        sendFeedBackButton = (Button) findViewById(R.id.button_send_user_feedback);
        feedbackEditText = (EditText) findViewById(R.id.edit_feedback);
        this.getWindow().setLayout(dialogWidth, dialogheight);
    }

    private void setUp() {

        /*getMoreButton.setOnClickListener(this);
        dissmissButton.setOnClickListener(this);
        creditIncTextView.setText("You just have increased your credit limit by " + baseContext.getString(R.string.rs_symbol) + amount + " !");
*/
        //screenshotView.setImageBitmap(image);
        caneclButton.setOnClickListener(this);
        sendFeedBackButton.setOnClickListener(this);
    }

    public UserFeedbackDialog(Context context, Bitmap bitmap) {
        super(context);
        baseContext = context;
        this.image = bitmap;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button_cancel_user_feedback:

                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_DILAOG,
                                GlobalConstants.ACTION_CANCEL,
                                GlobalConstants.LABLE_DIALOG_FEEDBACK);

                UserFeedbackDialog.this.dismiss();
                //((Activity)baseContext).finish();
                break;

            case R.id.button_send_user_feedback:
                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_DILAOG,
                                GlobalConstants.ACTION_DIALOG_OK,
                                GlobalConstants.LABLE_DIALOG_FEEDBACK);

                String feedbackText = feedbackEditText.getText().toString();
                if (!feedbackText.isEmpty()) {
                    mDialogResult.finish(GlobalConstants.SEND_FEEDBACK_MESSAGE,feedbackText,image);
                    break;
                }
                else {
                    feedbackEditText.setError("Can't be empty");
                }
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
    public void setDialogResult(OnMyDialogResult dialogResult){
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult{
        void finish(String result,String message,Bitmap bmp);
    }
}
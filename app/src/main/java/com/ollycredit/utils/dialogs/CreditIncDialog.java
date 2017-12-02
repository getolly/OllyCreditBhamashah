package com.ollycredit.utils.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.ui.card.card_home.CardHomeActivity;
import com.ollycredit.utils.GlobalConstants;


/**
 * Created by ch8n
 */

public class CreditIncDialog extends Dialog implements View.OnClickListener {


    private TextView creditIncTextView;
    private ImageView creditIncImageView;

    private Button getMoreButton;
    private Button dissmissButton;

    private String phone;
    private Context baseContext;

    private DisplayMetrics metrics;
    private int dialogWidth;
    private int dialogheight;

    private String amount;
    private int imageId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.pan_dialog_layout);
        init();

        OllyCreditApplication
                .getInstance()
                .trackScreenView(this.getClass().getSimpleName());

        setUp();

    }

    private void init() {
        creditIncTextView = (TextView) findViewById(R.id.tv_dialoginc_limit);
        creditIncImageView = (ImageView) findViewById(R.id.iv_dialoginc_limit);
        getMoreButton = (Button) findViewById(R.id.btn_dialoginc_getmore);
        dissmissButton = (Button) findViewById(R.id.btn_dialoginc_ok);

        metrics = baseContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        //dialogWidth = (9 * width) / 10;
        dialogWidth = width;
        dialogheight = LinearLayout.LayoutParams.WRAP_CONTENT;

        this.getWindow().setLayout(dialogWidth, dialogheight);
    }

    private void setUp() {

        creditIncImageView.setImageResource(imageId);
        getMoreButton.setOnClickListener(this);
        dissmissButton.setOnClickListener(this);
        creditIncTextView.setText("You just have increased your credit limit by " + baseContext.getString(R.string.rs_symbol) + amount + " !");

    }

    public CreditIncDialog(Context context, String amount, int Id) {
        super(context);
        this.amount = amount;
        baseContext = context;
        this.imageId = Id;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_dialoginc_ok:
                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_DILAOG,
                                GlobalConstants.ACTION_CANCEL,
                                GlobalConstants.LABLE_DIALOG_LIMIT_INC);

                CreditIncDialog.this.dismiss();
                ((Activity)baseContext).finish();
                break;

            case R.id.btn_dialoginc_getmore:

                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_DILAOG,
                                GlobalConstants.ACTION_DIALOG_OK,
                                GlobalConstants.LABLE_DIALOG_LIMIT_INC);


                baseContext.startActivity(new Intent(baseContext, CardHomeActivity.class));
                CreditIncDialog.this.dismiss();
                ((Activity)baseContext).finish();

                break;
        }
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
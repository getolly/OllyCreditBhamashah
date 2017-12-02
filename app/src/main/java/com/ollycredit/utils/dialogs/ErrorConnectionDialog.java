package com.ollycredit.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.utils.DialogInteractor;

/**
 * Created by ch8n on 28/8/17.
 */

public class ErrorConnectionDialog extends Dialog implements DialogInteractor.ErrorDialogInteractor {


    private View errorSomthing;
    private View errorConnection;
    private DisplayMetrics metrics;
    private Context context;
    private int dialogWidth;
    private int dialogheight;
    private View itemView;


    public ErrorConnectionDialog(Context context) {
        super(context, android.R.style.Theme_Translucent);
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OllyCreditApplication
                .getInstance()
                .trackScreenView(this.getClass().getSimpleName());


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        itemView = View.inflate(getContext(), R.layout.layout_error_no_conection, null);
        this.setContentView(itemView);
        init();
        setUp();
    }


    private void init() {

    }

    private void setUp() {

//        metrics = context.getResources().getDisplayMetrics();
//        int width = metrics.widthPixels+120;
//        int height = metrics.heightPixels+120;
//        dialogWidth = width;
//        dialogheight = height;
//
//        this.getWindow().setLayout(dialogWidth, dialogheight);


        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
    }


    @Override
    public void promptErrorSomthing() {
        errorSomthing.setVisibility(View.VISIBLE);
        errorConnection.setVisibility(View.GONE);
    }

    @Override
    public void promptErrorConnection() {
        errorSomthing.setVisibility(View.GONE);
        errorConnection.setVisibility(View.VISIBLE);
    }
}

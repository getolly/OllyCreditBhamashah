package com.ollycredit.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.utils.DialogInteractor;

/**
 * Created by ch8n on 28/8/17.
 */

public class LoadingDialog extends Dialog implements DialogInteractor.ErrorDialogInteractor {


    private ProgressBar progressBar;
    private DisplayMetrics metrics;
    private Context context;
    private int dialogWidth;
    private int dialogheight;


    public LoadingDialog(Context context) {
        super(context, android.R.style.Theme_Translucent);
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.layout_loading);

        OllyCreditApplication
                .getInstance()
                .trackScreenView(this.getClass().getSimpleName());

        init();
        setUp();
    }


    private void init() {
        progressBar = findViewById(R.id.progress);
    }

    private void setUp() {

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        progressBar
                .getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#00aad4"), android.graphics.PorterDuff.Mode.SRC_ATOP);

    }


    @Override
    public void promptErrorSomthing() {

    }

    @Override
    public void promptErrorConnection() {

    }
}

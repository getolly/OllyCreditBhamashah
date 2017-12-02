package com.ollycredit.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ollycredit.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilpreet on 22/10/17.
 */

public class UpdateDialog extends Dialog {

    @BindView(R.id.tv_update_msg)
    TextView tvUpdateMsg;

    @BindView(R.id.btn_update)
    AppCompatButton btnUpdate;

    private String msg;
    private Typeface typeface_reg, typeface_bold;

    public UpdateDialog(@NonNull Context context, String msg) {
        super(context);
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog_layout);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;

        this.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);

        typeface_reg = Typeface.createFromAsset(getContext().getAssets(), "fonts/Muli-Regular.ttf");
        typeface_bold = Typeface.createFromAsset(getContext().getAssets(), "fonts/muli-bold.ttf");

        tvUpdateMsg.setTypeface(typeface_reg);
        btnUpdate.setTypeface(typeface_bold);

        tvUpdateMsg.setText(msg);

    }

    @OnClick(R.id.btn_update)
    public void updateClicked() {
        dismiss();
        String appPackageName = getContext().getPackageName();
        try {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}

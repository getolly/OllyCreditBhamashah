package com.ollycredit.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Window;

import com.ollycredit.utils.dialogs.LoadingDialog;
import com.ollycredit.utils.helpers.ProgressHelper;


/**
 * Created by ch8n on 3/5/17.
 */

public abstract class BaseFragment extends Fragment {

    private BaseActivityCallback callback;
    private ProgressHelper progressHelper;
    private LoadingDialog loadingDialog;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressHelper = new ProgressHelper(getActivity());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
        } else {
            activity = null;
        }

        try {
            callback = (BaseActivityCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement BaseActivityCallback methods");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }


    public void showLoadingView() {
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    public void hideLoadingView() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }



    public void showDialogBox(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setCancelable(false)
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


    protected void showProgressDialog(String message) {
        if (callback != null)
            callback.showProgressDialog(message);
    }

    protected void hideProgressDialog() {
        if (callback != null)
            callback.hideProgressDialog();
    }

    protected void showProgressBar() {
        progressHelper.show();
    }

    protected void hideProgressBar() {
        progressHelper.hide();
    }

    protected abstract void setUp();

}

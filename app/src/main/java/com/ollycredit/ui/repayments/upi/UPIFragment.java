package com.ollycredit.ui.repayments.upi;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ollycredit.R;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Payu.PayuConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UPIFragment extends Fragment {

    @BindView(R.id.tv_upi_info)
    TextView tvUPIInfo;

    @BindView(R.id.et_virtual_address)
    EditText etVirtualAddress;

    private OnCLickListener onCLickListener;
    private PopupWindow pwHelperUPI;
    private SpannableString vpaSpannableMore;
    private SpannableString vpaSpannableLess;
    private PaymentParams paymentParams;

    public static UPIFragment newInstance() {
        return new UPIFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upi, container, false);
        ButterKnife.bind(this, view);
        setUpUI();
        return view;

    }


    private void setUpUI() {

        OnTextChangeListener onTextChangeListener = new OnTextChangeListener();
        onCLickListener = new OnCLickListener();
        etVirtualAddress.addTextChangedListener(onTextChangeListener);
        etVirtualAddress.setOnClickListener(onCLickListener);
        setSpannableTextView();


        paymentParams = new PaymentParams();
    }

    /**
     * Set the Info about UPI in TextView
     * Handle the 'show less' and 'show more' info option of UPI
     */
    private void setSpannableTextView() {
        String vpaMoreText = "VPA is your Virtual Payment Address in the format yourname@bankname." +
                "You can create your VPA from any UPI enabled Bank application or third party " +
                "application. You need the Bank application installed in your phone to complete " +
                "this transaction.";
        String vpaLessText = "VPA is your Virtual Payment Address in the format yourname@bankname.";

        String showLess = "Show Less";
        String showMore = "Show More";


        vpaSpannableMore = new SpannableString(vpaMoreText + " " + showLess);
        vpaSpannableLess = new SpannableString(vpaLessText + " " + showMore);
        ClickableSpan clickableSpanLess = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // do another thing
                tvUPIInfo.setText(vpaSpannableLess);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(getColor(getActivity().getApplicationContext(), com.payu.payuui.R.color.
                        cb_vpa_text_highlighter));
                ds.setUnderlineText(true);
            }
        };

        ClickableSpan clickableSpanMore = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // do another thing
                tvUPIInfo.setText(vpaSpannableMore);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(getColor(getActivity().getApplicationContext(), com.payu.payuui.R.color.
                        cb_vpa_text_highlighter));
                ds.setUnderlineText(true);
            }
        };

        vpaSpannableLess.setSpan(clickableSpanMore, vpaLessText.length() + 1, (vpaLessText +
                showMore).length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        vpaSpannableMore.setSpan(clickableSpanLess, vpaMoreText.length() + 1, (vpaMoreText +
                showLess).length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvUPIInfo.setText(vpaSpannableLess);
        tvUPIInfo.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Display the helper screen for UPI
     */
//    private void showHelperScreen() {
//        LayoutInflater inflater = (LayoutInflater)
//                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View helperView = inflater.inflate(com.payu.payuui.R.layout.upi_helper, null, false);
//        pwHelperUPI = new PopupWindow(helperView,
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                true);
//        (helperView.findViewById(com.payu.payuui.R.id.close_button)).setOnClickListener(onCLickListener);
//        pwHelperUPI.showAtLocation(getActivity().findViewById(R.id.tv_learn_upi), Gravity.CENTER, 0, 0);
//    }

    /**
     * Return the color res ID
     *
     * @param context application context
     * @param id      res ID
     * @return color res id
     */
    private int getColor(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    private class OnTextChangeListener implements TextWatcher {

        private int intialTextSize;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            intialTextSize = count;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!(count == PayuConstants.MAX_VPA_SIZE && intialTextSize > PayuConstants.
                    MAX_VPA_SIZE)) {
                etVirtualAddress.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.length() > PayuConstants.MAX_VPA_SIZE) {
                etVirtualAddress.setError(getActivity().getText(com.payu.payuui.R.string.
                        error_invalid_vpa));
                etVirtualAddress.setText(s.subSequence(0, PayuConstants.MAX_VPA_SIZE));
                etVirtualAddress.setSelection(PayuConstants.MAX_VPA_SIZE);
            } else {


                for (int i = s.length(); i > 0; i--) {
                    if (s.subSequence(i - 1, i).toString().equals("\n"))
                        s.replace(i - 1, i, "");
                }
            }

        }
    }

    public PaymentParams getPaymentParams() {
        paymentParams.setVpa(etVirtualAddress.getText().toString());
        return paymentParams;
    }

    private class OnCLickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.close_button) {
                pwHelperUPI.dismiss();
            } else if (v.getId() == R.id.et_virtual_address) {
                etVirtualAddress.setError(null);
            }
        }
    }
}

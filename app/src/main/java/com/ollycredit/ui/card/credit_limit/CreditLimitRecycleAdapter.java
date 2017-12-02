package com.ollycredit.ui.card.credit_limit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.model.CreditLimitAmounts;
import com.ollycredit.api.model.CreditLimitFlags;
import com.ollycredit.ui.card.card_history.CardHistoryActivity;
import com.ollycredit.ui.card.credit_limit.CreditLimitRecycleAdapter.CardLimitViewHolder;
import com.ollycredit.ui.card.verify.debit.debit_register.VerifyBankActivity;
import com.ollycredit.ui.card.verify.kyc.appoint_confirm.AppointConfirmActivity;
import com.ollycredit.ui.card.verify.kyc.kyc_register.AppointKycActivity;
import com.ollycredit.ui.card.verify.pan.VerifyPanActivity;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.helpers.TextFormatHelper;
import com.ollycredit.utils.helpers.ViewPagerHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ollycredit.R.id.image_limit_rv_icon;


/**
 * Created by ch8n on 24/7/17.
 */

public class CreditLimitRecycleAdapter extends RecyclerView.Adapter<CardLimitViewHolder> {


    private Context context;
    private List<String> moreCreditTitles;
    private List<String> moreCreditStatus;
    private List<Integer> moreCreditIconsIds;
    private int seq;
    private ViewPagerHelper viewPagerHelper;
    private CreditLimitAmounts creditLimitAmounts;
    private int creditLimit = 1100;
    private CreditLimitFlags docsStatus;
    private int stageInProgress = -1;
    private int stageCompleted = -1;

    private String color_cyan_main = "#00aad4";
    private String color_cyan_light = "#00e5ff";
    private String color_blue_text = "#1E2F4D";
    private String color_gray_main = "#728290";
    private boolean ekycSchedStatus;
    private int onboardingFlow ;
    private String color_green_main = "#11B96C";

    public CreditLimitRecycleAdapter(Context context,  int onboardingFlow, List<String>
            moreCreditTitles, List<String> moreCreditStatus, List<Integer> moreCreditIconsIds,
             CreditLimitAmounts amounts, CreditLimitFlags docsStatus, Integer creditLimit,
                                     boolean isEkycScheduled) {
        this.context = context;
        this.moreCreditTitles = moreCreditTitles;
        this.moreCreditStatus = moreCreditStatus;
        this.moreCreditIconsIds = moreCreditIconsIds;
        this.onboardingFlow = onboardingFlow;
//        this.seq = seq;
        viewPagerHelper = new ViewPagerHelper();
        creditLimitAmounts = amounts;
        this.docsStatus = docsStatus;
        this.ekycSchedStatus = isEkycScheduled;
        //this.creditLimit = creditLimit;
    }


    @Override
    public CardLimitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_credit_limit_list, parent, false);
        CardLimitViewHolder holder = new CardLimitViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(CardLimitViewHolder holder, int position) {
        //todo interacions

        if (docsStatus.getIsPanNumberLinked() == viewPagerHelper.DOC_UNVERIFIED) {
            stageInProgress = 0;

        } else if (docsStatus.getIsCardLinked() == viewPagerHelper.DOC_UNVERIFIED) {
            stageInProgress = 1;

        } else if (docsStatus.getIsKycLinked() == viewPagerHelper.DOC_UNVERIFIED) {
            stageInProgress = 2;


        } else if (docsStatus.getHasSuccessfulPayments() == viewPagerHelper.DOC_UNVERIFIED) {
            stageInProgress = 3;

        } else if (docsStatus.getHasSuccessfulPayments() == viewPagerHelper.DOC_VERIFIED) {
            stageInProgress = 4;
        }
        //overiding position and stageInProgress for operational flow
        if (onboardingFlow == GlobalConstants.APP_FLOW_OPERATIONAL) {
            stageInProgress = 3;
            position += 3;
        }
        holder.creditTaskTitle.setText(moreCreditTitles.get(position));
        holder.creditTaskStatus.setText(moreCreditStatus.get(position));
        holder.creditTaskIcon.setImageDrawable(ContextCompat.getDrawable(context, moreCreditIconsIds.get(position)));

        switch (position) {
            case 0:
                String panBonus = "+ " + context.getString(R.string.rs_symbol) + TextFormatHelper.indianRupeesFormat(Double.parseDouble(creditLimitAmounts.getPanNumberLinked()));
                String panCredit = context.getString(R.string.rs_symbol) + TextFormatHelper.indianRupeesFormat(creditLimit
                        + Double.parseDouble(creditLimitAmounts.getPanNumberLinked()));

                holder.creditNewLimit.setText(panCredit);
                holder.creditBonusAmount.setText(panBonus);

                break;
            case 1:
                String bankBonus = "+ " + context.getString(R.string.rs_symbol) + TextFormatHelper.indianRupeesFormat(Double.parseDouble(creditLimitAmounts.getCardLinked()));
                String bankCredit = context.getString(R.string.rs_symbol) + TextFormatHelper.indianRupeesFormat(creditLimit
                        + Double.parseDouble(creditLimitAmounts.getCardLinked())
                        + Double.parseDouble(creditLimitAmounts.getPanNumberLinked()));

                holder.creditNewLimit.setText(bankCredit);
                holder.creditBonusAmount.setText(bankBonus);

                break;
            case 2:
                String kycBonus = "+ " + context.getString(R.string.rs_symbol) + TextFormatHelper.indianRupeesFormat(Double.valueOf(creditLimitAmounts.getKycLinked()));
                String kycCredit = context.getString(R.string.rs_symbol) + TextFormatHelper.indianRupeesFormat(creditLimit
                        + Double.parseDouble(creditLimitAmounts.getKycLinked())
                        + Double.parseDouble(creditLimitAmounts.getCardLinked())
                        + Double.parseDouble(creditLimitAmounts.getPanNumberLinked()));

                if (ekycSchedStatus) {
                    holder.creditTaskStatus.setText("See Appointment");
                }
                holder.creditNewLimit.setText(kycCredit);
                holder.creditBonusAmount.setText(kycBonus);

                break;
            case 3:
                String repayBonus = "+ " + context.getString(R.string.rs_symbol) + TextFormatHelper.indianRupeesFormat(Double.
                        valueOf(creditLimitAmounts.getSuccessfulPayments()) * 2);
                String repayCredit = context.getString(R.string.rs_symbol) + TextFormatHelper.indianRupeesFormat(creditLimit
                        + Double.parseDouble(creditLimitAmounts.getSuccessfulPayments())
                        + Double.parseDouble(creditLimitAmounts.getKycLinked())
                        + Double.parseDouble(creditLimitAmounts.getCardLinked())
                        + Double.parseDouble(creditLimitAmounts.getPanNumberLinked()));

                holder.creditNewLimit.setText(repayCredit);
                holder.creditBonusAmount.setText(repayBonus);

                break;
        }

        final int pos = position;
        if (position == stageInProgress && position == 2)
            holder.creditTaskStatus.setVisibility(View.GONE);

        if (stageInProgress == position) {
            holder.creditTaskIcon.setBackgroundResource(R.drawable.circular_shape_blue);
            holder.creditTaskStatus.setTextColor(Color.parseColor(color_cyan_main));
            holder.creditTaskIcon.setColorFilter(Color.parseColor(color_cyan_light));
            holder.creditNewLimit.setTextColor(Color.parseColor(color_blue_text));
            holder.creditTaskTitle.setTextColor(Color.parseColor(color_blue_text));
            holder.creditBonusAmount.setTextColor(Color.parseColor(color_gray_main));

            holder.layoutRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (pos) {
                        case 0:
                            OllyCreditApplication.getInstance().trackEvent(
                                    GlobalConstants.CATEGORY_INCREASE_LIMIT,
                                    GlobalConstants.ACTION_GOTO,
                                    GlobalConstants.LABLE_VERIFY_PAN
                            );
                            context.startActivity(new Intent(context, VerifyPanActivity.class));
                            break;

                        case 1:
                            OllyCreditApplication.getInstance().trackEvent(
                                    GlobalConstants.CATEGORY_INCREASE_LIMIT,
                                    GlobalConstants.ACTION_GOTO,
                                    GlobalConstants.LABLE_VERIFY_DEBIT
                            );
                            context.startActivity(new Intent(context, VerifyBankActivity.class));
                            break;

                        case 2:
                            OllyCreditApplication.getInstance().trackEvent(
                                    GlobalConstants.CATEGORY_INCREASE_LIMIT,
                                    GlobalConstants.ACTION_GOTO,
                                    GlobalConstants.LABLE_VERIFY_EKYC
                            );
//                            if (ekycSchedStatus) {
//                                context.startActivity(new Intent(context, AppointConfirmActivity.class));
//                            } else {
//                                context.startActivity(new Intent(context, AppointKycActivity.class));
//                            }
                            break;

                        case 3:
                            OllyCreditApplication.getInstance().trackEvent(
                                    GlobalConstants.CATEGORY_INCREASE_LIMIT,
                                    GlobalConstants.ACTION_GOTO,
                                    GlobalConstants.LABLE_VERIFY_REPAYMENT
                            );
                            context.startActivity(new Intent(context, CardHistoryActivity.class));
                            break;
                    }

                }
            });

        }

        if (onboardingFlow != GlobalConstants.APP_FLOW_OPERATIONAL)
            for (int i = stageInProgress - 1; i >= -1; i--) {
                if (i == position) {
                    holder.creditTaskIcon.setColorFilter(Color.parseColor("#ffffff"));
                    holder.creditTaskIcon.setBackgroundResource(R.drawable.circular_shape_green);
                    holder.creditTaskStatus.setTextColor(Color.parseColor(color_green_main));
                    holder.creditTaskStatus.setText("Completed");
                    holder.creditNewLimit.setTextColor(Color.parseColor(color_blue_text));
                    holder.creditTaskTitle.setTextColor(Color.parseColor(color_blue_text));
                    holder.creditBonusAmount.setTextColor(Color.parseColor(color_gray_main));
                }
            }


//        else if (docsStatus.getIsPanNumberLinked() == viewPagerHelper.DOC_VERIFIED) {
//
//            holder.creditTaskIcon.setBackgroundResource(R.drawable.circular_shape_green);
//            holder.creditTaskStatus.setText("COMPLETED");
//            holder.creditTaskStatus.setTextColor(Color.parseColor(color_green_main));
//            holder.creditNewLimit.setTextColor(Color.parseColor(color_green_main));
//
//            holder.creditTaskTitle.setTextColor(Color.parseColor(color_blue_text));
//            holder.creditBonusAmount.setTextColor(Color.parseColor(color_blue_text));
//
//        } else if (docsStatus.getIsPanNumberLinked() == viewPagerHelper.DOC_UNVERIFIED) {
//            //todo changes
//        }

    }

    private void enableInOperationFlow() {

    }

    @Override
    public int getItemCount() {
        return onboardingFlow == GlobalConstants.APP_FLOW_OPERATIONAL ? 1 : moreCreditTitles.size();
    }

    public class CardLimitViewHolder extends ViewHolder {

        private ImageView creditTaskIcon;
        private TextView creditTaskTitle;
        private TextView creditTaskStatus;
        private TextView creditNewLimit;
        private TextView creditBonusAmount;
        private LinearLayout layoutRow;
        private View line;

        @BindView(R.id.fl_root)
        FrameLayout flRoot;


        public CardLimitViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            initViews(itemView);
        }

        private void initViews(View itemView) {
            creditTaskTitle = (TextView) itemView.findViewById(R.id.tv_limit_rv_title);
            creditTaskStatus = (TextView) itemView.findViewById(R.id.tv_limit_rv_status);
            creditNewLimit = (TextView) itemView.findViewById(R.id.tv_limit_rv_available);
            creditBonusAmount = (TextView) itemView.findViewById(R.id.tv_limit_rv_bonus);
            creditTaskIcon = (ImageView) itemView.findViewById(image_limit_rv_icon);
            layoutRow = (LinearLayout) itemView.findViewById(R.id.ll_layout);
            line = (View) itemView.findViewById(R.id.line);
        }
    }
}


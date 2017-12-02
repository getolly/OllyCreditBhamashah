package com.ollycredit.ui.card.card_history.fragments.transaction;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.base.BaseFragment;
import com.ollycredit.utils.GlobalConstants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends BaseFragment implements TransactionView {


    @BindView(R.id.trans_balance)
    View transHistoryView;
    @BindView(R.id.rv_trans_history)
    RecyclerView repaymentHistoryRV;

    @BindView(R.id.trans_layout_rv_nocontent)
    FrameLayout noContentFL;

    @BindView(R.id.trans_layout_rv_content)
    LinearLayout contentLL;

    //last trans
    private TextView lastTransAvailBalanceTV;
    private TextView lastTransUsedBalanceTV;
    private TextView lastTransLasttransTV;
    private TextView lastTransLasttransCostTV;
    private Button lastTransAllActivityButton;
    private TextView lastTransLasttransDateTime;
    private LinearLayout lastTransLinearLayout;
    private LinearLayoutManager mLayoutManager;
    private TransactionRecycleAdapter mAdapter;

    @Inject
    TransactionPresenter transactionPresenter;

    @Inject
    PreferencesHelper preferencesHelper;


    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_transaction, container, false);
        ButterKnife.bind(this, itemView);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);

        transactionPresenter.attachView(this);


        setUp();

        return itemView;
    }


    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        OllyCreditApplication.getInstance().trackScreenView(this.getClass().getSimpleName());
        showLoadingView();
        transactionPresenter.getTransactions(preferencesHelper.getToken());
        transactionPresenter.getrepayment(preferencesHelper.getToken());

    }

    @Override
    protected void setUp() {
        //last trans
        lastTransAvailBalanceTV = (TextView) transHistoryView.findViewById(R.id.tv_lastTrans_balance);
        lastTransUsedBalanceTV = (TextView) transHistoryView.findViewById(R.id.tv_lastTrans_used);
        lastTransLasttransTV = (TextView) transHistoryView.findViewById(R.id.tv_lastTrans_lastTranslable);
        lastTransLasttransCostTV = (TextView) transHistoryView.findViewById(R.id.tv_lastTrans_lastTranscost);
        lastTransLasttransDateTime = (TextView) transHistoryView.findViewById(R.id.tv_lastTrans_lastTransDatetime);
        lastTransAllActivityButton = (Button) transHistoryView.findViewById(R.id.btn_lastTrans_allactivity);
        lastTransLinearLayout = (LinearLayout) transHistoryView.findViewById(R.id.linear_latest_trans);

        lastTransAvailBalanceTV.setText(preferencesHelper.getString(GlobalConstants.PREF_BAL, "0").
                substring(0, preferencesHelper.getString(GlobalConstants.PREF_BAL, "0").indexOf(".")));
        lastTransUsedBalanceTV.setText(preferencesHelper.getString(GlobalConstants.PREF_USED, "0").
                substring(0, preferencesHelper.getString(GlobalConstants.PREF_USED, "0").indexOf(".")));


        lastTransLinearLayout.setVisibility(View.GONE);
        lastTransAllActivityButton.setVisibility(View.GONE);
        noContentFL.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void getTranSuccess(ResponseModel responseModel) {


        preferencesHelper.saveToken(responseModel.getToken());

        if (responseModel.getTransactionList().size() > 0) {
            noContentFL.setVisibility(View.GONE);
            contentLL.setVisibility(View.VISIBLE);
            repaymentHistoryRV.setVisibility(View.VISIBLE);
            mLayoutManager = new LinearLayoutManager(getActivity());
            repaymentHistoryRV.setLayoutManager(mLayoutManager);
            //todo content goes here
            mAdapter = new TransactionRecycleAdapter(getActivity(), responseModel.getTransactionList());
            repaymentHistoryRV.setAdapter(mAdapter);
        } else {
            noContentFL.setVisibility(View.VISIBLE);
            repaymentHistoryRV.setVisibility(View.GONE);
        }

    }

    @Override
    public void getReqFailed(int failedCode, String message) {

        hideLoadingView();
        showDialogBox(failedCode + " " + message);
    }

    @Override
    public void getRepaymentSuccess(ResponseModel responseModel) {
        hideLoadingView();
        lastTransUsedBalanceTV.setText(String.valueOf(Double.valueOf(responseModel.
                getAmountDue())+ Double.valueOf(responseModel.getAmountUsed())));
        preferencesHelper.saveToken(responseModel.getToken());
    }
}
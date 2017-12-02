package com.ollycredit.ui.repayments.store_cards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ollycredit.BuildConfig;
import com.ollycredit.R;
import com.ollycredit.api.model.PayUModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.base.BaseFragment;
import com.ollycredit.utils.PayUHashGenerator.PayuHashGeneratorPresenter;
import com.ollycredit.utils.PayUHashGenerator.PayuHashGeneratorView;
import com.payu.india.Interfaces.PaymentRelatedDetailsListener;
import com.payu.india.Interfaces.ValueAddedServiceApiListener;
import com.payu.india.Model.MerchantWebService;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuResponse;
import com.payu.india.Model.PostData;
import com.payu.india.Model.StoredCard;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.india.PostParams.MerchantWebServicePostParams;
import com.payu.india.Tasks.GetPaymentRelatedDetailsTask;
import com.payu.india.Tasks.ValueAddedServiceTask;
import com.payu.payuui.Adapter.SavedCardItemFragmentAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreCardsFragment extends BaseFragment
        implements PaymentRelatedDetailsListener,
        PayuHashGeneratorView,
        ValueAddedServiceApiListener
//        DeleteCardApiListener
{

    private boolean IS_CARD_STORED = false;

    @BindView(R.id.list_stored_card)
    ListView listStoredCard;

    @BindView(R.id.text_view_issuing_bank_down_error)
    TextView titleText;

    @Inject
    PayuHashGeneratorPresenter presenter;

    private StoredCardStatus storedCardStatus;
    private PayUModel payUModel;
    private MerchantWebService merchantWebService;
    private String mobileSdkHash, vasForMobileSdkHash, deleteCardHash;
    private ValueAddedServiceTask valueAddedServiceTask;
    private PayuResponse payuResponse, valueAddedPayuResponse;
    private ArrayList<StoredCard> storedCards;
    private SavedCardItemFragmentAdapter mAdapter;
    private View itemView;

    public static StoreCardsFragment newInstance(PayUModel payUModel) {
        StoreCardsFragment fragment = new StoreCardsFragment();
        Bundle args = new Bundle();
        args.putParcelable("payumodel", payUModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            payUModel = getArguments().getParcelable("payumodel");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        itemView = inflater.inflate(R.layout.fragment_store_cards, container, false);
        ButterKnife.bind(this, itemView);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
        setUp();
        return itemView;
    }

    @Override
    protected void setUp() {

        storedCardStatus = (StoredCardStatus) getContext();
        merchantWebService = new MerchantWebService();
        merchantWebService.setKey(payUModel.getKey());
        merchantWebService.setCommand(PayuConstants.PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK);
        merchantWebService.setVar1(payUModel.getUserCredentials());
        presenter.getHash(payUModel);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    public void setStoredCardStatus(StoredCardStatus storedCardStatus) {
        this.storedCardStatus = storedCardStatus;
    }

    @Override
    public void getHashSuccess(ResponseModel responseModel) {
        mobileSdkHash = responseModel.getPaymentRelatedDetailsForMobileSdkHash();
        vasForMobileSdkHash = responseModel.getVasForMobileSdkHash();
        deleteCardHash = responseModel.getDeleteUserCardHash();

        merchantWebService = new MerchantWebService();
        merchantWebService.setKey(payUModel.getKey());
        merchantWebService.setCommand(PayuConstants.PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK);
        merchantWebService.setVar1(payUModel.getUserCredentials());
        merchantWebService.setHash(responseModel.getPaymentRelatedDetailsForMobileSdkHash());

        PostData postData = new MerchantWebServicePostParams(merchantWebService).
                getMerchantWebServicePostParams();
        if (postData.getCode() == PayuErrors.NO_ERROR) {
            PayuConfig payuConfig = new PayuConfig();
            payuConfig.setData(postData.getResult());
            if (BuildConfig.IS_PRODUCTION)
                payuConfig.setEnvironment(PayuConstants.PRODUCTION_ENV);
            else
                payuConfig.setEnvironment(PayuConstants.STAGING_ENV);

            GetPaymentRelatedDetailsTask paymentRelatedDetailsForMobileSdkTask = new
                    GetPaymentRelatedDetailsTask(this);

            paymentRelatedDetailsForMobileSdkTask.execute(payuConfig);

        } else {
            Toast.makeText(getActivity(), postData.getResult(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onPaymentRelatedDetailsResponse(PayuResponse payuResponse) {
        this.payuResponse = payuResponse;

        storedCards = payuResponse.getStoredCards();

        if (storedCards != null) {
            IS_CARD_STORED = true;
            for (StoredCard s : storedCards) {
                Log.d("mytag", s.getCardName());
            }
        }
        if (storedCards == null || storedCards.size() == 0) {
            IS_CARD_STORED = false;
        }

        MerchantWebService valueAddedWebService = new MerchantWebService();
        valueAddedWebService.setKey(payUModel.getKey());
        valueAddedWebService.setCommand(PayuConstants.VAS_FOR_MOBILE_SDK);
        valueAddedWebService.setHash(vasForMobileSdkHash);
        valueAddedWebService.setVar1(PayuConstants.DEFAULT);
        valueAddedWebService.setVar2(PayuConstants.DEFAULT);
        valueAddedWebService.setVar3(PayuConstants.DEFAULT);
        PostData postData;

        if ((postData = new MerchantWebServicePostParams(valueAddedWebService).
                getMerchantWebServicePostParams()) != null && postData.getCode() == PayuErrors.NO_ERROR) {
            PayuConfig payuConfig = new PayuConfig();
            payuConfig.setData(postData.getResult());
            if (BuildConfig.IS_PRODUCTION)
                payuConfig.setEnvironment(PayuConstants.PRODUCTION_ENV);
            else
                payuConfig.setEnvironment(PayuConstants.STAGING_ENV);
            valueAddedServiceTask = new ValueAddedServiceTask(this);
            valueAddedServiceTask.execute(payuConfig);
        } else {
            if (postData != null)
                Toast.makeText(getActivity(), postData.getResult(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onValueAddedServiceApiResponse(PayuResponse payuResponse) {
        valueAddedPayuResponse = payuResponse;
        setupUI();
    }

    private void setupUI() {


        if (!IS_CARD_STORED) {
            titleText.setText("You have no Stored Cards");
        } else {
            StoreCardListAdapter storeCardListAdapter = new StoreCardListAdapter(
                    getContext(),
                    storedCards,
                    valueAddedPayuResponse.getIssuingBankStatus(),
                    new HashMap<String, String>()
            );
            listStoredCard.setAdapter(storeCardListAdapter);
        }
        storedCardStatus.getCardStored();

    }

    @Override
    public void getReqFailed(int errorCode, String msg) {

    }

    public interface StoredCardStatus {
        void getCardStored();
    }


}

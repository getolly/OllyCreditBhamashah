package com.ollycredit.ui.card.card_home;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;
import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.BaseActivity;
import com.ollycredit.ui.card.card_history.CardHistoryActivity;
import com.ollycredit.ui.card.card_history.repay_credit.amount.RepayAmountActivity;
import com.ollycredit.ui.card.credit_limit.CreditLimitActivity;
import com.ollycredit.ui.card.verify.pan.VerifyPanActivity;
import com.ollycredit.ui.navigation.NavDrawerFragment;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.custom_views.FlipCardView;
import com.ollycredit.utils.helpers.HelperClass;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ollycredit.R.id.btn_lastTrans_allactivity;
import static com.ollycredit.R.id.tv_avil_heading;
import static com.ollycredit.R.id.tv_used_heading;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_CLASSIC;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_OPERATIONAL;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_PREPAID;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_PRE_USER;
import static com.ollycredit.utils.GlobalConstants.APP_FLOW_SELECT;
import static com.ollycredit.utils.GlobalConstants.PREF_EMAIL_VERIFY;

public class CardHomeActivity extends BaseActivity implements OnClickListener, CardHomeView {


    @BindView(R.id.card_toolbar)
    Toolbar navToolbar;

    @BindView(R.id.cv_card_payback_layout)
    CardView paybackCardView;

    @BindView(R.id.payback_notify_color_cardhome)
    LinearLayout llPaybackNotifiyColor;

    @BindView(R.id.cv_card_pan_layout)
    CardView sharePanCardView;

    @BindView(R.id.cv_card_last_transaction_layout)
    CardView lastTransCardView;
    @BindView(R.id.cv_card_remain_credit_layout)
    CardView remCreditCardView;

    @BindView(R.id.pin_entry_view)
    PinEntryEditText pinEntryEditText;

    @BindView(R.id.view_dashboard_ollycard_back)
    LinearLayout ollyCardBackView;

//    @BindView(R.id.view_dashboard_payback)
//    View paybackView;

    @BindView(R.id.tv_payback_days)
    TextView paybackDaysTextView;

    @BindView(R.id.tv_payback_credit)
    TextView paybackCreditTextView;

    @BindView(R.id.tv_payback_min_credit)
    TextView paybackCreditMinimumTextView;

    @BindView(R.id.btn_payback)
    Button paybackButton;

    @BindView(R.id.ib_payback_expand)
    ImageButton paybackExpandImageButton;

    @BindView(R.id.view_dashboard_sharepin)
    View sharePanView;

    @BindView(R.id.flip_view)
    FlipCardView flipCardView;

    @BindView(R.id.view_dashboard_ollycard)
    View ollyCardFrontView;

    @BindView(R.id.view_dashboard_lasttrans)
    View lastTransView;
    @BindView(R.id.view_dashboard_remcredit)
    View remCreditView;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.card_home_scroll_view)
    NestedScrollView svCardHome;

    @BindString(R.string.rs_symbol)
    String rs_symbol;

    //payback

    @BindView(R.id.fl_email_layout)
    FrameLayout fl_email_layout;

    @BindView(R.id.ll_repay_card_min_due)
    LinearLayout ll_repay_card_min_due;

    //sharepin
    private LinearLayout sharePinlayout;

    //ollymain card
    private TextView ollyCardNumberTextView;
    private TextView ollyCardExpireTextView;
    private TextView ollyCardCVVTextView;
    private TextView ollyCardUserNameTextView;
    private TextView ollyCardExpireTextViewHeading;
    private TextView ollyCardUserNameTextViewHeading;
    private TextView ollyCardCVVTextViewHeading;
    //last trans
    private TextView lastTransAvailBalanceTV;
    private TextView lastTransUsedBalanceTV;
    private TextView lastTransLasttransTV;
    private TextView lastTransLasttransAmountTV;
    private TextView lastTransAvailHead;
    private TextView lastTransLastUsedHead;


    private Button lastTransAllActivityButton;
    private TextView lastTransLasttransDateTime;
    private TextView remCreditUsedCreditTV;
    private TextView remCreditPercTV;
    private DonutProgress remCreditProg;

    @BindView(R.id.btn_remcredit_morecredit)
    Button remCreditMoreButton;

    @BindView(R.id.iv_card_copy)
    ImageView ollyCardCopyImageButton;

    private LinearLayout lastTransLayout;

    private String amountDue, amountUsed;
    private boolean isdefualted = false;
    private int daysleft;

    @Inject
    CardHomePresenter cardHomePresenter;
    @Inject
    PreferencesHelper preferencesHelper;

    Typeface typeface_semi, typeface_reg, typeface_bold;
    private BroadcastReceiver networkReceiver;
    private String cardNumber = "";
    private String minAmountDue;
    private int emailStatus;
    private boolean isEmailMsgPrompted;
    private int appFlow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_home);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        setUp();

    }

    @Override
    protected void onPause() {
        super.onPause();
        hideErrorConnection();
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        getCard();

        OllyCreditApplication.getInstance().trackScreenView(this.getClass().getSimpleName());

        if (preferencesHelper.getInt(GlobalConstants.PREF_PAN_VERIFIED, 0) == 1)
            sharePanCardView.setVisibility(View.GONE);

        // paybackCardView.setVisibility(View.VISIBLE);


    }

    @Override
    protected void setUp() {

        setSupportActionBar(navToolbar);
        cardHomePresenter.attachView(this);

        initViews();
        isEmailMsgPrompted = false;
        appFlow = preferencesHelper.getInt(GlobalConstants.USER_APP_FLOW, 0);

        setUpNavDrawer();

        //showProgressDialog();
        showLoadingView();

//        paybackCardView.setVisibility(View.GONE);
        sharePanCardView.setVisibility(View.GONE);
//        ollyMainCardView.setVisibility(View.VISIBLE);
//        lastTransCardView.setVisibility(View.GONE);
//        remCreditCardView.setVisibility(View.VISIBLE);

        ollyCardCopyImageButton.setOnClickListener(this);
        lastTransAllActivityButton.setOnClickListener(this);
        remCreditMoreButton.setOnClickListener(this);
        sharePinlayout.setOnClickListener(this);

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isOnline = isOnline(CardHomeActivity.this);
                if (isOnline) {
                    hideErrorConnection();
                } else {
                    showErrorConnection();
                }
            }
        };


        paybackExpandImageButton.setVisibility(View.GONE);
        paybackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_CARD_HOME,
                                GlobalConstants.ACTION_GOTO,
                                GlobalConstants.LABLE_REPAYMENT);

                if (isdefualted) {
                    Toast.makeText(CardHomeActivity.this, "You are a defaulter , please repay before your cards get blocked", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(CardHomeActivity.this, RepayAmountActivity.class);
                intent.putExtra("amountdue", amountDue);
                intent.putExtra("amountused", amountUsed);
                intent.putExtra("amountMin", minAmountDue);
                intent.putExtra("isdefualted", isdefualted);
                intent.putExtra("dayleft", daysleft);
                startActivity(intent);


            }
        });


        switch (appFlow) {
            case APP_FLOW_PRE_USER:
            case APP_FLOW_CLASSIC:
            case APP_FLOW_SELECT:
            case APP_FLOW_PREPAID:
                remCreditMoreButton.setVisibility(View.VISIBLE);
                emailStatus = preferencesHelper.getInt(PREF_EMAIL_VERIFY, 0);

                if (emailStatus == 0) {
                    fl_email_layout.setVisibility(View.VISIBLE);
                    fl_email_layout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            OllyCreditApplication
                                    .getInstance()
                                    .trackEvent(GlobalConstants.CATEGORY_CARD_HOME,
                                            GlobalConstants.ACTION_VERIFY,
                                            GlobalConstants.LABLE_VERIFY_EMAIL);

                            cardHomePresenter.getUserInfo(preferencesHelper.getToken());
                            showProgressDialog();
                        }
                    });
                } else {
                    fl_email_layout.setVisibility(View.GONE);
                }
                break;
            case APP_FLOW_OPERATIONAL:
                fl_email_layout.setVisibility(View.GONE);
                break;
            default:

        }

        cardHomePresenter.getCard(preferencesHelper.getToken());
        cardHomePresenter.getrepayment(preferencesHelper.getToken());

        swipeRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_CARD_HOME,
                                GlobalConstants.ACTION_SWIPE,
                                GlobalConstants.LABLE_RE_OLLY_CARD);
                isEmailMsgPrompted = false;
                getCard();
            }
        });
    }


    private void setUpNavDrawer() {

        NavDrawerFragment drawerFragment = (NavDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.frag_nav_drawer);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_view);
        drawerFragment.setUpDrawer(R.id.frag_nav_drawer, drawerLayout, navToolbar);
    }


    private void initViews() {

        //prevent keyboard popping
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ollyCardBackView.setLayoutParams(new FrameLayout.LayoutParams(ollyCardFrontView.getWidth(),
                ollyCardFrontView.getHeight()));

        typeface_reg = Typeface.createFromAsset(getAssets(), "fonts/Muli-Regular.ttf");
        typeface_semi = Typeface.createFromAsset(getAssets(), "fonts/muli-semibold.ttf");
        typeface_bold = Typeface.createFromAsset(getAssets(), "fonts/muli-bold.ttf");

        //verify email
//        fl_email_layout = (FrameLayout) findViewById(R.id.fl_email_layout);

        //sharepin
        sharePinlayout = (LinearLayout) sharePanView.findViewById(R.id.layout_sharepin_pan);

        //main olly card

        ollyCardNumberTextView = (TextView) ollyCardFrontView.findViewById(R.id.tv_card_cardNumber);
        ollyCardExpireTextView = (TextView) ollyCardFrontView.findViewById(R.id.tv_card_cardExpire);
        ollyCardCVVTextView = (TextView) ollyCardFrontView.findViewById(R.id.tv_card_cvv);
        ollyCardUserNameTextView = (TextView) ollyCardFrontView.findViewById(R.id.tv_card_username);
        ollyCardCopyImageButton = (ImageView) ollyCardFrontView.findViewById(R.id.iv_card_copy);
        ollyCardUserNameTextViewHeading = (TextView) ollyCardFrontView.findViewById(R.id.tv_card_cardNumber_heading);
        ollyCardExpireTextViewHeading = (TextView) ollyCardFrontView.findViewById(R.id.tv_card_cardExpire_heading);
        ollyCardCVVTextViewHeading = (TextView) ollyCardFrontView.findViewById(R.id.tv_card_cvv_heading);

        ollyCardNumberTextView.setTypeface(typeface_semi);
        ollyCardUserNameTextViewHeading.setTypeface(typeface_semi);
        ollyCardExpireTextViewHeading.setTypeface(typeface_semi);
        ollyCardCVVTextView.setTypeface(typeface_semi);
        ollyCardUserNameTextView.setTypeface(typeface_semi);


        //last trans
        lastTransAvailBalanceTV = (TextView) lastTransView.findViewById(R.id.tv_lastTrans_balance);
        lastTransLayout = (LinearLayout) lastTransView.findViewById(R.id.linear_latest_trans);
        lastTransUsedBalanceTV = (TextView) lastTransView.findViewById(R.id.tv_lastTrans_used);
        lastTransLasttransTV = (TextView) lastTransView.findViewById(R.id.tv_lastTrans_lastTranslable);
        lastTransLasttransAmountTV = (TextView) lastTransView.findViewById(R.id.tv_lastTrans_lastTranscost);
        lastTransLasttransDateTime = (TextView) lastTransView.findViewById(R.id.tv_lastTrans_lastTransDatetime);
        lastTransAllActivityButton = (Button) lastTransView.findViewById(btn_lastTrans_allactivity);
        lastTransAvailHead = (TextView) lastTransView.findViewById(tv_avil_heading);
        lastTransLastUsedHead = (TextView) lastTransView.findViewById(tv_used_heading);


        lastTransAvailBalanceTV.setTypeface(typeface_reg);
        lastTransUsedBalanceTV.setTypeface(typeface_semi);
        lastTransAvailHead.setTypeface(typeface_semi);
        lastTransLastUsedHead.setTypeface(typeface_semi);
        lastTransAllActivityButton.setTypeface(typeface_bold);
        lastTransLasttransDateTime.setTypeface(typeface_semi);
        lastTransLasttransTV.setTypeface(typeface_semi);

        //remCredit
        remCreditUsedCreditTV = (TextView) remCreditView.findViewById(R.id.tv_remcredit_usedcredit);
        remCreditPercTV = (TextView) remCreditView.findViewById(R.id.tv_remcredit_perc);
        remCreditProg = (DonutProgress) remCreditView.findViewById(R.id.pb_remcredit_creditprogress);
//        remCreditMoreButton = (Button) remCreditView.findViewById(R.id.btn_remcredit_morecredit);
        remCreditProg.setShowText(false);
        remCreditProg.setFinishedStrokeColor(getResources().getColor(R.color.cyan_main));

        remCreditUsedCreditTV.setTypeface(typeface_semi);
        remCreditMoreButton.setTypeface(typeface_bold);

        pinEntryEditText.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                flipCardView.flipTheView();
                //TODO: logic for checking
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < cardNumber.length(); i++) {
                    builder.append(cardNumber.charAt(i));

                    if ((i + 1) % 4 == 0)
                        builder.append("  ");
                }
                ollyCardNumberTextView.setText(builder.toString());
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_lastTrans_allactivity:
                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_CARD_HOME,
                                GlobalConstants.ACTION_GOTO,
                                GlobalConstants.LABLE_TRANSACTION);
                startActivity(new Intent(CardHomeActivity.this, CardHistoryActivity.class));
                break;
            case R.id.btn_remcredit_morecredit:
                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_CARD_HOME,
                                GlobalConstants.ACTION_GOTO,
                                GlobalConstants.LABLE_REPAYMENT);
                startActivity(new Intent(CardHomeActivity.this, CreditLimitActivity.class));
                break;
            case R.id.layout_sharepin_pan:
                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_CARD_HOME,
                                GlobalConstants.ACTION_GOTO,
                                GlobalConstants.LABLE_VERIFY_PAN);
                startActivity(new Intent(CardHomeActivity.this, VerifyPanActivity.class));
                break;
            case R.id.iv_card_copy:
                OllyCreditApplication
                        .getInstance()
                        .trackEvent(GlobalConstants.CATEGORY_CARD_HOME,
                                GlobalConstants.ACTION_COPY_OLLY_CARD,
                                GlobalConstants.LABLE_OLLY_CARD);

                switch (appFlow) {
                    case APP_FLOW_PRE_USER:
                    case APP_FLOW_CLASSIC:
                    case APP_FLOW_SELECT:
                    case APP_FLOW_PREPAID:
                        if (!isdefualted) {
                            if (emailStatus == 1) {
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("olly card", cardNumber);
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(this, "Card number copied", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(this)
                                        .setCancelable(false)
                                        .setMessage("You have to verify your email to activate olly card")
                                        .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                OllyCreditApplication
                                                        .getInstance()
                                                        .trackEvent(GlobalConstants.CATEGORY_CARD_HOME,
                                                                GlobalConstants.ACTION_DIALOG_OK,
                                                                GlobalConstants.LABLE_VERIFY_EMAIL);
                                                dialogInterface.dismiss();
                                            }
                                        }).create();

                                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                alertDialog.show();
                            }

                        } else {
                            Toast.makeText(this, "Please repay your dues if you want to use our card!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case APP_FLOW_OPERATIONAL:
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("olly card", cardNumber);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(this, "Card number copied", Toast.LENGTH_SHORT).show();

                        break;
                    default:

                }

        }
    }


    private void getCard() {

        cardHomePresenter.getCard(preferencesHelper.getToken());
        cardHomePresenter.getrepayment(preferencesHelper.getToken());
        if (!isEmailMsgPrompted) {
            cardHomePresenter.getUserInfo(preferencesHelper.getToken());
        }

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @OnClick(R.id.tv_view_details)
    public void tvViewCardDetails() {
        flipCardView.flipTheView();
    }

    @Override
    public void getCardSuccess(ResponseModel cardResponseModel) {

//        hideProgressDialog();
        hideLoadingView();
        lastTransCardView.setVisibility(View.VISIBLE);
        // tutor();
        //card details
        cardNumber = cardResponseModel.getCard().getNumber();
        StringBuilder builder = new StringBuilder();
        switch (appFlow) {
            case APP_FLOW_CLASSIC:
            case APP_FLOW_SELECT:
                flipCardView.setFlipOnTouch(true);
                break;
            default:
        }

        for (int i = 0; i < cardNumber.length(); i++) {

//            if (i >= 6 && i <= 11) {
//                builder.append('x');
//            } else {
//                builder.append(cardNumber.charAt(i));
//            }
            builder.append(cardNumber.charAt(i));
            if ((i + 1) % 4 == 0)
                builder.append(" ");
        }

        // ollyCardNumberTextView.setText(HelperClass.creditNumberFormat(cardnumber.toString().trim()));

        ollyCardNumberTextView.setText(builder.toString());
        if (cardResponseModel.getCard().getCvv() == null) {
            ollyCardCVVTextViewHeading.setVisibility(View.GONE);
        } else {
            ollyCardCVVTextView.setText(cardResponseModel.getCard().getCvv());
        }
        ollyCardExpireTextView.setText(cardResponseModel.getCard().getValid());

        //last transcation

        if (cardResponseModel.getLastTransaction().getCardId() == null) {
            lastTransLayout.setVisibility(View.GONE);
        } else {

            lastTransLayout.setVisibility(View.VISIBLE);
            String lastTransString = "Last transaction: " + cardResponseModel.getLastTransaction().getDate();
            String lastTransTitleString = cardResponseModel.getLastTransaction().getMerchant() + ": " + cardResponseModel.getLastTransaction().getDescription();
            String lastTransAmountString = String.valueOf(cardResponseModel.getLastTransaction().getAmount());
            lastTransLasttransDateTime.setText(lastTransString);
            lastTransLasttransTV.setText(lastTransTitleString);
            lastTransLasttransAmountTV.setText(lastTransAmountString);

        }


        String transAmountString = cardResponseModel.getCard().getBalance();

        lastTransAvailBalanceTV.setText(transAmountString.substring(0, transAmountString.indexOf(".")));

        //double balance = cardResponseModel.getCard().getCreditLimit() - Double.parseDouble(cardResponseModel.getCard().getBalance());
//        lastTransUsedBalanceTV.setText(String.valueOf(Float.parseFloat(amountUsed)));

        preferencesHelper.saveToken(cardResponseModel.getToken());
        preferencesHelper.putString(GlobalConstants.PREF_BAL, cardResponseModel.getCard().getBalance());
        preferencesHelper.putString(GlobalConstants.PREF_USED, String.valueOf(cardResponseModel.getCard().getCreditLimit() - Double.parseDouble(cardResponseModel.getCard().getBalance())));
        preferencesHelper.putLong(GlobalConstants.PREF_MAX, cardResponseModel.getCard().getMaxCreditLimit());
        preferencesHelper.putLong(GlobalConstants.PREF_C_LIMIT, cardResponseModel.getCard().getCreditLimit());
        //name
        ollyCardUserNameTextView.setText(preferencesHelper.getString(GlobalConstants.PREF_USER_NAME_KEY, ""));

        double progress = (cardResponseModel.getCard().getCreditLimit() * 100) / cardResponseModel.getCard().getMaxCreditLimit();
        remCreditProg.setProgress((int) progress);
        remCreditPercTV.setText((int) Math.floor(progress) + "%");
        remCreditUsedCreditTV.setText("You are eligible to " + (int) Math.floor(progress) + "% of available Olly Credit");
    }

    @Override
    public void emailVerifyReq(ResponseModel responseModel) {
        swipeRefresh.setRefreshing(false);
        hideProgressDialog();
        emailStatus = responseModel.getUser().getEmailValidated();
        preferencesHelper.putInt(PREF_EMAIL_VERIFY, responseModel.getUser().getEmailValidated());
        preferencesHelper.saveToken(responseModel.getToken());
        preferencesHelper.saveBillCounter(responseModel.getBillCounter());


        Log.e("saveBill", String.valueOf(responseModel.getBillCounter()));
        if (responseModel.getUser().getEmailValidated() == 0) {
            hideLoadingView();
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("You have to verify your email to activate olly card")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            OllyCreditApplication
                                    .getInstance()
                                    .trackEvent(GlobalConstants.CATEGORY_CARD_HOME,
                                            GlobalConstants.ACTION_DIALOG_OK,
                                            GlobalConstants.LABLE_VERIFY_EMAIL);
                            dialogInterface.dismiss();
                        }
                    }).create();

            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.show();
            isEmailMsgPrompted = true;
        } else {
            fl_email_layout.setVisibility(View.GONE);
        }

    }

    @Override
    public void getCardFailed(int onCode, String message) {
        hideLoadingView();
        swipeRefresh.setRefreshing(false);
        hideProgressDialog();
    }


    @Override
    public void getRepaymentSuccess(ResponseModel responseModel) {

        hideLoadingView();
        lastTransUsedBalanceTV.setText(String.valueOf(Double.valueOf(responseModel.
                getAmountDue())+ Double.valueOf(responseModel.getAmountUsed())));

        amountUsed = responseModel.getAmountUsed();
        amountDue = responseModel.getAmountDue();
        minAmountDue = responseModel.getMinAmountDue().toString();
        //String.valueOf((Double.parseDouble(amountDue) * 40) / 100);
        isdefualted = responseModel.getIsDefaulted();

        if (amountDue.equals("0")) {

            paybackButton.setAlpha(0.5f);
            paybackButton.setEnabled(false);

        } else {
            paybackButton.setAlpha(1f);
            paybackButton.setEnabled(true);
        }

        if (preferencesHelper.getBillCounter() < 0) {
            paybackCardView.setVisibility(View.GONE);
            paybackButton.setAlpha(0.5f);
            paybackButton.setEnabled(false);

            ll_repay_card_min_due.setVisibility(View.GONE);
            paybackCreditTextView.setText(amountUsed);


            return;
        } else {

            paybackCardView.setVisibility(View.VISIBLE);
            paybackButton.setAlpha(1f);
            paybackButton.setEnabled(true);
            ll_repay_card_min_due.setVisibility(View.VISIBLE);

            if (amountDue.equals("0"))
                paybackCreditTextView.setText(amountUsed);
            else
                paybackCreditTextView.setText(amountDue);


        }
        paybackCreditMinimumTextView.setText(minAmountDue);

        getRepaymenttime();


    }

    private void getRepaymenttime() {

        daysleft = preferencesHelper.getBillCounter();


        if (daysleft < 10) {

            // green how many days left
            SpannableString spannableString = new SpannableString(9 - daysleft + " day[s] left to repay credit");
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 6, 0);
            paybackDaysTextView.setText(spannableString);
            llPaybackNotifiyColor.setBackgroundColor(ContextCompat.getColor(this, R.color.green_main));

        } else if (daysleft == 9) {

            // green zero days left
            daysleft = 0;
            SpannableString spannableString = new SpannableString(daysleft + " day[s] left to repay credit");
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 6, 0);
            paybackDaysTextView.setText(spannableString);
            llPaybackNotifiyColor.setBackgroundColor(ContextCompat.getColor(this, R.color.green_main));

        } else {
            // red
            SpannableString spannableString = new SpannableString("Pay immediately");
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, spannableString.length(), 0);
            paybackDaysTextView.setText(spannableString);
            llPaybackNotifiyColor.setBackgroundColor(ContextCompat.getColor(this, R.color.red_error));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.feedback_icon);

        if (menuItem != null) {
            HelperClass.tintMenuIcon(this, menuItem, R.color.white);
        }

        return true;
    }

}

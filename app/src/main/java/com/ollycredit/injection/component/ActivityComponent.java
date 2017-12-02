package com.ollycredit.injection.component;

import com.ollycredit.injection.module.ActivityModule;
import com.ollycredit.injection.scope.PerActivity;
import com.ollycredit.ui.card.card_history.CardHistoryActivity;
import com.ollycredit.ui.card.card_history.fragments.repayments.RepaymentFragment;
import com.ollycredit.ui.card.card_history.fragments.transaction.TransactionFragment;
import com.ollycredit.ui.card.card_history.repay_credit.amount.RepayAmountActivity;
import com.ollycredit.ui.card.card_home.CardHomeActivity;
import com.ollycredit.ui.card.credit_limit.CreditLimitActivity;
import com.ollycredit.ui.card.verify.debit.debit_register.VerifyBankActivity;
import com.ollycredit.ui.card.verify.kyc.appoint_address.AppointAddressActivity;
import com.ollycredit.ui.card.verify.kyc.appoint_confirm.AppointConfirmActivity;
import com.ollycredit.ui.card.verify.kyc.appoint_time.AppointTimeActivity;
import com.ollycredit.ui.card.verify.kyc.kyc_register.AppointKycActivity;
import com.ollycredit.ui.card.verify.pan.VerifyPanActivity;
import com.ollycredit.ui.help.HelpActivity;
import com.ollycredit.ui.navigation.NavDrawerFragment;
import com.ollycredit.ui.onboarding.landing_page.LandingPage;
import com.ollycredit.ui.onboarding.login.password.LoginPasswordActivity;
import com.ollycredit.ui.onboarding.login.pin.LoginPinActivity;
import com.ollycredit.ui.onboarding.phone.AccountNumberActivity;
import com.ollycredit.ui.onboarding.phone.PhoneNumberActivity;
import com.ollycredit.ui.onboarding.signup.ReadTermsConditionActivity;
import com.ollycredit.ui.onboarding.signup.create_pin.CreatePinActivity;
import com.ollycredit.ui.onboarding.signup.user_details.step_one.UserRegistrationActivity;
import com.ollycredit.ui.onboarding.signup.user_details.step_two.UserDetailsTwoActivity;
import com.ollycredit.ui.onboarding.signup.verify_email.VerifyEmailActivity;
import com.ollycredit.ui.recovery.customer_care.RecoverByPhoneCallMainActivity;
import com.ollycredit.ui.recovery.password.send_recover_mail.RecoverPasswordActivity;
import com.ollycredit.ui.recovery.password.RecoverPasswordCompleteActivity;
import com.ollycredit.ui.recovery.pin.RecoverPinCompleteActivity;
import com.ollycredit.ui.recovery.pin.confirm.PinResetConfirmActivity;
import com.ollycredit.ui.recovery.pin.set_pin.ResetPinActivity;
import com.ollycredit.ui.recovery.pin.validate_password.RecoverPasswordDetailsActivity;
import com.ollycredit.ui.recovery.pin.validate_phone.RecoverPinActivity;
import com.ollycredit.ui.recovery.pin.validate_user.ValidateUserActivity;
import com.ollycredit.ui.repayments.RepaymentActivity;
import com.ollycredit.ui.repayments.debit_card.DebitCardFragment;
import com.ollycredit.ui.repayments.net_banking.NetBankingFragment;
import com.ollycredit.ui.repayments.store_cards.StoreCardsFragment;
import com.ollycredit.ui.repayments.upi.UPIFragment;
import com.ollycredit.ui.splash.SplashActivity;
import com.ollycredit.ui.terms_conditions.LegalTermsConditionActivity;
import com.ollycredit.ui.terms_conditions.PrivatePolicyActivity;
import com.ollycredit.ui.terms_conditions.TermsConditionActivity;
import com.ollycredit.ui.terms_conditions.fragment.TermsConditionPageFragment;
import com.ollycredit.ui.user_profile.change_password.ChangePasswordActivity;
import com.ollycredit.ui.user_profile.user_details.UserProfileActivity;

import dagger.Component;


/**
 * Created by ch8n on 3/5/17.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(PhoneNumberActivity phoneNumberActivity);

    void inject(LoginPinActivity loginPinActivity);

    void inject(UserDetailsTwoActivity userDetailsTwoActivity);

    void inject(UserRegistrationActivity userRegistrationActivity);

    void inject(CreatePinActivity createPinActivity);

    void inject(ReadTermsConditionActivity readTermsConditionActivity);

    //void inject(TCPage1Activity tcPage1Activity);
    void inject(LandingPage landingPageActivity);

    void inject(SplashActivity splashActivity);

    void inject(RecoverPinActivity recoverPinActivity);

    void inject(RecoverPasswordDetailsActivity recoverPasswordDetailsActivity);

    void inject(RecoverPinCompleteActivity recoverPinCompleteActivity);

    void inject(RecoverByPhoneCallMainActivity recoverByPhoneCallMainActivity);

    void inject(LoginPasswordActivity loginPasswordActivity);

    void inject(RecoverPasswordActivity recoverPasswordActivity);

    void inject(RecoverPasswordCompleteActivity recoverPasswordCompleteActivity);

    void inject(CardHomeActivity cardHomeActivity);

    void inject(CardHistoryActivity cardHistoryActivity);

    void inject(CreditLimitActivity creditLimitActivity);

    void inject(ResetPinActivity resetPinActivity);

    void inject(VerifyPanActivity verifyPanActivity);

    void inject(TransactionFragment transactionFragment);

    void inject(RepaymentFragment repaymentFragment);

    void inject(NavDrawerFragment NavDrawerFragment);

    void inject(AppointKycActivity appointKycActivity);

    void inject(AppointConfirmActivity appointConfirmActivity);

    void inject(AppointAddressActivity appointAddressActivity);

    void inject(AppointTimeActivity appointTimeActivity);

    void inject(VerifyBankActivity verifyBankActivity);

    void inject(RepayAmountActivity editRepayActivity);

    void inject(TermsConditionActivity termsConditionActivity);

    void inject(TermsConditionPageFragment pageFragment);
    void inject(VerifyEmailActivity verifyEmailActivity);

    void inject(PinResetConfirmActivity pinResetConfirmActivity);

    void inject(UserProfileActivity userProfileActivity);
    void inject(ChangePasswordActivity changePasswordActivity);
    void inject(ValidateUserActivity validateUserActivity);
    void inject(LegalTermsConditionActivity legalTermsConditionActivity);
    void inject(PrivatePolicyActivity privatePolicyActivity);

    void inject(HelpActivity helpActivity);


    void inject(StoreCardsFragment storeCardsFragment);
    void inject(RepaymentActivity repaymentActivity);

    void inject(DebitCardFragment debitCardFragment);
    void inject(NetBankingFragment netBankingFragment);
    void inject(UPIFragment upiFragment);
    void inject(AccountNumberActivity accountNumberActivity);

}

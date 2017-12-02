package com.ollycredit.api.service;

import com.ollycredit.api.model.ChangePassResponseModel;
import com.ollycredit.api.model.PayUModel;
import com.ollycredit.api.model.RequestModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.api.model.bahama.UserAccDetail;
import com.ollycredit.api.model.bahama.UserPersonalDetails;
import com.ollycredit.api.model.kyc.KycRequestModel;
import com.ollycredit.api.model.kyc.KycResponseModel;
import com.ollycredit.api.model.response.UpdateModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;


/**
 * Created by puneet on 8/1/17.
 */

public interface ApiService {


    //get flow
    @GET("api/user/flow/{phone}")
    Observable<ResponseModel> getFlow(@Path("phone") String phoneNumber);

    //===================operational flow ===============================//

    //Login user using phone number
    @POST("api/auth/user/phone")
    Observable<ResponseModel> Login(@Body RequestModel requestModel);

    //Login (Check Pin Code)
    @POST("api/auth/user/verification/phone/login")
    Observable<ResponseModel> verifyPhoneLogin(@Body RequestModel requestModel,
                                               @Query("token") String token);

    @GET("/api/user/card/status")
    Observable<ResponseModel> getCardActiveStatus(@Query("token") String token);

    //Flow User Choose Pin Code
    @PUT("api/auth/user/pin")
    Observable<ResponseModel> changePinForFlowOperation(@Body RequestModel requestModel, @Query("token") String token);

    //Registration Choose Pin Code
    @POST("api/auth/user/pin")
    Observable<ResponseModel> createPinForUser(@Body RequestModel requestModel, @Query("token") String token);


    //======================PRE_USER_FLOW=================================//


    //create new user
    @POST("api/auth/user")
    Observable<ResponseModel> registerUser(@Body RequestModel requestModel);


    //Complete Registration
    @POST("api/auth/user/details")
    Observable<ResponseModel> completeRegistration(@Body RequestModel requestModel,
                                                   @Query("token") String token);

    //Verify mobile pin register
    @POST("api/auth/user/verification/phone")
    Observable<ResponseModel> verifyPhoneRegister(@Body RequestModel requestModel,
                                                  @Query("token") String token);

    //Choose Pin Code
    @POST("api/auth/user/forget/pin")
    Observable<ResponseModel> ForgetPinReset(@Body RequestModel requestModel, @Query("token") String token);

    //Resend Pin
    @GET("api/auth/user/pin")
    Observable<ResponseModel> resendPin(@Query("token") String token);


    //Login
    @POST("api/auth/user/phone")
    Observable<ResponseModel> loginPhone(@Body RequestModel requestModel);

    //Check Pin Code login
    @POST("api/auth/user/pin/check")
    Observable<ResponseModel> checkLogInPin(@Body RequestModel requestModel, @Query("token") String token);

    //check password
    @POST("api/auth/user/forget/pin/password/check")
    Observable<ResponseModel> verifyPassword(@Body RequestModel requestModel, @Query("token") String token);

    //forgot password
    @POST("api/auth/user/forget/password")
    Observable<ResponseModel> forgetPassword(@Query("token") String token);

    //Verify mobile pin forget pin
    @POST("api/auth/user/forget/pin/verification/phone")
    Observable<ResponseModel> verifyPhoneRecovery(@Body RequestModel requestModel, @Query("token") String token);

    //Reset Pin Code
    @POST("api/auth/user/pin")
    Observable<ResponseModel> resetPin(@Body RequestModel requestModel, @Query("token") String token);

    //Get User Account
    @GET("api/user")
    Observable<ResponseModel> getUserAccount(@Query("token") String token);

    //Get Card
    @GET("api/user/card")
    Observable<ResponseModel> getCard(@Query("token") String token);

    //Get Transactions
    @GET("api/user/transactions")
    Observable<ResponseModel> getTransactions(@Query("token") String token);

    //Create Transaction
    @FormUrlEncoded
    @POST("api/user/transactions")
    Observable<ResponseModel> createTransactions(@Query("token") String token);


    //Balance Check
    @POST("api/balance")
    Observable<ResponseModel> checkBalance(@Body RequestModel requestModel);

    //PAN
    @POST("api/auth/user/pan")
    Observable<ResponseModel> verifyPan(@Body RequestModel requestModel, @Query("token") String token);

    @GET("api/user/pan/status")
    Observable<ResponseModel> checkPanStatus(@Query("token") String token);

    // KYC Appointment (create appointment)
    @POST("api/user/profile/kyc/appointment")
    Observable<ResponseModel> createKycAppoint(@Body KycRequestModel requestModel, @Query("token") String token);

    // KYC Appointment (update appointment)
    @PUT("api/user/profile/kyc/appointment")
    Observable<ResponseModel> updateKycAppoint(@Body KycRequestModel requestModel, @Query("token") String token);

    // KYC Appointment (get appointment)
    @GET("api/user/profile/kyc/appointment")
    Observable<KycResponseModel> getKycAppoint(@Query("token") String token);

    @POST("api/payment/hash/payment")
    Observable<ResponseModel> generateHash(@Body PayUModel payuParams);

    //send user feedback
    @Multipart
    @POST("api/user/profile/feedback")
    Observable<ResponseModel> sendUserFeedback(@Part MultipartBody.Part image,
                                               @Part("content") RequestBody content,
                                               @Query("token") String token);

    @GET("api/user/transactions/amount")
    Observable<ResponseModel> repayment(@Query("token") String token);

    @PUT("api/auth/user/password")
    Observable<ChangePassResponseModel> changePassword(@Body ChangePassResponseModel passwordModel, @Query("token") String token);

    @GET("api/auth/user/verification/email")
    Observable<ResponseModel> resendEmail(@Query("token") String token);

    @GET("api/preuser")
    Observable<ResponseModel> getPreUserInfo(@Query("token") String token);

    @POST("api/user/profile/details/personal")
    Observable<ResponseModel> getPersonalDetails(@Body RequestModel requestModel, @Query("token") String token);

    @POST("api/user/profile/details/professional")
    Observable<ResponseModel> getProffesionalDetails(@Body RequestModel requestModel, @Query("token") String token);


    @POST("api/user/profile/details/id")
    Observable<ResponseModel> getDocumentStored(@Body RequestModel requestModel, @Query("token") String token);

    @GET("api/app/version/{versionCode}")
    Observable<UpdateModel> checkForUpdate(@Path("versionCode") int versionCode);

    @GET("api/app/content/repayment")
    Observable<ResponseModel> checkRepaymentData();

    @GET("api/app/content/help")
    Observable<ResponseModel> getHelp();

    @GET
    Observable<List<UserAccDetail>> getBhramAccountDetails(@Url String url);

    @GET
    Observable<UserPersonalDetails> getBhramUserDetails(@Url String url);


}

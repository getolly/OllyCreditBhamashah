package com.ollycredit.base;

import android.Manifest.permission;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.ollycredit.OllyCreditApplication;
import com.ollycredit.R;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.injection.component.ActivityComponent;
import com.ollycredit.injection.component.DaggerActivityComponent;
import com.ollycredit.injection.module.ActivityModule;
import com.ollycredit.utils.DialogInteractor;
import com.ollycredit.utils.GlobalConstants;
import com.ollycredit.utils.dialogs.ErrorConnectionDialog;
import com.ollycredit.utils.dialogs.ErrorSomthingDialog;
import com.ollycredit.utils.dialogs.LoadingDialog;
import com.ollycredit.utils.dialogs.UserFeedbackDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by ch8n on 3/5/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseActivityCallback, BaseView {

    private ActivityComponent activityComponent;
    private ProgressDialog progress;
    private String[] permissionArray;
    private Bitmap image;

    private DialogInteractor.ErrorDialogInteractor errorDialogInteractor;

    UserFeedbackDialog userFeedbackDialog;

    private ErrorConnectionDialog errorConnectionDialog;
    private ErrorSomthingDialog errorSomthingDialog;
    private LoadingDialog loadingDialog;


    @Inject
    ConcreteBasePresenter concreteBasePresenter;
    @Inject
    PreferencesHelper preferencesHelper;

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(OllyCreditApplication.get(this).component())
                    .build();
        }
        return activityComponent;
    }

    //for backbutton on toolbar
    protected void showBackOnToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected abstract void setUp();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return true;
    }

    //this handle when user feedback button pressed on every activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //attach concrete view
        concreteBasePresenter.attachView(this);

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;
            //handle user feedback when pressed
            case R.id.feedback_icon:
                //take screenshot of current activity
                View rootView = findViewById(android.R.id.content).getRootView();
                rootView.setDrawingCacheEnabled(true);
                Bitmap bitmap = rootView.getDrawingCache();

                //open user feedback dialog
                userFeedbackDialog = new UserFeedbackDialog(this, bitmap);

                //use listener to know which button is pressed
                userFeedbackDialog.setDialogResult(new UserFeedbackDialog.OnMyDialogResult() {
                    @Override
                    public void finish(String result, String message, Bitmap bmp) {
                        if (result.equals(GlobalConstants.SEND_FEEDBACK_MESSAGE)) {

                            showLoadingView();
                            //send feedback to server
                            File file = converBitmaptoFile(bmp, "feedback");
                            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                            MultipartBody.Part filePart = MultipartBody.Part.createFormData("data", file.getName(), reqFile);

                            RequestBody content = RequestBody.create(okhttp3.MultipartBody.FORM, message);

                            concreteBasePresenter.sendUserFeedback(filePart, content, preferencesHelper.getToken());


                        }
                    }
                });
                userFeedbackDialog.show();


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline(Context mContext) {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void showErrorSomthing() {
        errorSomthingDialog = new ErrorSomthingDialog(this);
        errorSomthingDialog.setCancelable(false);
        errorSomthingDialog.show();
    }

    public void showLoadingView() {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }


    public void showErrorConnection() {
        errorConnectionDialog = new ErrorConnectionDialog(this);
        errorConnectionDialog.setCancelable(false);
        errorConnectionDialog.show();
    }

    public void hideErrorConnection() {
        if (errorConnectionDialog != null && errorConnectionDialog.isShowing()) {
            errorConnectionDialog.dismiss();
        }
    }

    public void hideLoadingView() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public void hideErrorSomthing() {
        if (errorSomthingDialog.isShowing()) {
            errorSomthingDialog.dismiss();
        }
    }


    //for toast
    public void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    public void showToast(@NonNull String message, @NonNull int toastType) {
        Toast.makeText(BaseActivity.this, message, toastType).show();
    }

    //to show Progressdialog
    public void showProgressDialog() {
        showProgressDialog("Please wait...");
    }

    @Override
    public void showProgressDialog(String message) {
        if (progress == null) {
            progress = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
        }
        progress.setMessage(message);
        progress.show();
    }


    @Override
    public void hideProgress() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgressDialog() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }


    //fragemnt replace
    public void replaceFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        invalidateOptionsMenu();

        String backStateName = fragment.getClass().getName();
        boolean fragPopped = getSupportFragmentManager().popBackStackImmediate(backStateName, 0);

        if (!fragPopped && getSupportFragmentManager().findFragmentByTag(backStateName) ==
                null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(containerId, fragment, backStateName);
            if (addToBackStack) {
                transaction.addToBackStack(backStateName);
            }
            transaction.commit();
        }
    }


    public void showDialogBox(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
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

    private void permissioDeniedDialogBox(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(message)
                .setTitle("Caution")
                .setNeutralButton("Ok", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkForPermission();
                        dialogInterface.dismiss();
                    }
                }).create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
    }

    private void permissioDisabledDialogBox(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(message)
                .setTitle("Caution")
                .setNeutralButton("Ok", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gotoSettings();
                        dialogInterface.dismiss();
                    }
                }).create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
    }

    private void gotoSettings() {

    }

    private File converBitmaptoFile(Bitmap bitmap, String name) {
        File filesDir = getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("Base Activity", "Error writing bitmap", e);
        }
        return imageFile;
    }

   /* public String convertBitmapToString(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
        byte[] byte_arr = stream.toByteArray();
        String imageStr = Base64.encodeToString(byte_arr,Base64.DEFAULT);
        return imageStr;
    }*/

//
//    //We are calling this method to check the permission status
//    public void checkForPermission() {
//        //Getting the permission status
//        permissionArray = new String[]{permission.READ_PHONE_STATE, permission.RECEIVE_SMS};
//
//        for (String permission : permissionArray) {
//
//            int result = ContextCompat.checkSelfPermission(this, permission);
//            if (result == PackageManager.PERMISSION_DENIED) {
//                requestStoragePermission(permission);
//            }
//
//
//        }
//    }
//
//    private void requestStoragePermission(String permissionReq) {
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionReq)) {
//            //If the user has denied the permission previously your code will come to this block
//            //Here you can explain why you need this permission
//            //Explain here why you need this permission
//            switch (permissionReq) {
//                case permission.READ_PHONE_STATE:
//                    permissioDeniedDialogBox("Please provide Telephone permission, it's necessary for Functionality");
//                    break;
//                case permission.RECEIVE_SMS:
//                    permissioDeniedDialogBox("Please provide SMS read permission, it's necessary for Functionality");
//                    break;
//                default:
//
//            }
//        }
//
//        //ask for the permission
//        ActivityCompat.requestPermissions(this, permissionArray, GlobalUtils.BASE_PERMISSION_PHONE_STATE_CODE);
//
//    }
//
//
//    //This method will be called when the user will tap on allow or deny
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        //Checking the request code of our request
//        if (requestCode == GlobalUtils.BASE_PERMISSION_PHONE_STATE_CODE) {
//
//            //If permission is granted
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//            } else {
//                //Displaying another message if permission is not granted
//                Toast.makeText(this, "Please enable telephone permission", Toast.LENGTH_LONG).show();
//
//            }
//        }
//
//        if (requestCode == GlobalUtils.BASE_PERMISSION_RECEIVE_SMS_CODE) {
//
//            //If permission is granted
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//            } else {
//                //Displaying another message if permission is not granted
//                Toast.makeText(this, "Please enable SMS permission", Toast.LENGTH_LONG).show();
//            }
//        }
//
//    }

    //We are calling this method to check the permission status
    public void checkForPermission() {
        //Getting the permission status
        permissionArray = new String[]{permission.RECEIVE_SMS};

        for (String permission : permissionArray) {

            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result == PackageManager.PERMISSION_DENIED) {
                requestStoragePermission(permission);
            }


        }
    }

    private void requestStoragePermission(String permissionReq) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionReq)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            switch (permissionReq) {
                case permission.RECEIVE_SMS:
                    Toast.makeText(this, "Please enable SMS permission", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(this, permissionArray, GlobalConstants.BASE_PERMISSION_RECEIVE_SMS_CODE);
                    break;
                default:
            }
        }

        //ask for the permission
        ActivityCompat.requestPermissions(this, permissionArray, GlobalConstants.BASE_PERMISSION_RECEIVE_SMS_CODE);

    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == GlobalConstants.BASE_PERMISSION_RECEIVE_SMS_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                //Displaying another message if permission is not granted
                Toast.makeText(this, "Please enable SMS permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    //handle when send feedback success
    @Override
    public void sendFeedbackSuccess(ResponseModel responseModel) {
        hideLoadingView();
        userFeedbackDialog.dismiss();
//        UserFeedbackSendDialog userFeedbackSendDialog = new UserFeedbackSendDialog(BaseActivity.this);
//        userFeedbackSendDialog.show();
        Toast.makeText(this, "Feedback sent", Toast.LENGTH_SHORT).show();
    }

    //handle when send feedback failed
    @Override
    public void sendFeedbackFailure(int failedCode, String message) {
        hideProgressDialog();
        showDialogBox(message);

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}




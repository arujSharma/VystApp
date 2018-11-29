package vyst.business.activites;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vyst.business.R;
import vyst.business.app.App;
import vyst.business.pojo.CommonResponsePojo;
import vyst.business.pojo.LoginJson;
import vyst.business.pojo.SaveTokenJson;
import vyst.business.pojo.login.Login;
import vyst.business.repositories.main.MainRepository;
import vyst.business.util.CommonUtils;
import vyst.business.util.SharedPrefUtil;
import android.provider.Settings.Secure;
import android.widget.TextView;

public class OTPActivity extends BaseActivity implements View.OnClickListener{

    Button tv_get_otp;
    EditText et_number,et_otp;
    LinearLayout ll_otp;
    ImageView img_back;
    private Disposable disposable;
    private MainRepository mainRepository;

    int HIT_API_STATE = 1;
    int AUTHORIZE_OTP_STATE = 2;
    int CURRENT_STATE = 1;
    String otp_number = "";

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    String android_id="";

    TextView tv_resend_otp,tv_change_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        init();
          android_id = Secure.getString(getContentResolver(),
                Secure.ANDROID_ID);

    }

    private void init() {

        tv_get_otp = findViewById(R.id.tv_get_otp);
        et_number = findViewById(R.id.et_number);

        img_back= findViewById(R.id.img_back);

        et_otp= findViewById(R.id.et_otp);
        ll_otp= findViewById(R.id.ll_otp);


        img_back.setOnClickListener(this);
        tv_get_otp.setOnClickListener(this);

        tv_resend_otp= findViewById(R.id.tv_resend_otp);
        tv_change_no= findViewById(R.id.tv_change_no);
        tv_resend_otp.setOnClickListener(this);
        tv_change_no.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d("login", "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("login", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("login", "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };
    }


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        try {
            if(verificationId!=null && code!=null && !verificationId.isEmpty() && !code.isEmpty()) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                // [END verify_with_code]
                signInWithPhoneAuthCredential(credential);
            }
            else{
                CommonUtils.showSnackbar(OTPActivity.this, "Please enter correct OTP");

            }
        }

         catch(IllegalArgumentException i) {

             CommonUtils.showSnackbar(OTPActivity.this, "Please enter correct OTP");

         }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_get_otp:

                String number = et_number.getText().toString().trim();

                if(CURRENT_STATE==HIT_API_STATE) {
                    if (number.equalsIgnoreCase("")) {
                        CommonUtils.showSnackbar(OTPActivity.this, "Please enter phone number");
                    } else if (number.length() != 10) {
                        CommonUtils.showSnackbar(OTPActivity.this, "Please enter valid phone number");
                    } else {
                        if (CommonUtils.isConnectedToInternet(OTPActivity.this)) {
                            hitLoginApi(number);
                        } else {
                            CommonUtils.showSnackbar(OTPActivity.this, getString(R.string.please_check_your_internet_connection));
                        }
                    }
                }else if(CURRENT_STATE ==AUTHORIZE_OTP_STATE){

                    String otp_code = et_otp.getText().toString();


                    if(otp_code.length()==0){
                        CommonUtils.showSnackbar(OTPActivity.this, "Please enter OTP");
                    }
                    else if(otp_code.length()!=6){
                        CommonUtils.showSnackbar(OTPActivity.this, "Please enter valid OTP");
                    }
                    else{
                        verifyPhoneNumberWithCode(mVerificationId, otp_code);
                    }

            }


                break;


            case R.id.img_back:

                Intent intent = new Intent(getApplication(), LoginActivity.class);
                startActivity(intent);

                finish();

                break;


            case R.id.tv_resend_otp:
                // finish();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        otp_number,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        OTPActivity.this,               // Activity (for callback binding)
                        mCallbacks);        // OnVerificationStateChangedCallbacks
                SharedPrefUtil.setPhone(OTPActivity.this, otp_number);

                CommonUtils.showSnackbar(OTPActivity.this, "OTP has been resend");

                break;

            case R.id.tv_change_no:
                // finish();
                Intent intent1 = new Intent(getApplication(), OTPActivity.class);
                startActivity(intent1);
                finish();

                break;
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), LoginActivity.class);
        startActivity(intent);

        finish();
    }

    private void hitLoginApi(final String number) {

        CommonUtils.showProgressDialog(OTPActivity.this,getResources().getString(R.string.please_wait));

        LoginJson loginJson  = new LoginJson();
        loginJson.setPhone(number);

        mainRepository = App.getApp().getMainRepository();
        mainRepository.loginApi("application/json",loginJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Login>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;

                    }

                    @Override
                    public void onNext(Login value) {

                        //  mainActivityViewInterface.logMessage(TAGMESSAGE,"value "+value.get(0).getName());
                        CommonUtils.hideDialog();


                        if(value.getMessage().equalsIgnoreCase("Not present")){

                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:

                                            dialog.dismiss();

                                            break;

                               /* case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    dialog.dismiss();
                                    break;*/
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(OTPActivity.this);
                            builder.setMessage("You are not a registered shopkeeper.").
                                    setPositiveButton("OK", dialogClickListener)
                                  //  setTitle("Success")
                           /* .setNegativeButton(getString(R.string.no), dialogClickListener)*/.show();

                        }
                        else if(value.getMessage().equalsIgnoreCase("Mobile Already present")){

                            if(value.getData().getUserType().equalsIgnoreCase("S")) {

                                ll_otp.setVisibility(View.VISIBLE);
                                tv_get_otp.setText("Login");

                                CURRENT_STATE = AUTHORIZE_OTP_STATE;

                                otp_number = "+91"+number;


                                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                        otp_number,        // Phone number to verify
                                        60,                 // Timeout duration
                                        TimeUnit.SECONDS,   // Unit of timeout
                                        OTPActivity.this,               // Activity (for callback binding)
                                        mCallbacks);        // OnVerificationStateChangedCallbacks


                                SharedPrefUtil.setAccessToken(OTPActivity.this, value.getData().getToken());
                                SharedPrefUtil.setEmail(OTPActivity.this, value.getData().getEmail());
                                SharedPrefUtil.setAddress(OTPActivity.this, value.getData().getLocationAddress());
                                SharedPrefUtil.setName(OTPActivity.this, value.getData().getName());
                                SharedPrefUtil.setPhone(OTPActivity.this, value.getData().getPhone());
                                SharedPrefUtil.setUserId(OTPActivity.this, value.getData().getUserId());
                                SharedPrefUtil.setLocationId(OTPActivity.this,""+ value.getData().getLocationId());

                             //   hitRegisterTokenApi();

                               /* SharedPrefUtil.setUserLoggedIn(OTPActivity.this, true);
                                Intent intent = new Intent(getApplication(), MainActivity.class);
                                startActivity(intent);
                                finish();*/

                            }else{
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:

                                                dialog.dismiss();

                                                break;

                               /* case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    dialog.dismiss();
                                    break;*/
                                        }
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(OTPActivity.this);
                                builder.setMessage("You are not a registered shopkeeper.").
                                        setPositiveButton("OK", dialogClickListener)
                                        //  setTitle("Success")
                           /* .setNegativeButton(getString(R.string.no), dialogClickListener)*/.show();
                            }


                        }
                        else{
                            CommonUtils.showSnackbar(OTPActivity.this, "something went wrong");
                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                        CommonUtils.hideDialog();

                        CommonUtils.showSnackbar(OTPActivity.this, e.getMessage());


                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void hitRegisterTokenApi() {

        CommonUtils.showProgressDialog(OTPActivity.this,getResources().getString(R.string.please_wait));



        SaveTokenJson saveTokenJson  = new SaveTokenJson();
        saveTokenJson.setDevice_id(android_id);
        saveTokenJson.setDevice_token(SharedPrefUtil.getNotificationToken());
        saveTokenJson.setPlatform("Android");
        saveTokenJson.setToken(SharedPrefUtil.getAccessToken(OTPActivity.this));
        saveTokenJson.setUser_id(SharedPrefUtil.getUserId(OTPActivity.this));

        mainRepository = App.getApp().getMainRepository();
        mainRepository.saveTokenApi("application/json",saveTokenJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponsePojo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;

                    }

                    @Override
                    public void onNext(CommonResponsePojo value) {

                        //  mainActivityViewInterface.logMessage(TAGMESSAGE,"value "+value.get(0).getName());
                        CommonUtils.hideDialog();

                        if(value.getMessage().equalsIgnoreCase("Successfully saved")) {

                            SharedPrefUtil.setUserLoggedIn(OTPActivity.this, true);
                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            CommonUtils.showSnackbar(OTPActivity.this, value.getMessage());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        CommonUtils.hideDialog();

                        CommonUtils.showSnackbar(OTPActivity.this, e.getMessage());


                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login", "signInWithCredential:success");


                          /*  SharedPrefUtil.setUserLoggedIn(OTPActivity.this, true);
                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            startActivity(intent);
                            finish();*/

                          hitRegisterTokenApi();

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("login", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:

                                            dialog.dismiss();

                                            break;

                               /* case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    dialog.dismiss();
                                    break;*/
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(OTPActivity.this);
                            builder.setMessage(task.getException().getMessage()).
                                    setPositiveButton("OK", dialogClickListener).
                                      setTitle("Error")
                           /* .setNegativeButton(getString(R.string.no), dialogClickListener)*/.show();


                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }
}

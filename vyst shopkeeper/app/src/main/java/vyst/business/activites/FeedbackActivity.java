package vyst.business.activites;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vyst.business.R;
import vyst.business.app.App;
import vyst.business.pojo.CommonResponsePojo;
import vyst.business.pojo.FeedbackJson;
import vyst.business.pojo.RegisterJson;
import vyst.business.pojo.register.RegisterShopkeeperPojo;
import vyst.business.repositories.main.MainRepository;
import vyst.business.util.CommonUtils;
import vyst.business.util.SharedPrefUtil;

public class FeedbackActivity extends BaseActivity implements View.OnClickListener{

    ImageView img_back;
    EditText et_feedback;
    Button tv_send;

    private Disposable disposable;
    private MainRepository mainRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        init();
    }

    private void init() {
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        tv_send = findViewById(R.id.tv_send);
        tv_send.setOnClickListener(this);
        et_feedback = findViewById(R.id.et_feedback);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:

                finish();

                break;


            case R.id.tv_send:

              String feedback = et_feedback.getText().toString().trim();
              if(feedback.equalsIgnoreCase("")){
                  CommonUtils.showSnackbar(FeedbackActivity.this,"Please enter feedback");
              }else{

                  if (CommonUtils.isConnectedToInternet(FeedbackActivity.this)) {

                      feedbackApi(feedback);
                  } else {
                      CommonUtils.showSnackbar(FeedbackActivity.this, getString(R.string.please_check_your_internet_connection));

                  }


              }


                break;

        }
    }

    private void feedbackApi(String feedback) {

        CommonUtils.showProgressDialog(FeedbackActivity.this,getResources().getString(R.string.please_wait));

        FeedbackJson feedbackJson  = new FeedbackJson();

        feedbackJson.setToken(SharedPrefUtil.getAccessToken(FeedbackActivity.this));
        feedbackJson.setUser_id(SharedPrefUtil.getUserId(FeedbackActivity.this));
        feedbackJson.setFeedback(feedback);


        mainRepository = App.getApp().getMainRepository();
        mainRepository.feedbackApi("application/json",feedbackJson)
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



                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:

                                        finish();


                                        break;


                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(FeedbackActivity.this);
                        builder.setMessage(value.getMessage()).
                                setPositiveButton("OK", dialogClickListener)
                                /*setTitle("Success")*/.show();



                    }

                    @Override
                    public void onError(Throwable e) {

                        CommonUtils.hideDialog();

                        CommonUtils.showSnackbar(FeedbackActivity.this, e.getMessage());


                    }

                    @Override
                    public void onComplete() {

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

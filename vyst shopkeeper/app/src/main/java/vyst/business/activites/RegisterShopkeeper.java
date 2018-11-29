package vyst.business.activites;

import android.content.DialogInterface;
import android.content.Intent;
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
import vyst.business.pojo.OrderJson;
import vyst.business.pojo.RegisterJson;
import vyst.business.pojo.orders.Orders;
import vyst.business.pojo.register.RegisterShopkeeperPojo;
import vyst.business.repositories.main.MainRepository;
import vyst.business.util.CommonUtils;
import vyst.business.util.SharedPrefUtil;

public class RegisterShopkeeper extends BaseActivity implements View.OnClickListener{

    Button tv_submit;
    EditText et_name,et_number,et_address;
    ImageView img_back;

    private Disposable disposable;
    private MainRepository mainRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shopkeeper);

        init();


    }

    private void init() {

        tv_submit = findViewById(R.id.tv_submit);
        img_back= findViewById(R.id.img_back);

        et_name = findViewById(R.id.et_name);
        et_number = findViewById(R.id.et_number);
        et_address = findViewById(R.id.et_address);

        tv_submit.setOnClickListener(this);
        img_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_submit:

                String name = et_name.getText().toString().trim();
                String number = et_number.getText().toString().trim();
                String address = et_address.getText().toString().trim();

                if(name.equalsIgnoreCase("")){
                    CommonUtils.showSnackbar(RegisterShopkeeper.this,"Please enter name");

                }else if(number.equalsIgnoreCase("")){
                    CommonUtils.showSnackbar(RegisterShopkeeper.this,"Please enter phone number");

                }

                else if(number.length()!=10){

                    CommonUtils.showSnackbar(RegisterShopkeeper.this,"Please enter valid phone number");
                }
                else if(address.equalsIgnoreCase("")){
                    CommonUtils.showSnackbar(RegisterShopkeeper.this,"Please enter address");

                }
               else{



                    if (CommonUtils.isConnectedToInternet(RegisterShopkeeper.this)) {

                        registerShopkeeperApi(name,number,address);
                    } else {
                        CommonUtils.showSnackbar(RegisterShopkeeper.this, getString(R.string.please_check_your_internet_connection));

                    }




                }



                break;


            case R.id.img_back:

                finish();

                break;
        }

    }

    private void registerShopkeeperApi(String name, String number, String address) {


        CommonUtils.showProgressDialog(RegisterShopkeeper.this,getResources().getString(R.string.please_wait));

        RegisterJson registerJson  = new RegisterJson();
        registerJson.setAddress(address);
        registerJson.setAvailability("1");
        registerJson.setEmail("");
        registerJson.setPhone(number);
        registerJson.setName(name);


        mainRepository = App.getApp().getMainRepository();
        mainRepository.registerApi("application/json",registerJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RegisterShopkeeperPojo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;

                    }

                    @Override
                    public void onNext(RegisterShopkeeperPojo value) {

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

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterShopkeeper.this);
                        builder.setMessage("Ready to be Vyst. Our person will contact you soon.").
                                setPositiveButton("OK", dialogClickListener).
                                setTitle("Success").show();



                    }

                    @Override
                    public void onError(Throwable e) {

                        CommonUtils.hideDialog();

                        CommonUtils.showSnackbar(RegisterShopkeeper.this, e.getMessage());


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

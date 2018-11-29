package vyst.business.activites;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vyst.business.R;
import vyst.business.app.App;
import vyst.business.helper.CommonListeners;
import vyst.business.helper.DialogPopUps;
import vyst.business.pojo.CommonResponsePojo;
import vyst.business.pojo.OrderJson;
import vyst.business.pojo.location.Datum;
import vyst.business.pojo.location.LocationPojo;
import vyst.business.repositories.main.MainRepository;
import vyst.business.util.CommonUtils;
import vyst.business.util.SharedPrefUtil;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    ImageView img_back;
    RelativeLayout rl_items;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1234;
    TextView tv_name, tv_number, tv_email, tv_location, tv_address, txt_options;

    private Disposable disposable;
    private MainRepository mainRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
    }

    private void init() {

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        rl_items = findViewById(R.id.rl_items);
        rl_items.setOnClickListener(this);

        tv_name = findViewById(R.id.tv_name);
        tv_number = findViewById(R.id.tv_number);
        tv_email = findViewById(R.id.tv_email);
        tv_location = findViewById(R.id.tv_location);
        tv_address = findViewById(R.id.tv_address);
        txt_options = findViewById(R.id.txt_options);
        txt_options.setOnClickListener(this);


        String name = SharedPrefUtil.getName(ProfileActivity.this);
        String shopkeeper_name = name.substring(0,1).toUpperCase() +name.substring(1);

        tv_name.setText(shopkeeper_name);
        tv_number.setText(SharedPrefUtil.getPhone(ProfileActivity.this));
        tv_email.setText(SharedPrefUtil.getEmail(ProfileActivity.this));

      /*  if(!SharedPrefUtil.getlocationId(ProfileActivity.this).trim().equalsIgnoreCase(""))
        tv_location.setText("Sector "+SharedPrefUtil.getlocationId(ProfileActivity.this));*/

        if(SharedPrefUtil.getLocationArray().size()==0){
         /*  ArrayList<Datum> data1 = (ArrayList<Datum>) data;
           SharedPrefUtil.saveLocationArray(data1);*/

            if (CommonUtils.isConnectedToInternet(ProfileActivity.this)) {

                hitLocationListApi();
            }


        }else{

            String loc_id = SharedPrefUtil.getlocationId(ProfileActivity.this);
            ArrayList<Datum> datumArrayList = SharedPrefUtil.getLocationArray();

            for(int i=0;i<datumArrayList.size();i++){

                if(datumArrayList.get(i).getLocationId().toString().equalsIgnoreCase(loc_id)){
                    tv_location.setText(datumArrayList.get(i).getLocationAddress());
                }

            }

        }


        tv_address.setText(SharedPrefUtil.getAddress(ProfileActivity.this));


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:

                finish();

                break;


            case R.id.rl_items:

                Intent intent = new Intent(getApplication(), ItemListActivity.class);
                startActivity(intent);

                break;

            case R.id.txt_options:


                DialogPopUps.options(ProfileActivity.this, new CommonListeners.Options() {
                    @Override
                    public void share() {

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");

                        intent.putExtra(Intent.EXTRA_TEXT,"You spend time with your loved ones, we will get your groceries delivered at home."+"\n"+"http://vyst.app.link/");

                        startActivity(Intent.createChooser(intent, "Share"));



                    }

                    @Override
                    public void feedback() {

                        Intent intent1 = new Intent(getApplication(), FeedbackActivity.class);
                        startActivity(intent1);

                    }

                    @Override
                    public void callUs() {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        try {

                                            if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                                                    Manifest.permission.CALL_PHONE)
                                                    != PackageManager.PERMISSION_GRANTED) {

                                                ActivityCompat.requestPermissions(ProfileActivity.this,
                                                        new String[]{Manifest.permission.CALL_PHONE},
                                                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
                                            } else {
                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:" + "+919569888866"));
                                                startActivity(callIntent);
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setMessage("+919569888866").
                                setPositiveButton("Call", dialogClickListener)
                                .setNegativeButton("Cancel", dialogClickListener).show();
                    }

                    @Override
                    public void aboutUs() {


                        CommonUtils.openLink(ProfileActivity.this, "zero2unicorn.com");
                    }

                    @Override
                    public void rateUs() {

                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }

                    }

                    @Override
                    public void logOut() {


                        if (CommonUtils.isConnectedToInternet(ProfileActivity.this)) {

                            hitLogoutApi();
                        } else {
                            CommonUtils.showSnackbar(ProfileActivity.this, getString(R.string.please_check_your_internet_connection));

                        }


                    }

                    @Override
                    public void cancel() {

                    }
                });


                break;


        }
    }

    private void hitLogoutApi() {
        OrderJson orderJson = new OrderJson();

        orderJson.setToken(SharedPrefUtil.getAccessToken(ProfileActivity.this));
        orderJson.setUser_id(SharedPrefUtil.getUserId(ProfileActivity.this));

        CommonUtils.showProgressDialog(ProfileActivity.this,getResources().getString(R.string.please_wait));



        mainRepository = App.getApp().getMainRepository();
        mainRepository.logOutApi("application/json",orderJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponsePojo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;

                    }

                    @Override
                    public void onNext(CommonResponsePojo value) {

                        CommonUtils.hideDialog();

                        SharedPrefUtil.clearPreferences(getApplicationContext());
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }

                    @Override
                    public void onError(Throwable e) {

                        CommonUtils.hideDialog();

                        CommonUtils.showSnackbar(ProfileActivity.this, e.getMessage());


                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                            Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                        // permission was granted, yay!
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + "+919888542154"));
                        startActivity(callIntent);
                    }


                } else {

                    // permission denied
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }




    private void hitLocationListApi() {




        mainRepository = App.getApp().getMainRepository();
        mainRepository.getLocationList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LocationPojo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;

                    }

                    @Override
                    public void onNext(LocationPojo value) {

                        //  mainActivityViewInterface.logMessage(TAGMESSAGE,"value "+value.get(0).getName());



                        ArrayList<Datum> data1 = (ArrayList<Datum>) value.getData();
                        SharedPrefUtil.saveLocationArray(data1);

                        for(int i=0;i<data1.size();i++){

                            if(data1.get(i).getLocationId().toString().equalsIgnoreCase(SharedPrefUtil.getlocationId(ProfileActivity.this))){
                                tv_location.setText(data1.get(i).getLocationAddress());
                            }

                        }


                    }

                    @Override
                    public void onError(Throwable e) {



                        //   CommonUtils.showSnackbar(SelectLocationActivity.this, e.getMessage());


                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }




}


//1. 1st letter capital
//2. discount type khali disable

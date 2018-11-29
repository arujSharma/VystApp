package vyst.business.activites;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vyst.business.R;
import vyst.business.adapters.OrdersAdapter;
import vyst.business.adapters.OrdersPendingAdapter;
import vyst.business.app.App;
import vyst.business.helper.CircularTextView;
import vyst.business.helper.CommonListeners;
import vyst.business.helper.DialogPopUps;
import vyst.business.interfaces.OrdersInterface;
import vyst.business.pojo.CommonResponsePojo;
import vyst.business.pojo.LoginJson;
import vyst.business.pojo.OrderJson;
import vyst.business.pojo.UpdateOrderJson;
import vyst.business.pojo.login.Login;
import vyst.business.pojo.orders.OrderList;
import vyst.business.pojo.orders.Orders;
import vyst.business.repositories.main.MainRepository;
import vyst.business.util.CommonUtils;
import vyst.business.util.SharedPrefUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener, OrdersInterface{

    CircularTextView circularTextView,circularTextViewAccepted;
    RecyclerView rv_orders,rv_orders_pending;
    private OrdersAdapter ordersAdapter;
    private OrdersPendingAdapter ordersPendingAdapter;
    ImageView img_refresh,img_profile,img_filter;

    private Disposable disposable;
    private MainRepository mainRepository;

    TextView tv_all,tv_pending, tv_no_accepted_order,tv_no_pending_order;
    boolean isAllSelected = true;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1234;

    List<OrderList> orderListPending=new ArrayList<>();
    List<OrderList> orderLists=new ArrayList<>();
    int pending_count = 0,accepted_count =0;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




       // circularTextView.setStrokeWidth(1);
       // circularTextView.setStrokeColor("#ffffff");
        init();

        final Handler handler = new Handler();
        Timer    timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            orderApi();
                        }
                        catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 180000);
//
//        if (CommonUtils.isConnectedToInternet(MainActivity.this)) {
//
//            orderApi();
//        } else {
//            CommonUtils.showSnackbar(MainActivity.this, getString(R.string.please_check_your_internet_connection));
//
//        }


    }



    private void init() {
        circularTextView = findViewById(R.id.circularTextView);
        circularTextView.setSolidColor("#E13C3B");

        circularTextViewAccepted= findViewById(R.id.circularTextViewAccepted);
        circularTextViewAccepted.setSolidColor("#E13C3B");

        rv_orders = findViewById(R.id.rv_orders);
        rv_orders_pending = findViewById(R.id.rv_orders_pending);

        ordersAdapter = new OrdersAdapter(MainActivity.this,this);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_orders.setLayoutManager(mLayoutManager);
        rv_orders.setItemAnimator(new DefaultItemAnimator());
        rv_orders.setAdapter(ordersAdapter);

        ordersPendingAdapter = new OrdersPendingAdapter(MainActivity.this,this);
        final RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        rv_orders_pending.setLayoutManager(mLayoutManager1);
        rv_orders_pending.setItemAnimator(new DefaultItemAnimator());
        rv_orders_pending.setAdapter(ordersPendingAdapter);

        img_refresh= findViewById(R.id.img_refresh);
        img_refresh.setOnClickListener(this);

        img_profile= findViewById(R.id.img_profile);
        img_profile.setVisibility(View.INVISIBLE);
        img_profile.setOnClickListener(this);

        img_filter= findViewById(R.id.img_filter);
        img_filter.setOnClickListener(this);

        tv_all= findViewById(R.id.tv_all);
        tv_pending= findViewById(R.id.tv_pending);
        tv_all.setOnClickListener(this);
        tv_pending.setOnClickListener(this);



        tv_no_accepted_order= findViewById(R.id.tv_no_accepted_order);
        tv_no_pending_order= findViewById(R.id.tv_no_pending_order);
        timer= new Timer();

    }



    private void orderApi() {
        CommonUtils.showProgressDialog(MainActivity.this,getResources().getString(R.string.please_wait));

        OrderJson orderJson  = new OrderJson();
        orderJson.setToken(SharedPrefUtil.getAccessToken(MainActivity.this));
        orderJson.setUser_id(SharedPrefUtil.getUserId(MainActivity.this));

        mainRepository = App.getApp().getMainRepository();
        mainRepository.orderApi("application/json",orderJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Orders>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;

                    }

                    @Override
                    public void onNext(Orders value) {

                        //  mainActivityViewInterface.logMessage(TAGMESSAGE,"value "+value.get(0).getName());
                        CommonUtils.hideDialog();






                        accepted_count = 0;
                        orderLists.clear();

                        for(int i=0; i<value.getOrderList().size();i++){

                            if(value.getOrderList().get(i).getStatus().equalsIgnoreCase("accepted")){

                                orderLists.add(value.getOrderList().get(i));
                                accepted_count = accepted_count+1;
                            }


                        }
                        ordersAdapter.addingData(orderLists);

                        orderListPending.clear();
                        pending_count = 0;

                        for(int i=0; i<value.getOrderList().size();i++){

                            if(value.getOrderList().get(i).getStatus().equalsIgnoreCase("pending")){

                                orderListPending.add(value.getOrderList().get(i));
                                pending_count = pending_count+1;
                            }


                        }

                        ordersPendingAdapter.addingData(orderListPending);

                        if(accepted_count==0){
                            circularTextViewAccepted.setVisibility(View.GONE);
                        }else{
                            circularTextViewAccepted.setVisibility(View.VISIBLE);
                            circularTextViewAccepted.setText(""+accepted_count);

                        }

                        if(pending_count==0){
                            circularTextView.setVisibility(View.GONE);
                        }else{
                            circularTextView.setVisibility(View.VISIBLE);
                            circularTextView.setText(""+pending_count);
                        }

                        if(isAllSelected){
                            rv_orders_pending.setVisibility(View.GONE);
                            tv_no_pending_order.setVisibility(View.GONE);
                           // circularTextView.setVisibility(View.GONE);

                            if(orderLists.size()==0){
                                rv_orders.setVisibility(View.GONE);
                                tv_no_accepted_order.setVisibility(View.VISIBLE);
                            }else{
                                rv_orders.setVisibility(View.VISIBLE);
                                tv_no_accepted_order.setVisibility(View.GONE);
                            }




                        }else{

                          //  circularTextViewAccepted.setVisibility(View.GONE);

                            rv_orders.setVisibility(View.GONE);
                            tv_no_accepted_order.setVisibility(View.GONE);

                            if(orderListPending.size()==0){
                                rv_orders_pending.setVisibility(View.GONE);
                                tv_no_pending_order.setVisibility(View.VISIBLE);
                            }else{
                                rv_orders_pending.setVisibility(View.VISIBLE);
                                tv_no_pending_order.setVisibility(View.GONE);
                            }



                        }






                    }

                    @Override
                    public void onError(Throwable e) {

                        CommonUtils.hideDialog();

                        CommonUtils.showSnackbar(MainActivity.this, e.getMessage());


                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }
    private void refreshOrders()
    {
        //   Crashlytics.getInstance().crash();

        if (CommonUtils.isConnectedToInternet(MainActivity.this)) {

            orderApi();
        } else {
            CommonUtils.showSnackbar(MainActivity.this, getString(R.string.please_check_your_internet_connection));

        }

    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.img_refresh:
                refreshOrders();
                break;

            case R.id.img_profile:


                Intent intent = new Intent(getApplication(), ProfileActivity.class);
                startActivity(intent);


                break;


            case R.id.img_filter:

                DialogPopUps.deliveryTime(MainActivity.this, new CommonListeners.DeliveryTime() {
                    @Override
                    public void now() {

                        ordersAdapter.filterNow();
                        ordersPendingAdapter.filterNow();
                    }

                    @Override
                    public void morning() {
                        ordersAdapter.filterMorning();
                        ordersPendingAdapter.filterMorning();
                    }

                    @Override
                    public void evening() {
                        ordersAdapter.filterEvening();
                        ordersPendingAdapter.filterEvening();

                    }

                    @Override
                    public void allOrders() {
                        ordersAdapter.filterAll();
                        ordersPendingAdapter.filterAll();

                    }
                });
                break;



            case R.id.tv_all:


                isAllSelected = true;

                tv_all.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_selected));
                tv_pending.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_all_bg));

                tv_all.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_pending.setTextColor(getResources().getColor(R.color.colorLightGrey));



                tv_no_pending_order.setVisibility(View.GONE);
                rv_orders_pending.setVisibility(View.GONE);

                if(orderLists.size()==0){
                    tv_no_accepted_order.setVisibility(View.VISIBLE);
                    rv_orders.setVisibility(View.GONE);
                }else{
                    rv_orders.setVisibility(View.VISIBLE);
                    tv_no_accepted_order.setVisibility(View.GONE);
                }

                break;


            case R.id.tv_pending:

                isAllSelected = false;
                tv_all.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_all_bg));
                tv_pending.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_selected));

                tv_all.setTextColor(getResources().getColor(R.color.colorLightGrey));
                tv_pending.setTextColor(getResources().getColor(R.color.colorPrimary));





                tv_no_accepted_order.setVisibility(View.GONE);
                rv_orders.setVisibility(View.GONE);

                if(orderListPending.size()==0){
                    tv_no_pending_order.setVisibility(View.VISIBLE);
                    rv_orders_pending.setVisibility(View.GONE);
                }else{
                    tv_no_pending_order.setVisibility(View.GONE);
                    rv_orders_pending.setVisibility(View.VISIBLE);
                }

                break;



        }


    }

    @Override
    public void call(final String phone_no) {


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        try {

                            if (ContextCompat.checkSelfPermission(MainActivity.this,
                                    Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            } else {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone_no));
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

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(phone_no).
                setPositiveButton("Call", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();

    }

    @Override
    public void acceptOrder(final String order_id) {





        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        acceptOrderApi(order_id);

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure?").
                setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener)
                .setTitle("Accept Order").show();


    }




    @Override
    public void cancelOrder(final String order_id) {



        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        cancelOrderApi(order_id);

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure?").
                setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener)
                .setTitle("Cancel Order").show();

    }



    @Override
    public void deliverOrder(final String order_id, final String received_amount) {



        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        deliverOrderApi(order_id, received_amount);

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure?").
                setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener)
                .setTitle("Deliver Order").show();

    }

    @Override
    public void acceptOrderSize(int size) {

        circularTextViewAccepted.setText(""+size);
        if(size==0){
            circularTextViewAccepted.setVisibility(View.GONE);
        }else{
            circularTextViewAccepted.setVisibility(View.VISIBLE);

        }


        if(isAllSelected) {

            if (size == 0) {
                rv_orders.setVisibility(View.GONE);
                tv_no_accepted_order.setVisibility(View.VISIBLE);
            } else {
                rv_orders.setVisibility(View.VISIBLE);
                tv_no_accepted_order.setVisibility(View.GONE);
            }
        }




    }

    @Override
    public void pendingOrderSize(int size) {
        circularTextView.setText(""+size);

        if(size==0){
            circularTextView.setVisibility(View.GONE);
        }else{
            circularTextView.setVisibility(View.VISIBLE);
        }


        if(!isAllSelected){

            if(size==0){
                rv_orders_pending.setVisibility(View.GONE);
                tv_no_pending_order.setVisibility(View.VISIBLE);
            }else{
                rv_orders_pending.setVisibility(View.VISIBLE);
                tv_no_pending_order.setVisibility(View.GONE);
            }

        }

    }


    private void acceptOrderApi(String order_id) {

        CommonUtils.showProgressDialog(MainActivity.this,getResources().getString(R.string.please_wait));

        UpdateOrderJson updateOrderJson  = new UpdateOrderJson();
        updateOrderJson.setToken(SharedPrefUtil.getAccessToken(MainActivity.this));
        updateOrderJson.setUser_id(SharedPrefUtil.getUserId(MainActivity.this));
        updateOrderJson.setOrder_id(Integer.parseInt(order_id));

        mainRepository = App.getApp().getMainRepository();
        mainRepository.acceptOrderApi("application/json",updateOrderJson)
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

                        String msg,status;

                        if(value.getMessage().equalsIgnoreCase("Successfully fetched")){
                            msg = "Order accepted";
                            status = "Success";
                        }else{
                            msg = value.getMessage();
                            status = "Error";
                        }


                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:

                                        dialog.dismiss();
                                        orderApi();


                                        break;


                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(msg).
                                setPositiveButton("OK", dialogClickListener).
                                setTitle(status).show();


                    }

                    @Override
                    public void onError(Throwable e) {

                        CommonUtils.hideDialog();

                        CommonUtils.showSnackbar(MainActivity.this, e.getMessage());


                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void cancelOrderApi(String order_id) {

        CommonUtils.showProgressDialog(MainActivity.this,getResources().getString(R.string.please_wait));

        UpdateOrderJson updateOrderJson  = new UpdateOrderJson();
        updateOrderJson.setToken(SharedPrefUtil.getAccessToken(MainActivity.this));
        updateOrderJson.setUser_id(SharedPrefUtil.getUserId(MainActivity.this));
        updateOrderJson.setOrder_id(Integer.parseInt(order_id));

        mainRepository = App.getApp().getMainRepository();
        mainRepository.cancelOrderApi("application/json",updateOrderJson)
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

                        String msg,status;

                        if(value.getMessage().equalsIgnoreCase("Successfully fetched")){
                            msg = "Order cancelled";
                            status = "Success";
                        }else{
                            msg = value.getMessage();
                            status = "Error";
                        }


                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:

                                        dialog.dismiss();
                                        orderApi();


                                        break;


                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(msg).
                                setPositiveButton("OK", dialogClickListener).
                                setTitle(status).show();


                    }

                    @Override
                    public void onError(Throwable e) {

                        CommonUtils.hideDialog();

                        CommonUtils.showSnackbar(MainActivity.this, e.getMessage());


                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void deliverOrderApi(String order_id, String received_amount) {

        CommonUtils.showProgressDialog(MainActivity.this,getResources().getString(R.string.please_wait));

        UpdateOrderJson updateOrderJson  = new UpdateOrderJson();
        updateOrderJson.setToken(SharedPrefUtil.getAccessToken(MainActivity.this));
        updateOrderJson.setUser_id(SharedPrefUtil.getUserId(MainActivity.this));
        updateOrderJson.setOrder_id(Integer.parseInt(order_id));
        updateOrderJson.setReceived_amount(received_amount);

        mainRepository = App.getApp().getMainRepository();
        mainRepository.deliverOrderApi("application/json",updateOrderJson)
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

                        String msg,status;

                        if(value.getMessage().equalsIgnoreCase("Successfully fetched")){
                            msg = "Order delivered";
                            status = "Success";
                        }else{
                            msg = value.getMessage();
                            status = "Error";
                        }


                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:

                                        dialog.dismiss();
                                        orderApi();


                                        break;


                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(msg).
                                setPositiveButton("OK", dialogClickListener).
                                setTitle(status).show();


                    }

                    @Override
                    public void onError(Throwable e) {

                        CommonUtils.hideDialog();

                        CommonUtils.showSnackbar(MainActivity.this, e.getMessage());


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

                    if (ContextCompat.checkSelfPermission(MainActivity.this,
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



    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String update) {

        if (CommonUtils.isConnectedToInternet(MainActivity.this)) {

            orderApi();
        } else {
            CommonUtils.showSnackbar(MainActivity.this, getString(R.string.please_check_your_internet_connection));

        }


    };


/*    @Override
    protected void onResume() {
        super.onResume();

        if (CommonUtils.isConnectedToInternet(MainActivity.this)) {

            orderApi();
        } else {
            CommonUtils.showSnackbar(MainActivity.this, getString(R.string.please_check_your_internet_connection));

        }

    }*/
}

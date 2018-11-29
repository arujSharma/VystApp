package vyst.business.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vyst.business.R;
import vyst.business.adapters.OrdersAdapter;
import vyst.business.adapters.ShopkeeperItemsAdapter;
import vyst.business.app.App;
import vyst.business.interfaces.ItemAvailabilityChangeInterface;
import vyst.business.pojo.CommonResponsePojo;
import vyst.business.pojo.ItemAvaialbilityJson;
import vyst.business.pojo.OrderJson;
import vyst.business.pojo.items.ItemsPojo;
import vyst.business.pojo.orders.Orders;
import vyst.business.repositories.main.MainRepository;
import vyst.business.util.CommonUtils;
import vyst.business.util.SharedPrefUtil;

public class ItemListActivity extends BaseActivity implements View.OnClickListener,ItemAvailabilityChangeInterface {

    ImageView img_back,img_add;
    RecyclerView rv_item_list;
    ShopkeeperItemsAdapter shopkeeperItemsAdapter;

    private Disposable disposable;
    private MainRepository mainRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        init();
    }

    private void init() {

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        img_add= findViewById(R.id.img_add);
        img_add.setOnClickListener(this);

        rv_item_list = findViewById(R.id.rv_item_list);
        shopkeeperItemsAdapter = new ShopkeeperItemsAdapter(ItemListActivity.this,this);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_item_list.setLayoutManager(mLayoutManager);
        rv_item_list.setItemAnimator(new DefaultItemAnimator());
        rv_item_list.setAdapter(shopkeeperItemsAdapter);




    }

    private void itemApi() {


        CommonUtils.showProgressDialog(ItemListActivity.this,getResources().getString(R.string.please_wait));

        OrderJson orderJson  = new OrderJson();
        orderJson.setToken(SharedPrefUtil.getAccessToken(ItemListActivity.this));
        orderJson.setUser_id(SharedPrefUtil.getUserId(ItemListActivity.this));

        mainRepository = App.getApp().getMainRepository();
        mainRepository.itemApi("application/json",orderJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ItemsPojo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;

                    }

                    @Override
                    public void onNext(ItemsPojo value) {

                        //  mainActivityViewInterface.logMessage(TAGMESSAGE,"value "+value.get(0).getName());
                        CommonUtils.hideDialog();

                        shopkeeperItemsAdapter.addingData(value.getData());


                    }

                    @Override
                    public void onError(Throwable e) {

                        CommonUtils.hideDialog();

                        CommonUtils.showSnackbar(ItemListActivity.this, e.getMessage());


                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:

                finish();

                break;


            case R.id.img_add:


                Intent intent = new Intent(getApplication(), ProductDetails.class);
                intent.putExtra("status","add");
                startActivity(intent);



                break;




        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CommonUtils.isConnectedToInternet(ItemListActivity.this)) {

            itemApi();
        } else {
            CommonUtils.showSnackbar(ItemListActivity.this, getString(R.string.please_check_your_internet_connection));

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }


    @Override
    public void changeAvailability(boolean isAvailable,String item_id) {



        CommonUtils.showProgressDialog(ItemListActivity.this,getResources().getString(R.string.please_wait));

        ItemAvaialbilityJson itemAvaialbilityJson  = new ItemAvaialbilityJson();
        itemAvaialbilityJson.setToken(SharedPrefUtil.getAccessToken(ItemListActivity.this));
        itemAvaialbilityJson.setUser_id(SharedPrefUtil.getUserId(ItemListActivity.this));
        itemAvaialbilityJson.setItem_id(item_id);
        itemAvaialbilityJson.setAvailability(isAvailable);

        mainRepository = App.getApp().getMainRepository();
        mainRepository.itemAvailabilityApi("application/json",itemAvaialbilityJson)
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



                    }

                    @Override
                    public void onError(Throwable e) {

                        CommonUtils.hideDialog();

                        CommonUtils.showSnackbar(ItemListActivity.this, e.getMessage());


                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }




}

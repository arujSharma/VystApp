package vyst.business.activites;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Locale;

import vyst.business.R;
import vyst.business.adapters.OrderItemsAdapter;
import vyst.business.pojo.orders.OrderList;


public class ProductPendingDetailsActivity extends AppCompatActivity {
    private TextView tv_name,tv_loc_address,tv_address_id,tv_phone_no,tv_message;
    private String name, loc_address, address_id, phone_no, message;
    public TextView tv_order_no,tv_price,tv_time,tv_del_time,tv_status;
    RecyclerView rv_items;
    OrderItemsAdapter orderItemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_pending_details);
        init();

    }
    public void init(){
        tv_name =  findViewById(R.id.tv_name);
        tv_loc_address =  findViewById(R.id.tv_loc_address);
        tv_address_id =  findViewById(R.id.tv_address_id);
        tv_phone_no = findViewById(R.id.tv_phone_no);
        tv_message=  findViewById(R.id.tv_message);
        tv_order_no = findViewById(R.id.tv_order_no);
        tv_price = findViewById(R.id.tv_price);
        tv_time =  findViewById(R.id.tv_time);
        tv_del_time = findViewById(R.id.tv_del_time);
        tv_status =  findViewById(R.id.tv_status);
        rv_items =  findViewById(R.id.rv_items);

        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        Gson gson = new Gson();
        OrderList order = gson.fromJson(getIntent().getStringExtra("order"), OrderList.class);
        String message = order.getMessage();
        String name = order.getLocationId().getName().substring(0,1).toUpperCase() + order.getLocationId().getName().substring(1);
        String loc_address=(order.getLocationId().getLocationAddress());
        String address_id=("Sector "+order.getLocationId().getLocationId());
        String phone_no=(order.getLocationId().getPhone());
        tv_name.setText(name);
        tv_loc_address.setText(loc_address);
        tv_address_id.setText(address_id);
        tv_phone_no.setText(phone_no);
        tv_message.setText("Message: "+message);

        tv_order_no.setText("Order # "+order.getOrderId());
        tv_price.setText("Rs. "+order.getBill());

        String order_time = order.getOrderTime().substring(0,1).toUpperCase() + order.getOrderTime().substring(1);
        tv_del_time.setText("Delivery Time: "+order_time);

        String status = order.getStatus().substring(0,1).toUpperCase() + order.getStatus().substring(1);
        tv_status.setText(status);

        Long timestamp = order.getCreatedTime();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000L);
        String date = DateFormat.format("EEE, dd MMM, hh:mm aa", cal).toString();

        tv_time.setText("Order Time: "+date);



        orderItemsAdapter = new OrderItemsAdapter(ProductPendingDetailsActivity.this);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ProductPendingDetailsActivity.this);
        rv_items.setLayoutManager(mLayoutManager);
        rv_items.setItemAnimator(new DefaultItemAnimator());
        rv_items.setAdapter(orderItemsAdapter);
        orderItemsAdapter.addingData(order.getProductsId());


    }
}

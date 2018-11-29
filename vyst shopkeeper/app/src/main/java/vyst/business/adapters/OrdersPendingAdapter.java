package vyst.business.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import vyst.business.R;
import vyst.business.activites.OrderDetailsActivity;
import vyst.business.activites.ProductPendingDetailsActivity;
import vyst.business.interfaces.OrdersInterface;
import vyst.business.pojo.orders.OrderList;
import vyst.business.util.CommonUtils;

/**
 * Created by Admin on 4/16/2018.
 */

public class OrdersPendingAdapter extends RecyclerView.Adapter<OrdersPendingAdapter.MyViewHolder> {


    private List<OrderList> orderLists=new ArrayList<>();
    Context context;
    private ArrayList<OrderList> filteredData=new ArrayList<>();
    OrdersInterface ordersInterface;
    Intent intent;

    private OrderItemsAdapter orderItemsAdapter;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView tv_order_no,tv_price,tv_time,tv_del_time,tv_status;
        RecyclerView rv_items;
        Button accept_order,cancel_order,tv_view_order;



        public MyViewHolder(View view) {
            super(view);
            // button_view_details =  view.findViewById(R.id.button_view_details);
            tv_order_no =  view.findViewById(R.id.tv_order_no);
            tv_price =  view.findViewById(R.id.tv_price);
            tv_time =  view.findViewById(R.id.tv_time);
            tv_del_time =  view.findViewById(R.id.tv_del_time);
            tv_status =  view.findViewById(R.id.tv_status);
            rv_items =  view.findViewById(R.id.rv_items);
            tv_view_order=  view.findViewById(R.id.tv_view_order);
            accept_order= view.findViewById(R.id.accept);
            cancel_order= view.findViewById(R.id.cancel);



        }
    }


    public OrdersPendingAdapter(Context context, OrdersInterface ordersInterface) {
        this.context = context;
        this.ordersInterface = ordersInterface;
        intent = new Intent(context,ProductPendingDetailsActivity.class);

    }


    public void addingData(List<OrderList> orderLists) {
        this.orderLists = orderLists;
        filteredData = new ArrayList<>();
        filteredData.addAll(orderLists);

        ordersInterface.pendingOrderSize(orderLists.size());
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_orderpending, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final OrderList order = orderLists.get(position);
        //tv_order_no,tv_price,tv_time,tv_del_time,tv_status;


       holder.tv_order_no.setText("Order # "+order.getOrderId());
        holder.tv_price.setText("Rs. "+order.getBill());

        String order_time = order.getOrderTime().substring(0,1).toUpperCase() + order.getOrderTime().substring(1);
        holder.tv_del_time.setText("Delivery Time: "+order_time);

        String status = order.getStatus().substring(0,1).toUpperCase() + order.getStatus().substring(1);
        holder.tv_status.setText(status);

        Long timestamp = order.getCreatedTime();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000L);
        String date = DateFormat.format("EEE, dd MMM, hh:mm aa", cal).toString();

        holder.tv_time.setText("Order Time: "+date);


        orderItemsAdapter = new OrderItemsAdapter(context);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        holder.rv_items.setLayoutManager(mLayoutManager);
        holder.rv_items.setItemAnimator(new DefaultItemAnimator());
        holder.rv_items.setAdapter(orderItemsAdapter);

        orderItemsAdapter.addingData(order.getProductsId());


        holder.accept_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isConnectedToInternet(context)) {

                    ordersInterface.acceptOrder(order.getOrderId());
                } else {
                    CommonUtils.showSnackbar((Activity) context,
                            context.getString(R.string.please_check_your_internet_connection));

                }


            }
        });
        holder.cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isConnectedToInternet(context)) {

                    ordersInterface.cancelOrder(order.getOrderId());
                } else {
                    CommonUtils.showSnackbar((Activity) context,
                            context.getString(R.string.please_check_your_internet_connection));
                }


            }
        });


        holder.tv_view_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson=new Gson();
                String myJson=gson.toJson(order);

                intent.putExtra("order",myJson);
                context.startActivity(intent);




            }
        });

    }

    @Override
    public int getItemCount() {
        return orderLists.size();
    }



    public void filterNow() {


        orderLists.clear();

        for (OrderList orderList : filteredData) {
            if (orderList.getOrderTime().equalsIgnoreCase("now")

                    ) {
                this.orderLists.add(orderList);
                notifyDataSetChanged();
            }
        }

        ordersInterface.pendingOrderSize(orderLists.size());

        notifyDataSetChanged();

    }

    public void filterMorning() {

        orderLists.clear();

        for (OrderList orderList : filteredData) {
            if (orderList.getOrderTime().equalsIgnoreCase("morning")

                    ) {
                this.orderLists.add(orderList);
                notifyDataSetChanged();
            }
        }

        ordersInterface.pendingOrderSize(orderLists.size());

        notifyDataSetChanged();

    }

    public void filterEvening() {

        orderLists.clear();

        for (OrderList orderList : filteredData) {
            if (orderList.getOrderTime().equalsIgnoreCase("evening")

                    ) {
                this.orderLists.add(orderList);
                notifyDataSetChanged();
            }
        }

        ordersInterface.pendingOrderSize(orderLists.size());
        notifyDataSetChanged();

    }

    public void filterAll() {
        orderLists.clear();
        orderLists.addAll(filteredData);

        ordersInterface.pendingOrderSize(orderLists.size());
        notifyDataSetChanged();
    }





}

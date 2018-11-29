package vyst.business.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import vyst.business.R;
import vyst.business.activites.MainActivity;
import vyst.business.activites.OrderDetailsActivity;
import vyst.business.interfaces.OrdersInterface;
import vyst.business.pojo.orders.OrderList;
import vyst.business.pojo.orders.ProductList;
import vyst.business.pojo.orders.ProductsId;
import vyst.business.util.CommonUtils;

/**
 * Created by Admin on 4/15/2018.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {


    private List<OrderList> orderLists=new ArrayList<>();
    Context context;

    OrdersInterface ordersInterface;
    private OrderItemsAdapter orderItemsAdapter;
    private ArrayList<OrderList> filteredData=new ArrayList<>();
    Intent intent;

//                final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);


//                dialog.setContentView(R.layout.popup_accept_order);


//


//                ImageView close_button;


//                View view;


//                Button tv_accept_order;


//                TextView tv_name,tv_loc_address,tv_address_id,tv_phone_no,tv_message;


//                RelativeLayout rl_phone;


//


//                close_button =  dialog.findViewById(R.id.close_button);


//


//                tv_accept_order =  dialog.findViewById(R.id.tv_accept_order);


//


//                tv_message=  dialog.findViewById(R.id.tv_message);


//


//                view =  dialog.findViewById(R.id.view);


//


//                  String message = order.getMessage();


//                if(message.equalsIgnoreCase("")){


//                    tv_message.setVisibility(View.GONE);


//                }else{


//                    tv_message.setVisibility(View.VISIBLE);


//                    tv_message.setText("Message: "+message);


//                }


//


//


//                tv_name =  dialog.findViewById(R.id.tv_name);


//                tv_loc_address =  dialog.findViewById(R.id.tv_loc_address);


//                tv_address_id =  dialog.findViewById(R.id.tv_address_id);


//                tv_phone_no =  dialog.findViewById(R.id.tv_phone_no);


//                rl_phone =  dialog.findViewById(R.id.rl_phone);



    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView tv_order_no,tv_price,tv_time,tv_del_time,tv_status;
        Button tv_view_order,tv_deliver_order;
        RecyclerView rv_items;



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
            tv_deliver_order=view.findViewById(R.id.tv_deliver_order);

        }
    }


    public OrdersAdapter(Context context,OrdersInterface ordersInterface) {
        this.context = context;
        this.ordersInterface = ordersInterface;
        intent = new Intent(context, OrderDetailsActivity.class);
    }


    public void addingData(List<OrderList> orderLists) {
        this.orderLists = orderLists;

        filteredData = new ArrayList<>();
        filteredData.addAll(orderLists);
        ordersInterface.acceptOrderSize(orderLists.size());
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_order, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final OrderList order = orderLists.get(position);
        //tv_order_no,tv_price,tv_time,tv_del_time,tv_status;

        holder.tv_order_no.setText("Order # "+order.getOrderId());
        holder.tv_price.setText("Rs. "+order.getBill());
        //holder.tv_price1.setText("Price: Rs. "+order.getOthers().getDeliveryCharges());
        //holder.tv_help.setText("Rs. "+order.getOthers().getHelpUs());


        final String order_time = order.getOrderTime().substring(0,1).toUpperCase() + order.getOrderTime().substring(1);
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


        holder.tv_deliver_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isConnectedToInternet(context)) {

                            ordersInterface.deliverOrder(order.getOrderId(),order.getBill());
                        } else {
                            CommonUtils.showSnackbar((Activity) context, context.getString(R.string.please_check_your_internet_connection));

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
//
//                ordersInterface.deliverOrder(order.getOrderId());
//                notifyDataSetChanged();
            //    ActivityCompat.startActivityForResult((Activity) OrdersAdapter,intent,1);

//                rl_phone.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        ordersInterface.call(order.getLocationId().getPhone());
//
//
//
//                    }
//                });
//
//                close_button =  dialog.findViewById(R.id.close_button);
//
//                tv_accept_order =  dialog.findViewById(R.id.tv_accept_order);
//                view =  dialog.findViewById(R.id.view);
//
//                close_button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//
//                view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//
//
//
//                orderDetailsActivity.tv_accept_order.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if (CommonUtils.isConnectedToInternet(context)) {
//
//                            ordersInterface.deliverOrder(order.getOrderId());
//                        } else {
//                            CommonUtils.showSnackbar((Activity) context, context.getString(R.string.please_check_your_internet_connection));
//
//                        }
//
//
//
//
//                    }
//                });

            /*    dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);*/
//
//                dialog.show();
//
//
           }
        });

    }

//    public void acceptOrder(){
//        if(OrderDetailsActivity.token!=""){
//            if (CommonUtils.isConnectedToInternet(context)) {
//                            ordersInterface.deliverOrder(OrderDetailsActivity.token);
//                            OrderDetailsActivity.token="";
//                        } else {
//                            CommonUtils.showSnackbar((Activity) context, context.getString(R.string.please_check_your_internet_connection));
//
//                        }
//
//        }
//    }


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
        ordersInterface.acceptOrderSize(orderLists.size());
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
        ordersInterface.acceptOrderSize(orderLists.size());
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
        ordersInterface.acceptOrderSize(orderLists.size());
        notifyDataSetChanged();

    }

    public void filterAll() {
        orderLists.clear();
        orderLists.addAll(filteredData);
        ordersInterface.acceptOrderSize(orderLists.size());
        notifyDataSetChanged();
    }




}

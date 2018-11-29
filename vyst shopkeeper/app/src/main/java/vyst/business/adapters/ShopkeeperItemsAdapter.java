package vyst.business.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import vyst.business.R;
import vyst.business.activites.ProductDetails;
import vyst.business.interfaces.ItemAvailabilityChangeInterface;
import vyst.business.pojo.items.Datum;
import vyst.business.pojo.orders.OrderList;
import vyst.business.util.CommonUtils;

/**
 * Created by Admin on 4/16/2018.
 */

public class ShopkeeperItemsAdapter extends RecyclerView.Adapter<ShopkeeperItemsAdapter.MyViewHolder> {


    private List<Datum> itemList=new ArrayList<>();
    Context context;
    ItemAvailabilityChangeInterface itemAvailabilityChangeInterface;



    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView txt_item_name;
        public ImageView img_edit;
        public Switch switch_available;



        public MyViewHolder(View view) {
            super(view);

            txt_item_name =  view.findViewById(R.id.txt_item_name);
            img_edit =  view.findViewById(R.id.img_edit);
            switch_available=  view.findViewById(R.id.switch_available);

        }
    }


    public ShopkeeperItemsAdapter(Context context,ItemAvailabilityChangeInterface itemAvailabilityChangeInterface) {
        this.context = context;
        this.itemAvailabilityChangeInterface = itemAvailabilityChangeInterface;
    }


    public void addingData(List<Datum> itemList) {
        this.itemList = itemList;


        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Datum datum = itemList.get(position);

        String item_name = datum.getItemName().substring(0,1).toUpperCase() +datum.getItemName().substring(1);
        holder.txt_item_name.setText(item_name);

       /* boolean availability = true;
        if(datum.isAvailable()!=null){

        }*/

        holder.switch_available.setChecked(datum.isAvailable());

        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("status","edit");

                intent.putExtra("description",datum.getDescription());
                intent.putExtra("discount",datum.getDiscount());
                intent.putExtra("item_id",datum.getItemId());
                intent.putExtra("item_name",datum.getItemName());
                intent.putExtra("price",datum.getPrice());
                intent.putExtra("type",datum.getType());

                intent.putExtra("image",datum.getFileName());


                context.startActivity(intent);

            }
        });


        holder.switch_available.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d("yoo","isChecked "+isChecked);


                if (CommonUtils.isConnectedToInternet(context)) {

                    itemAvailabilityChangeInterface.changeAvailability(isChecked,datum.getItemId());
                } else {
                    buttonView.setChecked(!isChecked);
                    CommonUtils.showSnackbar((Activity) context, context.getString(R.string.please_check_your_internet_connection));

                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }



}

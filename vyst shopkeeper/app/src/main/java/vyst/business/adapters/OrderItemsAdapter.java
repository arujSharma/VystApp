package vyst.business.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import vyst.business.R;
import vyst.business.pojo.orders.OrderList;
import vyst.business.pojo.orders.ProductList;
import vyst.business.pojo.orders.ProductsId;

/**
 * Created by Admin on 4/16/2018.
 */

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.MyViewHolder> {


    private ProductList productLists=new ProductList();
    private List<ProductsId> productsId= new ArrayList<>();
    Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder {



       public TextView tv_item;



        public MyViewHolder(View view) {
            super(view);

          tv_item =  view.findViewById(R.id.tv_item);




        }
    }


    public OrderItemsAdapter(Context context) {
        this.context = context;

    }


    public void addingData(List <ProductsId> productsId) {
       // this.productLists = productLists;
        this.productsId=productsId;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_order_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ProductsId productId = productsId.get(position);
        //tv_order_no,tv_price,tv_time,tv_del_time,tv_status;
       holder.tv_item.setText(productId.getQty()+ " X "+productId.getProductList().getItemName());



    }

    @Override
    public int getItemCount() {
        return productsId.size();
    }


}

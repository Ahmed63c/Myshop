package com.ahmedcancels.myshop.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ahmedcancels.myshop.Interface.ItemClickListner;
import com.ahmedcancels.myshop.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtproductname,txtproductprice,txtproductquantity;

    private ItemClickListner  itemClickListner;




    public CartViewHolder(View itemView) {
        super(itemView);
        txtproductname=itemView.findViewById(R.id.cart_product_name);
        txtproductprice=itemView.findViewById(R.id.cart_product_price);
        txtproductquantity=itemView.findViewById(R.id.cart_product_quantity);




    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v ,getAdapterPosition(),false);
    }


    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}

package com.ahmedcancels.myshop;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmedcancels.myshop.Model.Cart;
import com.ahmedcancels.myshop.ViewHolder.CartViewHolder;
import com.ahmedcancels.myshop.prevalent.prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.view.LayoutInflater.from;

public class CartActivity extends AppCompatActivity {
    private TextView totalprice;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        recyclerView = findViewById(R.id.cartlist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);







    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference cartlistref= FirebaseDatabase.getInstance().getReference().child("cart list");
        FirebaseRecyclerOptions<Cart> options=new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartlistref.child("User View").child(prevalent.currentOnlineUser.getPhone()).child("Products"), Cart.class).build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                        holder.txtproductname.setText(model.getPname());
                        holder.txtproductprice.setText(model.getPrice());
                        holder.txtproductquantity.setText(model.getQuantity());






                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item,viewGroup,false);
                        CartViewHolder holder=new CartViewHolder(view);
                        return  holder;



                    }
                };





        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
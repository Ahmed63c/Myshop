package com.ahmedcancels.myshop;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmedcancels.myshop.Model.Products;
import com.ahmedcancels.myshop.prevalent.prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addtocart;
    private ImageView productimage;
    private TextView productprice,productdescription,productname;
    private String productid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        addtocart=findViewById(R.id.addto_cart);
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingtocartlist();
            }
        });


        productimage=findViewById(R.id.product_image_details);
        productname=findViewById(R.id.product_name_details);
        productprice=findViewById(R.id.product_price_details);
        productdescription=findViewById(R.id.product_description_details);

        productid=getIntent().getStringExtra("pid");
        getproductdetails();


    }

    private void getproductdetails() {
        final DatabaseReference productref= FirebaseDatabase.getInstance().getReference().child("Products");
        productref.child(productid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Products products=dataSnapshot.getValue(Products.class);
                    productname.setText(products.getPname().toString());
                    productname.setText(products.getPrice().toString());
                    productname.setText(products.getDescription().toString());
                    Picasso.get().load(products.getImage()).into(productimage);





                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








    }

    public void addingtocartlist() {

        String  saveCurrentDate,saveCurrentTime;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        final   DatabaseReference  cartlistref=FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String,Object>  cartmap =new HashMap<>();
        //cartmap.put("pid",productid);
        cartmap.put("pname",productname.getText().toString());
        cartmap.put("price",productprice.getText().toString());
        cartmap.put("date",saveCurrentDate.toString());
        cartmap.put("time",saveCurrentTime.toString());
        cartmap.put("quantity","1");
        cartmap.put("dicount","");
        cartlistref.child("User view").child(prevalent.currentOnlineUser.getPhone()).child("Products").child(productid).updateChildren(cartmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            cartlistref.child("Admin view").child(prevalent.currentOnlineUser.getPhone()).child("Products").child(productid)
                                    .updateChildren(cartmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ProductDetailsActivity.this,"added to cart",Toast.LENGTH_SHORT).show();

                                    Intent i=new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                    startActivity(i);

                                }
                            });




                        }
                    }
                });






    }


}

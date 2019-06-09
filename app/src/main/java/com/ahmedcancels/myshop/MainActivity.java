package com.ahmedcancels.myshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ahmedcancels.myshop.Model.Users;
import com.ahmedcancels.myshop.prevalent.prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

   private Button join_btn,login_btn;
   private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        join_btn= (Button) findViewById(R.id.main_join_now_btn);
        login_btn= (Button) findViewById(R.id.main_login_btn);
        Paper.init(this);
        pd=new ProgressDialog(this);

login_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        Intent login= new Intent(MainActivity.this,LoginActivity.class);
        startActivity(login);



    }
});



join_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent  register= new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(register);
    }
});

        String UserPhoneKey = Paper.book().read(prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(prevalent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != "")
        {
            if (!TextUtils.isEmpty(UserPhoneKey)  &&  !TextUtils.isEmpty(UserPasswordKey))
            {
                allowaccess(UserPhoneKey, UserPasswordKey);

                pd.setTitle("Already Logged in");
                pd.setMessage("Please wait.....");
                pd.setCanceledOnTouchOutside(false);
                pd.show();
            }
        }






    }

    private void allowaccess(final String phone, final String password) {


        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child("Users").child(phone).exists())){
                    Users userdata=dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    if (userdata.getPhone().equals(phone)){
                        if(userdata.getPassword().equals(password)){
                            pd.dismiss();
                            Intent home=new Intent(MainActivity.this,HomeActivity.class);
                            prevalent.currentOnlineUser=userdata;
                            startActivity(home);



                        }
                        else {


                            Toast.makeText(MainActivity.this,"Wrong password",Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }



                    }





                }
                else{
                    Toast.makeText(MainActivity.this,"access denied this  phone number is not registered ",Toast.LENGTH_SHORT).show();
                    pd.dismiss();


                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







    }


    }


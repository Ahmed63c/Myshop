package com.ahmedcancels.myshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    private Button createaccount_btn;
    private EditText input_name,input_password,input_phonenumber;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        createaccount_btn=(Button) findViewById(R.id.register_btn);
        input_name=findViewById(R.id.register_name_input);
        input_password=findViewById(R.id.register_password_input);
        input_phonenumber=findViewById(R.id.register_phone_number_input);
        pd =new ProgressDialog(this);



createaccount_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       Createaccount();
    }
});







    }

    private void Createaccount() {


        String name=input_name.getText().toString();
        String phone=input_phonenumber.getText().toString();
        String password=input_password.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this,"please enter your name",Toast.LENGTH_SHORT).show();

        }
        else if  (TextUtils.isEmpty(phone)){


            Toast.makeText(this,"please enter your phone number",Toast.LENGTH_SHORT).show();


        }
      else  if (TextUtils.isEmpty(password)){



            Toast.makeText(this,"please enter your password",Toast.LENGTH_SHORT).show();

        }
      else{
            pd.setTitle("create acount");
            pd.setMessage("please wait......");
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            Validatephonenumber(name,phone,password);

      }



    }

    private void Validatephonenumber(final String name, final String phone,final String password) {

        final DatabaseReference Rootref;
        Rootref=FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child("Users").child(phone).exists())){

                    HashMap<String,Object> userdatamap=new HashMap<>();
                    userdatamap.put("phone",phone);
                    userdatamap.put("name",name);
                    userdatamap.put("password",password);



                    Rootref.child("Users").child(phone).updateChildren(userdatamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"congratges your account crated",Toast.LENGTH_SHORT).show();
                                Intent login = new Intent(RegisterActivity.this ,LoginActivity.class);
                                startActivity(login);

                            }
                            else {
                                pd.dismiss();
                                Toast.makeText(RegisterActivity.this,"network error try again ",Toast.LENGTH_SHORT).show();



                            }





                        }
                    });






                }
                else{

                    Toast.makeText(RegisterActivity.this,"phone is already exist",Toast.LENGTH_SHORT).show();
                    pd.dismiss();

                    Intent main=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(main);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegisterActivity.this,"errrrrrrrrrrrrrr",Toast.LENGTH_SHORT).show();

            }
        });








    }



}

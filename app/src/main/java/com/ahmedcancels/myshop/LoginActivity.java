package com.ahmedcancels.myshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmedcancels.myshop.Model.Users;
import com.ahmedcancels.myshop.prevalent.prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private Button loginbtn;
    private EditText inputphone, inputpassword;
    private TextView Adminlink,Notadminlink;
    private ProgressDialog pd;
    private CheckBox checkboxrememberme;
    private String parentDbName = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        inputpassword=findViewById(R.id.login_password_input);
        inputphone=findViewById(R.id.login_phone_number_input);
        loginbtn=findViewById(R.id.login_btn);
        pd=new ProgressDialog(this);
        checkboxrememberme=findViewById(R.id.checkbox_rememberme);
        Adminlink=findViewById(R.id.admin_link);
        Notadminlink=findViewById(R.id.notadmin_link);
        Paper.init(this);





        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginuser();








            }
        });

        Adminlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginbtn.setText("Login Admin");
                Adminlink.setVisibility(View.INVISIBLE);
                Notadminlink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";


            }
        });
        Notadminlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loginbtn.setText("Login");
                Adminlink.setVisibility(View.VISIBLE);
                Notadminlink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });

    }

    private void loginuser() {
        String phone = inputphone.getText().toString();
        String password = inputpassword.getText().toString();


        if (TextUtils.isEmpty(phone)) {


            Toast.makeText(this, "please enter your phone number", Toast.LENGTH_SHORT).show();


        } else if (TextUtils.isEmpty(password)) {


            Toast.makeText(this, "please enter your password", Toast.LENGTH_SHORT).show();
        }

        else{
            pd.setTitle("Login");
            pd.setMessage("please wait......");
            pd.setCanceledOnTouchOutside(false);
            pd.show();
            allowaccesstoaccount(phone,password);



        }


    }

    public void allowaccesstoaccount(final String phone, final String password) {

        if (checkboxrememberme.isChecked()){
            Paper.book().write(prevalent.UserPhoneKey,phone);
            Paper.book().write(prevalent.UserPasswordKey,password);

        }


        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child(parentDbName).child(phone).exists())){
                    Users userdata=dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                    if (userdata.getPhone().equals(phone)){
                        if(userdata.getPassword().equals(password)){


                            if (parentDbName.equals("Admins")){
                                Toast.makeText(LoginActivity.this, "Welcome Admin, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                pd.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminCatogryAvtivity.class);
                                startActivity(intent);






                            }
                            else if (parentDbName.equals("Users"))
                                {
                                    Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                    pd.dismiss();

                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    prevalent.currentOnlineUser = userdata;
                                    startActivity(intent);
                                }







                        }
                        else {


                            Toast.makeText(LoginActivity.this,"Wrong password",Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }



                    }





                }
                else{
                    Toast.makeText(LoginActivity.this,"access denied this  phone number is not registered ",Toast.LENGTH_SHORT).show();
                    pd.dismiss();


                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







    }

}

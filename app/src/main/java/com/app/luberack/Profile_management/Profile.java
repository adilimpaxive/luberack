package com.app.luberack.Profile_management;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.luberack.Fragments.Email_Verification;
import com.app.luberack.R;

public class Profile extends AppCompatActivity {

    LinearLayout signin,signup;
    LinearLayout signin_back,signup_back;
    TextView changing_text,linearsignin,linearsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        signin=(LinearLayout)findViewById(R.id.linear_signin);
        signup=(LinearLayout)findViewById(R.id.linear_signup);
        signin_back=(LinearLayout)findViewById(R.id.linear_background_signin);
        signup_back=(LinearLayout)findViewById(R.id.linear_background_signup);
        changing_text=(TextView)findViewById(R.id.tv_profile);
        linearsignin=(TextView)findViewById(R.id.tv_signin);
        linearsignup=(TextView)findViewById(R.id.tv_signup);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment f1 = new SignIn();
        fragmentTransaction.replace(R.id.frame_profile, f1, null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signin_back.setVisibility(View.VISIBLE);
                signin_back.setBackgroundColor(Color.parseColor("#c54f4f"));
                signup_back.setVisibility(View.GONE);
                linearsignin.setTextColor(Color.BLACK);
                linearsignup.setTextColor(Color.parseColor("#979797"));
                changing_text.setText("Signin to get started");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment f1 = new SignIn();
                fragmentTransaction.replace(R.id.frame_profile, f1, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup_back.setBackgroundColor(Color.parseColor("#c54f4f"));
                signin_back.setVisibility(View.GONE);
                signup_back.setVisibility(View.VISIBLE);
                linearsignup.setTextColor(Color.BLACK);
                changing_text.setText("Sign up to enjoy this service");
                linearsignin.setTextColor(Color.parseColor("#979797"));
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment f1 = new SignUp();
                fragmentTransaction.replace(R.id.frame_profile, f1, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


    }
}

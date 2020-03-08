package com.app.giftfcard.Profile_management;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.giftfcard.R;

public class ForgetPassword extends AppCompatActivity {
    LinearLayout reset_password,confirm_password;
    LinearLayout signin_back,signup_back;
    TextView changing_text,linearsignin,linearsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        reset_password=(LinearLayout)findViewById(R.id.linear_reset_password);
//        confirm_password=(LinearLayout)findViewById(R.id.linear_confirm_password);
        signin_back=(LinearLayout)findViewById(R.id.linear_background_signin);
        signup_back=(LinearLayout)findViewById(R.id.linear_background_signup);
        linearsignin=(TextView)findViewById(R.id.tv_signin);
        linearsignup=(TextView)findViewById(R.id.tv_signup);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment f1 = new ResetPasword();
        fragmentTransaction.replace(R.id.frame_reset_password, f1, null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signin_back.setVisibility(View.VISIBLE);
                signin_back.setBackgroundColor(Color.parseColor("#c54f4f"));
                signup_back.setVisibility(View.GONE);
                linearsignin.setTextColor(Color.BLACK);
                linearsignup.setTextColor(Color.parseColor("#979797"));
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment f1 = new ResetPasword();
                fragmentTransaction.replace(R.id.frame_reset_password, f1, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
//        confirm_password.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signup_back.setBackgroundColor(Color.parseColor("#c54f4f"));
//                signin_back.setVisibility(View.GONE);
//                signup_back.setVisibility(View.VISIBLE);
//                linearsignup.setTextColor(Color.BLACK);
//                linearsignin.setTextColor(Color.parseColor("#979797"));
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                Fragment f1 = new ConfirmPassword();
//                fragmentTransaction.replace(R.id.frame_reset_password, f1, null);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        });
    }
}

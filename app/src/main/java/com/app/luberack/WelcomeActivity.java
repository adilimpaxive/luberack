package com.app.luberack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.luberack.Fragments.Activity_Vehicles_Details;
import com.app.luberack.Profile_management.SessionManager;
import com.app.luberack.Profile_management.Welcome;

public class WelcomeActivity extends AppCompatActivity {

    TextView tv_continue;
    SessionManager sessionManager;
    RelativeLayout relativeLayout;
    Toolbar toolbar;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Welcome");
        Intent intent = getIntent();
        name = intent.getStringExtra("Name");

        if(name.equals("Welcome")) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment f1 = new Welcome();
            fragmentTransaction.replace(R.id.home_frame, f1, null);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else{
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment f1 = new Activity_Vehicles_Details();
            fragmentTransaction.replace(R.id.home_frame, f1, null);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }



    }
}

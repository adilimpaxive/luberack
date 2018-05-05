package com.app.luberack;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.app.luberack.Fragments.HomeMain;
import com.app.luberack.Fragments.ProfileUpdate;
import com.app.luberack.Fragments.Shops;
import com.app.luberack.Profile_management.SignUp;

public class Home extends AppCompatActivity {

    Toolbar toolbar;
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTextMessage = (TextView) findViewById(R.id.message);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment f1 = new HomeMain();
        fragmentTransaction.replace(R.id.home_frame, f1, null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    toolbar.setTitle("Home");
                    Fragment f1 = new HomeMain();
                    fragmentTransaction.replace(R.id.home_frame, f1, null);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_shops:
                    toolbar.setTitle("Shops");
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    Fragment f2 = new Shops();
                    fragmentTransaction1.replace(R.id.home_frame, f2, null);
                    fragmentTransaction1.addToBackStack(null);
                    fragmentTransaction1.commit();
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle("Profile");
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    Fragment f3 = new ProfileUpdate();
                    fragmentTransaction3.replace(R.id.home_frame, f3, null);
                    fragmentTransaction3.addToBackStack(null);
                    fragmentTransaction3.commit();
                    return true;
            }
            return false;
        }
    };

}

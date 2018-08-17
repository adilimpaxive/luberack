package com.app.luberack;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.luberack.Fragments.HomeMain;
import com.app.luberack.Fragments.HomeOilChange;
import com.app.luberack.Fragments.ProfileUpdate;
import com.app.luberack.Fragments.Shops;

public class Home extends AppCompatActivity {

    Toolbar toolbar;
    private TextView mTextMessage;
    ImageView imageViewProfile,imageViewHome,imageViewOilChange,imageViewBrakes,imageViewEstimate,imageViewShop,imageViewAlignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTextMessage = (TextView) findViewById(R.id.message);
        imageViewProfile = findViewById(R.id.profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment f1 = new HomeMain();
        fragmentTransaction.replace(R.id.home_frame, f1, null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
     //   navigation.getMenu().clear();
     //   navigation.inflateMenu(R.menu.navigation);

        imageViewAlignment = findViewById(R.id.alignment);
        imageViewOilChange = findViewById(R.id.oil_change);
        imageViewBrakes = findViewById(R.id.brakes);
        imageViewEstimate = findViewById(R.id.repair_estimate);
        imageViewShop = findViewById(R.id.shop);
        imageViewHome = findViewById(R.id.home);
        imageViewHome.setImageResource(R.drawable.home_selected);

      //  BottomNavigationViewHelper.disableShiftMode(navigation);//Dont forgot this line
       // navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetImages();
                imageViewProfile.setImageResource(R.drawable.profile_selected);
                Home.this.setTitle("Profile");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment f1 = new ProfileUpdate();
                fragmentTransaction.replace(R.id.home_frame, f1, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetImages();
                imageViewHome.setImageResource(R.drawable.home_selected);
                Home.this.setTitle("Home");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment f1 = new HomeMain();
                fragmentTransaction.replace(R.id.home_frame, f1, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        imageViewAlignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetImages();
                imageViewAlignment.setImageResource(R.drawable.alignment_selected);
                HomeMain.type = "Alignment";
                Home.this.setTitle("Alignment");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment f1 = new HomeOilChange();
                fragmentTransaction.replace(R.id.home_frame, f1, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        imageViewEstimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetImages();
                imageViewEstimate.setImageResource(R.drawable.tyre_estimate_selected);
                HomeMain.type = "Car Tire";
                Home.this.setTitle("Car Tyre");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment f1 = new HomeOilChange();
                fragmentTransaction.replace(R.id.home_frame, f1, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        imageViewShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetImages();
                imageViewShop.setImageResource(R.drawable.shop_selected);
                Home.this.setTitle("Shops");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment f1 = new Shops();
                fragmentTransaction.replace(R.id.home_frame, f1, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        imageViewBrakes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetImages();
                imageViewBrakes.setImageResource(R.drawable.brakes_selected);
                HomeMain.type = "Brakes";
                Home.this.setTitle("Brakes");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment f1 = new HomeOilChange();
                fragmentTransaction.replace(R.id.home_frame, f1, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        imageViewOilChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetImages();
                imageViewOilChange.setImageResource(R.drawable.oil_change_selected);
                HomeMain.type = "Oil Change";
                Home.this.setTitle("Oil Change");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment f1 = new HomeOilChange();
                fragmentTransaction.replace(R.id.home_frame, f1, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
    }

  /*  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_welcome:
                    FragmentTransaction fragmentTransaction0 = getSupportFragmentManager().beginTransaction();
                    toolbar.setTitle("Welcome");
                    Fragment f0 = new HomeMain();
                    fragmentTransaction0.replace(R.id.home_frame, f0, null);
                    fragmentTransaction0.addToBackStack(null);
                    fragmentTransaction0.commit();
                    return true;
                case R.id.navigation_home:
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    toolbar.setTitle("Lets get vehicle information");
                    Fragment f1 = new HomeMain();
                    fragmentTransaction.replace(R.id.home_frame, f1, null);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_shops:
                  //  toolbar.setTitle("Shops");
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    Fragment f2 = new HomeOilChange();
                    HomeMain.type = "Oil Change";
                    fragmentTransaction1.replace(R.id.home_frame, f2, null);
                    fragmentTransaction1.addToBackStack(null);
                    fragmentTransaction1.commit();
                    return true;
                case R.id.allignment:
                  //  toolbar.setTitle("Shops");
                    FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    Fragment f4 = new HomeOilChange();
                    HomeMain.type = "Alignment";
                    fragmentTransaction4.replace(R.id.home_frame, f4, null);
                    fragmentTransaction4.addToBackStack(null);
                    fragmentTransaction4.commit();
                    return true;
                case R.id.brakes:
                 //   toolbar.setTitle("Shops");
                    FragmentTransaction fragmentTransaction5 = getSupportFragmentManager().beginTransaction();
                    Fragment f5 = new HomeOilChange();
                    HomeMain.type = "Brakes";
                    fragmentTransaction5.replace(R.id.home_frame, f5, null);
                    fragmentTransaction5.addToBackStack(null);
                    fragmentTransaction5.commit();
                    return true;
                case R.id.estimate:
                    toolbar.setTitle("Shops");
                    FragmentTransaction fragmentTransaction6 = getSupportFragmentManager().beginTransaction();
                    Fragment f6 = new get_estimate();
                    fragmentTransaction6.replace(R.id.home_frame, f6, null);
                    fragmentTransaction6.addToBackStack(null);
                    fragmentTransaction6.commit();
                    return true;
                case R.id.navigation_profile:
                   // toolbar.setTitle("Profile");
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    Fragment f3 = new HomeOilChange();
                    HomeMain.type = "Car Tire";
                    fragmentTransaction3.replace(R.id.home_frame, f3, null);
                    fragmentTransaction3.addToBackStack(null);
                    fragmentTransaction3.commit();
                    return true;
            }
            return false;
        }
    };*/

    public void SetImages(){
        imageViewHome.setImageResource(R.drawable.home_uns);
        imageViewOilChange.setImageResource(R.drawable.oil_change_uns);
        imageViewBrakes.setImageResource(R.drawable.brakes_uns);
        imageViewProfile.setImageResource(R.drawable.profile_uns);
        imageViewShop.setImageResource(R.drawable.shop_uns);
        imageViewEstimate.setImageResource(R.drawable.tyre_estimate_uns);
        imageViewAlignment.setImageResource(R.drawable.alignment_uns);
    }

}

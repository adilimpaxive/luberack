package com.app.luberack.Profile_management;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.luberack.Fragments.Activity_Vehicles_Details;
import com.app.luberack.Home;
import com.app.luberack.R;

public class Welcome extends Fragment {


    TextView tv_continue,textViewHello;
    SessionManager sessionManager;
    RelativeLayout relativeLayout;
    ImageView imageView;
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.activity_welcome, container, false);
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getContext());

       // setContentView(R.layout.activity_splash);
       // sessionManager=new SessionManager(Welcome.this);

        tv_continue=(TextView) view.findViewById(R.id.tv_continue_to_setup);
        textViewHello = view.findViewById(R.id.tv_hello);
        textViewHello.setText("Hello "+sessionManager.getUserName()+"!");
        imageView = view.findViewById(R.id.right);

        if(Splash.is_signup.equals("No")) {
            tv_continue.setTextColor(R.color.black);
            tv_continue.setText("How can we help you today?");
            imageView.setVisibility(View.INVISIBLE);
        }

        relativeLayout = view.findViewById(R.id.relative);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                if(!Splash.is_signup.equals("No")) {
                    FragmentTransaction fragmentTransaction3 = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment f3 = new Activity_Vehicles_Details(); //ProfileUpdate();
                    fragmentTransaction3.replace(R.id.home_frame, f3, null);
                    fragmentTransaction3.addToBackStack(null);
                    fragmentTransaction3.commit();
                }else{
                    Intent intent = new Intent(getContext(), Home.class);
                    startActivity(intent);
                }

            }
        },SPLASH_DISPLAY_LENGTH);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // toolbar.setTitle("Profile");
               /* FragmentTransaction fragmentTransaction3 = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment f3 = new Activity_Vehicles_Details(); //ProfileUpdate();
                fragmentTransaction3.replace(R.id.home_frame, f3, null);
                fragmentTransaction3.addToBackStack(null);
                fragmentTransaction3.commit();*/
               // return true;
            }
        });

        return view;
    }
}

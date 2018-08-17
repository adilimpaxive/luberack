package com.app.luberack.Profile_management;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.luberack.Fragments.Activity_Vehicles_Details;
import com.app.luberack.R;

public class AfterWelcome extends Fragment {


    TextView tv_continue;
    ImageView imageViewRight;
    SessionManager sessionManager;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.activity_after_welcome, container, false);
        super.onCreate(savedInstanceState);

       // setContentView(R.layout.activity_splash);
       // sessionManager=new SessionManager(Welcome.this);

        tv_continue=(TextView) view.findViewById(R.id.tv_continue_to_setup);
        imageViewRight = view.findViewById(R.id.right);
        imageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // toolbar.setTitle("Profile");
                FragmentTransaction fragmentTransaction3 = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment f3 = new Activity_Vehicles_Details(); //ProfileUpdate();
                fragmentTransaction3.replace(R.id.home_frame, f3, null);
                fragmentTransaction3.addToBackStack(null);
                fragmentTransaction3.commit();
               // return true;

            }
        });

        return view;
    }
}

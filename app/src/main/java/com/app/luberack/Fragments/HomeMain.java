package com.app.giftfcard.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.app.giftfcard.R;

public class HomeMain extends Fragment {
    ImageView im_oil_change,im_brakes,im_alignment,im_getestmimate;
    public static String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_home_main, container, false);

       im_oil_change=view.findViewById(R.id.oil_change);
       im_getestmimate=view.findViewById(R.id.repair_estimate);
        im_brakes=view.findViewById(R.id.brakes);
        im_alignment=view.findViewById(R.id.alignment);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
       im_oil_change.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Fragment f1=new HomeOilChange();
               Bundle b=new Bundle();
               b.putString("Type","Oil Change");
               type = "Oil Change";
               FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
               fragmentTransaction.replace(R.id.home_frame, f1, null);
               fragmentTransaction.addToBackStack(null);
               fragmentTransaction.commit();


           }
       });
        im_brakes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f1=new HomeOilChange();
                type = "Brakes";
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Bundle b=new Bundle();
                b.putString("Type","Brakes");
                fragmentTransaction.replace(R.id.home_frame, f1, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });
        im_alignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f1=new HomeOilChange();
                type = "Alignment";
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Bundle b=new Bundle();
                b.putString("Type","Allignment");
                fragmentTransaction.replace(R.id.home_frame, f1, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });



        im_getestmimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f1=new HomeOilChange();
                type = "Car Tire";
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Bundle b=new Bundle();
                b.putString("Type","Car Tier");
                fragmentTransaction.replace(R.id.home_frame, f1, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });
//        im_alignment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment f1=new HomeOilChange();
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();;
//                fragmentTransaction.replace(R.id.home_frame, f1, null);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//
//
//            }
//        });
//        im_brakes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment f1=new History_Data();
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();;
//                fragmentTransaction.replace(R.id.home_frame, f1, null);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//
//
//            }
//        });
       return  view;
    }

}

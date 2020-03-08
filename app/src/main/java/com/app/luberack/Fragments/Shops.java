package com.app.luberack.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.luberack.R;

public class Shops extends Fragment {
    LinearLayout oil_change,alignment,brakes,car_tire,oil_change_back,alignment_back,car_tire_back,brakes_back;
    TextView tv_oil_change,tv_alignment,tv_brakes,tv_car_tire;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_shops, container, false);
        oil_change=(LinearLayout)view.findViewById(R.id.linear_oil_change);
        alignment=(LinearLayout)view.findViewById(R.id.linear_alignment);
        brakes=(LinearLayout)view.findViewById(R.id.linear_brakes);
        car_tire=(LinearLayout)view.findViewById(R.id.linear_car_tire);

        oil_change_back=(LinearLayout)view.findViewById(R.id.linear_background_oil_change);
        alignment_back=(LinearLayout)view.findViewById(R.id.linear_background_alignment);
        car_tire_back=(LinearLayout)view.findViewById(R.id.linear_background_car_tire);
        brakes_back=(LinearLayout)view.findViewById(R.id.linear_background_brakes);

        tv_oil_change=(TextView)view.findViewById(R.id.tv_oilchange);
        tv_alignment=(TextView)view.findViewById(R.id.tv_alignment);
        tv_brakes=(TextView)view.findViewById(R.id.tv_brakes);
        tv_car_tire=(TextView)view.findViewById(R.id.tv_car_tire);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment f1 = new OilChange();
        fragmentTransaction.replace(R.id.frame_profile, f1, null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        oil_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                oil_change_back.setVisibility(View.VISIBLE);
                oil_change_back.setBackgroundColor(Color.parseColor("#c54f4f"));
                brakes_back.setVisibility(View.GONE);
                car_tire_back.setVisibility(View.GONE);
                alignment_back.setVisibility(View.GONE);
                tv_oil_change.setTextColor(Color.BLACK);
                tv_alignment.setTextColor(Color.parseColor("#979797"));
                tv_brakes.setTextColor(Color.parseColor("#979797"));
                tv_car_tire.setTextColor(Color.parseColor("#979797"));

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment f1 = new OilChange();
                fragmentTransaction.replace(R.id.frame_profile, f1, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        brakes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oil_change_back.setVisibility(View.GONE);
                brakes_back.setVisibility(View.VISIBLE);
                brakes_back.setBackgroundColor(Color.parseColor("#c54f4f"));
                car_tire_back.setVisibility(View.GONE);
                alignment_back.setVisibility(View.GONE);
                tv_brakes.setTextColor(Color.BLACK);
                tv_alignment.setTextColor(Color.parseColor("#979797"));
                tv_oil_change.setTextColor(Color.parseColor("#979797"));
                tv_car_tire.setTextColor(Color.parseColor("#979797"));
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment f1 = new Brakes();
                fragmentTransaction.replace(R.id.frame_profile, f1, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        car_tire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                oil_change_back.setVisibility(View.GONE);
                car_tire_back.setVisibility(View.VISIBLE);
                car_tire_back.setBackgroundColor(Color.parseColor("#c54f4f"));
                brakes_back.setVisibility(View.GONE);
                alignment_back.setVisibility(View.GONE);
                tv_car_tire.setTextColor(Color.BLACK);
                tv_alignment.setTextColor(Color.parseColor("#979797"));
                tv_oil_change.setTextColor(Color.parseColor("#979797"));
                tv_brakes.setTextColor(Color.parseColor("#979797"));;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment f1 = new CarTyre();
                fragmentTransaction.replace(R.id.frame_profile, f1, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        alignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oil_change_back.setVisibility(View.GONE);
                brakes_back.setVisibility(View.GONE);
                car_tire_back.setVisibility(View.GONE);
                alignment_back.setVisibility(View.VISIBLE);
                alignment_back.setBackgroundColor(Color.parseColor("#c54f4f"));

                tv_alignment.setTextColor(Color.BLACK);
                tv_brakes.setTextColor(Color.parseColor("#979797"));
                tv_oil_change.setTextColor(Color.parseColor("#979797"));
                tv_car_tire.setTextColor(Color.parseColor("#979797"));;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment f1 = new Alignment();
                fragmentTransaction.replace(R.id.frame_profile, f1, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return view;
    }

}

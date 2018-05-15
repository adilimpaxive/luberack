package com.app.luberack.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.luberack.R;


public class VehicleDetail extends Fragment {
    TextView click_to_full_view_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_vehicle_detail, container, false);
        click_to_full_view_btn=(TextView)view.findViewById(R.id.click_to_full_view_btn);
        click_to_full_view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentTransaction fragmentTransaction3 = getFragmentManager().beginTransaction();
//                Fragment f3 = new VehicleDetail();
//                fragmentTransaction3.replace(R.id.home_frame, f3, null);
//                fragmentTransaction3.addToBackStack(null);
//                fragmentTransaction3.commit();
            }
        });
        return  view;
    }

}

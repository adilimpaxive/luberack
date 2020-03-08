package com.app.giftfcard.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.giftfcard.Fragments.VehicleDetail;
import com.app.giftfcard.ModelClasses.VehicleData;
import com.app.giftfcard.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Farid Mushtaq on 1/12/2018.
 */

public  class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    List<VehicleData> productList= Collections.emptyList();
    Context _context;

    public VehicleAdapter(Context context, ArrayList<VehicleData> products) {

        inflater = LayoutInflater.from(context);
        this.productList=products;
        this._context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.vehicle_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        VehicleData data = productList.get(position);
        holder.vehicleName.setText(data.getName());
        holder.vehicleModel.setText(data.getLocation());
        holder.vehicleYear.setText(data.getReviews());

        if (data.getImage() != null) {
            if (! data.getImage().equalsIgnoreCase("")) {
                Picasso.with(_context).load(data.getImage()).into(holder.imageView);
            }
        }
        

    }
    @Override
    public int getItemCount() {
        return productList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder  {

        ImageView imageView;
        TextView reviews,vehicleName,vehicleModel,vehicleYear,location,view_shop;
        //TextView rating;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.iv_oil_shop);
          //  location=itemView.findViewById(R.id.tv_shop_address);
          //  reviews = itemView.findViewById(R.id.tv_review);
            vehicleName = itemView.findViewById(R.id.tv_vehicle_name);
            vehicleModel = itemView.findViewById(R.id.tv_vehicle_model);
            vehicleYear = itemView.findViewById(R.id.tv_vehicle_year);
          //  view_shop=itemView.findViewById(R.id.btn_view_shop);

         //   view_shop.setOnClickListener(this);
            
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    VehicleData events = productList.get(pos);
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                 //   AppCompatActivity appCompatActivity = _context.getApplicationContext();
                    FragmentTransaction fragmentTransaction3 = activity.getSupportFragmentManager().beginTransaction();
                    Fragment f3 = new VehicleDetail(); //ProfileUpdate();
                    Bundle b=new Bundle();
                    b.putString("Name",events.getName());
                    b.putString("Model",events.getLocation());
                    b.putString("Year",events.getReviews());
                    b.putString("Size",events.getLat());
                    b.putString("Fwd",events.getType());
                    f3.setArguments(b);
                    fragmentTransaction3.replace(R.id.home_frame, f3, null);
                    fragmentTransaction3.addToBackStack(null);
                    fragmentTransaction3.commit();
                }
            });
        }
    }
}

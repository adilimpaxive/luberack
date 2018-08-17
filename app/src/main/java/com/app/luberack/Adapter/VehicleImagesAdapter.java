package com.app.luberack.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.luberack.Fragments.Activity_Vehicles_Details;
import com.app.luberack.ModelClasses.VehicleData;
import com.app.luberack.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Farid Mushtaq on 1/12/2018.
 */

public  class VehicleImagesAdapter extends RecyclerView.Adapter<VehicleImagesAdapter.MyViewHolder> implements View.OnClickListener{

    private final LayoutInflater inflater;
    List<VehicleData> productList= Collections.emptyList();
    Context _context;
    public static String imgUrl,imgYear,model;

    public VehicleImagesAdapter(Context context, ArrayList<VehicleData> products) {

        inflater = LayoutInflater.from(context);
        this.productList=products;
        this._context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.vehicle_images, parent, false);
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

    @Override
    public void onClick(View view) {

        //VehicleData events = productList.get(pos);
    }


    class MyViewHolder extends RecyclerView.ViewHolder  {

        ImageView imageView;
        TextView reviews,vehicleName,vehicleModel,vehicleYear,location,view_shop;
        LinearLayout linearLayout;
        //TextView rating;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.iv_oil_shop);
          //  location=itemView.findViewById(R.id.tv_shop_address);
          //  reviews = itemView.findViewById(R.id.tv_review);
            vehicleName = itemView.findViewById(R.id.tv_vehicle_name);
            vehicleModel = itemView.findViewById(R.id.tv_vehicle_model);
            vehicleYear = itemView.findViewById(R.id.tv_vehicle_year);
            linearLayout = itemView.findViewById(R.id.linear);
          //  view_shop=itemView.findViewById(R.id.btn_view_shop);

         //   view_shop.setOnClickListener(this);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    VehicleData events = productList.get(pos);
                    imgUrl = events.getImage();
                    imgYear = events.getReviews();
                    model = events.getLocation();
                   // ArrayAdapter<CharSequence> yAdapter = ArrayAdapter.createFromResource(_context, R.array.oil_change_year, android.R.layout.simple_spinner_item);
                    Activity_Vehicles_Details.et_year.setSelection(Activity_Vehicles_Details.yAdapter.getPosition(imgYear));
                    Activity_Vehicles_Details.et_model.setSelection(Activity_Vehicles_Details.mAdapter.getPosition(model));
                    Activity_Vehicles_Details.dialog.dismiss();
                }
            });
        }
    }
}

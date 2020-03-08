package com.app.luberack.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.luberack.ModelClasses.CarTyreData;
import com.app.luberack.R;
import com.app.luberack.map.MapFragment;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Farid Mushtaq on 1/12/2018.
 */

public  class CarTyreAdapter extends RecyclerView.Adapter<CarTyreAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    List<CarTyreData> productList= Collections.emptyList();
    Context _context;

    public CarTyreAdapter(Context context, List<CarTyreData> products) {

        inflater = LayoutInflater.from(context);
        this.productList=products;
        this._context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.oil_change_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        CarTyreData data = productList.get(position);
        holder.shopName.setText(data.getName());
        holder.reviews.setText(data.getReviews());
        holder.location.setText(data.getLocation());
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


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView reviews,shopName,location,view_shop;
        //TextView rating;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.iv_oil_shop);
            location=itemView.findViewById(R.id.tv_shop_address);
            reviews = itemView.findViewById(R.id.tv_review);
            shopName = itemView.findViewById(R.id.tv_shop_name);
            view_shop=itemView.findViewById(R.id.btn_view_shop);

            view_shop.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.e("RecyclerView", "CLICK!");
            // get position
            int pos = getAdapterPosition();

            // check if item still exists
            if(pos != RecyclerView.NO_POSITION) {

                CarTyreData events = productList.get(pos);

                Fragment f = new MapFragment();
                Bundle b = new Bundle();
                b.putString("id", events.getId());
                b.putString("address",events.getLocation());
                b.putString("name", events.getName());
                b.putString("reviews",events.getReviews());
                b.putString("img_url",events.getImage());
                b.putString("lat",events.getLat());
                b.putString("lng",events.getLng());
                b.putString("type",events.getType());

                f.setArguments(b);
                ((AppCompatActivity) _context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.home_frame, f)
                        .addToBackStack("customtag")
                        .commit();

            }
        }
    }
}

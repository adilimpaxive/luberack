package com.app.luberack.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.luberack.ModelClasses.get_estimate_modal;
import com.app.luberack.R;

import java.util.List;

/**
 * Created by ahmad on 5/2/2018.
 */

    public class HistoryAdapter extends RecyclerView.Adapter {
        List common_repair_es;
        TextView comm,his;
        public class MyViewHolder extends RecyclerView.ViewHolder {



            public MyViewHolder(View view) {
                super(view);
                //initialize textviews
                comm = (TextView) view.findViewById(R.id.comm);
                his = (TextView) view.findViewById(R.id.his);
            }
        }

        public HistoryAdapter(List common_repair_es) {
            this.common_repair_es = common_repair_es;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_text_data, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            //get item at position
            get_estimate_modal people = (get_estimate_modal) common_repair_es.get(position);
            //setText from item to textview
//        holder.comm.setText(people.getStr1());
//        holder.his.setText(people.getStr2());
            comm.setText(people.getStr1());
            his.setText(people.getStr2());

        }

        @Override
        public int getItemCount() {
            return common_repair_es.size();
        }
    }


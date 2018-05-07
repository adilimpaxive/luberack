package com.app.luberack.ModelClasses;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.luberack.Adapter.get_estimate_adapter;
import com.app.luberack.ModelClasses.get_estimate_modal;
import com.app.luberack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmad on 5/2/2018.
 */

public class History_Data extends Fragment {
    private List cList = new ArrayList<>();
    private RecyclerView recyclerView1,recyclerView2;
    private get_estimate_adapter vAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView1 = (RecyclerView) view.findViewById(R.id.details1);
        recyclerView2 = (RecyclerView) view.findViewById(R.id.details2);


        //initialize Adapterclass with List
        vAdapter = new get_estimate_adapter(cList);
        //set layout Manager of recyclerview
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView1.setLayoutManager(mLayoutManager);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(vAdapter);//add adapter to recyclerview
        preparedataData();//call function to add data to recyclerview
        vAdapter = new get_estimate_adapter(cList);
        //set layout Manager of recyclerview
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView2.setLayoutManager(mLayoutManager1);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(vAdapter);//add adapter to recyclerview
        preparedataData();//call function to add data to recyclerview


        return  view;
    }

    //function to add data to List
    private void preparedataData() {
        get_estimate_modal name = new get_estimate_modal("Abraham Lincoln", "1809");
        cList.add(name);
        name = new get_estimate_modal("John F. Kennedy", "1917");
        cList.add(name);
        name = new get_estimate_modal("Bill Gates ", "1955");
        cList.add(name);
        name = new get_estimate_modal("Muhammad Ali ", "1942");
        cList.add(name);
        name = new get_estimate_modal("Christopher Columbus ", "1451");
        cList.add(name);
        name = new get_estimate_modal("George Orwell", "1903");
        cList.add(name);
        name = new get_estimate_modal("Charles Darwin ", "1809");
        cList.add(name);
        name = new get_estimate_modal("Elvis Presley", "1935");
        cList.add(name);
        name = new get_estimate_modal("Albert Einstein", "1879");
        cList.add(name);
        name = new get_estimate_modal("Queen Victoria", "1819");
        cList.add(name);
        name = new get_estimate_modal("Jawaharlal Nehru", "1889");
        cList.add(name);
        name = new get_estimate_modal("Leonardo da Vinci", "1452");
        cList.add(name);
        name = new get_estimate_modal("Pablo Picasso", "1881");
        cList.add(name);
        name = new get_estimate_modal("Vincent Van Gogh", "1853");
        cList.add(name);
        name = new get_estimate_modal("Thomas Edison", "1847");
        cList.add(name);
        name = new get_estimate_modal("Henry Ford", "1863");
        cList.add(name);
        name = new get_estimate_modal("Michael Jordan", "1963");
        cList.add(name);


        //notify datasetChanged to show the added items to the list in recyclerview
        vAdapter.notifyDataSetChanged();
    }
}

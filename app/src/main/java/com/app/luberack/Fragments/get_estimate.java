package com.app.luberack.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import com.app.luberack.Adapter.get_estimate_adapter;
import com.app.luberack.ModelClasses.get_estimate_modal;
import com.app.luberack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmad on 5/2/2018.
 */

public class get_estimate extends Fragment {
    private List cList = new ArrayList<>();
    private RecyclerView recyclerView;
    private get_estimate_adapter vAdapter;
    String Make,Year,Model;
    Spinner et_make, et_year, et_model;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_get_estimate, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        et_make = view.findViewById(R.id.et_make);
        et_year = view.findViewById(R.id.et_year);
        et_model = view.findViewById(R.id.et_model);

        //////////////////////////
        ///////////////Honda
        /////////////////////////
        final List<String> honda = new ArrayList<String>();
        honda.add("Select model");
        honda.add("Accord");
        honda.add("Civic");
        honda.add("Civic Del Sol");
        honda.add("Prelude");

        //////////////////////////
        ///////////////Acura
        /////////////////////////
        final List<String> acura = new ArrayList<String>();
        acura.add("Select model");
        acura.add("Integra");
        acura.add("Legend");
        acura.add("Vigor");





        et_make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Make= String.valueOf(parent.getItemAtPosition(position));
                if(Make.equals("Acura"))
                {
                    et_model.setEnabled(true);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item,acura);
                    adapter.setDropDownViewResource(R.layout.spinner_item);
                    et_model.setAdapter(adapter);
                }
                if(Make.equals("Honda"))
                {
                    et_model.setEnabled(true);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item,honda);
                    adapter.setDropDownViewResource(R.layout.spinner_item);
                    et_model.setAdapter(adapter);
                }
//                Toast.makeText(getContext(), "dddsfsdffsf"+Make, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        et_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Year= String.valueOf(parent.getItemAtPosition(position));
//               Toast.makeText(getContext(), Year, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        et_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Model= String.valueOf(parent.getItemAtPosition(position));
//                Toast.makeText(getContext(), Model, Toast.LENGTH_SHORT).show();
                ((TextView) et_model.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //initialize Adapterclass with List
        vAdapter = new get_estimate_adapter(cList);
        //set layout Manager of recyclerview
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(vAdapter);//add adapter to recyclerview
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

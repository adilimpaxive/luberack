package com.app.giftfcard.Fragments;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.giftfcard.Adapter.get_estimate_adapter;
import com.app.giftfcard.ModelClasses.get_estimate_modal;
import com.app.giftfcard.Profile_management.SessionManager;
import com.app.giftfcard.R;
import com.app.giftfcard.utility.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    ProgressDialog sweetProgressDialog;
    SessionManager sessionManager;
    Button buttonSave;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_get_estimate, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        et_make = view.findViewById(R.id.et_make);
        et_year = view.findViewById(R.id.et_year);
        et_model = view.findViewById(R.id.et_model);
        buttonSave = view.findViewById(R.id.buttonPanel);

        sweetProgressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        sweetProgressDialog.setMessage("Create Vehicle...");
        sweetProgressDialog.setCancelable(false);
        sessionManager = new SessionManager(getContext());

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



        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   signup();
            }
        });


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


    void signup() {
        sweetProgressDialog.show();

        AndroidNetworking.upload(Config.URL)

                .addMultipartParameter("tag", "insertVehicle")
                .addMultipartParameter("user_id",sessionManager.getUserID())
                .addMultipartParameter("cmp_name", et_make.getSelectedItem().toString())
                .addMultipartParameter("year", et_year.getSelectedItem().toString())
                .addMultipartParameter("model", et_model.getSelectedItem().toString())
                .addMultipartParameter("size", "133")
                .addMultipartParameter("fwd", "fwd")
                .setTag("uploadTest")
                //  .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (sweetProgressDialog.isShowing()) {
                            sweetProgressDialog.dismiss();
                        }
                        try {
                            Log.e("tag", "response " + response);
                            if (response != null) {
                                JSONObject jObj = new JSONObject(response);
                                JSONArray jsonArray=new JSONArray();
                                int success = jObj.getInt("success");
                                if (success == 1) {
                                    Toast.makeText(getContext(), "Vehicle added successfully", Toast.LENGTH_LONG).show();

                                    // session.saveUserID(temp.getString("user_id"));
                                    //          session.createLoginSession(email,contact,password);
                                    //          session.savePassword(password);
                                   // onSignupSuccess();
                                } else {
                                    // Error occurred. Get the error
                                    // message
                                    String errorMsg = jObj.getString("error_msg");
                                  //  onSignupFailed(errorMsg);
                                }
                            }

                        } catch (JSONException e) {
                            if (sweetProgressDialog.isShowing()) {
                                sweetProgressDialog.dismiss();
                            }
                            Log.i("myTag", e.toString());
                            Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        if (sweetProgressDialog.isShowing()) {
                            sweetProgressDialog.dismiss();
                        }
                        Toast.makeText(getContext(), "Sign Up failed, try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}

package com.app.luberack.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.luberack.Adapter.VehicleAdapter;
import com.app.luberack.Adapter.VehicleImagesAdapter;
import com.app.luberack.Adapter.ViewPagerAdapter;
import com.app.luberack.Adapter.get_estimate_adapter;
import com.app.luberack.Home;
import com.app.luberack.ModelClasses.AlignmentData;
import com.app.luberack.ModelClasses.VehicleData;
import com.app.luberack.MyEvent;
import com.app.luberack.Profile_management.SessionManager;
import com.app.luberack.Profile_management.UserInteractionListener;
import com.app.luberack.R;
import com.app.luberack.utility.Config;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ahmad on 5/2/2018.
 */

public class Activity_Vehicles_Details extends Fragment implements com.app.luberack.Profile_management.UserInteractionListener {
    private List cList = new ArrayList<>();
    private RecyclerView recyclerView,recyclerViewAlert;
    VehicleAdapter adapterTop;
    ViewPagerAdapter viewPagerAdapter;
    AlignmentData data;
    ArrayList<VehicleData> oilChangeDataList;
    private get_estimate_adapter vAdapter;
    String Make,Year,Model;
    Spinner et_make,sp_vin;
    public static Spinner et_year,et_model;
    ProgressDialog sweetProgressDialog;
    SessionManager sessionManager;
    EditText etEngineSize,etFw,etVin;
    Button buttonSave,buttonNext,buttonGo;
    public int select=0;
    public int start = 1,startModel=1;
    VehicleImagesAdapter adapterImage;
   // AlignmentData data;
    ArrayList<VehicleData> vehicleImagesDataList;
    public static AlertDialog.Builder alertDialog;
    public static Dialog dialog;
    public static ArrayAdapter<CharSequence> nAdapter,mAdapter,yAdapter;
    ViewPager viewPager;
    private boolean userIsInteracting;
    private UserInteractionListener userInteractionListener;
    int c=0,show_dialouge=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.activity_vehicles_details, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        et_make = view.findViewById(R.id.et_make);
        et_year = view.findViewById(R.id.et_year);
        et_model = view.findViewById(R.id.et_model);
        etEngineSize = view.findViewById(R.id.et_engine_size);
        etFw = view.findViewById(R.id.et_fra_wd);
        etVin = view.findViewById(R.id.et_vin);
        buttonSave = view.findViewById(R.id.buttonPanel);
        buttonNext = view.findViewById(R.id.buttonNext);
        buttonGo = view.findViewById(R.id.btnGo);
        sp_vin = view.findViewById(R.id.sp_vin);
        viewPager = view.findViewById(R.id.cra_iv);

      //  et_year.setEnabled(false);

        spinnerVin();

        sweetProgressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        sweetProgressDialog.setMessage("Create Vehicle...");
        sweetProgressDialog.setCancelable(false);
        sessionManager = new SessionManager(getContext());


        getActivity().setTitle("Add Vehicle");

        etVin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  ShowVin();
            }
        });

        oilChangeDataList=new ArrayList<>();
        vehicleImagesDataList = new ArrayList<>();


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

        final List<String> ford = new ArrayList<String>();
        ford.add("Select model");
        ford.add("F-150");

        final List<String> plymouth = new ArrayList<String>();
        plymouth.add("Select model");
        plymouth.add("Breeze");


        retrieveAlignedShops();

         this.setUserInteractionListener(userInteractionListener);

        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sp_vin.getSelectedItem().toString().equals("VIN(Optional)")){
                    Toast.makeText(getContext(),"Choose VIN!",Toast.LENGTH_LONG).show();
                }else {
                    ShowVin("Yes");
                    et_year.setSelection(0);
                   // getVehicleVin(sp_vin.getSelectedItem().toString());
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Home.class);
                startActivity(intent);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etEngineSize.getText().toString().isEmpty() || etEngineSize.getText().toString().length()==0){
                    Toast.makeText(getContext(),"Alert: Engine size required!",Toast.LENGTH_LONG).show();
                    etEngineSize.setFocusable(true);
                    return;
                }
                if(etFw.getText().toString().isEmpty() || etFw.getText().toString().length()==0){
                    Toast.makeText(getContext(),"Alert: Fwd/awd/rwd required!",Toast.LENGTH_LONG).show();
                    etFw.setFocusable(true);
                    return;
                }
                if(et_year.getSelectedItem().toString().equals("Select Year")){
                    Toast.makeText(getContext(),"Alert: Select year!",Toast.LENGTH_LONG).show();
                    return;
                }
                signup();
            }
        });


        et_make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(select==0 && start==0) {
                    Make = String.valueOf(parent.getItemAtPosition(position));
                    et_year.setSelection(0);
                    ShowVin("No");
                }
                if(start==parent.getCount()) start=0;

//                Toast.makeText(getContext(), "dddsfsdffsf"+Make, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                start=0;
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
                et_year.setSelection(0);

               // if(select==0 && startModel==0) {
                  //  getVehicleYears(Make,Model);
                   // ShowVin("No");
               // }
              //  if(startModel==parent.getCount()) startModel=0;
//                Toast.makeText(getContext(), Model, Toast.LENGTH_SHORT).show();
              //  ((TextView) et_model.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                startModel=0;
            }
        });


        //initialize Adapterclass with List
        vAdapter = new get_estimate_adapter(cList);
        //set layout Manager of recyclerview
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

       // recyclerView.setAdapter(vAdapter);//add adapter to recyclerview
       // preparedataData();//call function to add data to recyclerview

        getVehicleCompany();

        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Users location result
        if (requestCode == 123 && resultCode == RESULT_OK) {

        }
    }


    void signup() {
        sweetProgressDialog.show();

        AndroidNetworking.upload(Config.URL)

                .addMultipartParameter("tag", "insertVehicle")
                .addMultipartParameter("user_id",sessionManager.getUserID())
                .addMultipartParameter("cmp_name", et_make.getSelectedItem().toString())
                .addMultipartParameter("year", et_year.getSelectedItem().toString())
                .addMultipartParameter("model", et_model.getSelectedItem().toString())
                .addMultipartParameter("size", etEngineSize.getText().toString())
                .addMultipartParameter("fwd", etFw.getText().toString())
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
                                    retrieveAlignedShops();
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


    private void retrieveAlignedShops() {
        oilChangeDataList=new ArrayList<>();
        sweetProgressDialog.setMessage("Fetching Vehicle...");
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (sweetProgressDialog.isShowing()) {
                    sweetProgressDialog.dismiss();
                }
                try {
                    Log.e("taggg", "response " + response);
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                   /*// jsonArray.length();
                    jsonArray.getJSONObject(0).getString("user_name");*/

                    if (success == 1) {
                        /*JSONObject temp = jObj.getJSONObject("home");
                        Log.e("tag", "" + temp);*/
                        //    Boolean restricted = temp.getBoolean("restricted");
                        JSONArray homesArray = jObj.getJSONArray("list");
                        for (int i = 0; i < homesArray.length(); i++) {
                            JSONObject homeObj = homesArray.getJSONObject(i);

                            oilChangeDataList.add(new VehicleData(homeObj.getString("id"), homeObj.getString("image"), homeObj.getString("vehicleName"), homeObj.getString("vehicleModel"), homeObj.getString("vehicleYear"), homeObj.getString("engine_size"), homeObj.getString("fwd_awd_rwd"), homeObj.getString("vehicleYear")));

                        }
                        adapterTop = new VehicleAdapter(getContext(), oilChangeDataList);
                        viewPagerAdapter = new ViewPagerAdapter(getContext(), oilChangeDataList);

                        recyclerView.setAdapter(adapterTop);

                        MyEvent myEvent = new MyEvent(oilChangeDataList);
                        EventBus.getDefault().post(myEvent);

//                        viewPager.setAdapter(viewPagerAdapter);
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(), "No vehicle exist!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    if (sweetProgressDialog.isShowing()) {
                        sweetProgressDialog.dismiss();
                    }
                    Log.i("myTag", e.toString());
                    Toast.makeText(getContext(), "No vehicle exist!", Toast.LENGTH_SHORT).show();
                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("myTag", error.toString());
                        if (sweetProgressDialog.isShowing()) {
                            sweetProgressDialog.dismiss();
                        }
                        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to getFeatured url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "getVehicle");
                params.put("id", sessionManager.getUserID());


                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myReq.setShouldCache(false);
        queue.add(myReq);
        sweetProgressDialog.show();
    }

    public void ShowVin(final String vin){
//        if(vin.equalsIgnoreCase("yes"))
//        {
//            alertDialog = new AlertDialog.Builder(getContext());
//            View convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_vehicle_images, null);
//            alertDialog.setView(convertView);
//            //alertDialog.setTitle(getContext().getResources().getString(R.string.memberList));
//            dialog = alertDialog.create();
//            recyclerViewAlert = (RecyclerView) convertView.findViewById(R.id.RecyclerView);
//            recyclerViewAlert.setLayoutManager(new LinearLayoutManager(getContext()));
//            recyclerViewAlert.setHasFixedSize(true);
//            getVehicleVin(sp_vin.getSelectedItem().toString());
//            dialog.show();
//        }
//        else
//        {
//            getVehicleModel(Make);
//        }
        alertDialog = new AlertDialog.Builder(getContext());
        View convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_vehicle_images, null);
        alertDialog.setView(convertView);
        //alertDialog.setTitle(getContext().getResources().getString(R.string.memberList));
        dialog = alertDialog.create();
        recyclerViewAlert = (RecyclerView) convertView.findViewById(R.id.RecyclerView);
        recyclerViewAlert.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAlert.setHasFixedSize(true);

        if(vin.equals("No")) {
            getVehicleModel(Make);
            dialog.show();
           // retrieveImages();
        }else {
            getVehicleVin(sp_vin.getSelectedItem().toString());
            dialog.show();
        }
    }

    public void spinnerVin(){
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item);
        arrayAdapter.add("VIN(Optional)");
        arrayAdapter.add("12345678911121314");
        arrayAdapter.add("12345678915161718");
        arrayAdapter.add("12345678920212223");
        arrayAdapter.add("12345678924252627");
        arrayAdapter.add("12345678928293031");
        sp_vin.setAdapter(arrayAdapter);
    }


    private void getVehicleVin(final String vin) {
        vehicleImagesDataList = new ArrayList<>();
        sweetProgressDialog.setMessage("Fetching Vehicle...");
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (sweetProgressDialog.isShowing()) {
                    sweetProgressDialog.dismiss();
                }
                try {
                    Log.e("tag", "response " + response);
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                   /*// jsonArray.length();
                    jsonArray.getJSONObject(0).getString("user_name");*/

                    if (success == 1) {
                        et_model.setEnabled(false);
                       // et_year.setEnabled(false);
                        et_make.setEnabled(false);
                        select=1;
                        /*JSONObject temp = jObj.getJSONObject("home");
                        Log.e("tag", "" + temp);*/
                        //    Boolean restricted = temp.getBoolean("restricted");
                        JSONArray homesArray = jObj.getJSONArray("list");
                        String name[] = new String[homesArray.length()];
                        String model[] = new String[homesArray.length()];
                        String year[] = new String[homesArray.length()];
                        for (int i = 0; i < homesArray.length(); i++) {
                            JSONObject homeObj = homesArray.getJSONObject(i);
                            Make = homeObj.getString("cmp_name");
                            Model = homeObj.getString("model");
                            Year = homeObj.getString("year");

                            name[i] = homeObj.getString("cmp_name");
                            model[i] = homeObj.getString("model");
                            year[i] = homeObj.getString("year");

                            nAdapter = new ArrayAdapter<CharSequence>(getContext(),R.layout.spinner_item,name);
                            mAdapter = new ArrayAdapter<CharSequence>(getContext(),R.layout.spinner_item,model);
                         //   yAdapter = new ArrayAdapter<CharSequence>(getContext(),R.layout.spinner_item,year);

                           et_make.setAdapter(nAdapter);
                          // et_year.setAdapter(yAdapter);
                           et_model.setAdapter(mAdapter);
                            vehicleImagesDataList.add(new VehicleData(homeObj.getString("id"), homeObj.getString("image"), Make, homeObj.getString("model"), homeObj.getString("year"), homeObj.getString("year"), homeObj.getString("year"), homeObj.getString("year")));
                        }
                        adapterImage = new VehicleImagesAdapter(getContext(), vehicleImagesDataList);
                        recyclerViewAlert.setAdapter(adapterImage);

                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        //Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    if (sweetProgressDialog.isShowing()) {
                        sweetProgressDialog.dismiss();
                    }
                    Log.i("myTag", e.toString());
                    Toast.makeText(getContext(), "No vehicle exist!", Toast.LENGTH_SHORT).show();
                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("myTag", error.toString());
                        if (sweetProgressDialog.isShowing()) {
                            sweetProgressDialog.dismiss();
                        }
                        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to getFeatured url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "getVehicleByVin");
                params.put("id", vin);


                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myReq.setShouldCache(false);
        queue.add(myReq);
        sweetProgressDialog.show();
    }


    private void getVehicleCompany() {

        sweetProgressDialog.setMessage("Fetching Vehicle...");
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (sweetProgressDialog.isShowing()) {
                    sweetProgressDialog.dismiss();
                }
                try {
                    Log.e("tag", "response " + response);
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                   /*// jsonArray.length();
                    jsonArray.getJSONObject(0).getString("user_name");*/

                    if (success == 1) {

                        JSONArray homesArray = jObj.getJSONArray("list");
                        String vehicleCompanies[] = new String[homesArray.length()];
                        for (int i = 0; i < homesArray.length(); i++) {
                            JSONObject homeObj = homesArray.getJSONObject(i);
                            vehicleCompanies[i] = homeObj.getString("cmp_name");
                        }
                        nAdapter = new ArrayAdapter<CharSequence>(getContext(),R.layout.spinner_item,vehicleCompanies);
                      //  et_make.setSelected(false);  // must
                     //   et_make.setSelection(0,true);  //must
                        et_make.setAdapter(nAdapter);
                        start=homesArray.length();

                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    if (sweetProgressDialog.isShowing()) {
                        sweetProgressDialog.dismiss();
                    }
                    Log.i("myTag", e.toString());
                    Toast.makeText(getContext(), "No vehicle exist!", Toast.LENGTH_SHORT).show();
                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("myTag", error.toString());
                        if (sweetProgressDialog.isShowing()) {
                            sweetProgressDialog.dismiss();
                        }
//                        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to getFeatured url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "VehicleCompany");
               // params.put("id", vin);


                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myReq.setShouldCache(false);
        queue.add(myReq);
        sweetProgressDialog.show();
    }

    private void getVehicleModel(final String model) {
        vehicleImagesDataList = new ArrayList<>();
        sweetProgressDialog.setMessage("Fetching Vehicle Model...");
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (sweetProgressDialog.isShowing()) {
                    sweetProgressDialog.dismiss();
                }
                try {
                    Log.e("tag", "response " + response);
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                   /*// jsonArray.length();
                    jsonArray.getJSONObject(0).getString("user_name");*/

                    if (success == 1) {

                        JSONArray homesArray = jObj.getJSONArray("list");
                        String vehicleCompanies[] = new String[homesArray.length()+1];
                        String vehicleYears[] = new String[homesArray.length()+1];
                        for (int i = 0; i < homesArray.length(); i++) {
                            JSONObject homeObj = homesArray.getJSONObject(i);

                            if(i==0){
                                vehicleCompanies[i] ="Select Model";
                                vehicleYears[i] = "Select Year";
                            }

                            vehicleCompanies[i+1] = homeObj.getString("model");
                            vehicleYears[i+1] = homeObj.getString("year");
                            vehicleImagesDataList.add(new VehicleData(homeObj.getString("id"), homeObj.getString("image"), Make, homeObj.getString("model"), homeObj.getString("year"), homeObj.getString("year"), homeObj.getString("year"), homeObj.getString("year")));
                        }

                    adapterImage = new VehicleImagesAdapter(getContext(), vehicleImagesDataList);
                        startModel=homesArray.length();
                        VehicleImagesAdapter.modelClick="1";
                    recyclerViewAlert.setAdapter(adapterImage);
                        mAdapter = new ArrayAdapter<CharSequence>(getContext(),R.layout.spinner_item,vehicleCompanies);
                        et_model.setAdapter(mAdapter);
                     //   yAdapter = new ArrayAdapter<CharSequence>(getContext(),R.layout.spinner_item,vehicleYears);
                        et_model.setAdapter(mAdapter);
                    //    et_year.setAdapter(yAdapter);

                        et_model.setEnabled(true);
                       // et_year.setEnabled(true);

                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    if (sweetProgressDialog.isShowing()) {
                        sweetProgressDialog.dismiss();
                    }
                    Log.i("myTag", e.toString());
                    Toast.makeText(getContext(), "No vehicle exist!", Toast.LENGTH_SHORT).show();
                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("myTag", error.toString());
                        if (sweetProgressDialog.isShowing()) {
                            sweetProgressDialog.dismiss();
                        }
                        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to getFeatured url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "VehicleModel");
                 params.put("id", model);


                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myReq.setShouldCache(false);
        queue.add(myReq);
        sweetProgressDialog.show();
    }


    private void getVehicleYears(final String make,final String model) {
        vehicleImagesDataList = new ArrayList<>();
        sweetProgressDialog.setMessage("Fetching Vehicle Years...");
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (sweetProgressDialog.isShowing()) {
                    sweetProgressDialog.dismiss();
                }
                try {
                    Log.e("tag", "response " + response);
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                   /*// jsonArray.length();
                    jsonArray.getJSONObject(0).getString("user_name");*/

                    if (success == 1) {

                        JSONArray homesArray = jObj.getJSONArray("list");
                        String vehicleCompanies[] = new String[homesArray.length()+1];
                        String vehicleYears[] = new String[homesArray.length()+1];
                        for (int i = 0; i < homesArray.length(); i++) {
                            JSONObject homeObj = homesArray.getJSONObject(i);

                            if(i==0)vehicleYears[i] = "Select Year";

                            vehicleCompanies[i+1] = homeObj.getString("model");

                            vehicleYears[i+1] = homeObj.getString("year");

                            vehicleImagesDataList.add(new VehicleData(homeObj.getString("id"), homeObj.getString("image"), Make, homeObj.getString("model"), homeObj.getString("year"), homeObj.getString("year"), homeObj.getString("year"), homeObj.getString("year")));
                        }

                      //  adapterImage = new VehicleImagesAdapter(getContext(), vehicleImagesDataList);
                      //  startModel=homesArray.length();
                      //  recyclerViewAlert.setAdapter(adapterImage);
                      //  mAdapter = new ArrayAdapter<CharSequence>(getContext(),R.layout.spinner_item,vehicleCompanies);
                      //  et_model.setAdapter(mAdapter);
                        yAdapter = new ArrayAdapter<CharSequence>(getContext(),R.layout.spinner_item,vehicleYears);
                        et_year.setAdapter(yAdapter);

                       // et_model.setEnabled(true);
                        et_year.setEnabled(true);

                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    if (sweetProgressDialog.isShowing()) {
                        sweetProgressDialog.dismiss();
                    }
                    Log.i("myTag", e.toString());
                    Toast.makeText(getContext(), "No vehicle exist!", Toast.LENGTH_SHORT).show();
                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("myTag", error.toString());
                        if (sweetProgressDialog.isShowing()) {
                            sweetProgressDialog.dismiss();
                        }
                        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to getFeatured url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "VehicleYear");
                params.put("id", make);
                params.put("year", model);


                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myReq.setShouldCache(false);
        queue.add(myReq);
        sweetProgressDialog.show();
    }
    public void setUserInteractionListener(UserInteractionListener userInteractionListener) {
        this.userInteractionListener = userInteractionListener;
    }
    @Override
    public void onUserInteraction() {
        //  super.onUserInteraction();
        userIsInteracting = true;
    }


}

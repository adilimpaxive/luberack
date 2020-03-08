package com.app.giftfcard.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.giftfcard.Adapter.BrakesAdapter;
import com.app.giftfcard.ModelClasses.BrakesData;
import com.app.giftfcard.Profile_management.SessionManager;
import com.app.giftfcard.R;
import com.app.giftfcard.utility.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ahmad on 5/7/2018.
 */

public class brakes_frag extends Fragment {


    Spinner et_make, et_year, et_model;
    EditText et_engine_size, et_fra_wd;
    Button panel_Pluin;
    SessionManager sessionManager;
    String service="Brakes service";

    BrakesAdapter adapterTop;
    BrakesData data;
    ArrayList<BrakesData> oilChangeDataList;
    RecyclerView recyclerViewFeatured;
    LinearLayoutManager layoutManager;
    private ProgressDialog sweetProgressDialog;


    String Make,Year,Model, engine,FRA;
    String min_price;
    String max_price;
    int pos;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home_oil_brackes, container, false);
        et_make = view.findViewById(R.id.et_make);
        et_year = view.findViewById(R.id.et_year);
        et_model = view.findViewById(R.id.et_model);
        et_engine_size=view.findViewById(R.id.et_engine_size);
        panel_Pluin=view.findViewById(R.id.buttonPanel);


        sweetProgressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        sweetProgressDialog.setMessage(String.format(getResources().getString(R.string.retr)));
        sweetProgressDialog.setCancelable(false);
        oilChangeDataList=new ArrayList<>();
        recyclerViewFeatured = view.findViewById(R.id.oil_change_recycler);
        recyclerViewFeatured.setLayoutManager(new LinearLayoutManager(getContext()));
        retrieveOilShops();

        et_engine_size.setText(service);

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
                //Toast.makeText(getContext(), Year, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        et_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Model= String.valueOf(parent.getItemAtPosition(position));
               // Toast.makeText(getContext(), Model, Toast.LENGTH_SHORT).show();
                ((TextView) et_model.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        panel_Pluin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SaveDate();

//                onSigninSuccess();

            }
        });



        return view;

    }

    private void SaveDate()
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                if (progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
                try {
                    Log.i("tag", "response " + response);
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        JSONObject temp = jObj.getJSONObject("charges");
                        min_price=temp.getString("min_price");
                        max_price=temp.getString("max_price");
                        Log.i("val",""+min_price+""+max_price);
                        onSigninSuccess();
                    }

                } catch (JSONException e) {
//                    if (progressDialog.isShowing()) {
//                        progressDialog.dismiss();
//                    }
                    Log.i("myTag", e.toString());
                    Toast.makeText(getContext(), "Parsing error", Toast.LENGTH_SHORT).show();

                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("myTag", error.toString());
//                        if (progressDialog.isShowing()) {
//                            progressDialog.dismiss();
//                        }
                        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "costEstimate");
                params.put("cmp_name", Make);
                params.put("year", Year);
                params.put("model", Model);
                params.put("service", "Brakes");
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myReq.setShouldCache(false);
        queue.add(myReq);
//        progressDialog.show();
    }
    private void onSigninSuccess() {
        Bundle bundle=new Bundle();
        Fragment f=new Appointment();
        bundle.putString("brakes",service);
        bundle.putString("brakes_make",Make);
        bundle.putString("brakes_model",Model);
        bundle.putString("brakes_year",Year);
        bundle.putString("min_price",min_price);
        bundle.putString("max_price",max_price);
        bundle.putString("oil_code","10003");
        f.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_frame, f, null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    private void onSigninFailed(String errorMsg) {
//        if (progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
        Toast.makeText(getContext(), ""+errorMsg, Toast.LENGTH_SHORT).show();
    }

    private void retrieveOilShops() {
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
                        /*JSONObject temp = jObj.getJSONObject("home");
                        Log.e("tag", "" + temp);*/
                        //    Boolean restricted = temp.getBoolean("restricted");
                        JSONArray homesArray = jObj.getJSONArray("shops");
                        for (int i = 0; i < homesArray.length(); i++) {
                            JSONObject homeObj = homesArray.getJSONObject(i);

                            oilChangeDataList.add(new BrakesData(homeObj.getString("id"), homeObj.getString("img_url"), homeObj.getString("name"), homeObj.getString("address"), homeObj.getString("reviews"), homeObj.getString("lat"), homeObj.getString("type"), homeObj.getString("lng")));


                        }
                        adapterTop = new BrakesAdapter(getContext(), oilChangeDataList);

                        recyclerViewFeatured.setAdapter(adapterTop);
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
                    Toast.makeText(getContext(), "Parsing error", Toast.LENGTH_SHORT).show();

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
                params.put("tag", "showAllShops");
                params.put("type", "Brakes");
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
}
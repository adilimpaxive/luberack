package com.app.luberack.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.app.luberack.Profile_management.SessionManager;
import com.app.luberack.R;
import com.app.luberack.utility.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeOilChange extends Fragment {
    Spinner et_make, et_year, et_model;
    EditText et_engine_size, et_fra_wd;
    Button panel_Pluin;
    SessionManager sessionManager;
    String service="Oil Change Service";


    String Make,Year,Model, engine,FRA;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home_oil_change, container, false);

        et_make = view.findViewById(R.id.et_make);
        et_year = view.findViewById(R.id.et_year);
        et_model = view.findViewById(R.id.et_model);
        et_engine_size=view.findViewById(R.id.et_engine_size);
        panel_Pluin=view.findViewById(R.id.buttonPanel);

        et_engine_size.setText(service);




        et_make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 Make= String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(getContext(), "dddsfsdffsf"+Make, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        et_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Year= String.valueOf(parent.getItemAtPosition(position));
               Toast.makeText(getContext(), Year, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        et_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Model= String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(getContext(), Model, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        panel_Pluin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                SaveDate();

                onSigninSuccess();

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
                    Log.e("tag", "response " + response);
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
//                    jsonArray.length();
//                    jsonArray.getJSONObject(0).getString("user_name");


//                    Log.d("fname", EmailHolder);
                    if (success == 1) {
//                        JSONObject temp = jObj.getJSONObject("user");
//                        Log.e("tag", "" + temp);
                        //    Boolean restricted = temp.getBoolean("restricted");
                        Toast.makeText(getContext(), "User Successfully registered ,now login.", Toast.LENGTH_SHORT).show();


//                        sessionManager.savePassword(PasswordHolder);
//                        sessionManager.saveUserEmail(EmailHolder);
                        onSigninSuccess();

                    } else if (success == 0) {
                        Toast.makeText(getContext(), "User already exist.", Toast.LENGTH_SHORT).show();

                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        onSigninFailed(errorMsg);
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
                params.put("tag", "register");
                params.put("first_name", Make);
                params.put("second_name", Year);
                params.put("email", Model);
                params.put("password", engine);
                params.put("password", FRA);
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
        bundle.putString("oil_change",service);
        bundle.putString("oil_make",Make);
        bundle.putString("oil_model",Model);
        bundle.putString("oil_year",Year);
        bundle.putString("oil_code","10002");
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
}
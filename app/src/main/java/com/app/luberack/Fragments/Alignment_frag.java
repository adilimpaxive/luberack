package com.app.luberack.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.app.luberack.Profile_management.SessionManager;
import com.app.luberack.R;
import com.app.luberack.utility.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ahmad on 5/7/2018.
 */

public class Alignment_frag extends Fragment {


    Spinner et_make, et_year, et_model;
    EditText et_engine_size, et_fra_wd;
    Button panel_Pluin;
    SessionManager sessionManager;
    String service="Alignment";

    String Make,Year,Model, engine,FRA;
    String min_price;
    String max_price;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_home_oil_aligment, container, false);
        et_make = view.findViewById(R.id.et_make);
        et_year = view.findViewById(R.id.et_year);
        et_model = view.findViewById(R.id.et_model);
        et_engine_size=view.findViewById(R.id.et_engine_size);
        panel_Pluin=view.findViewById(R.id.buttonPanel);


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
                ((TextView) et_model.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
                // Toast.makeText(getContext(), Model, Toast.LENGTH_SHORT).show();
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
                    Log.e("tag", "response " + response);
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
                params.put("service", "Alignment");
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
        bundle.putString("ali_change",service);
        bundle.putString("ali_make",Make);
        bundle.putString("ali_model",Model);
        bundle.putString("ali_year",Year);
        bundle.putString("min_price",min_price);
        bundle.putString("max_price",max_price);
        bundle.putString("oil_code","10004");
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
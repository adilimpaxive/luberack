package com.app.luberack.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.luberack.Home;
import com.app.luberack.Profile_management.SessionManager;
import com.app.luberack.R;
import com.app.luberack.utility.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahmad on 5/10/2018.
 */

public class Activity_Vehicles_Details extends AppCompatActivity {

    Spinner et_make, et_year, et_model;
    Button btn_save;
    SessionManager session;
    private ProgressDialog sweetProgressDialog;
    String Make,Year,Model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles_details);

        et_make = findViewById(R.id.et_make);
        et_year = findViewById(R.id.et_year);
        et_model = findViewById(R.id.et_model);
        btn_save=findViewById(R.id.btn_save);

        sweetProgressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        sweetProgressDialog.setMessage(String.format(getResources().getString(R.string.signin)));
        sweetProgressDialog.setCancelable(false);


        et_make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Make= String.valueOf(parent.getItemAtPosition(position));
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session.saveCompName(Make);
                session.saveYear(Year);
                session.saveModel(Model);

//                detailsSave();
            }
        });
    }

    private void detailsSave() {
        RequestQueue queue = Volley.newRequestQueue(this);

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
                        JSONObject temp = jObj.getJSONObject("user");
                        Log.e("tag", "" + temp);
                        //    Boolean restricted = temp.getBoolean("restricted");


                        //  String password= temp.getString("user_password");
                        //    session.savePassword(password);
                        // saving user details in preferences


                        //Opening map
                        onSaveSuccess();
                        //}
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(Activity_Vehicles_Details.this, "" + errorMsg.toString(), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    if (sweetProgressDialog.isShowing()) {
                        sweetProgressDialog.dismiss();
                    }
                    Log.i("myTag", e.toString());
                    Toast.makeText(Activity_Vehicles_Details.this, "" + e.toString(), Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(Activity_Vehicles_Details.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "login");
//                params.put("email", Uemail);
//                params.put("password", Upassword);
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

    private void onSaveSuccess() {
        if (sweetProgressDialog.isShowing()) {
            sweetProgressDialog.dismiss();
        }

        Intent intent = new Intent(this, Home.class);
        // Add new Flag to start new Activity
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        this.finish();
    }
}

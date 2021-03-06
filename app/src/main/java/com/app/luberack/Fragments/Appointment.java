package com.app.luberack.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by ahmad on 5/8/2018.
 */

public class Appointment extends Fragment {

    EditText et_make, et_year, et_model, et_need_for_car, et_date, et_time, et_user_name, et_user_email, et_data_phone, et_estimate;
    String make, m_year, model, need_for_car, m_date, time, user_name, user_email, data_phone;
    Button btn_appoint,dialogButtoncancel;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    DatePickerDialog.OnDateSetListener date;
    SessionManager sessionManager;
    String min_price;
    String max_price;
    String app_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        et_make = view.findViewById(R.id.et_make);
        et_year = view.findViewById(R.id.et_year);
        et_model = view.findViewById(R.id.et_model);
        et_need_for_car = view.findViewById(R.id.et_need_for_car);
        et_date = view.findViewById(R.id.et_date);
        et_time = view.findViewById(R.id.et_time);
        et_user_name = view.findViewById(R.id.user_name);
        et_user_email = view.findViewById(R.id.user_email);
        et_data_phone = view.findViewById(R.id.data_phone);
//        et_estimate = view.findViewById(R.id.et_estimate);

        btn_appoint = view.findViewById(R.id.btn_appoint);

        ///////////////////
        ////////Get Bundle data
        /////////////////////
        sessionManager=new SessionManager(getContext());

        et_user_name.setText(sessionManager.getUserName());
        et_user_email.setText(sessionManager.getUserEmail());


        ///////////////
        ///////End
        //////////////////


        ////////////////////
        ////////Date Picker
        ////////////////////////
        calendar = Calendar.getInstance();


        Bundle b = getArguments();
        if (b != null) {
            // handle your code here.

            String oil_code = b.getString("oil_code");

            if (oil_code.equals("10002")) {
                String oilModel = b.getString("oil_model");
                String oilMake = b.getString("oil_make");
                String oilYear = b.getString("oil_year");
                min_price = b.getString("min_price");
                max_price = b.getString("max_price");
                String oil_change_service = b.getString("oil_change");
                et_make.setText(oilMake);
                et_year.setText(oilYear);
                et_model.setText(oilModel);
                et_need_for_car.setText(oil_change_service);
//                et_estimate.setText(min_price + "$-" + max_price + "$");


            }
           else if (oil_code.equals("10003")) {
                String oilModel = b.getString("brakes_model");
                String oilMake = b.getString("brakes_make");
                String oilYear = b.getString("brakes_year");
                min_price = b.getString("min_price");
                max_price = b.getString("max_price");
                String oil_change_service = b.getString("oil_change");
                et_make.setText(oilMake);
                et_year.setText(oilYear);
                et_model.setText(oilModel);
                et_need_for_car.setText(b.getString("brakes"));
//                et_estimate.setText(min_price + "$-" + max_price + "$");


            }
           else if (oil_code.equals("10004")) {
                String oilModel = b.getString("ali_model");
                String oilMake = b.getString("ali_make");
                String oilYear = b.getString("ali_year");
                min_price = b.getString("min_price");
                max_price = b.getString("max_price");
                String oil_change_service = b.getString("oil_change");
                et_make.setText(oilMake);
                et_year.setText(oilYear);
                et_model.setText(oilModel);
                et_need_for_car.setText(b.getString("ali_change"));
//                et_estimate.setText(min_price + "$-" + max_price + "$");


            }

        }
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        ///////////////////
        ///////////End
        //////////////////
        ///////////////////////
        ///////////////TIme Picker
        ////////////////////////////
        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        et_time.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }


        });
        ///////////////////
        ////////////End
        //////////////////


        btn_appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make = et_make.getText().toString();
                model = et_model.getText().toString();
                m_year = et_year.getText().toString();
                m_date = et_date.getText().toString();
                time = et_time.getText().toString();
                need_for_car = et_need_for_car.getText().toString();
                user_name = et_user_name.getText().toString();
                user_email = et_user_email.getText().toString();
                data_phone = et_data_phone.getText().toString();

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.custom_dialougebox_fragment);
                dialog.setTitle("Title...");

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Your expected price is "+min_price + "$-" + max_price + "$");
                ImageView image = (ImageView) dialog.findViewById(R.id.image);

                if(validate()) {
                    Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    Button dialogButtoncancel = dialog.findViewById(R.id.dialogButtoncancel);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            SaveDate();
                        }
                    });
                    dialogButtoncancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            }
        });



        return view;
    }

    ////////////////////////
    ///////////
    /////////////////////////
    private boolean validate() {
        boolean valid = true;

        if (m_date.isEmpty()) {
            et_date.setError("Please Enter Data");

            valid = false;
        } else {
            et_date.setError(null);
        }

        if (time.isEmpty()) {
            et_time.setError("Please Enter Time");

            valid = false;
        } else {
            et_time.setError(null);
        }
        if (data_phone.isEmpty()) {
            et_data_phone.setError("Please Enter Phone");

            valid = false;
        } else {
            et_data_phone.setError(null);
        }


        return valid;
    }
    ///////////////////
    //////////Date Picker method
    ////////////////
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_date.setText(sdf.format(calendar.getTime()));
    }
    //////////////////////////////
    ///////////end
    ///////////////////////////

    private void SaveDate() {
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
//                        JSONObject temp = jObj.getJSONObject("service");
////                        Log.e("tag", "" + temp);
//                            String restricted = temp.getString("app_id");
                        Toast.makeText(getContext(), "Appointment Registered.", Toast.LENGTH_SHORT).show();
//                        Log.i("if","asd"+restricted);

//                        sessionManager.savePassword(PasswordHolder);
//                        sessionManager.saveUserEmail(EmailHolder);
                        onSigninSuccess();

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
                params.put("tag", "makeAppointment");
                params.put("cmp_name", make);
                params.put("year", m_year);
                params.put("model", model);
                params.put("date", m_date);
                params.put("time", time);
                params.put("name", user_name);
                params.put("phone", data_phone);
                params.put("email", user_email);
                params.put("service", need_for_car);
                params.put("user_id",sessionManager.getUserID());
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
        Toast.makeText(getContext(), "Thanks!. You will get notification soon about your appointment", Toast.LENGTH_SHORT).show();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment f1 = new HomeMain();
        fragmentTransaction.replace(R.id.home_frame, f1, null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    private void onSigninFailed(String errorMsg) {
//        if (progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
        Toast.makeText(getContext(), "Something is missing!" + errorMsg, Toast.LENGTH_SHORT).show();
    }

}

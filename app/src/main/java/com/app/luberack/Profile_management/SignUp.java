package com.app.giftfcard.Profile_management;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.app.giftfcard.Fragments.Email_Verfication_code;
import com.app.giftfcard.R;
import com.app.giftfcard.map.SignupMapFragment;
import com.app.giftfcard.utility.AlertDialogManager;
import com.app.giftfcard.utility.Config;
import com.app.giftfcard.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class SignUp extends Fragment {

    EditText email,passsword,username,confirm_password,et_city;
    Button btn_signup;
    private ProgressDialog sweetProgressDialog;
    private AlertDialogManager alert;
    SessionManager session;
    String Uname,Uemail,Upassword,Uconfirm_password;
    AlertDialog alertDialog;
    String lat,lng;
    String address;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sign_up, container, false);
        email=view.findViewById(R.id.et_email);
        passsword=view.findViewById(R.id.et_password);
        username=view.findViewById(R.id.et_username);
        confirm_password=view.findViewById(R.id.et_confirm_password);
        et_city=view.findViewById(R.id.et_city);
        btn_signup=view.findViewById(R.id.btn_create);
        session = new SessionManager(getContext());
        alert = new AlertDialogManager();
        sweetProgressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        sweetProgressDialog.setMessage(String.format(getResources().getString(R.string.signup)));
        sweetProgressDialog.setCancelable(false);
        if (container != null) {
            container.removeAllViews();
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            String getGEmail = bundle.getString("email");
            String GetGName = bundle.getString("name");
            username.setText(GetGName);
            email.setText(getGEmail);
        }

        et_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isNetworkAvailable(getActivity())) {
                    if (Utility.isLocationEnabled(getActivity())) {
                        SignupMapFragment signupMapFragment = new SignupMapFragment();
                        signupMapFragment.setTargetFragment(SignUp.this, 123);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.frame_profile, signupMapFragment)
                                .addToBackStack("customtag")
                                .commit();
                    } else {
                        showLocationAlertDialog(getContext());
                    }
                } else {
                    alert.showAlertDialog(getContext(), "No Network Available", "Please turn on Internet Connectivity", false);
                }

            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    //Save in database
                    if (Utility.isNetworkAvailable(getActivity())) {
                        signup();
                        //userSignup();
                    } else {
                        alert.showAlertDialog(getContext(), "No Network Available", "Please turn on Internet Connectivity", false);
                    }
                } else {
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Users location result
        if (requestCode == 123 && resultCode == RESULT_OK) {
            address = data.getStringExtra("useraddress");
            lat = data.getStringExtra("userlat");
            lng = data.getStringExtra("userlng");
            et_city.setText(address);
        }

    }
    //Validating data
    private boolean validate() {
        boolean valid = true;
        Uname = username.getText().toString().trim();
        Uemail = email.getText().toString().trim();
        Upassword = passsword.getText().toString().trim();
        Uconfirm_password=confirm_password.getText().toString().trim();

        if (Uname.isEmpty()) {
            username.setError("Please Enter user name");

            valid = false;
        } else {
            username.setError(null);
        }


        if (Uemail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(Uemail).matches()) {
            email.setError("Please Enter Email");
            valid = false;
        } else {
            email.setError(null);
        }


        if (Upassword.isEmpty() || Upassword.length() < 4) {
            passsword.setError("Password length must be at least 4");
            valid = false;
        } else {
            passsword.setError(null);
        }
//        if (Uconfirm_password.equals(Upassword)) {
//            confirm_password.setError("Password doesn't match.");
//            valid = false;
//        } else {
//            confirm_password.setError(null);
//        }


        return valid;
    }

    void signup() {
        sweetProgressDialog.show();
        if(sweetProgressDialog.isShowing())
        {
            sweetProgressDialog.dismiss();
            onSignupSuccess();
        }


        AndroidNetworking.upload(Config.URL)

                .addMultipartParameter("tag", "register")
                .addMultipartParameter("email", Uemail)
                .addMultipartParameter("password", Upassword)
                .addMultipartParameter("name", Uname)
                .addMultipartParameter("address", address)
                 .addMultipartParameter("lat", lat)
                .addMultipartParameter("lng", lng)
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
                                    Toast.makeText(getContext(), "Please check the code to verify your mail", Toast.LENGTH_LONG).show();

                                    // session.saveUserID(temp.getString("user_id"));
                                    //          session.createLoginSession(email,contact,password);
                                    //          session.savePassword(password);
                                    onSignupSuccess();
                                } else {
                                    // Error occurred. Get the error
                                    // message
                                    String errorMsg = jObj.getString("error_msg");
                                    onSignupFailed(errorMsg);
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

    //Opening payment activity on successful signup
    private void onSignupSuccess() {
        if (sweetProgressDialog.isShowing()) {
            sweetProgressDialog.dismiss();
        }
//        Intent intent = new Intent(getActivity(), Home.class);
//        // Add new Flag to start new Activity
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        getActivity().finish();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment f1 = new Email_Verfication_code();
        Bundle b=new Bundle();
        b.putString("Email",Uemail);
        b.putString("Password",Upassword);

        f1.setArguments(b);
        fragmentTransaction.replace(R.id.frame_profile, f1, null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        Fragment f1 = new SignIn();
//        fragmentTransaction.replace(R.id.frame_profile, f1, null);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
    }

    private void onSignupFailed(String errorMsg) {
        if (sweetProgressDialog.isShowing()) {
            sweetProgressDialog.dismiss();
        }

        alert.showAlertDialog(getContext(), "Signup failed", "" + errorMsg, false);
    }
    public void showLocationAlertDialog(final Context context) {
        alertDialog = new AlertDialog.Builder(context).create();
        // Setting Dialog Title
        alertDialog.setTitle(context.getResources().getString(R.string.gps_network_not_enabled));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(myIntent, 1);
                //get gps
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            }
        });

        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
    }


    private void userSignup() {
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
                        Toast.makeText(getContext(), "Please check the code to verify your mail", Toast.LENGTH_LONG).show();

                        // session.saveUserID(temp.getString("user_id"));
                        //          session.createLoginSession(email,contact,password);
                        //          session.savePassword(password);
                        onSignupSuccess();
                    } else {
                        // Error occurred. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        onSignupFailed(errorMsg);
                    }

                } catch (JSONException e) {
                    if (sweetProgressDialog.isShowing()) {
                        sweetProgressDialog.dismiss();
                    }
                    Log.i("myTag", e.toString());
                    Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(getContext(), "" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "register");
                params.put("email", Uemail);
                params.put("password", Upassword);
                params.put("name", Uname);
                params.put("address", address);
                params.put("lat", lat);
                params.put("lng", lng);
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

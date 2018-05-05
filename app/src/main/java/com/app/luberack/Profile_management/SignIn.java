package com.app.luberack.Profile_management;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.luberack.Home;
import com.app.luberack.R;
import com.app.luberack.utility.AlertDialogManager;
import com.app.luberack.utility.Config;
import com.app.luberack.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends Fragment {

    TextView tv_forgot_password;
    EditText email,passsword;
    Button login;
    private ProgressDialog sweetProgressDialog;
    private AlertDialogManager alert;
    SessionManager session;
    String Uemail,Upassword;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sign_in, container, false);
        tv_forgot_password=view.findViewById(R.id.tv_forgot_password);
        email=view.findViewById(R.id.et_email);
        passsword=view.findViewById(R.id.et_password);
        login=view.findViewById(R.id.btn_login);
        session = new SessionManager(getContext());
        alert = new AlertDialogManager();
        sweetProgressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        sweetProgressDialog.setMessage(String.format(getResources().getString(R.string.signup)));
        sweetProgressDialog.setCancelable(false);
        if (container != null) {
            container.removeAllViews();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (validate()) {
                        if (Utility.isNetworkAvailable(getActivity())) {
                            //Checking credentials from server
                            userLoign();
//                            onSigninSuccess();
                        } else {
                            alert.showAlertDialog(getContext(), "No Network Available", "Please turn on Internet Connectivity", false);
                        }
                    }
                }

        });
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent in=new Intent(getContext(),ForgetPassword.class);
               startActivity(in);
            }
        });
        return view;
    }

    //Validating data
    private boolean validate() {
        boolean valid = true;
        Uemail = email.getText().toString().trim();
        Upassword = passsword.getText().toString().trim();

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

        return valid;
    }

    //Checking credentials from server
    private void userLoign() {
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
                        JSONObject temp = jObj.getJSONObject("user");
                        Log.e("tag", "" + temp);
                        //    Boolean restricted = temp.getBoolean("restricted");


                        String user_id = temp.getString("user_id");
                        String email = temp.getString("email");
                        //  String password= temp.getString("user_password");
                        //    session.savePassword(password);
                        // saving user details in preferences
                        session.saveUserID(user_id);
                        session.saveUserName(temp.getString("name"));
                        session.saveAddress(temp.getString("address"));
                        session.saveCity(temp.getString("address"));
                        session.saveLat(temp.getString("lat"));
                        session.saveLng(temp.getString("lng"));
                        session.createLoginSession(email);

                        //Opening map
                        onSigninSuccess();
                        //}
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        onSigninFailed(errorMsg);
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
                params.put("tag", "login");
                params.put("email", Uemail);
                params.put("password", Upassword);
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
    private void onSigninSuccess() {
        if (sweetProgressDialog.isShowing()) {
            sweetProgressDialog.dismiss();
        }

            Intent intent = new Intent(getActivity(), Home.class);
            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();




    }

    private void onSigninFailed(String errorMsg) {
        if (sweetProgressDialog.isShowing()) {
            sweetProgressDialog.dismiss();
        }
        alert.showAlertDialog(getContext(), "Signin Failed", "" + errorMsg, false);
    }
}
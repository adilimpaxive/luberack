package com.app.luberack.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.app.luberack.Profile_management.SessionManager;
import com.app.luberack.Profile_management.Splash;
import com.app.luberack.R;
import com.app.luberack.WelcomeActivity;
import com.app.luberack.utility.AlertDialogManager;
import com.app.luberack.utility.Config;
import com.app.luberack.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahmad on 5/10/2018.
 */

public class Email_Verfication_code extends Fragment {
    EditText et_code;
    TextView et_code_text;
    Button btn_code_verify;
    String text_code;
    private ProgressDialog sweetProgressDialog;
    private AlertDialogManager alert;
    SessionManager session;
    String email,password;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enter_code, container, false);
        et_code=view.findViewById(R.id.et_code);
        et_code_text=view.findViewById(R.id.et_code_text);
        btn_code_verify=view.findViewById(R.id.btn_code_verify);
        session = new SessionManager(getContext());

        btn_code_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    //Save in database
                    if (Utility.isNetworkAvailable(getActivity())) {
                        sendCode();
                    } else {
                        alert.showAlertDialog(getContext(), "No Network Available", "Please turn on Internet Connectivity", false);
                    }
                } else {
                }
            }
        });
        Bundle b=getArguments();
        email = b.getString("Email");
        password = b.getString("Password");
        et_code_text.setText(email);




        return view;
    }

    private boolean validate() {
        boolean valid = true;
        text_code = et_code.getText().toString().trim();



        if (text_code.isEmpty() ) {
            et_code.setError("Please Enter Code");
            valid = false;
        } else {
            et_code.setError(null);
        }
        return valid;
    }
    //Checking credentials from server
    private void sendCode() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                if (sweetProgressDialog.isShowing()) {
//                    sweetProgressDialog.dismiss();
//                }
                try {
                    Log.e("tag", "response " + response);
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");
                   /*// jsonArray.length();
                    jsonArray.getJSONObject(0).getString("user_name");*/
                    if (success.equals("Sign up successfully.")) {
                        userLoign();
                        //onSigninSuccess();
                        //}
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        onCodeFailed(errorMsg);
                    }

                } catch (JSONException e) {
                    Log.i("myTag", e.toString());
                    Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();

                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("myTag", error.toString());
                        Toast.makeText(getContext(), "" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "confirmCode");
                params.put("email", email);
                params.put("code", text_code);

                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myReq.setShouldCache(false);
        queue.add(myReq);
//        sweetProgressDialog.show();
    }
    /*private void onSigninSuccess() {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment f1 = new SignIn();
        fragmentTransaction.replace(R.id.frame_profile, f1, null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();




    }*/

    private void onCodeFailed(String errorMsg) {
        Toast.makeText(getContext(), "Code doesn not match or server error!", Toast.LENGTH_SHORT).show();
//        if (sweetProgressDialog.isShowing()) {
//            sweetProgressDialog.dismiss();
//        }
//        alert.showAlertDialog(getContext(), "Signin Failed", "" + errorMsg, false);
    }

    //Checking credentials from server
    private void userLoign() {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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

                    Log.i("myTag", e.toString());
                    Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();

                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("myTag", error.toString());

                        Toast.makeText(getContext(), "" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "login");
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myReq.setShouldCache(false);
        queue.add(myReq);
//        sweetProgressDialog.show();
    }
    private void onSigninSuccess() {

        Splash.is_signup = "Yes";

        Intent intent = new Intent(getActivity(), WelcomeActivity.class);
        // Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("Name","Welcome");
        startActivity(intent);
        getActivity().finish();
    }

    private void onSigninFailed(String errorMsg) {

        alert.showAlertDialog(getContext(), "Signin Failed", "" + errorMsg, false);
    }
}

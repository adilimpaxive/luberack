package com.app.luberack.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.luberack.Profile_management.ConfirmPassword;
import com.app.luberack.Profile_management.Profile;
import com.app.luberack.Profile_management.SignIn;
import com.app.luberack.R;
import com.app.luberack.utility.AlertDialogManager;
import com.app.luberack.utility.Config;
import com.app.luberack.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahmad on 5/14/2018.
 */


public class Re_Enter_Password extends Fragment {
    private ProgressDialog sweetProgressDialog;
    private AlertDialogManager alert;

    EditText et_password,re_enterpassword;
    Button btn_confirm;
    String password,email;
    String confirm_password;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.re_enter_password, container, false);
        et_password = view.findViewById(R.id.et_password);
        re_enterpassword = view.findViewById(R.id.re_enterpassword);
        btn_confirm = view.findViewById(R.id.btn_confirm);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
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




        Bundle b = getArguments();
        email = b.getString("email");
        return view;
    }
    private boolean validate() {
        boolean valid = true;
        password = et_password.getText().toString().trim();
        confirm_password= re_enterpassword.getText().toString().trim();

        if (password.isEmpty()) {
            et_password.setError("Please Enter Password");
            valid = false;
        } else {
            et_password.setError(null);
        }
        if (confirm_password.isEmpty()  ) {
            re_enterpassword.setError("Please Enter Password");
            valid = false;
        } else {
            re_enterpassword.setError(null);
        }

        return valid;
    }

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
                    if (success.equals("Password updated.")) {
                        onSigninSuccess();
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
                params.put("tag", "newPassword");
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

//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        Fragment f1 = new SignIn();
//        fragmentTransaction.replace(R.id.frame_profile, f1, null);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();


//        Fragment  f1=new ConfirmPassword();
//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frame_reset_password, f1, null);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//        Intent intent=new Intent();
        startActivity(new Intent(getContext(), Profile.class));
//        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();



    }

    private void onCodeFailed(String errorMsg) {
//        if (sweetProgressDialog.isShowing()) {
//            sweetProgressDialog.dismiss();
//        }
//        alert.showAlertDialog(getContext(), "Signin Failed", "" + errorMsg, false);

        Toast.makeText(getContext(), "Password doesn not match or server error!", Toast.LENGTH_SHORT).show();
    }

}

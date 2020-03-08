package com.app.giftfcard.Profile_management;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.app.giftfcard.Fragments.Re_Enter_Password;
import com.app.giftfcard.R;
import com.app.giftfcard.utility.AlertDialogManager;
import com.app.giftfcard.utility.Config;
import com.app.giftfcard.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmPassword extends Fragment {

    EditText password;
    TextView tv_resend;
    Button btn_send;
    String Upassword;
    String mail;
    private ProgressDialog sweetProgressDialog;
    private AlertDialogManager alert;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_confirm_password, container, false);
        password=view.findViewById(R.id.et_password);
        btn_send=view.findViewById(R.id.btn_confirm);
        tv_resend=view.findViewById(R.id.tv_resend);


        Bundle b=getArguments();
        mail=b.getString("email");
        Toast.makeText(getContext(), ""+mail, Toast.LENGTH_SHORT).show();


        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });

        ///////////
        ////// enter code
        ///////////////////
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    //Save in database
                    if (Utility.isNetworkAvailable(getActivity())) {
                        sendPasswordCOde();
                    } else {
                        alert.showAlertDialog(getContext(), "No Network Available", "Please turn on Internet Connectivity", false);
                    }
                } else {
                }

            }
        });

        return view;
    }
    ///////////////////
    ///////////Resend code
    /////////////////////
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
                    int success = jObj.getInt("success");
                   /*// jsonArray.length();
                    jsonArray.getJSONObject(0).getString("user_name");*/
                    if (success==1) {
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
                params.put("tag", "forgotpassword");
                params.put("email", mail);


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

        Toast.makeText(getContext(), "Please confirm code", Toast.LENGTH_SHORT).show();



    }

    private void onCodeFailed(String errorMsg) {
//        if (sweetProgressDialog.isShowing()) {
//            sweetProgressDialog.dismiss();
//        }
//        alert.showAlertDialog(getContext(), "Signin Failed", "" + errorMsg, false);
    }

    //Validating data
    private boolean validate() {
        boolean valid = true;

        Upassword = password.getText().toString().trim();
        Log.i("U",Upassword);




        if (Upassword.isEmpty() || Upassword.length() < 4) {
            password.setError("Password length must be at least 4");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }


    //////////////////////
    ////////////Verify Code
    /////////////////////
    private void sendPasswordCOde() {
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
                    if (success.equals("Code matched.")) {
                        onCOdeSuccess();
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
                params.put("tag", "confirmPasswordCode");
                params.put("email", mail);
                params.put("code", Upassword);

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
    private void onCOdeSuccess() {

//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        Fragment f1 = new SignIn();
//        fragmentTransaction.replace(R.id.frame_profile, f1, null);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
        Bundle b=new Bundle();
        Fragment  f1=new Re_Enter_Password();
        b.putString("email",mail);
        f1.setArguments(b);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_reset_password, f1, null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        Toast.makeText(getContext(), "Find the code", Toast.LENGTH_SHORT).show();



    }

    private void onPasswordSuccessFailed(String errorMsg) {
        Toast.makeText(getContext(), "Code doesn not match or server error!", Toast.LENGTH_SHORT).show();
//        if (sweetProgressDialog.isShowing()) {
//            sweetProgressDialog.dismiss();
//        }
//        alert.showAlertDialog(getContext(), "Signin Failed", "" + errorMsg, false);
    }
}
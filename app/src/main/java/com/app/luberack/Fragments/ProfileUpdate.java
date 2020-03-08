package com.app.luberack.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.luberack.Adapter.ViewPagerAdapter;
import com.app.luberack.ModelClasses.VehicleData;
import com.app.luberack.MyEvent;
import com.app.luberack.Profile_management.SessionManager;
import com.app.luberack.R;
import com.app.luberack.WelcomeActivity;
import com.app.luberack.map.SignupMapFragment;
import com.app.luberack.utility.AlertDialogManager;
import com.app.luberack.utility.Config;
import com.app.luberack.utility.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class ProfileUpdate extends Fragment {

    EditText first_name_et, emial_et, change_passwrod_et, search_address_et;
    AlertDialog alertDialog;
    String lat, lng;
    String address;
    Button save_btn;
    private AlertDialogManager alert;
    SessionManager sessionManager;
    String user_name, u_address, u_email;
    ImageView imageViewAdd, imageViewEdit, imageViewDisplay;
    String password;
    TextView click_to_full_view_btn;
    Button logout_btn;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_update, container, false);
        sessionManager = new SessionManager(getContext());

        EventBus.getDefault().register(this);

        first_name_et = view.findViewById(R.id.first_name_et);
        emial_et = view.findViewById(R.id.emial_et);
        change_passwrod_et = view.findViewById(R.id.change_passwrod_et);
        search_address_et = view.findViewById(R.id.search_address_et);
        save_btn = view.findViewById(R.id.save_btn);
        logout_btn = view.findViewById(R.id.logout_btn);

        imageViewAdd = view.findViewById(R.id.add);
        imageViewEdit = view.findViewById(R.id.edit);
//        imageViewDisplay = view.findViewById(R.id.cra_iv);
        viewPager = view.findViewById(R.id.cra_iv);

        first_name_et.setText(sessionManager.getUserName());
        emial_et.setText(sessionManager.getUserEmail());
        search_address_et.setText(sessionManager.getAddress());
        change_passwrod_et.setText(sessionManager.getPassword());
        lat = sessionManager.getLat();
        lng = sessionManager.getLng();
        Log.i("getData", "  " + sessionManager.getUserEmail() + "  " + sessionManager.getAddress());

        click_to_full_view_btn = (TextView) view.findViewById(R.id.click_to_full_view_btn);
        click_to_full_view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction3 = getFragmentManager().beginTransaction();
                /*VehicleDetail f3 = new VehicleDetail();
                fragmentTransaction3.replace(R.id.home_frame, f3, null);
                fragmentTransaction3.addToBackStack(null);
                fragmentTransaction3.commit();*/
            }
        });
        imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WelcomeActivity.class);
                intent.putExtra("Name", "Vehicle");
                startActivity(intent);
            }
        });
        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WelcomeActivity.class);
                intent.putExtra("Name", "Vehicle");
                startActivity(intent);
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
            }
        });
        search_address_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isNetworkAvailable(getActivity())) {
                    if (Utility.isLocationEnabled(getActivity())) {
                        SignupMapFragment signupMapFragment = new SignupMapFragment();
                        signupMapFragment.setTargetFragment(ProfileUpdate.this, 123);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.home_frame, signupMapFragment)
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

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user_name = first_name_et.getText().toString();
                u_email = emial_et.getText().toString();
                password = change_passwrod_et.getText().toString();
                u_address = search_address_et.getText().toString();
                if (isValidEmail(u_email)) {
                    insertService();
                } else {
                    emial_et.setError("Invalid Email");
                }
            }
        });

        return view;
    }

    private boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Users location result
        if (requestCode == 123 && resultCode == RESULT_OK) {
            address = data.getStringExtra("useraddress");
            lat = data.getStringExtra("userlat");
            lng = data.getStringExtra("userlng");
            search_address_et.setText(address);
        }

    }

    //////////////////////////
    /////////////Update Response
    ////////////////////////////
    RequestQueue queue;
    ProgressDialog sweetAlertDialog;

    private void insertService() {
        sweetAlertDialog = ProgressDialog.show(getContext(), "Loading", "Please Wait...", false, false);
        queue = Volley.newRequestQueue(getContext());

        StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sweetAlertDialog.dismiss();
                try {
                    Log.e("tag", "response " + response);
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    // boolean error = jObj.getBoolean("error");
                    if (success == 1) {
                        // User successfully stored in MySQL
//
                        //updating prefs
                        sessionManager.saveUserName(user_name);
                        sessionManager.saveUserEmail(u_email);
                        sessionManager.savePassword(password);
                        sessionManager.saveAddress(u_address);
                        sessionManager.saveLat(lat);
                        sessionManager.saveLng(lng);

                        Toast.makeText(getContext(), "Profile is updated successfully...", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Couldn't Update profile", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    // if (sweetAlertDialog.isShowing()) {
                    sweetAlertDialog.dismiss();
                    //  }
                    Log.i("myTag", e.toString());
                    Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();

                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("myTag", error.toString());
                        //  if (sweetAlertDialog.isShowing()) {
                        if (sweetAlertDialog.isShowing()) {
                            sweetAlertDialog.dismiss();
                        }
                        //   }
                        Toast.makeText(getContext(), "" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register urluser_name,u_address,u_email
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "updateProfile");
                params.put("user_id", sessionManager.getUserID());
                params.put("email", u_email);
                params.put("password", password);
                params.put("name", user_name);
                params.put("address", u_address);
                params.put("lat", lat);
                params.put("lng", lng);
                Log.i("TAG", "new_name" + user_name);
                Log.i("TAG", "new_email" + u_email);

                // params.put("registrationIds", session.getRegistrationId()!=null? session.getRegistrationId() : "");

                return params;
            }
        };


        myReq.setRetryPolicy(new DefaultRetryPolicy(
                20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myReq.setShouldCache(false);
        queue.add(myReq);
        // sweetAlertDialog.show();
    }
    ///////////////////////////
    /////////////Response
    ////////////////////////////

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MyEvent myEvent) {
        ArrayList<VehicleData> vehicleImages= myEvent.getMessage();
        viewPagerAdapter = new ViewPagerAdapter(getContext(), vehicleImages);
        viewPager.setAdapter(viewPagerAdapter);
    }

}

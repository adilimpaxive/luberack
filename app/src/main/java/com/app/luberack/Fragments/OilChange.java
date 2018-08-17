package com.app.luberack.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.luberack.Adapter.ShopsAdapter;
import com.app.luberack.ModelClasses.OilChangeData;
import com.app.luberack.R;
import com.app.luberack.utility.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OilChange extends Fragment {
    ShopsAdapter adapterTop;
    OilChangeData data;
    ArrayList<OilChangeData> oilChangeDataList;
    RecyclerView recyclerViewFeatured;
    LinearLayoutManager layoutManager;
    private ProgressDialog sweetProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_oil_change, container, false);

        sweetProgressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        sweetProgressDialog.setMessage(String.format(getResources().getString(R.string.retr)));
        sweetProgressDialog.setCancelable(false);
        oilChangeDataList=new ArrayList<>();

        recyclerViewFeatured = view.findViewById(R.id.oil_change_recycler);
        recyclerViewFeatured.setLayoutManager(new LinearLayoutManager(getContext()));

        retrieveOilShops();
        return  view;
    }
    //Retrieving data from server
    private void retrieveOilShops() {
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
                        /*JSONObject temp = jObj.getJSONObject("home");
                        Log.e("tag", "" + temp);*/
                        //    Boolean restricted = temp.getBoolean("restricted");
                        JSONArray homesArray = jObj.getJSONArray("shops");
                        for (int i = 0; i < homesArray.length(); i++) {
                            JSONObject homeObj = homesArray.getJSONObject(i);

                            oilChangeDataList.add(new OilChangeData(homeObj.getString("id"), homeObj.getString("img_url"), homeObj.getString("name"), homeObj.getString("address"), homeObj.getString("reviews"), homeObj.getString("lat"), homeObj.getString("type"), homeObj.getString("lng"),"",""));


                        }
                        adapterTop = new ShopsAdapter(getContext(), oilChangeDataList);

                        recyclerViewFeatured.setAdapter(adapterTop);
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    if (sweetProgressDialog.isShowing()) {
                        sweetProgressDialog.dismiss();
                    }
                    Log.i("myTag", e.toString());
                    Toast.makeText(getContext(), "Parsing error", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to getFeatured url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "showShops");
                params.put("type", "Oil Change");
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

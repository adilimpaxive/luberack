package com.app.giftfcard.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.app.giftfcard.Adapter.VehicleImagesAdapter;
import com.app.giftfcard.ModelClasses.AlignmentData;
import com.app.giftfcard.ModelClasses.VehicleData;
import com.app.giftfcard.R;
import com.app.giftfcard.utility.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class VehicleImagesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    VehicleImagesAdapter adapterTop;
    AlignmentData data;
    ArrayList<VehicleData> vehicleImagesDataList;
    ProgressDialog sweetProgressDialog;
    String modelNo,make;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VehicleImagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VehicleImagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VehicleImagesFragment newInstance(String param1, String param2) {
        VehicleImagesFragment fragment = new VehicleImagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle_images, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        sweetProgressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        sweetProgressDialog.setMessage("Fetching Vehicle...");
        sweetProgressDialog.setCancelable(false);
        vehicleImagesDataList=new ArrayList<>();

        Bundle bundle = getArguments();
        modelNo = bundle.getString("Model");
        make = bundle.getString("Name");

        retrieveImages();

        return view;
    }

    private void retrieveImages() {

        sweetProgressDialog.setMessage("Fetching Vehicle Images...");
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
                        JSONArray homesArray = jObj.getJSONArray("charges");
                        for (int i = 0; i < homesArray.length(); i++) {
                            JSONObject homeObj = homesArray.getJSONObject(i);

                            vehicleImagesDataList.add(new VehicleData(homeObj.getString("id"), homeObj.getString("img_url"), make, homeObj.getString("vehicle_model"), homeObj.getString("vehicle_year"), homeObj.getString("vehicle_year"), homeObj.getString("vehicle_year"), homeObj.getString("vehicle_year")));

                        }
                        adapterTop = new VehicleImagesAdapter(getContext(), vehicleImagesDataList);

                        recyclerView.setAdapter(adapterTop);
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
                    Toast.makeText(getContext(), "No vehicle exist!", Toast.LENGTH_SHORT).show();
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
                params.put("tag", "vehicleImages");
                params.put("model", modelNo);


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

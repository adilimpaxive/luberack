package com.app.luberack.Fragments;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.luberack.Adapter.VehicleAdapter;
import com.app.luberack.Adapter.get_estimate_adapter;
import com.app.luberack.ModelClasses.AlignmentData;
import com.app.luberack.ModelClasses.VehicleData;
import com.app.luberack.ModelClasses.get_estimate_modal;
import com.app.luberack.Profile_management.SessionManager;
import com.app.luberack.R;
import com.app.luberack.utility.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmad on 5/2/2018.
 */

public class VehicleDetail extends Fragment {
    private List cList = new ArrayList<>();
    private RecyclerView recyclerView;
    VehicleAdapter adapterTop;
    AlignmentData data;
    ArrayList<VehicleData> oilChangeDataList;
    private get_estimate_adapter vAdapter;
    String Make,Year,Model,size,fwd;
    Spinner et_make, et_year, et_model;
    ProgressDialog sweetProgressDialog;
    SessionManager sessionManager;
    EditText etEngineSize,etFw;
    Button buttonSave,buttonNext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_vehicle_detail, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);

        et_make = view.findViewById(R.id.et_make);
        et_year = view.findViewById(R.id.et_year);
        et_model = view.findViewById(R.id.et_model);
        etEngineSize = view.findViewById(R.id.et_engine_size);
        etFw = view.findViewById(R.id.et_fra_wd);
        buttonSave = view.findViewById(R.id.buttonPanel);


        sweetProgressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        sweetProgressDialog.setMessage("Create Vehicle...");
        sweetProgressDialog.setCancelable(false);
        sessionManager = new SessionManager(getContext());

        oilChangeDataList=new ArrayList<>();

        Bundle bundle = getArguments();
        Make = bundle.getString("Name");
        Model = bundle.getString("Model");
        Year = bundle.getString("Year");
        size = bundle.getString("Size");
        fwd = bundle.getString("Fwd");

        ArrayAdapter<CharSequence> nAdapter = ArrayAdapter.createFromResource(getContext(), R.array.oil_change_make, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> mAdapter;
        if(Make.equals("Honda")) {
            mAdapter = ArrayAdapter.createFromResource(getContext(), R.array.oil_change_make_Honda, android.R.layout.simple_spinner_item);
        }else  mAdapter = ArrayAdapter.createFromResource(getContext(), R.array.oil_change_make_Acura, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> yAdapter = ArrayAdapter.createFromResource(getContext(), R.array.oil_change_year, android.R.layout.simple_spinner_item);

        et_make.setSelection(nAdapter.getPosition(Make));
        et_model.setSelection(mAdapter.getPosition(Model));
        et_year.setSelection(yAdapter.getPosition(Year));

        etEngineSize.setText(size);
        etFw.setText(fwd);

        //////////////////////////
        ///////////////Honda
        /////////////////////////
        final List<String> honda = new ArrayList<String>();
        honda.add("Select model");
        honda.add("Accord");
        honda.add("Civic");
        honda.add("Civic Del Sol");
        honda.add("Prelude");

        //////////////////////////
        ///////////////Acura
        /////////////////////////
        final List<String> acura = new ArrayList<String>();
        acura.add("Select model");
        acura.add("Integra");
        acura.add("Legend");
        acura.add("Vigor");


      //  retrieveAlignedShops();


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etEngineSize.getText().toString().isEmpty() || etEngineSize.getText().toString().length()==0){
                    Toast.makeText(getContext(),"Alert: Engine size required!",Toast.LENGTH_LONG).show();
                    etEngineSize.setFocusable(true);
                    return;
                }
                if(etFw.getText().toString().isEmpty() || etFw.getText().toString().length()==0){
                    Toast.makeText(getContext(),"Alert: Fwd/awd/rwd required!",Toast.LENGTH_LONG).show();
                    etFw.setFocusable(true);
                    return;
                }
                signup();
            }
        });


        et_make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Make= String.valueOf(parent.getItemAtPosition(position));
                if(Make.equals("Acura"))
                {
                    et_model.setEnabled(true);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item,acura);
                    adapter.setDropDownViewResource(R.layout.spinner_item);
                    et_model.setAdapter(adapter);
                }
                if(Make.equals("Honda"))
                {
                    et_model.setEnabled(true);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item,honda);
                    adapter.setDropDownViewResource(R.layout.spinner_item);
                    et_model.setAdapter(adapter);
                }
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
            //    ((TextView) et_model.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //initialize Adapterclass with List
        vAdapter = new get_estimate_adapter(cList);
        //set layout Manager of recyclerview
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // recyclerView.setAdapter(vAdapter);//add adapter to recyclerview
        // preparedataData();//call function to add data to recyclerview


        return  view;
    }

    //function to add data to List
    private void preparedataData() {
        get_estimate_modal name = new get_estimate_modal("Abraham Lincoln", "1809");
        cList.add(name);
        name = new get_estimate_modal("John F. Kennedy", "1917");
        cList.add(name);
        name = new get_estimate_modal("Bill Gates ", "1955");
        cList.add(name);
        name = new get_estimate_modal("Muhammad Ali ", "1942");
        cList.add(name);
        name = new get_estimate_modal("Christopher Columbus ", "1451");
        cList.add(name);
        name = new get_estimate_modal("George Orwell", "1903");
        cList.add(name);
        name = new get_estimate_modal("Charles Darwin ", "1809");
        cList.add(name);
        name = new get_estimate_modal("Elvis Presley", "1935");
        cList.add(name);
        name = new get_estimate_modal("Albert Einstein", "1879");
        cList.add(name);
        name = new get_estimate_modal("Queen Victoria", "1819");
        cList.add(name);
        name = new get_estimate_modal("Jawaharlal Nehru", "1889");
        cList.add(name);
        name = new get_estimate_modal("Leonardo da Vinci", "1452");
        cList.add(name);
        name = new get_estimate_modal("Pablo Picasso", "1881");
        cList.add(name);
        name = new get_estimate_modal("Vincent Van Gogh", "1853");
        cList.add(name);
        name = new get_estimate_modal("Thomas Edison", "1847");
        cList.add(name);
        name = new get_estimate_modal("Henry Ford", "1863");
        cList.add(name);
        name = new get_estimate_modal("Michael Jordan", "1963");
        cList.add(name);


        //notify datasetChanged to show the added items to the list in recyclerview
        vAdapter.notifyDataSetChanged();
    }


    void signup() {
        sweetProgressDialog.show();

        AndroidNetworking.upload(Config.URL)

                .addMultipartParameter("tag", "insertVehicle")
                .addMultipartParameter("user_id",sessionManager.getUserID())
                .addMultipartParameter("cmp_name", et_make.getSelectedItem().toString())
                .addMultipartParameter("year", et_year.getSelectedItem().toString())
                .addMultipartParameter("model", et_model.getSelectedItem().toString())
                .addMultipartParameter("size", etEngineSize.getText().toString())
                .addMultipartParameter("fwd", etFw.getText().toString())
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
                                    Toast.makeText(getContext(), "Vehicle added successfully", Toast.LENGTH_LONG).show();
                                   // retrieveAlignedShops();
                                    // session.saveUserID(temp.getString("user_id"));
                                    //          session.createLoginSession(email,contact,password);
                                    //          session.savePassword(password);
                                    // onSignupSuccess();
                                } else {
                                    // Error occurred. Get the error
                                    // message
                                    String errorMsg = jObj.getString("error_msg");
                                    //  onSignupFailed(errorMsg);
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
}

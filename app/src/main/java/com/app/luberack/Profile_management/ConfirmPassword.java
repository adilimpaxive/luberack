package com.app.luberack.Profile_management;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.app.luberack.R;

public class ConfirmPassword extends Fragment {

    EditText password;
    Button btn_send;
    String Upassword;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_confirm_password, container, false);
        password=view.findViewById(R.id.et_password);
        btn_send=view.findViewById(R.id.btn_confirm);
        return view;
    }
    //Validating data
    private boolean validate() {
        boolean valid = true;

        Upassword = password.getText().toString().trim();




        if (Upassword.isEmpty() || Upassword.length() < 4) {
            password.setError("Password length must be at least 4");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }
}
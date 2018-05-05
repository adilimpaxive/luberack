package com.app.luberack.Profile_management;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.app.luberack.R;

public class ResetPasword extends Fragment {
    EditText email;
    Button btn_send;
    String Uemail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_reset_pasword, container, false);
        email=view.findViewById(R.id.et_email);
        btn_send=view.findViewById(R.id.btn_send);
        return view;
    }
    //Validating data
    private boolean validate() {
        boolean valid = true;
        Uemail = email.getText().toString().trim();

        if (Uemail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(Uemail).matches()) {
            email.setError("Please Enter Email");
            valid = false;
        } else {
            email.setError(null);
        }

        return valid;
    }
}

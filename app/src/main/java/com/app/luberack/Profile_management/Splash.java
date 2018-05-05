package com.app.luberack.Profile_management;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.luberack.Home;
import com.app.luberack.R;

import org.w3c.dom.Text;

public class Splash extends AppCompatActivity {


    TextView tv_continue;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sessionManager=new SessionManager(Splash.this);

        tv_continue=(TextView)findViewById(R.id.tv_continue_to_setup);
        tv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sessionManager.checkLogin()) {
                    Intent intent = new Intent(Splash.this, Home.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(Splash.this, Profile.class);
                    startActivity(intent);
                }
            }
        });
    }
}

package com.app.giftfcard.Profile_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.app.giftfcard.R;
import com.app.giftfcard.WelcomeActivity;

public class Splash extends AppCompatActivity {


    TextView tv_continue;
    SessionManager sessionManager;
    public static String is_signup="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sessionManager=new SessionManager(Splash.this);

        if(sessionManager.checkLogin()) {
            is_signup = "No";
            Intent intent = new Intent(Splash.this, WelcomeActivity.class);
            intent.putExtra("Name","Welcome");
            startActivity(intent);
        }

        tv_continue=(TextView)findViewById(R.id.tv_continue_to_setup);
        tv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sessionManager.checkLogin()) {
                    is_signup = "No";
                    Intent intent = new Intent(Splash.this, WelcomeActivity.class);
                    intent.putExtra("Name","Welcome");
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(Splash.this, Profile.class);
                    startActivity(intent);
                }
            }
        });
    }
}

package com.example.connectfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NoAcount extends MainActivity implements View.OnClickListener{
    Button logement,exitBtn,signUp,google,facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_acount);
        logement=(Button)findViewById(R.id.logement);
        logement.setOnClickListener(this);
        exitBtn= (Button) findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(this);
        signUp=(Button)findViewById(R.id.signUp);
        signUp.setOnClickListener(this);
        google=(Button)findViewById(R.id.google);
        google.setOnClickListener(this);

     facebook=(Button)findViewById(R.id.facebook);
        facebook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logement:
                Toast.makeText( NoAcount.this, "il faut d'abord créér un compte ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.exitBtn:
                Intent intent3 =new Intent(NoAcount.this,HomeActivity.class);
                startActivity(intent3);
                break;
            case R.id.signUp:
                Intent intent4 =new Intent(NoAcount.this,MainActivity.class);
                startActivity(intent4);
                break;
           case R.id.google:
              super.signIn();
              break;
            case R.id.facebook:
               super.loginFacebook();
               break;
        }

    }

    }

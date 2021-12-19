package com.example.connectfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
        ImageView  FavoriteBtn;
        ImageView ProfilBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FavoriteBtn = findViewById(R.id.favoriteBtn);
        FavoriteBtn.setOnClickListener(this);
        ProfilBtn = findViewById(R.id.profilBtn);
        ProfilBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favoriteBtn:
                Intent intent = new Intent(HomeActivity.this, FavoriteList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.profilBtn:
        Intent intent2 = new Intent(HomeActivity.this, Page2.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent2);
        break;
    }

    }
}

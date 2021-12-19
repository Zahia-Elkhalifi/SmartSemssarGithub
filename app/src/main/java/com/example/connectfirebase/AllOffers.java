package com.example.connectfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class AllOffers extends AppCompatActivity implements View.OnClickListener {
    Button homeBtn;
    private RecyclerView annonceRV;
    private OfferAdapter annonceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_offers);

        homeBtn = (Button) findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(this);

        annonceRV = findViewById(R.id.rcv_offer);
        // here we have created new array list and added data to it.
        annonceAdapter = new OfferAdapter(this,Page2.mListAnnonce);
        annonceRV.setLayoutManager( new LinearLayoutManager( this));
        annonceRV.setAdapter(annonceAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeBtn:
                Intent intent =new Intent(AllOffers.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}
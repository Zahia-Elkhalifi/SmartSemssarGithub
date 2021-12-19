package com.example.connectfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView  FavoriteBtn;
    ImageView ProfilBtn;
    private FirebaseFirestore mFirestore;
    private RecyclerView annonceRV;
    private AnnonceAdapter annonceAdapter;
    private static ArrayList<AnnonceModel> mListAnnonce;
    private String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FavoriteBtn = findViewById(R.id.favoriteBtn);
        FavoriteBtn.setOnClickListener(this);
        ProfilBtn = findViewById(R.id.profilBtn);
        ProfilBtn.setOnClickListener(this);
        annonceRV = findViewById(R.id.recycleView);


        annonceAdapter = new AnnonceAdapter(this,SplashScrennActivity.mListAnnonce);
        annonceRV.setLayoutManager( new LinearLayoutManager( this));
        annonceRV.setAdapter(annonceAdapter);

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
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser fireBaseUser = mAuth.getCurrentUser();
                if (fireBaseUser == null) {
                    Intent intent2 = new Intent(HomeActivity.this, NoAcount.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                } else {
                    Intent intent2 = new Intent(HomeActivity.this, Page2.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);

                }
                break;
        }

    }
}
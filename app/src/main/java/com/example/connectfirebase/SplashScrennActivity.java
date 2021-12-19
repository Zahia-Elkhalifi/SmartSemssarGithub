package com.example.connectfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Member;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class SplashScrennActivity extends AppCompatActivity {
    private ImageView fullName ;
    private ImageView logo ;
    private ImageView pointLogo ;
    private ImageView rectangle ;
    private FirebaseFirestore mFirestore;
    protected static ArrayList<AnnonceModel> mListAnnonce;
    private static int SPLASH_SCREEN_TIME_OUT=5000;
    protected static String price;
    private StorageReference storageReference;
    protected static String uri;
    String s;
    //After completion of 5000 ms, the next activity will get started.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screnn);
        //set full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Assign variable
        fullName =(ImageView)findViewById(R.id.fullName);
        logo =(ImageView)findViewById(R.id.Logo);
        pointLogo =(ImageView)findViewById(R.id.pointLogo);
        rectangle=(ImageView)findViewById(R.id.rectangle);
        //initialize  animation
        fullName.animate().alpha(0).setDuration(1000);
        rectangle.animate().alpha(1).setDuration(2000);
        logo.animate().alpha(1).setDuration(3000);
        pointLogo.animate().alpha(1).setDuration(3000);
        logo.animate().translationXBy(300).setDuration(2000);
        pointLogo.animate().translationXBy(-200).setDuration(2000);
        logo.animate().scaleX(1.3f).scaleY(1.3f).setDuration(2000);
        pointLogo.animate().scaleX(1.1f).scaleY(1.1f).setDuration(2000);
        rectangle.animate().scaleX(1.3f).scaleY(1.3f).setDuration(2000);
        //this will bind your MainActivity.class file with activity_main.

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashScrennActivity.this,
                        MainActivity.class);
                //Intent is used to switch from one activity to another.

                startActivity(i);
                //invoke the SecondActivity.

                finish();
                //the current activity will get finished.
            }
        }, SPLASH_SCREEN_TIME_OUT);

        mFirestore= FirebaseFirestore.getInstance();
        mListAnnonce = new ArrayList<>();

        CollectionReference collectionReference = mFirestore.collection("Logements");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    if( task.getResult().isEmpty()==true){
                        Toast.makeText(SplashScrennActivity.this,"aaaa",Toast.LENGTH_SHORT).show();

                    }
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        s = document.getId();
                        CollectionReference collectionReference = mFirestore.collection("Logements").document(document.getId()).collection("Logements");
                        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {



                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                    FirebaseUser mUser = mAuth.getCurrentUser();

                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        Toast.makeText(SplashScrennActivity.this, document.getString("prix"),Toast.LENGTH_SHORT).show();
                                        mListAnnonce.add(new AnnonceModel(document.getString("prix"), document.getString("rue")+" "+document.getString("entourage"),R.drawable.logo));

                                    }
                                }


                                else {
                                }
                            }
                        });




                    }

                } else {
                }
            }
        });




    }
}
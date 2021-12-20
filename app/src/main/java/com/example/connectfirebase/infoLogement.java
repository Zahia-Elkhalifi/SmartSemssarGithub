package com.example.connectfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class infoLogement extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView rcvPhoto;
    private PhotoAdapter photoAdapter;
    private TextView type,prix,description;
    ImageView home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_logement);
        rcvPhoto = findViewById(R.id.rcv_photo);
        photoAdapter = new PhotoAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rcvPhoto.setLayoutManager(gridLayoutManager);
        rcvPhoto.setAdapter(photoAdapter);
        photoAdapter.setData(PhotoAdapter.mListPhoto);
        type=findViewById(R.id.type);
        description=findViewById(R.id.description);
        prix=findViewById(R.id.prix);
        type.setText(Louer.type);
        prix.setText(Louer1.prix);
        description.setMovementMethod(new ScrollingMovementMethod());
        description.setText(Louer1.description);
        home=findViewById(R.id.home);
        home.setOnClickListener(this);
        //desactivate the onLongClick method
    }

    @Override
    public void onBackPressed() {
        Louer1.prix=null;
        Louer1.titre=null;
        Louer1.description=null;
        PhotoAdapter.mListPhoto=null;
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.home:
                onBackPressed();
                break;

        }
    }
}
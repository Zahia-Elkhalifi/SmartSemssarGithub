package com.example.connectfirebase;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Louer extends AppCompatActivity  implements View.OnClickListener{
    EditText editEntourage,editRue,editResidence;
    protected static String entourage,rue,residence,userId,type;
    private FirebaseAuth mAuth;
    private FirebaseUser fireBaseUser;
    private FirebaseFirestore mFirestore;
    private RadioButton appartement,villa,maison,chambre,studio;
    private ImageView map,Suivant;
    private RadioGroup radioGroup;
    RadioButton radioButton;
    protected static Logement logement =new Logement();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_louer);
        appartement= (RadioButton) findViewById(R.id.appartement);
        villa=(RadioButton) findViewById(R.id.villa);
        maison=(RadioButton) findViewById(R.id.maison);
        chambre=(RadioButton) findViewById(R.id.chambre);
        studio=(RadioButton) findViewById(R.id.studio);
        editEntourage=(EditText)findViewById(R.id.entourage);
        editRue=(EditText)findViewById(R.id.rue);
        editResidence=(EditText)findViewById(R.id.residence);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        Suivant= (ImageView) findViewById(R.id.suivant);
        Suivant.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        fireBaseUser = mAuth.getCurrentUser();
        userId=fireBaseUser.getUid();
        mFirestore=FirebaseFirestore.getInstance();

        map=(ImageView) findViewById(R.id.map);
        map.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Louer1.prix=null;
        Louer1.titre=null;
        Louer1.description=null;
        PhotoAdapter.mListPhoto=null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.suivant:
                next();
                break;

            case R.id.map:
                Intent intent=new Intent(Louer.this,TryMap.class);
                startActivity(intent);
                break;

        }
    }

    private void next() {

        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        entourage= editEntourage.getText().toString().trim();
        residence= editResidence.getText().toString().trim();
        rue= editRue.getText().toString().trim();
        if(entourage.isEmpty()){
            editEntourage.setError("Entourage requis");
            editEntourage.requestFocus();

        }
        else if(rue.isEmpty()){
            editRue.setError("Rue requis");
            editRue.requestFocus();

        }
        else if(residence.isEmpty()){
            editResidence.setError("Résidence requis");
            editResidence.requestFocus();

        }




        else{

            type=radioButton.getText().toString().trim();

            logement.setType(type);
            logement.setEntourage(entourage);
            logement.setRue(rue);
            logement.setResidenceinfo(residence);
            logement.setUserId(userId);



            Intent intent =new Intent(Louer.this,Louer1.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
    }

}

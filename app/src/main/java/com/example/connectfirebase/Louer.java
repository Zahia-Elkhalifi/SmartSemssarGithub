package com.example.connectfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Louer extends AppCompatActivity  implements View.OnClickListener{
    EditText editEntourage,editRue,editResidence;
    String entourage,rue,residence,userId;
   private FirebaseAuth mAuth;
    private FirebaseUser fireBaseUser;
    private FirebaseFirestore mFirestore;
   private RadioButton appartement,villa,maison,chambre,studio;
   private ImageView map,Suivant;
   private RadioGroup radioGroup;
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.map:
                int selectedId = radioGroup.getCheckedRadioButtonId();
               RadioButton radioButton = (RadioButton) findViewById(selectedId);

                entourage= editEntourage.getText().toString().trim();
                residence= editResidence.getText().toString().trim();
                rue= editRue.getText().toString().trim();
                DocumentReference nouveauLogement =mFirestore.collection("Logement").document();
               Logement logement =new Logement();
                logement.setType(radioButton.getText().toString());
               logement.setEntourage(entourage);
               logement.setRue(rue);
               logement.setResidenceinfo(residence);
               logement.setUserId(userId);

               nouveauLogement.set(logement).addOnCompleteListener(new OnCompleteListener<Void>() {

                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){
                           Toast.makeText(Louer.this,"firestore yaah",Toast.LENGTH_SHORT).show();

                       }
                       else{
                           Toast.makeText(Louer.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                       }
                   }
               });
                break;
                 case R.id.suivant:
                Intent intent =new Intent(Louer.this,Louer1.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

        }
    }
}

package com.example.connectfirebase;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class Page2 extends AppCompatActivity implements View.OnClickListener  {
    ImageView imageProfileImage;
    Button changeProfile,logement,exitBtn;
    StorageReference storageReference;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    TextView fullName,email,showAll,telephone,showAllOffers;
    String userId;
    protected static ArrayList<OfferModel> mListAnnonce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);


        fullName=findViewById(R.id.profileFullName);
        email=findViewById(R.id.profileEmail);
        telephone=findViewById(R.id.profileTelephone);

        imageProfileImage = (ImageView) findViewById(R.id.profileImageView);

        changeProfile = (Button) findViewById(R.id.changeProfile);
        changeProfile.setOnClickListener(this);

        logement=(Button)findViewById(R.id.logement);
        logement.setOnClickListener(this);

        showAll = (TextView) findViewById(R.id.showAll);
        showAll.setOnClickListener(this);

        showAllOffers=(TextView) findViewById(R.id.showAllOffers);
        showAllOffers.setOnClickListener(this);

        exitBtn = (Button) findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(this);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
            fullName.setText(signInAccount.getDisplayName());
            email.setText(signInAccount.getEmail());
        }

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profilRef = storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"profile.jpg") ;
        profilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageProfileImage);
            }
        });

        userId=mAuth.getCurrentUser().getUid();
        DocumentReference documentReference =fStore.collection("users").document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        fullName.setText(documentSnapshot.getString("fullName"));
                        email.setText(documentSnapshot.getString("Email"));
                        telephone.setText(documentSnapshot.getString("Téléphone"));
                    }
                    else{
                        Log.d(TAG, "No such document");
                    }
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        mListAnnonce = new ArrayList<>();


        CollectionReference collectionReference = fStore.collection("Logements").document(mAuth.getCurrentUser().getUid()).collection("Logements");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {



            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser mUser = mAuth.getCurrentUser();

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        mListAnnonce.add(new OfferModel(document.getString("prix"), document.getString("rue")+" "+document.getString("entourage"),R.drawable.logo));

                    }
                }


                else {
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.changeProfile:
                Intent i = new Intent(v.getContext(),EditProfile.class);
                i.putExtra("fullName",fullName.getText().toString());
                i.putExtra("Email",email.getText().toString());
                i.putExtra("Téléphone",telephone.getText().toString());
                startActivity(i);
                break;
            case R.id.logement:
                Intent intent =new Intent(Page2.this,Louer.class);
                startActivity(intent);
                break;
            case R.id.showAll:
                Intent intent2 = new Intent(v.getContext(),ShowAll.class);
                startActivity(intent2);
                break;
            case R.id.exitBtn:
                Intent intent3 =new Intent(Page2.this,HomeActivity.class);
                startActivity(intent3);
                break;
            case R.id.showAllOffers:
                Intent intent4 = new Intent(v.getContext(),AllOffers.class);
                startActivity(intent4);
        }
    }
}

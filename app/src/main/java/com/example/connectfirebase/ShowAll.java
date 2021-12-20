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
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ShowAll extends AppCompatActivity implements View.OnClickListener{
    ImageView imageProfileImage;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    FirebaseUser mUser;
    StorageReference storageReference;
    TextView fullName,email,telephone;
    String userId;
    Button logOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);

        fullName=findViewById(R.id.profileFullName1);
        email=findViewById(R.id.profileEmail1);
        telephone=findViewById(R.id.profileTelephone1);

        logOut = (Button) findViewById(R.id.logOut) ;
        logOut.setOnClickListener(this);

        imageProfileImage=(ImageView)findViewById(R.id.profileImageView);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
            fullName.setText(signInAccount.getDisplayName());
            email.setText(signInAccount.getEmail());
        }

        mAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        mUser=mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profilRef = storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"profile.jpg");
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.logOut:
                logOut();
                break;

        }
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent intent =new Intent(getApplicationContext(),NoAcount.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
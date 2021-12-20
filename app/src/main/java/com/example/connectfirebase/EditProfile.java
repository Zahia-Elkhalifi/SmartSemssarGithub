package com.example.connectfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity implements View.OnClickListener  {
    public static final String TAG="TAG";
    EditText profileFullName,emailProfile,telephoneProfile,passwordOld, passwordNew,passwordConfirm;
    Button changePicture,saveButton;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    FirebaseUser mUser;
    ImageView imageProfileImage;
    StorageReference storageReference;
    Button logOut;
    ProgressBar progressBar;


    private static final int GalleryPick = 1;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    String cameraPermission[];
    String storagePermission[];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileFullName=findViewById(R.id.profileFullName);
        emailProfile = findViewById(R.id.emailProfile);
        telephoneProfile=findViewById(R.id.profileTelephone1);

        changePicture = (Button) findViewById(R.id.button10);
        changePicture.setOnClickListener(this);

        saveButton=(Button) findViewById(R.id.saveProfileInfo);
        saveButton.setOnClickListener(this);

        logOut = (Button) findViewById(R.id.logOut);
        logOut.setOnClickListener(this);

        imageProfileImage=(ImageView)findViewById(R.id.profileImageView);

        mAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        mUser=mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        Intent data = getIntent();
        String fullName = data.getStringExtra("fullName");
        String Email = data.getStringExtra("Email");
        String telephone = data.getStringExtra("Téléphone");

        profileFullName.setText(fullName);
        emailProfile.setText(Email);
        telephoneProfile.setText(telephone);

        cameraPermission=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        StorageReference profilRef = storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"profile.jpg") ;
        profilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageProfileImage);
            }
        });
        passwordOld = findViewById(R.id.passwordActuel);
        passwordNew = findViewById(R.id.passwordNouveau);
        passwordConfirm = findViewById(R.id.passwordNouveauConfirm);
        progressBar = findViewById(R.id.progress);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button10:
                int which=0;
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromGallery();
                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
                break;
            case R.id.saveProfileInfo:
                saveInfo();
                String textOldPass = passwordOld.getText().toString();
                String textNewPass = passwordNew.getText().toString();
                String textConfirmPass = passwordConfirm.getText().toString();
                if(textOldPass.isEmpty() || textNewPass.isEmpty() || textConfirmPass.isEmpty()){
                    Toast.makeText(EditProfile.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else if(textNewPass.length() <6){
                    Toast.makeText(EditProfile.this, "The new password length schould be more than 6 characters", Toast.LENGTH_SHORT).show();
                }else if(!textConfirmPass.equals(textNewPass)){
                    Toast.makeText(EditProfile.this, "Confirm password does not match new password", Toast.LENGTH_SHORT).show();
                }else{
                    changePassword(textOldPass,textNewPass);
                }
                break;
            case R.id.logOut:
                logOut();
                break;

        }
    }

    private void changePassword(String textOldPass, String textNewPass) {
    progressBar.setVisibility(View.VISIBLE);
        AuthCredential authCredential = EmailAuthProvider.getCredential(mUser.getEmail(), textOldPass);
        mUser.reauthenticate( authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mUser.updatePassword(textNewPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mAuth.signOut();
                                Intent intent = new Intent(EditProfile.this,Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(EditProfile.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(EditProfile.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickFromGallery() {
        CropImage.activity().start(EditProfile.this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void saveInfo() {
        if(profileFullName.getText().toString().isEmpty() || emailProfile.getText().toString().isEmpty() || telephoneProfile.getText().toString().isEmpty()){
            Toast.makeText(EditProfile.this, "One or many fields are empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            String email=emailProfile.getText().toString();
            mUser.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    DocumentReference docRef = fStore.collection("users").document(mUser.getUid());
                    Map<String,Object> edited = new HashMap<>();
                    edited.put("Email",email);
                    edited.put("fullName",profileFullName.getText().toString());
                    edited.put("Téléphone",telephoneProfile.getText().toString());
                    docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditProfile.this, "profile updated ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Page2.class));
                            finish();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
                Picasso.get().load(imageUri).into(imageProfileImage);
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference fileRef = storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageProfileImage);
                    }
                });
                // Toast.makeText(Page2.this,"Image Uploaded.",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
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
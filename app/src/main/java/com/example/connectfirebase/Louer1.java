package com.example.connectfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class Louer1 extends AppCompatActivity implements View.OnClickListener {
    private ImageView Precedent, AddPic,suivant;
    private RecyclerView rcvPhoto;
    private PhotoAdapter photoAdapter;
    private FirebaseFirestore mFirestore;
    static EditText editPrix,editTitre,editDescription;
    protected static String prix,titre,description;
    private static int upload_count;
    protected static String documentId;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_louer1);
        Precedent = (ImageView) findViewById(R.id.precedent);
        Precedent.setOnClickListener(this);
        AddPic = findViewById(R.id.addPic);
        AddPic.setOnClickListener(this);
        rcvPhoto = findViewById(R.id.rcv_photo);
        photoAdapter = new PhotoAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rcvPhoto.setLayoutManager(gridLayoutManager);
        rcvPhoto.setAdapter(photoAdapter);
        mFirestore=FirebaseFirestore.getInstance();
        editPrix = findViewById(R.id.prix);
        editTitre = findViewById(R.id.titre);
        editDescription = findViewById(R.id.description);
        suivant = findViewById(R.id.suivant);
        suivant.setOnClickListener(this);

        if(prix!=null){
            editPrix.setText(prix);

        }
        if(titre!=null){
            editTitre.setText(titre);

        }
        if(description!=null){
            editDescription.setText(description);

        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.precedent:
                onBackPressed();
                break;
            case R.id.addPic:
                requestPermissions();
                break;
            case R.id.suivant:
                addLogement();
                break;

        }

    }



    @Override
    public void onBackPressed() {
        prix = editPrix.getText().toString().trim();
        titre = editTitre.getText().toString().trim();
        description = editDescription.getText().toString().trim();
        super.onBackPressed();
    }

    private void addLogement() {
        prix = editPrix.getText().toString().trim();
        titre = editTitre.getText().toString().trim();
        description = editDescription.getText().toString().trim();
        if(prix.isEmpty()){
            editPrix.setError("Entourage requis");
            editPrix.requestFocus();

        }
        else if(titre.isEmpty()){
            editTitre.setError("Rue requis");
            editTitre.requestFocus();

        }
        else if(description.isEmpty()){
            editDescription.setError("Résidence requis");
            editDescription.requestFocus();

        }
        else{


            Louer.logement.setPrix(prix);
            Louer.logement.setTitre(titre);
            Louer.logement.setDescription(description);
            Map<String ,Object> dummyMap= new HashMap<>();


            DocumentReference b =mFirestore.collection("Logements").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            b.set(dummyMap);
            DocumentReference nouveauLogement =  b.collection("Logements").document();
            nouveauLogement.set(dummyMap);
            documentId=nouveauLogement.getId();

            if( addImage()==1){
                nouveauLogement.set(Louer.logement).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Louer1.this,"firestore yaah",Toast.LENGTH_SHORT).show();
                            Intent intent2 =new Intent(Louer1.this,infoLogement.class);
                            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent2);
                        }
                        else{
                            Toast.makeText(Louer1.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });}}


    }

    protected int  addImage() {
        if(PhotoAdapter.mListPhoto==null || PhotoAdapter.mListPhoto.size()<3){
            Toast.makeText(Louer1.this,"please enter 3 photos minimum",Toast.LENGTH_SHORT).show();

            return 0;
        }

        else{

            StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("Logements").child(documentId);
            for(upload_count = 0 ;upload_count<PhotoAdapter.mListPhoto.size();upload_count++){
                Uri individualImage =PhotoAdapter.mListPhoto.get(upload_count);
                StorageReference ImageName =ImageFolder.child("Image"+individualImage.getLastPathSegment());
                ImageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url =String.valueOf(uri);
                                storeLink(url);
                            }

                        });
                    }


                });
            } return 1;
        }
    }

    private void storeLink(String url) {
        CollectionReference nouveauLogement =mFirestore.collection("Logements").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Logements").document(Louer1.documentId).collection("images");

        HashMap<String,String> hashMap =new HashMap();
        hashMap.put("ImgLink",url);
        nouveauLogement.add( hashMap);

    }

    private void requestPermissions() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                selectImageFromGallery();
                Toast.makeText( Louer1.this, "Permission Permission accordée", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(Louer1.this, "Permission refusée\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                .check();
    }

    private void selectImageFromGallery() {

        TedBottomPicker.with(Louer1.this)
                .setPeekHeight(1600)
                .showTitle(false)
                .setCompleteButtonText("Done")
                .setEmptySelectionText("No Select")
                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(List<Uri> uriList) {
                        // here is selected image uri list
                        if(uriList !=null && !uriList.isEmpty()){
                            photoAdapter.setData(uriList);
                        }
                    }
                });



    }
}
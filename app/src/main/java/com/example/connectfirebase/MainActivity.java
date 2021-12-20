package com.example.connectfirebase;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN =101 ;
    private Button signUp,google,facebook,login,ExitBtn;
    private EditText editTextFullName,editTextEmail,editTextPassword,editTextConfirmPassword,editTelephone;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private GoogleSignInClient mGoogleSignInClient;
    private String userId;
    private FirebaseFirestore fStore;


    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser fireBaseUser = mAuth.getCurrentUser();

        if (fireBaseUser != null && fireBaseUser.isEmailVerified()) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();

        }


        if (fireBaseUser != null) {
            for (UserInfo userInfo : fireBaseUser.getProviderData()) {
                if (userInfo.getProviderId().equals("facebook.com")) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);
                    finish();
                }
            }
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        signUp=(Button)findViewById(R.id.signUp);
        signUp.setOnClickListener(this);

        login=(Button)findViewById(R.id.goToSignUp);
        login.setOnClickListener(this);

        editTelephone=(EditText) findViewById(R.id.signUpTelephone);
        editTextFullName=(EditText) findViewById(R.id.signUpFullName);
        editTextEmail=(EditText) findViewById(R.id.signUpEmail);
        editTextPassword=(EditText) findViewById(R.id.signUpPassword);
        editTextConfirmPassword=(EditText) findViewById(R.id.confirmPassword);
        progressDialog=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        fStore=FirebaseFirestore.getInstance();

        google=(Button)findViewById(R.id.google);
        google.setOnClickListener(this);
        createRequestGoogle();
        facebook=(Button)findViewById(R.id.facebook);
        facebook.setOnClickListener(this);
             ExitBtn= (Button) findViewById(R.id.exitBtn);
        ExitBtn.setOnClickListener(this);



    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signUp:
                registerUser();
                break;
            case R.id.google:
                signIn();
                break;
            case R.id.goToSignUp:
                loginPage();
                break;
            case R.id.facebook:
                loginFacebook();
                break;
            case R.id.exitBtn:
                Intent intent2 =new Intent(MainActivity.this,HomeActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);;
                break;

        }

    }

    protected void loginFacebook() {

        Intent intent =new Intent(MainActivity.this, FacebookAuhActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void loginPage() {
        Intent intent =new Intent(MainActivity.this,Login.class);
        startActivity(intent);
    }


    private void registerUser() {
        String fullName =editTextFullName.getText().toString().trim();
        String confirmPassword =  editTextConfirmPassword.getText().toString().trim();
        String email =editTextEmail.getText().toString().trim();
        String password =editTextPassword.getText().toString().trim();
        String telephone= editTelephone.getText().toString().trim();

        if(fullName.isEmpty()){
            editTextFullName.setError("Full name is required");
            editTextFullName.requestFocus();

        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();

        }
        else if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();

        }
        else if(password.length() < 6){
            editTextPassword.setError("min is 6");
            editTextPassword.requestFocus();

        }
        else if(confirmPassword.isEmpty()){
              editTextConfirmPassword.setError("Confirm your password");
              editTextConfirmPassword.requestFocus();

        }
        else if(!password.equals(confirmPassword)){
              editTextConfirmPassword.setError("password don't much both field");
              editTextConfirmPassword.requestFocus();

        }
        else if (telephone.isEmpty()){
            editTelephone.setError("phone is required");
            editTelephone.requestFocus();
        }
        else{

            progressDialog.setMessage("please wait while registration");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        progressDialog.dismiss();

                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this,"registred successfuly,please verify your email for email verification",Toast.LENGTH_SHORT).show();
                                    editTextEmail.setText("");
                                    editTextFullName.setText("");
                                    editTextPassword.setText("");
                                    editTextConfirmPassword.setText("");
                                    editTelephone.setText("");

                                    userId = mAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = fStore.collection("users").document(userId);
                                    Map<String,Object> user = new HashMap<>();
                                    user.put("fullName",fullName);
                                    user.put("Email",email);
                                    user.put("Téléphone",telephone);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG,"onSuccess :user Profile is create for "+userId);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG,"on Failure");
                                        }
                                    });

                                }
                                else{
                                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();


                                }

                            }
                        });




                }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"registration failded "+task.getException().getMessage()+" Try again",Toast.LENGTH_SHORT).show();

                    }
            }
        });

        }


    }



    //google
    protected   void createRequestGoogle(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }
    protected  void signIn() {
        mGoogleSignInClient.signOut();
        progressDialog.setMessage("Google Sign In...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

                Toast.makeText(MainActivity.this,"registration failded "+e.getMessage()+" Try again",Toast.LENGTH_SHORT).show();

            }
        }
    }
    protected void firebaseAuthWithGoogle(String idToken) {


        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            updateUI(currentUser);
                            Intent intent = new Intent(getApplicationContext(),Page2.class);
                        } else {

                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                           finish();
                        }
                    }
                });
    }

    protected void updateUI(FirebaseUser user) {
        Intent intent =new Intent(MainActivity.this,Page2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
       finish();
    }
}

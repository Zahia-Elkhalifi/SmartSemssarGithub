package com.example.connectfirebase;


import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends MainActivity  implements View.OnClickListener{
    private Button login,google,facebook,signUp,ExitBtn;
    private EditText editTextEmail,editTextPassword;
    private FirebaseAuth mAuth;
    TextView forgetpass;
    ProgressDialog progressDialog;
    ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=(Button)findViewById(R.id.login);
        google=(Button)findViewById(R.id.google);
        google.setOnClickListener(this);
        editTextEmail=(EditText) findViewById(R.id.signUpFullName);
        editTextPassword=(EditText) findViewById(R.id.loginPassword);
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(this);
        forgetpass=findViewById(R.id.forgetpass);
        forgetpass.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
        super.createRequestGoogle();
        facebook=(Button)findViewById(R.id.facebook);
        facebook.setOnClickListener(this);
        signUp=(Button)findViewById(R.id.goToSignUp);
        signUp.setOnClickListener(this);
        ExitBtn= (Button) findViewById(R.id.exitBtn);
        ExitBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                login();
                break;

            case R.id.google:
                super.signIn();
                break;

            case R.id.forgetpass:
                showRecoverPasswordDialog();
                break;
            case R.id.facebook:
                super.loginFacebook();
                break;

            case R.id.goToSignUp:
                Intent intent =new Intent(Login.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
                
         case R.id.exitBtn:
                Intent intent2 =new Intent(Login.this,HomeActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                break;


        }

    }
    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder =new AlertDialog.Builder(Login.this);
        builder.setTitle("Enter your Email to Recover Password");
        LinearLayout linearLayout=new LinearLayout(Login.this);
        final EditText emailet= new EditText(Login.this);
// write the email using which you registered
        emailet.setHint("enter your email");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email1 = emailet.getText().toString().trim();
                beginRecovery(email1);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();


    }

    private void beginRecovery(String email1) {
        loadingBar = new ProgressDialog(Login.this);
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        mAuth.sendPasswordResetEmail(email1).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if(task.isSuccessful())
                {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(Login.this,"Done sent",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(Login.this,"Error Occured",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(Login.this,"Error Failed",Toast.LENGTH_LONG).show();
            }
        });
    }




    private void login() {
        String email =editTextEmail.getText().toString().trim();
        String password =editTextPassword.getText().toString().trim();
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();

        }
        else if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();

        }

        else{
            progressDialog.setMessage("Login...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        if(mAuth.getCurrentUser().isEmailVerified()){
                            Intent intent =new Intent(getApplicationContext(),Page2.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }
                        else{
                            Toast.makeText(Login.this,"please verify your email adress",Toast.LENGTH_SHORT).show();

                        }

                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(Login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

    }

}

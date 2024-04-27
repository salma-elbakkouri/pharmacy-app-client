package com.example.mypharmacy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private TextView toRegisterBTN;
    private TextView forgotPasswordBTN;
    private TextView loginBTN;
    private EditText emailTextLogin;
    private EditText passwordTextLogin;
    private ImageView homeBTN;
    private ImageView fbLogin;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    String fullName;
    FirebaseAuth mAuth;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toRegisterBTN = findViewById(R.id.toRegisterBTN);
        forgotPasswordBTN = findViewById(R.id.forgotPasswordBTN);
        loginBTN = findViewById(R.id.loginBTN);
        emailTextLogin = findViewById(R.id.emailTextLogin);
        passwordTextLogin = findViewById(R.id.passwordTextLogin);
        homeBTN = findViewById(R.id.homeBTN);
        fbLogin = findViewById(R.id.fbLogin);




        forgotPasswordBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent (LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Intent i = new Intent(LoginActivity.this, UserPharmacies.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        toRegisterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);

            }
        });

        homeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loginUser() {
        String email = emailTextLogin.getText().toString();
        String password = passwordTextLogin.getText().toString();

        if (email.isEmpty())
        {
            emailTextLogin.setError("ce champ est neceassaire");
            emailTextLogin.requestFocus();
        }
        else if (!email.matches(emailPattern))
        {
            emailTextLogin.setError("veuillez saisir um email valide!");
            emailTextLogin.requestFocus();
        }
        else if (password.isEmpty())
        {
            passwordTextLogin.setError("ce champ est neceassaire");
            passwordTextLogin.requestFocus();
        }
        else if (password.length()<6)
        {
            passwordTextLogin.setError("le minimum nombre de characteres est 6");
            passwordTextLogin.requestFocus();
        }
        else
        {
            progressDialog.setMessage("identification en cours , veuillez attender un moment");
            progressDialog.setTitle("identification");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull  Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        //redirect to profile
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user.isEmailVerified())
                        {
                            Intent i = new Intent(LoginActivity.this, UserPharmacies.class);
                            startActivity(i);
                        }
                        else
                        {
                            user.sendEmailVerification();
                            Toast.makeText(LoginActivity.this, "veuillez verifier votre boite email pour verifier votre compte", Toast.LENGTH_SHORT).show();
                        }



                    }
                    else 
                    {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "l'identification a echoue veuillez verifier vos cordonnees", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
}
package com.example.mypharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypharmacy.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private TextView toLoginBTN;
    private Button RegisterBTN;
    private EditText fullnameText;
    private EditText emailTextRegister;
    private EditText passwordTextRegister;
    private EditText confirmPasswordText;
    private ImageView homeBTN2;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toLoginBTN = findViewById(R.id.toLoginBTN);
        RegisterBTN = findViewById(R.id.registerBTN);
        fullnameText = findViewById(R.id.fullnameText);
        emailTextRegister = findViewById(R.id.emailTextRegister);
        passwordTextRegister = findViewById(R.id.passwordTextRegister);
        confirmPasswordText = findViewById(R.id.confirmPasswordText);
        homeBTN2 = findViewById(R.id.homeBTN2);

        progressDialog = new ProgressDialog(this);

        toLoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        homeBTN2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        RegisterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser() {
        String fullname = fullnameText.getText().toString();
        String email = emailTextRegister.getText().toString();
        String password = passwordTextRegister.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        if (fullname.isEmpty()) {
            fullnameText.setError("ce champ est necessaire");
            fullnameText.requestFocus();
        } else if (email.isEmpty()) {
            emailTextRegister.setError("ce champ est neceassaire");
            emailTextRegister.requestFocus();
        } else if (!email.matches(emailPattern)) {
            emailTextRegister.setError("veuillez saisir um email valide!");
            emailTextRegister.requestFocus();
        } else if (password.isEmpty()) {
            passwordTextRegister.setError("ce champ est neceassaire");
            passwordTextRegister.requestFocus();
        } else if (password.length() < 6) {
            passwordTextRegister.setError("le minimum nombre de characteres est 6");
            passwordTextRegister.requestFocus();
        } else if (confirmPassword.isEmpty()) {
            confirmPasswordText.setError("ce champ est neceassaire");
            confirmPasswordText.requestFocus();
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordText.setError("mot de passe incorrect! Ressayez");
            confirmPasswordText.requestFocus();
        } else {


            progressDialog.setMessage("inscription en cours , veuillez attender un moment");
            progressDialog.setTitle("inscription");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        User user = new User(fullname, email);

                        FirebaseDatabase.getInstance().getReference("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "inscirption faite avec succees", Toast.LENGTH_SHORT).show();

                                    //redirect to login page
                                    Intent i = new Intent(RegisterActivity.this , LoginActivity.class);
                                    startActivity(i);
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "erreur l'inscription a echoue", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, task.getException()+" erreur l'inscription a echoue", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }
}
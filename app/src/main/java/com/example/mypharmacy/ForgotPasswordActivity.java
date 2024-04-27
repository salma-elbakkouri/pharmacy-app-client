package com.example.mypharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button resetBTN;
    private EditText emailTextReset;
    private ProgressDialog progressDialog;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailTextReset = findViewById(R.id.emailTextReset);
        resetBTN = findViewById(R.id.resetBTN);

        auth= FirebaseAuth.getInstance();
        
        resetBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = emailTextReset.getText().toString().trim();
        if (email.isEmpty())
        {
            emailTextReset.setError("ce champ est necessaire");
            emailTextReset.requestFocus();
            return;
        }
        else if (!email.matches(emailPattern))
        {
            emailTextReset.setError("veuillez saisir um email valide!");
            emailTextReset.requestFocus();
            return;
        }
        else
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Recuperation");
            progressDialog.setMessage("attendez un moment");
            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ForgotPasswordActivity.this, "veuillez verifier votre boite email!", Toast.LENGTH_SHORT).show();
                    }
                    else 
                    {
                        Toast.makeText(ForgotPasswordActivity.this, "erreur! veuillez ressayer!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
}
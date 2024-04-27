package com.example.mypharmacy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button utilisateurBTN;
    private Button pharmacienBTN;
    private Button aboutBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utilisateurBTN = findViewById(R.id.utilisateurBTN);
        pharmacienBTN = findViewById(R.id.pharmacienBTN);
        aboutBTN = findViewById(R.id.aboutBTN);

        utilisateurBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PharmaciesOnlineActivity.class);
                startActivity(i);
            }
        });

        pharmacienBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });





    }

}
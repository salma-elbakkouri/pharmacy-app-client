package com.example.mypharmacy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypharmacy.models.Pharmacies_pending;
import com.example.mypharmacy.utils.MyAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserPharmacies extends AppCompatActivity {

    private TextView txt_option;
    private ImageView openDrawerBTN;
    private DrawerLayout drawerLayout;
    private TextView toolbarText;
    private Toolbar toolbar;
    private FloatingActionButton addfloatingBTN;
    private FloatingActionButton reloadfloatingBTN;
    private ProgressBar progressBar;

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    MyAdapter myAdapter;
    ArrayList<Pharmacies_pending> list;

    String uid;


    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference reference;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_user_pharmacies);

        progressBar = findViewById(R.id.progressBarPharmacies);
        progressBar.setVisibility(View.VISIBLE);

        toolbarText= findViewById(R.id.toolbarText);
        addfloatingBTN = findViewById(R.id.addfloatingBTN);
        reloadfloatingBTN= findViewById(R.id.reloadfloatingBTN);


        toolbarText.setText("Pharmacies");
        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);

        txt_option = findViewById(R.id.txt_option);


        drawerLayout = findViewById(R.id.drawer_layout);
        openDrawerBTN = findViewById(R.id.openDrawerBTN);
        openDrawerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                if (item.getItemId() == R.id.nav_profile)
                {
                    Toast.makeText(UserPharmacies.this, "profile", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId() == R.id.nav_pharmacies)
                {
                    startActivity(new Intent(UserPharmacies.this, UserPharmacies.class));
                    finish();
                }
                else if (item.getItemId() == R.id.nav_changeMail)
                {
                    Toast.makeText(UserPharmacies.this, "changer email", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId() == R.id.nav_logout)
                {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(UserPharmacies.this, LoginActivity.class));
                    finish();
                }
                else if (item.getItemId() == R.id.nav_share)
                {
                    Toast.makeText(UserPharmacies.this, "partager", Toast.LENGTH_SHORT).show();
                }
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        recyclerView = findViewById(R.id.PharmaciesList);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Pharmacies_pending");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addfloatingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserPharmacies.this , PharmacienHomeActivity.class);
                i.putExtra("btn","valider");
                startActivity(i);
            }
        });

        reloadfloatingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = FirebaseDatabase.getInstance().getReference("Pharmacies_pending")
                .orderByChild("uid").equalTo(uid);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());


        list = new ArrayList<>();
        myAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    progressBar.setVisibility(View.GONE);
                    Pharmacies_pending pharmacy = dataSnapshot.getValue(Pharmacies_pending.class);
                    pharmacy.setKey(dataSnapshot.getKey());
                    list.add(pharmacy);
                }
                progressBar.setVisibility(View.GONE);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });


        updateNavHeader();
    }

    private void updateNavHeader() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String userfullname = snapshot.child("fullName").getValue().toString();
                NavigationView navigationView = findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                TextView navFullname = headerView.findViewById(R.id.nav_fullname);
                TextView nav_user_mail = headerView.findViewById(R.id.nav_user_mail);

                nav_user_mail.setText(currentUser.getEmail());
                navFullname.setText(userfullname);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(UserPharmacies.this, error.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
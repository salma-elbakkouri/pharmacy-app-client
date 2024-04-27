package com.example.mypharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypharmacy.models.Pharmacies_pending;
import com.example.mypharmacy.dao.PharmacieDAO;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;

public class PharmacienHomeActivity extends AppCompatActivity {

    private ImageView openDrawerBTN;
    private DrawerLayout drawerLayout;
    private TextView toolbarText;
    private Toolbar toolbar;
    private EditText nomPharmacieText;
    private EditText numPortableText;
    private EditText numFixText;
    private EditText adresseMailText;
    private EditText adressPharmacieText;
    private EditText finGardeText;
    private EditText debutGardeText;
    private CheckBox checkBox;
    DatePickerDialog.OnDateSetListener setListener;
    private Button actionBTN;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    String uid = "";
    String state = "en attente";
    private ImageView txt_option;


    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_pharmacien_home_activity);

        toolbarText = findViewById(R.id.toolbarText);
        toolbarText.setText("Pharmacies");
        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");


        actionBTN = findViewById(R.id.actionBTN);
        String btn = getIntent().getStringExtra("btn");
        actionBTN.setText(btn);





        drawerLayout = findViewById(R.id.drawer_layout);
        openDrawerBTN = findViewById(R.id.openDrawerBTN);
        openDrawerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        nomPharmacieText = findViewById(R.id.nomPharmacie);
        numPortableText = findViewById(R.id.numPortable);
        numFixText = findViewById(R.id.numFix);
        adresseMailText = findViewById(R.id.adresseMail);
        adressPharmacieText = findViewById(R.id.adressPharmacie);
        finGardeText = findViewById(R.id.finGarde);
        debutGardeText = findViewById(R.id.debutGarde);
        checkBox = findViewById(R.id.checkBox);

        progressDialog = new ProgressDialog(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                if (item.getItemId() == R.id.nav_profile) {
                    Toast.makeText(PharmacienHomeActivity.this, "profile", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.nav_pharmacies) {
                    startActivity(new Intent(PharmacienHomeActivity.this, UserPharmacies.class));
                    finish();
                } else if (item.getItemId() == R.id.nav_changeMail) {
                    Toast.makeText(PharmacienHomeActivity.this, "changer email", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.nav_logout) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(PharmacienHomeActivity.this, LoginActivity.class));
                    finish();
                } else if (item.getItemId() == R.id.nav_share) {
                    Toast.makeText(PharmacienHomeActivity.this, "partager", Toast.LENGTH_SHORT).show();
                }
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        debutGardeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PharmacienHomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        debutGardeText.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });

        finGardeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PharmacienHomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        finGardeText.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    finGardeText.setVisibility(View.VISIBLE);
                    debutGardeText.setVisibility(View.VISIBLE);
                } else {
                    debutGardeText.setVisibility(View.GONE);
                    finGardeText.setVisibility(View.GONE);
                }
            }
        });

        Pharmacies_pending pharmacies_pending_edit = (Pharmacies_pending) getIntent().getSerializableExtra("EDIT");
        if (pharmacies_pending_edit !=null)
        {
            nomPharmacieText.setText(pharmacies_pending_edit.getNom());
            numPortableText.setText(pharmacies_pending_edit.getNumPortable());
            numFixText.setText(pharmacies_pending_edit.getNumFix());
            adressPharmacieText.setText(pharmacies_pending_edit.getAdressePharmacie());
            adresseMailText.setText(pharmacies_pending_edit.getAdresseEmail());
            debutGardeText.setText(pharmacies_pending_edit.getDebutGarde());
            finGardeText.setText(pharmacies_pending_edit.getFinGarde());
        }
        else
        {
            actionBTN.setText("valider");
        }

        actionBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((actionBTN.getText()).equals("modifier")) {
                    String nomPharmacie = nomPharmacieText.getText().toString();
                    String numPortable = numPortableText.getText().toString();
                    String numFix = numFixText.getText().toString();
                    String adressPharmacie = adressPharmacieText.getText().toString();
                    String adressMail = adresseMailText.getText().toString();
                    String debutGarde = debutGardeText.getText().toString();
                    String finGarde = finGardeText.getText().toString();


                    if (checkBox.isChecked()) {
                        if (nomPharmacie.isEmpty()) {
                            nomPharmacieText.setError("ce champ est necessaire");
                            nomPharmacieText.requestFocus();
                        } else if (numPortable.isEmpty()) {
                            numPortableText.setError("ce champ est necessaire");
                            numPortableText.requestFocus();
                        }
                        else if (numPortable.length()<10 || numPortable.length()>10)
                        {
                            numPortableText.setError("veuillez saisir un numero valide");
                            numPortableText.requestFocus();
                        }
                        else if (numFix.length()<10 || numPortable.length()>10)
                        {
                            numFixText.setError("veuillez saisir un numero valide");
                            numFixText.requestFocus();
                        }
                        else if (numFix.isEmpty()) {
                            numFixText.setError("ce champ est necessaire");
                            numFixText.requestFocus();
                        } else if (adressPharmacie.isEmpty()) {
                            adressPharmacieText.setError("ce champ est necessaire");
                            adressPharmacieText.requestFocus();
                        } else if (adressMail.isEmpty()) {
                            adresseMailText.setError("ce champ est necessaire");
                            adresseMailText.requestFocus();
                        }
                        else if (!adressMail.matches(emailPattern)) {
                            adresseMailText.setError("veuillez saisir um email valide!");
                            adresseMailText.requestFocus();
                        }
                        else if (debutGarde.isEmpty()) {
                            debutGardeText.setError("ce champ est necessaire");
                            debutGardeText.requestFocus();
                        } else if (finGarde.isEmpty()) {
                            finGardeText.setError("ce champ est necessaire");
                            finGardeText.requestFocus();
                        } else

                        {
                            PharmacieDAO pharmacieDAO = new PharmacieDAO();

                            progressDialog.setMessage("veuillez attender un moment");
                            progressDialog.setTitle("modification");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("nom", nomPharmacieText.getText().toString());
                            hashMap.put("numPortable", numPortableText.getText().toString());
                            hashMap.put("numFix", numFixText.getText().toString());
                            hashMap.put("adressePharmacie", adressPharmacieText.getText().toString());
                            hashMap.put("adresseMail", adresseMailText.getText().toString());
                            hashMap.put("debutGarde", debutGardeText.getText().toString());
                            hashMap.put("finGarde", finGardeText.getText().toString());



                            pharmacieDAO.update(pharmacies_pending_edit.getKey(), hashMap).addOnSuccessListener(suc ->
                            {
                                progressDialog.dismiss();
                                Intent i = new Intent(PharmacienHomeActivity.this, UserPharmacies.class);
                                startActivity(i);
                                Toast.makeText(PharmacienHomeActivity.this, "dossier modifie avec succees", Toast.LENGTH_SHORT).show();
                                finish();
                            }).addOnFailureListener(err ->
                            {
                                progressDialog.dismiss();
                                Toast.makeText(PharmacienHomeActivity.this, "" + err.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    } else if (!checkBox.isChecked()) {
                        if (nomPharmacie.isEmpty()) {
                            nomPharmacieText.setError("ce champ est necessaire");
                            nomPharmacieText.requestFocus();
                        }
                        else if (numPortable.isEmpty())
                        {
                            numPortableText.setError("ce champ est necessaire");
                            numPortableText.requestFocus();
                        }
                        else if (numPortable.length()<10 || numPortable.length()>10)
                        {
                            numPortableText.setError("veuillez saisir un numero valide");
                            numPortableText.requestFocus();
                        }
                        else if (numFix.length()<10 || numPortable.length()>10)
                        {
                            numFixText.setError("veuillez saisir un numero valide");
                            numFixText.requestFocus();
                        }
                        else if (numFix.isEmpty()) {
                            numFixText.setError("ce champ est necessaire");
                            numFixText.requestFocus();
                        } else if (adressPharmacie.isEmpty()) {
                            adressPharmacieText.setError("ce champ est necessaire");
                            adressPharmacieText.requestFocus();
                        } else if (adressMail.isEmpty()) {
                            adresseMailText.setError("ce champ est necessaire");
                            adresseMailText.requestFocus();
                        }
                        else if (!adressMail.matches(emailPattern)) {
                            adresseMailText.setError("veuillez saisir um email valide!");
                            adresseMailText.requestFocus();
                        } else {
                            PharmacieDAO pharmacieDAO = new PharmacieDAO();

                            progressDialog.setMessage("veuillez attender un moment");
                            progressDialog.setTitle("modification");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("nom", nomPharmacieText.getText().toString());
                            hashMap.put("numPortable", numPortableText.getText().toString());
                            hashMap.put("numFix", numFixText.getText().toString());
                            hashMap.put("adressePharmacie", adressPharmacieText.getText().toString());
                            hashMap.put("adresseMail", adresseMailText.getText().toString());
                            hashMap.put("debutGarde", debutGardeText.getText().toString());
                            hashMap.put("finGarde", finGardeText.getText().toString());


                            pharmacieDAO.update(pharmacies_pending_edit.getKey(), hashMap).addOnSuccessListener(suc ->
                            {
                                progressDialog.dismiss();
                                Intent i = new Intent(PharmacienHomeActivity.this, UserPharmacies.class);
                                startActivity(i);
                                Toast.makeText(PharmacienHomeActivity.this, "dossier modifie avec succees", Toast.LENGTH_SHORT).show();
                                finish();
                            }).addOnFailureListener(err ->
                            {
                                progressDialog.dismiss();
                                Toast.makeText(PharmacienHomeActivity.this, "" + err.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                }


                else if ((actionBTN.getText()).equals("valider")) {
                    storePharmacies();
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());

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
                Toast.makeText(PharmacienHomeActivity.this, error.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }






    private void storePharmacies() {
        String nomPharmacie = nomPharmacieText.getText().toString();
        String numPortable = numPortableText.getText().toString();
        String numFix = numFixText.getText().toString();
        String adressPharmacie = adressPharmacieText.getText().toString();
        String adressMail = adresseMailText.getText().toString();
        String debutGarde = debutGardeText.getText().toString();
        String finGarde = finGardeText.getText().toString();

        PharmacieDAO pharmacieDAO = new PharmacieDAO();

        if (checkBox.isChecked()) {
            if (nomPharmacie.isEmpty()) {
                nomPharmacieText.setError("ce champ est necessaire");
                nomPharmacieText.requestFocus();
            } else if (numPortable.isEmpty()) {
                numPortableText.setError("ce champ est necessaire");
                numPortableText.requestFocus();
            }
            else if (numPortable.length()<10 || numPortable.length()>10)
            {
                numPortableText.setError("veuillez saisir un numero valide");
                numPortableText.requestFocus();
            }
            else if (numFix.length()<10 || numPortable.length()>10)
            {
                numFixText.setError("veuillez saisir un numero valide");
                numFixText.requestFocus();
            }
            else if (numFix.isEmpty()) {
                numFixText.setError("ce champ est necessaire");
                numFixText.requestFocus();
            } else if (adressPharmacie.isEmpty()) {
                adressPharmacieText.setError("ce champ est necessaire");
                adressPharmacieText.requestFocus();
            } else if (adressMail.isEmpty()) {
                adresseMailText.setError("ce champ est necessaire");
                adresseMailText.requestFocus();
            }
            else if (!adressMail.matches(emailPattern)) {
                adresseMailText.setError("veuillez saisir um email valide!");
                adresseMailText.requestFocus();
            }
            else if (debutGarde.isEmpty()) {
                debutGardeText.setError("ce champ est necessaire");
                debutGardeText.requestFocus();
            } else if (finGarde.isEmpty()) {
                finGardeText.setError("ce champ est necessaire");
                finGardeText.requestFocus();
            } else {
                uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                progressDialog.setMessage("veuillez attender un moment");
                progressDialog.setTitle("ajout");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                Pharmacies_pending pharmacies_pending = new Pharmacies_pending(nomPharmacie, numPortable, numFix, adressMail, adressPharmacie, debutGarde, finGarde, uid, state);

                pharmacieDAO.add(pharmacies_pending).addOnSuccessListener(suc ->
                {
                    progressDialog.dismiss();
                    Intent i = new Intent(PharmacienHomeActivity.this, UserPharmacies.class);
                    startActivity(i);

                    Toast.makeText(this, "dossier ajoute avec succees", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(err ->
                {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + err.getMessage(), Toast.LENGTH_SHORT).show();
                });


            }
        } else if (!checkBox.isChecked()) {
            if (nomPharmacie.isEmpty()) {
                nomPharmacieText.setError("ce champ est necessaire");
                nomPharmacieText.requestFocus();
            } else if (numPortable.isEmpty()) {
                numPortableText.setError("ce champ est necessaire");
                numPortableText.requestFocus();
            } else if (numFix.isEmpty()) {
                numFixText.setError("ce champ est necessaire");
                numFixText.requestFocus();
            }
            else if (numPortable.length()<10 || numPortable.length()>10)
            {
                numPortableText.setError("veuillez saisir un numero valide");
                numPortableText.requestFocus();
            }
            else if (numFix.length()<10 || numPortable.length()>10)
            {
                numFixText.setError("veuillez saisir un numero valide");
                numFixText.requestFocus();
            }
            else if (numFix.isEmpty()) {
                numFixText.setError("ce champ est necessaire");
                numFixText.requestFocus();
            } else if (adressPharmacie.isEmpty()) {
                adressPharmacieText.setError("ce champ est necessaire");
                adressPharmacieText.requestFocus();
            } else if (adressMail.isEmpty()) {
                adresseMailText.setError("ce champ est necessaire");
                adresseMailText.requestFocus();
            }
            else if (!adressMail.matches(emailPattern)) {
                adresseMailText.setError("veuillez saisir um email valide!");
                adresseMailText.requestFocus();
            } else {
                progressDialog.setMessage("veuillez attender un moment");
                progressDialog.setTitle("ajout");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Pharmacies_pending pharmacie = new Pharmacies_pending(nomPharmacie, numPortable, numFix, adressMail, adressPharmacie, "", "", uid, state);
                pharmacieDAO.add(pharmacie).addOnSuccessListener(suc ->
                {
                    progressDialog.dismiss();
                    Intent i = new Intent(PharmacienHomeActivity.this, UserPharmacies.class);
                    startActivity(i);
                    Toast.makeText(this, "dossier ajoute avec succees", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(err ->
                {
                    progressDialog.dismiss();
                    Toast.makeText(this, " " + err.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }


    }
}
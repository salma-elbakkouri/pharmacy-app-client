package com.example.mypharmacy.dao;

import android.widget.Toast;

import com.example.mypharmacy.models.Pharmacies_pending;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class PharmacieDAO {

    private DatabaseReference databaseReference;
    public PharmacieDAO()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference =db.getReference(Pharmacies_pending.class.getSimpleName());
    }
    public Task<Void> add(Pharmacies_pending pharmacies_pending)
    {
        return databaseReference.push().setValue(pharmacies_pending);
    }
    public Task<Void> update(String key , HashMap<String , Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }
    public Task<Void> delete(String key)
    {
        return databaseReference.child(key).removeValue();
    }

}

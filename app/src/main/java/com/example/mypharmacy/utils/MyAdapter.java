package com.example.mypharmacy.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypharmacy.PharmacienHomeActivity;
import com.example.mypharmacy.R;
import com.example.mypharmacy.UserPharmacies;
import com.example.mypharmacy.dao.PharmacieDAO;
import com.example.mypharmacy.models.Pharmacies_pending;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Pharmacies_pending> list;

    public MyAdapter(Context context, ArrayList<Pharmacies_pending> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        //  uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Pharmacies_pending pharmacy = list.get(position);

        holder.nom.setText(pharmacy.getNom());
        holder.txt_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context, holder.txt_option);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_edit:
                                Intent intent = new Intent(context, PharmacienHomeActivity.class);
                                intent.putExtra("btn", "modifier");
                                intent.putExtra("EDIT",pharmacy);
                                context.startActivity(intent);
                                break;
                            case R.id.menu_remove:
                                PharmacieDAO pharmacieDAO = new PharmacieDAO();

                                pharmacieDAO.delete(pharmacy.getKey()).addOnSuccessListener(suc ->
                                {
                                    Intent i = new Intent(context, UserPharmacies.class);
                                    context.startActivity(i);

                                    Toast.makeText(context, "dossier supprime avec succees", Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(err ->
                                {
                                    Toast.makeText(context, "" + err.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nom;
        TextView txt_option;
        Button validerBTN;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            validerBTN = itemView.findViewById(R.id.actionBTN);
            nom = itemView.findViewById(R.id.nomPharmacieText);
            txt_option = itemView.findViewById(R.id.txt_option);

        }
    }

}


package com.example.connectfirebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnnonceAdapter extends RecyclerView.Adapter<AnnonceAdapter.MyViewHolder> {
    Context context;
    private ArrayList<AnnonceModel> listAnnonce;

    public AnnonceAdapter(Context context, ArrayList<AnnonceModel> listAnnonce) {
        this.context = context;
        this.listAnnonce = listAnnonce;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row_home,null);
        return new AnnonceAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // to set data to textview and imageview of each iteam_Annonce
        AnnonceModel itemList = listAnnonce.get(position);
        holder.price.setText( itemList.getPrix());
        holder.address.setText( itemList.getAddress());
        holder.annonceImg.setImageResource(itemList.getAnnonceimg());
    }

    @Override
    public int getItemCount() {
        if (listAnnonce!=null){
            return listAnnonce.size();
        }
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView annonceImg;
        private TextView price, address;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            annonceImg = itemView.findViewById(R.id.image);
            price = itemView.findViewById(R.id.price);
            address = itemView.findViewById(R.id.position);
        }
    }


}

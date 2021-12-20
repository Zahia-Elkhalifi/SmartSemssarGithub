package com.example.connectfirebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnnonceAdapterPage2 extends RecyclerView.Adapter<AnnonceAdapterPage2.MyViewHolder> {
    private Context ct;
    private ArrayList<AnnonceModelPage2> mListAnnonce1;
    public AnnonceAdapterPage2(Context ct,ArrayList<AnnonceModelPage2> mListAnnonce1){
        this.ct = ct;
        this.mListAnnonce1=mListAnnonce1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row_page2, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // to set data to textview and imageview of each iteam_Annonce
        final AnnonceModelPage2 itemList = mListAnnonce1.get(position);
        holder.price.setText( itemList.getPrix());
        holder.address.setText( itemList.getAddress());
        holder.annonceImg.setImageResource(itemList.getAnnonceimg());
        holder.textOptionDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //display option menu
                PopupMenu popupMenu = new PopupMenu(ct, holder.textOptionDigit);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.save:
                                Toast.makeText(ct, "saved", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.delete:
                                mListAnnonce1.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(ct, "deleted", Toast.LENGTH_SHORT).show();
                                break;
                            default:
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
        // this method is used for showing number
        // of card items in recycler view.
        if (mListAnnonce1!=null){
            return mListAnnonce1.size();
        }
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView annonceImg;
        private TextView price, address,textOptionDigit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            annonceImg = itemView.findViewById(R.id.LogImage);
            price = itemView.findViewById(R.id.price);
            address = itemView.findViewById(R.id.position);
            textOptionDigit = itemView.findViewById(R.id.textOptonDigit);
        }
    }

}

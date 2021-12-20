package com.example.connectfirebase;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter  extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private static FirebaseFirestore mFirestore=FirebaseFirestore.getInstance();
    private Context mcontext;
    protected static ArrayList<Uri> mListPhoto;
    public PhotoAdapter(Context mcontext) {
        this.mcontext = mcontext;
    }



    public  void setData(List<Uri>list){
        this.mListPhoto = (ArrayList<Uri>) list;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo,parent,false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Uri uri =mListPhoto.get(position);
        if(uri == null){
            return;
        }
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mcontext.getContentResolver(),uri);
            if (bitmap!=null){
                holder.imgPhoto.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.imgPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mcontext)
                        .setTitle("Supprimer l'image")
                        .setMessage("voulez-vous vraiment supprimer?")
                        .setIcon(R.drawable.ic_delet)
                        .setPositiveButton("oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mListPhoto.remove(position);
                                notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mListPhoto!=null){
            return mListPhoto.size();
        }
        return 0;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder{
        protected static ImageView imgPhoto;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto =itemView.findViewById(R.id.img_photo);
        }


    }
}

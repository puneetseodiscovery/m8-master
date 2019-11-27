package com.m8sworld.m8.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.m8sworld.m8.R;


import java.util.ArrayList;


public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.ViewHolder> {
    Context context;
    ArrayList<String> addImagePojos;

    public UploadImageAdapter(Context context, ArrayList<String> addImagePojos) {
        this.context = context;
        this.addImagePojos = addImagePojos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.customimage, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        Glide.with(context).load(addImagePojos.get(i)).placeholder(R.drawable.ic_image).dontAnimate().dontTransform().into(viewHolder.Image);


        viewHolder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                addImagePojos.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, addImagePojos.size());

            }
        });
    }

    @Override
    public int getItemCount() {
        return addImagePojos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView deleteImage, Image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            deleteImage = itemView.findViewById(R.id.image_delete);
            Image = itemView.findViewById(R.id.image);
        }
    }
}

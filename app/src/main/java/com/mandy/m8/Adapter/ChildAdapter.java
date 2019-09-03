package com.mandy.m8.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mandy.m8.R;
import com.mandy.m8.RetrofitModel.TreeListApi;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {
    Context context;
    ArrayList<TreeListApi.Treeuser> arrayList = new ArrayList<>();
    onItemClick onItem;
    int a = 0;

    public interface onItemClick {
        void onItemclick(int pos);
    }

    public void setOnItem(onItemClick Item) {
        onItem = Item;
    }


    public ChildAdapter(Context context, ArrayList<TreeListApi.Treeuser> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.childroot, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final TreeListApi.Treeuser data = arrayList.get(i);

        viewHolder.textView.setText(data.getName());

        Glide.with(context).load("http://m8.amrdev.site/public/upload/users/profiles/" + data.getProfileImage()).dontAnimate().dontTransform().placeholder(R.drawable.myaccount).into(viewHolder.roundedImageView);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItem != null) {
                    int pos = (data.getId());
                    if (pos != RecyclerView.NO_POSITION) {
                        onItem.onItemclick(pos);
                    }
                }
            }
        });

        if (arrayList.get(i).getParentusers() == 0) {
            viewHolder.view2.setVisibility(View.GONE);
            viewHolder.view3.setVisibility(View.GONE);
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.imageView2.setVisibility(View.GONE);
        } else {
            viewHolder.view2.setVisibility(View.VISIBLE);
            viewHolder.view3.setVisibility(View.VISIBLE);
            viewHolder.imageView.setVisibility(View.VISIBLE);
            viewHolder.imageView2.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView roundedImageView;
        TextView textView;
        ImageView imageView, imageView2;
        View view2, view3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roundedImageView = itemView.findViewById(R.id.child_image);
            textView = itemView.findViewById(R.id.child_text);
            view2 = itemView.findViewById(R.id.view2);
            view3 = itemView.findViewById(R.id.view3);
            imageView = itemView.findViewById(R.id.imageadd2);
            imageView2 = itemView.findViewById(R.id.imageadd3);

        }
    }
}

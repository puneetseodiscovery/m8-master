package com.m8sworld.m8.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.m8sworld.m8.R;
import com.m8sworld.m8.RetrofitModel.TreeListApi;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {
    Context context;
    ArrayList<TreeListApi.Treeuser> arrayList = new ArrayList<>();
    onItemClick onItem;
    int a = 0;

    public interface onItemClick {
        void onItemclick(int pos,String name);
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

        String name = data.getName();
        String[] arrayList1 = name.split("~!~");
        String text = "<font color=#444862>"+arrayList1[0]+"</font> <br>" + arrayList1[1];
        //txtChf.setText("<font color=\"#444862\">" + response.body().getData().getName() + "</font>" + "\n" + comission);
        //txtChf.setText(Html.fromHtml(text));


        if (data.getIsBuyer()) {
            String textBuyer = "<font color=\"#EE373E\"><bold>"+"(Buyer)"+"</bold></font>";
            //viewHolder.textView.setText(Html.fromHtml(text)+"\n"+Html.fromHtml(textBuyer));
            text = text + "<br>" + textBuyer;
            viewHolder.textView.setText(Html.fromHtml(text));
        }
        else
        {
            viewHolder.textView.setText(Html.fromHtml(text));
        }


        Glide.with(context).load("https://app.m8s.world/public/upload/users/profiles/" + data.getProfileImage()).dontAnimate().dontTransform().placeholder(R.drawable.myaccount).into(viewHolder.roundedImageView);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItem != null) {
                    int pos = (data.getId());
                    if (pos != RecyclerView.NO_POSITION) {
                        onItem.onItemclick(pos,data.getName());
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

        if (i==arrayList.size()-1)
        {
            viewHolder.view.setVisibility(View.GONE);
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
        View view2, view3,view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roundedImageView = itemView.findViewById(R.id.child_image);
            textView = itemView.findViewById(R.id.child_text);
            view2 = itemView.findViewById(R.id.view2);
            view3 = itemView.findViewById(R.id.view3);
            view = itemView.findViewById(R.id.view);
            imageView = itemView.findViewById(R.id.imageadd2);
            imageView2 = itemView.findViewById(R.id.imageadd3);

        }
    }
}

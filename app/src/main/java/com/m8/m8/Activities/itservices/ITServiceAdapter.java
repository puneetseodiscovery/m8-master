package com.m8.m8.Activities.itservices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.m8.m8.R;

import java.util.ArrayList;

public class ITServiceAdapter extends RecyclerView.Adapter<ITServiceAdapter.MyViewHolder> {

    ArrayList<ITServiceResponseModel.Datum> arrayList;
    Context context;
    public static int lastCheckedPosition = -1;
    private RadioButton lastCheckedRB = null;

    public ITServiceAdapter(Context itServices, ArrayList<ITServiceResponseModel.Datum> arrayList) {
        this.arrayList = arrayList;
        this.context = itServices;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itservicelayout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.title.setText(arrayList.get(position).getName());
        Glide.with(context).load("http://app.m8s.world/upload/itservices/" + arrayList.get(position).getImage()).dontAnimate().dontTransform().into(holder.image);
        holder.checkbox.setChecked(position == lastCheckedPosition);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        RadioButton checkbox;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            checkbox = view.findViewById(R.id.checkbox);
            image = view.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = getAdapterPosition();
                    //because of this blinking problem occurs so
                    //i have a suggestion to add notifyDataSetChanged();
                    //   notifyItemRangeChanged(0, list.length);//blink list problem
                    notifyDataSetChanged();
                }
            });

        }
    }
}

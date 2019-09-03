package com.mandy.m8car.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mandy.m8car.Activities.CommingActivity;
import com.mandy.m8car.Activities.HomeActivity;

import com.mandy.m8car.R;
import com.mandy.m8car.RetrofitModel.GetStartCategoryApi;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mandy.m8car.ServiceGenerator;

import java.util.ArrayList;

public class StartAdapter extends RecyclerView.Adapter<StartAdapter.ViewHolder> {

    Context context;
    ArrayList<GetStartCategoryApi.Datum> arrayList = new ArrayList<>();

    public StartAdapter(Context context, ArrayList<GetStartCategoryApi.Datum> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.startimage, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        final GetStartCategoryApi.Datum getStartCategoryApi = arrayList.get(i);

        viewHolder.textView.setText(getStartCategoryApi.getName());


        if (getStartCategoryApi.getImage() == null) {
            viewHolder.imageView.setImageResource(R.drawable.ic_image);
        } else {
            Glide.with(context).load(ServiceGenerator.BASE_API_URL_FOR_START_CATEGORY + getStartCategoryApi.getImage().toString()).dontAnimate().dontTransform().into(viewHolder.imageView);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.getAdapterPosition() == 0) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Category", getStartCategoryApi.getId().toString());
                    editor.commit();

                    Intent intent = new Intent(v.getContext(), HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                } else {
                    Intent intent = new Intent(context, CommingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.startImage);
            textView = itemView.findViewById(R.id.textImage);

        }
    }
}

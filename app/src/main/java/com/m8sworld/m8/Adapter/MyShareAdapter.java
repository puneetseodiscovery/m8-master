package com.m8sworld.m8.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.m8sworld.m8.Fragments.DetailsFragment;
import com.m8sworld.m8.R;
import com.m8sworld.m8.RetrofitModel.GetSharedApi;
import com.wang.avi.AVLoadingIndicatorView;


import java.util.ArrayList;

public class MyShareAdapter extends RecyclerView.Adapter<MyShareAdapter.ViewHolder> {
    Context context;
    FragmentManager manager;
    ArrayList<GetSharedApi.Datum> arrayList = new ArrayList<>();

    public MyShareAdapter(Context context, FragmentManager manager, ArrayList<GetSharedApi.Datum> arrayList) {
        this.context = context;
        this.manager = manager;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custompotentail, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final GetSharedApi.Datum datum = arrayList.get(i);

        viewHolder.txtPrice.setText(datum.getCurrency() + " " + datum.getPrice());
        viewHolder.txtHeading.setText(datum.getTitle());
        if (datum.getMandate() != null) {
            viewHolder.txtComission.setText(datum.getCurrency() + " " + datum.getMandate().getCalculatePrice());
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsFragment detailsFragment = new DetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ItemId", datum.getId().toString());
                detailsFragment.setArguments(bundle);
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, detailsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        viewHolder.avLoadingIndicatorView.setVisibility(View.VISIBLE);


        Glide.with(context).load("https://app.m8s.world/public/upload/items/" + datum.getImages().get(0).getImage()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                viewHolder.avLoadingIndicatorView.setVisibility(View.GONE);
                return false;
            }
        }).into(viewHolder.imageView);
//        Glide.with(context).load("https://app.m8s.world/public/upload/items/" + datum.getImages().get(0).getImage()).dontAnimate().dontTransform().into(viewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtHeading, txtPrice, txtComission;
        ImageView imageView;
        AVLoadingIndicatorView avLoadingIndicatorView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtComission = itemView.findViewById(R.id.potential_commission);
            txtHeading = itemView.findViewById(R.id.potential_heading);
            txtPrice = itemView.findViewById(R.id.potential_price);
            imageView = itemView.findViewById(R.id.imageview);
            avLoadingIndicatorView = itemView.findViewById(R.id.avloading);

        }
    }
}

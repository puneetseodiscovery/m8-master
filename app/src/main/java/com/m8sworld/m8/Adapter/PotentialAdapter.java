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
import com.m8sworld.m8.RetrofitModel.GetPotentailApi;
import com.m8sworld.m8.util.SharedRate;
import com.wang.avi.AVLoadingIndicatorView;


import java.util.ArrayList;

public class PotentialAdapter extends RecyclerView.Adapter<PotentialAdapter.ViewHolder> {

    Context context;
    FragmentManager manager;
    ArrayList<GetPotentailApi.Datum> arrayList = new ArrayList<>();

    public PotentialAdapter(Context context, FragmentManager manager, ArrayList<GetPotentailApi.Datum> arrayList) {
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final GetPotentailApi.Datum datum = arrayList.get(i);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = datum.getId();
                DetailsFragment detailsFragment = new DetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ItemId", String.valueOf(id));
                detailsFragment.setArguments(bundle);
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, detailsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        viewHolder.txtHeading.setText(datum.getTitle());
        if (datum.getPrice() != null) {
            SharedRate sharedRate = new SharedRate(context);
            double price = Double.parseDouble(datum.getPrice()) * Double.parseDouble(sharedRate.getShared());
            viewHolder.txtPrice.setText(sharedRate.getCurrencyCode() + " " + String.format("%.2f", price));
        }

        if (datum.getMandate() != null) {
//            SharedRate sharedRate = new SharedRate(context);
//            double price = Double.parseDouble(datum.getMandate().getCalculatePrice()) * Double.parseDouble(sharedRate.getShared());
            //viewHolder.txtComission.setText(datum.getCurrency() + " " + datum.getMandate().getCalculatePrice());
            viewHolder.txtComission.setText(" CHF " + datum.getMandate().getCalculatePrice());
        } else {
            SharedRate sharedRate = new SharedRate(context);
            //viewHolder.txtComission.setText(sharedRate.getCurrencyCode() + " 0");
            viewHolder.txtComission.setText(" CHF " + " 0");

        }
        viewHolder.avLoadingIndicatorView.setVisibility(View.VISIBLE);
        Glide.with(context).load("https://app.m8s.world/public/upload/items/" + datum.getImages().get(0).getImage().toString())
                .listener(new RequestListener<Drawable>() {
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

        if (datum.getItemTreeStatus()!=null) {
            if (datum.getItemTreeStatus().equals("under_offer")) {
                viewHolder.itemStatus.setText(" Under offer ");
            }

            if (datum.getItemTreeStatus().equals("contract_exchange")) {
                viewHolder.itemStatus.setText(" Contracts exchanged ");
            }

            if (datum.getItemTreeStatus().equals("sale_completed")) {
                viewHolder.itemStatus.setText(" Sold ");
            }
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtHeading, txtComission, txtPrice,itemStatus;
        AVLoadingIndicatorView avLoadingIndicatorView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            txtComission = itemView.findViewById(R.id.potential_commission);
            txtHeading = itemView.findViewById(R.id.potential_heading);
            itemStatus = itemView.findViewById(R.id.itemStatus);
            txtPrice = itemView.findViewById(R.id.potential_price);
            avLoadingIndicatorView = itemView.findViewById(R.id.avloading);


        }
    }
}
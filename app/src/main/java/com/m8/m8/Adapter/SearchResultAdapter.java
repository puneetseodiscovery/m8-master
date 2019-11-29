package com.m8.m8.Adapter;

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
import com.m8.m8.Fragments.DetailsFragment;
import com.m8.m8.Fragments.ViewAllFragment;
import com.m8.m8.R;
import com.m8.m8.RetrofitModel.SearchApi;
import com.google.android.gms.common.util.CollectionUtils;
import com.m8.m8.util.SharedRate;
import com.m8.m8.util.SharedToken;
import com.wang.avi.AVLoadingIndicatorView;


import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    Context context;
    FragmentManager manager;
    ArrayList<SearchApi.Datum> arrayList = new ArrayList<>();

    public SearchResultAdapter(Context context, FragmentManager manager, ArrayList<SearchApi.Datum> arrayList) {
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

        final SearchApi.Datum datum = arrayList.get(i);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsFragment searchReultFragment = new DetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ItemId", datum.getId().toString());
                searchReultFragment.setArguments(bundle);
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, searchReultFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        if (datum.getPrice() != null) {
            SharedRate sharedRate = new SharedRate(context);
            double price = Double.parseDouble(datum.getPrice()) * Double.parseDouble(sharedRate.getShared());
            //viewHolder.txtPrice.setText(sharedRate.getCurrencyCode() + " " + String.format("%.2f", price));
            viewHolder.txtPrice.setText(datum.getCurrency() + " " + datum.getItemCurPrice());
        }

        if (datum.getMandate() != null) {
//            SharedRate sharedRate = new SharedRate(context);
//            double price = Double.parseDouble(datum.getMandate().getCalculatePrice()) * Double.parseDouble(sharedRate.getShared());
//            viewHolder.txtComission.setText(sharedRate.getCurrencyCode() + " " + String.format("%.2f", price));
            viewHolder.txtComission.setText(" CHF " + " " + datum.getMandate().getCalculatePrice().toString());
        } else {
            SharedRate sharedRate = new SharedRate(context);
            viewHolder.txtComission.setText(" CHF " + " 0");

        }

        SharedToken sharedToken = new SharedToken(context);
        String catid = sharedToken.getCatId();

        if (ViewAllFragment.catName!=null&&!ViewAllFragment.catName.equals("Property"))
        {
            String s = datum.getTitle();
            viewHolder.txtHeading.setText(s.replace("Bedroom",""));
        }
        else
        {
            viewHolder.txtHeading.setText(datum.getTitle());
        }

        //viewHolder.txtHeading.setText(datum.getTitle());
        viewHolder.avLoadingIndicatorView.setVisibility(View.VISIBLE);

        if (CollectionUtils.isEmpty(datum.getImages())) {
            viewHolder.imageView.setImageResource(R.drawable.ic_image);
        } else {

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
//            Glide.with(context).load("https://app.m8s.world/public/upload/items/" + datum.getImages().get(0).getImage()).dontAnimate().dontTransform().into(viewHolder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtHeading, txtPrice, txtComission;
        AVLoadingIndicatorView avLoadingIndicatorView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            txtComission = itemView.findViewById(R.id.potential_commission);
            txtHeading = itemView.findViewById(R.id.potential_heading);
            txtPrice = itemView.findViewById(R.id.potential_price);
            avLoadingIndicatorView = itemView.findViewById(R.id.avloading);
        }
    }

}

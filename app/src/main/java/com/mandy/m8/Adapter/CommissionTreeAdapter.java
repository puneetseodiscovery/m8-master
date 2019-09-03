package com.mandy.m8.Adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mandy.m8.R;
import com.mandy.m8.Fragments.CommissionSubFragment.CommissionDetailsFragment;
import com.mandy.m8.RetrofitModel.ComissionListApi;
import com.mandy.m8.util.SharedRate;

import java.util.ArrayList;

public class CommissionTreeAdapter extends RecyclerView.Adapter<CommissionTreeAdapter.ViewHolder> {

    Context context;
    FragmentManager manager;
    ArrayList<ComissionListApi.Datum> arrayList = new ArrayList<>();

    public CommissionTreeAdapter(Context context, FragmentManager manager, ArrayList<ComissionListApi.Datum> arrayList) {
        this.context = context;
        this.manager = manager;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.commissiontree, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final ComissionListApi.Datum datum = arrayList.get(i);

        viewHolder.mProperty.setText(datum.getTitle());
        if (datum.getPrice() != null) {
            SharedRate sharedRate = new SharedRate(context);
            double price = Double.parseDouble(datum.getPrice()) * Double.parseDouble(sharedRate.getShared());
            viewHolder.mPrice.setText(sharedRate.getCurrencyCode() + " " + String.format("%.2f", price));
        }

        if (datum.getMandate() != null) {
            SharedRate sharedRate = new SharedRate(context);
            double price = Double.parseDouble(datum.getMandate().getCalculatePrice()) * Double.parseDouble(sharedRate.getShared());
            viewHolder.mCommission.setText(sharedRate.getCurrencyCode() + " " + String.format("%.2f", price));
        } else {
            SharedRate sharedRate = new SharedRate(context);
            viewHolder.mCommission.setText(sharedRate.getCurrencyCode() + " 0");

        }
        final String id = String.valueOf(datum.getId());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommissionDetailsFragment commissionDetailsFragment = new CommissionDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("PropertyId", id);
                if (datum.getMandate() != null) {
                    bundle.putString("comission", viewHolder.mCommission.getText().toString());
                } else {
                    bundle.putString("comission", " 0");

                }
                bundle.putString("price", viewHolder.mPrice.getText().toString());
                bundle.putString("name", datum.getTitle());
                bundle.putString("image", datum.getImages().get(0).getImage());
                commissionDetailsFragment.setArguments(bundle);
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, commissionDetailsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTree, mProperty, mPrice, mCommission;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTree = itemView.findViewById(R.id.R_mytree);
            mCommission = itemView.findViewById(R.id.R_commission);
            mPrice = itemView.findViewById(R.id.R_price);
            mProperty = itemView.findViewById(R.id.R_property);
            imageView = itemView.findViewById(R.id.R_image);
        }
    }
}

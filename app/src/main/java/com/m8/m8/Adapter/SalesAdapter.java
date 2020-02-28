package com.m8.m8.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.m8.m8.Activities.MyM8sEarningDetailActivity;
import com.m8.m8.Models.SalesModel;
import com.m8.m8.R;

import java.util.ArrayList;

public class SalesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<SalesModel.Datum> arrayList;
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEMS = 1;
    private LayoutInflater inflater;

    public SalesAdapter(Context context, ArrayList<SalesModel.Datum> arrayList) {

        this.context = context;
        this.arrayList = arrayList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEMS) {
            // Here Inflating your recyclerview item layout
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.saleslayout, parent, false);
            return new MyViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            // Here Inflating your header view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_content_header, parent, false);
            return new HeaderViewHolder(itemView);
        }
        else return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder){

            final MyViewHolder itemViewHolder = (MyViewHolder) holder;

            // Following code give a row of header and decrease the one position from listview items
            final SalesModel.Datum data = arrayList.get(position-1);

            // You have to set your listview items values with the help of model class and you can modify as per your needs

            itemViewHolder.date.setText(data.getDate());
            itemViewHolder.refereed_name.setText(data.getItemLink());
            itemViewHolder.referrer.setText(data.getSalesPrice());
            itemViewHolder.package_purchased.setText(data.getCommissionOffered());
            itemViewHolder.date_paid.setText(data.getCommissionEarned());
            itemViewHolder.amount_paid_out.setText(data.getDatePaid());

            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MyM8sEarningDetailActivity.class);
                    intent.putExtra("ItemId",data.getItemId().toString());
                    context.startActivity(intent);
                }
            });
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEMS;
    }

    @Override
    public int getItemCount() {
        return arrayList.size()+1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, refereed_name, referrer,package_purchased,date_paid,amount_paid_out;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            refereed_name = (TextView) view.findViewById(R.id.refereed_name);
            referrer = (TextView) view.findViewById(R.id.referrer);
            package_purchased = (TextView) view.findViewById(R.id.package_purchased);
            date_paid = (TextView) view.findViewById(R.id.date_paid);
            amount_paid_out = (TextView) view.findViewById(R.id.amount_paid_out);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View view) {
            super(view);
        }
    }

}
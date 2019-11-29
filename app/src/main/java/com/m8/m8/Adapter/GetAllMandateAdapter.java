package com.m8.m8.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m8.m8.R;
import com.m8.m8.RetrofitModel.GetAllMandate;

import java.util.ArrayList;

public class GetAllMandateAdapter extends RecyclerView.Adapter<GetAllMandateAdapter.ViewHolder> {

    Context context;
    ArrayList<GetAllMandate.Datum> allMandates = new ArrayList<>();


    public GetAllMandateAdapter(Context context, ArrayList<GetAllMandate.Datum> allMandates) {
        this.context = context;
        this.allMandates = allMandates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.customdoc, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        final GetAllMandate.Datum datum = allMandates.get(i);

        viewHolder.textView1.setText(datum.getItem().getTitle());
        viewHolder.textView2.setText(datum.getMandatePdf());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://app.m8s.world/public/upload/items/mandate/"+datum.getMandatePdf();
                Log.d("Url123", url);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return allMandates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.text1);
            textView2 = itemView.findViewById(R.id.text2);
        }
    }
}

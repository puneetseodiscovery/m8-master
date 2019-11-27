package com.m8sworld.m8.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m8sworld.m8.R;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {
    Context context;
    String[] room;
    int pos = -1;

    private OnItemClickListener itemClickListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public RoomsAdapter(Context context, String[] rooms) {
        this.context = context;
        this.room = rooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_rooms, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.textView.setText(room[i]);
        final int number = viewHolder.getAdapterPosition() + 1;

        if (pos == viewHolder.getAdapterPosition()) {
            viewHolder.textView.setBackgroundResource(R.drawable.buttonback);
            viewHolder.textView.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            viewHolder.textView.setBackgroundResource(R.drawable.roomback);
            viewHolder.textView.setTextColor(context.getResources().getColor(R.color.black));
        }

        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = viewHolder.getAdapterPosition();
                notifyDataSetChanged();
                if (itemClickListener != null) {
                    int postin = number;
                    if (postin != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClick(postin);
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return room.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.rooms);
        }
    }
}

package com.example.kfa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class startingAdapter extends RecyclerView.Adapter {
    ArrayList<String> name = new ArrayList<>();
    Context context;

    public startingAdapter(Context context, ArrayList<String> name) {
        this.context = context;
        this.name = name;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.starting_item,parent,false);
        startingAdapter.MyViewHolder viewHolder = new startingAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        startingAdapter.MyViewHolder myViewHolder = (startingAdapter.MyViewHolder)holder;
        myViewHolder.playerName.setText(name.get(position));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView playerName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.startingXi);
        }
    }
}

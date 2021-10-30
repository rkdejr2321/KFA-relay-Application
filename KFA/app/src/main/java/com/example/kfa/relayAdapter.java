package com.example.kfa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class relayAdapter extends RecyclerView.Adapter {
    private ArrayList<Relay> relays = new ArrayList<>();
    Context context;
    public relayAdapter(Context context, ArrayList<Relay> relays){
        this.context = context;
        this.relays = relays;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.relay_item,parent,false);
        relayAdapter.MyViewHolder viewHolder = new relayAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        relayAdapter.MyViewHolder myViewHolder = (relayAdapter.MyViewHolder)holder;
        if(relays.get(position).getRelay().isEmpty()){
            myViewHolder.relay.setText("경기 시작 전 입니다");
        }
        else{
            myViewHolder.relay.setText(relays.get(position).getRelay());
        }
    }

    @Override
    public int getItemCount() {
        return relays.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView relay;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            relay = itemView.findViewById(R.id.relay_text);
        }
    }
}

package com.example.kfa;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class communityAdapter extends RecyclerView.Adapter {
    private ArrayList<Community> communities = new ArrayList<>();
    Context context;
    public communityAdapter(Context context, ArrayList<Community> communities){
        this.context = context;
        this.communities = communities;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item,parent,false);
        communityAdapter.MyViewHolder viewHolder = new communityAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        communityAdapter.MyViewHolder myViewHolder = (communityAdapter.MyViewHolder)holder;
        myViewHolder.community_title.setText(communities.get(position).getCommunity_title());
        myViewHolder.community_content.setText(communities.get(position).getCommunity_content());
        myViewHolder.community_time.setText(toTimeStamp(communities.get(position).getTime()));
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), communityPostActivity.class);
                intent.putExtra("post1",communities.get(position));
                context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    public int getItemCount() {
        return communities.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView community_title;
        TextView community_content;
        TextView community_time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            community_title = itemView.findViewById(R.id.commu_titleID);
            community_content = itemView.findViewById(R.id.commu_contentID);
            community_time = itemView.findViewById(R.id.commu_timeID);
        }
    }
    public String toTimeStamp(long num){
        Date toTimeStamp = new Date(num);
        SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.KOREA);
        return datef.format(toTimeStamp) ;
    }
}

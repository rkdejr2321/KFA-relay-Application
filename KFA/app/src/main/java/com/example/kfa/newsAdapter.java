package com.example.kfa;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class newsAdapter extends RecyclerView.Adapter {
    private ArrayList<News> newsList = new ArrayList<>();
    Context context;

    public newsAdapter(Context context,ArrayList<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }
    public void addNews(ArrayList<News> newsList){
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder)holder;

        myViewHolder.title.setText(newsList.get(position).getNews_title());
        myViewHolder.summary.setText(newsList.get(position).getNews_content());
        myViewHolder.title.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Toast.makeText(context,position+"번째 아이템",Toast.LENGTH_SHORT).show();
          }
      });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView summary;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.news_title);
            summary = itemView.findViewById(R.id.news_summary);
        }
    }
}

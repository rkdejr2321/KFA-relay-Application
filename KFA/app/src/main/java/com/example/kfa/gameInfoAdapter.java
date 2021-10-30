package com.example.kfa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class gameInfoAdapter extends RecyclerView.Adapter {
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference rootRef = firebaseStorage.getReference().child("Flag_pic");
    private ArrayList<GameInfo> gameInfos = new ArrayList<>();
    Context context;
    public gameInfoAdapter(Context context, ArrayList<GameInfo> gameInfos){
        this.context = context;
        this.gameInfos = gameInfos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_info_item,parent,false);
        gameInfoAdapter.MyViewHolder viewHolder = new gameInfoAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        gameInfoAdapter.MyViewHolder myViewHolder = (gameInfoAdapter.MyViewHolder)holder;
        StorageReference homeImgRef = rootRef.child(gameInfos.get(position).getHomeFlag());
        StorageReference awayImgRef = rootRef.child(gameInfos.get(position).getAwayFlag());

        if(homeImgRef != null){
            homeImgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(myViewHolder.homeFlag);
                }
            });
        }
        if(awayImgRef != null){
            awayImgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(myViewHolder.awayFlag);
                }
            });
        }
        myViewHolder.homeName.setText(gameInfos.get(position).getHomeName());
        myViewHolder.awayName.setText(gameInfos.get(position).getAwayName());
        if(gameInfos.get(position).getTitle().isEmpty()){
            myViewHolder.title.setText("미정");
        }
        else
        {
            myViewHolder.title.setText(gameInfos.get(position).getTitle());
        }
        if(gameInfos.get(position).getInfo().isEmpty()){
            myViewHolder.info.setText("미정");
        }
        else{
            myViewHolder.info.setText(gameInfos.get(position).getInfo());
        }
        myViewHolder.homeScore.setText(gameInfos.get(position).getHomeScore());
        myViewHolder.awayScore.setText(gameInfos.get(position).getAwayScore());

        if(!gameInfos.get(position).getHomeScore().isEmpty() && !gameInfos.get(position).getAwayScore().isEmpty()){
            myViewHolder.homeScore.setVisibility(View.VISIBLE);
            myViewHolder.awayScore.setVisibility(View.VISIBLE);
            if(Integer.parseInt(gameInfos.get(position).getHomeScore()) > Integer.parseInt(gameInfos.get(position).getAwayScore())){
                myViewHolder.homeScore.setTextColor(Color.RED);
            }
            else if(Integer.parseInt(gameInfos.get(position).getHomeScore()) < Integer.parseInt(gameInfos.get(position).getAwayScore())){
                myViewHolder.awayScore.setTextColor(Color.RED);
            }
        }
        else {

        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(),GameActivity.class);
                intent.putExtra("game_info",gameInfos.get(position));
                context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });

    }

    @Override
    public int getItemCount() {
        return gameInfos.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView homeName;
        TextView awayName;
        TextView title;
        TextView info;
        TextView homeScore;
        TextView awayScore;
        ImageView homeFlag;
        ImageView awayFlag;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            homeFlag = itemView.findViewById(R.id.HomeTeamLogo);
            awayFlag = itemView.findViewById(R.id.AwayTeamLogo);
            homeName = itemView.findViewById(R.id.homeName);
            awayName = itemView.findViewById(R.id.awayName);
            title = itemView.findViewById(R.id.title);
            info = itemView.findViewById(R.id.info);
            homeScore = itemView.findViewById(R.id.homeScore);
            awayScore = itemView.findViewById(R.id.awayScore);
        }
    }
}

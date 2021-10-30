package com.example.kfa;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class depthAdapter extends RecyclerView.Adapter {
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference rootRef = firebaseStorage.getReference().child("Player_pic");
    private ArrayList<Player> players = new ArrayList<>();
    Context context;

    public depthAdapter(Context context, ArrayList<Player> players){
        this.context = context;
        this.players = players;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.depth_item,parent,false);
        depthAdapter.MyViewHolder viewHolder = new depthAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        depthAdapter.MyViewHolder myViewHolder = (depthAdapter.MyViewHolder)holder;
        StorageReference playerImg = rootRef.child(players.get(position).getImg());
        if(playerImg != null){
            playerImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(myViewHolder.playerPic);
                }
            });
        }
        myViewHolder.playerName.setText(players.get(position).getName());
        myViewHolder.playerPosition.setText(players.get(position).getPos());
        myViewHolder.playerAge.setText(String.valueOf(players.get(position).getAge()));
        myViewHolder.playerPhysical.setText(players.get(position).getHeight() + "/" + players.get(position).getWeight());
        myViewHolder.playerClubTeam.setText(players.get(position).getTeam());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView playerName;
        TextView playerClubTeam;
        TextView playerAge;
        TextView playerPosition;
        TextView playerPhysical;
        ImageView playerPic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.player_name);
            playerClubTeam  = itemView.findViewById(R.id.player_clubTeam);
            playerAge = itemView.findViewById(R.id.player_age);
            playerPhysical = itemView.findViewById(R.id.player_physical);
            playerPosition = itemView.findViewById(R.id.player_position);
            playerPic = itemView.findViewById(R.id.player_pic);
        }
    }
}

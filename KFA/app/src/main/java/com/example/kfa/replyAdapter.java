package com.example.kfa;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.LinkAddress;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class replyAdapter extends RecyclerView.Adapter {
    ArrayList<Reply> replies = new ArrayList<>();
    EditText editText = communityPostActivity.getEditText();
    Context context;
    boolean click = false;
    Reply currentReply;
    public replyAdapter(Context context, ArrayList<Reply> replies) {
        this.context = context;
        this.replies = replies;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_item, parent, false);
        replyAdapter.MyViewHolder viewHolder = new replyAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        replyAdapter.MyViewHolder myViewHolder = (replyAdapter.MyViewHolder) holder;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        myViewHolder.reply.setText(replies.get(position).getReplyText());
        myViewHolder.nestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.showSoftInput(editText,0);
                click = true;
                currentReply = replies.get(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView reply;
        ImageButton nestBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reply = itemView.findViewById(R.id.reply_text);
            nestBtn = itemView.findViewById(R.id.nestedBtn);
        }
    }

}

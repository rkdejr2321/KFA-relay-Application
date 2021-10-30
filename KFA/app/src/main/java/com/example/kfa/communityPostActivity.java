package com.example.kfa;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class communityPostActivity extends AppCompatActivity {
    private static EditText reply_text;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    ArrayList<Reply> replies = new ArrayList<>();
    replyAdapter adapter;

    RecyclerView recyclerView;

    public static EditText getEditText(){
        return reply_text;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_post);


        reply_text = (EditText) findViewById(R.id.reply_input);
        Button reply_uploadBtn = (Button) findViewById(R.id.reply_uploadBtn);
        Intent intent = getIntent();

        Community community = (Community) intent.getSerializableExtra("post1");
        TextView postTitle = (TextView) findViewById(R.id.post_titleID);
        TextView postContent = (TextView) findViewById(R.id.post_contentID);
        TextView postTime = (TextView) findViewById(R.id.post_timeID);

        postTitle.setText(community.getCommunity_title());
        postContent.setText(community.getCommunity_content());
        postTime.setText(toTimeStamp(community.getTime()));

        mDatabase.child("post").child(community.getPostID()).child("reply").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                replies.clear();
                recyclerView = findViewById(R.id.reply_recycler);
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Reply reply = snapshot.getValue(Reply.class);
                    replies.add(reply);
                }
                adapter = new replyAdapter(getBaseContext(), replies);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reply_uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reply_text.getText().toString().isEmpty()) {
                    Toast.makeText(communityPostActivity.this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference pushedPostRef = mDatabase.push();
                    Reply reply = new Reply(community.getPostID(), reply_text.getText().toString().trim(), pushedPostRef.getKey());
                   if(adapter.click == false){
                       mDatabase.child("post").child(reply.getParentID()).child("reply").child(reply.getReplyID()).setValue(reply);
                   }
                   else if (adapter.click){
                       Reply nestReply = adapter.currentReply;
                       mDatabase.child("post").child(nestReply.getParentID()).child("reply").child(nestReply.getReplyID()).child("nested").child(reply.getReplyID()).setValue(reply);
                       adapter.click = false;
                   }
                }
                keyBoardReset(reply_text);
            }
        });

    }
    //long 타입의 타임스탬프 -> 연도-월-일 형태로 변환 함수
    public String toTimeStamp(long num) {
        Date toTimeStamp = new Date(num);
        SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.KOREA);
        return datef.format(toTimeStamp);
    }
    //키보드 입력창 초기화 및 키보드 내림 함수
    public void keyBoardReset(EditText text) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        text.setText(null);
        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
    }
   
}

package com.example.kfa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.internal.InternalTokenProvider;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class uploadActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference pushedPostRef = mDatabase.push();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_upload);
        Button upload_cancelBtn = (Button) findViewById(R.id.upload_cancelBtn);
        Button upload_completeBtn = (Button)findViewById(R.id.upload_completeBtn);
        EditText title = (EditText) findViewById(R.id.upload_title);
        EditText content = (EditText) findViewById(R.id.upload_content);
        upload_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        upload_completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().trim().isEmpty() || content.getText().toString().trim().isEmpty()){
                    Toast.makeText(uploadActivity.this,"내용을 입력하세요.",Toast.LENGTH_SHORT).show();
                }
                else{
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    long time = timestamp.getTime();
                    Community community = new Community(title.getText().toString().trim(),content.getText().toString().trim(),time,pushedPostRef.getKey());
                    mDatabase.child("post").child(pushedPostRef.getKey()).setValue(community);
                    Intent intent = new Intent(getApplicationContext(),communityActivity.class);
                    startActivity(intent);
                    Toast.makeText(uploadActivity.this,"등록이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

package com.example.kfa;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;;
import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {
    gameInfoAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<GameInfo> Home_vs_Away = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("KFA");

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        ImageButton depth = (ImageButton) findViewById(R.id.depthBtn);
        ImageButton community = (ImageButton) findViewById(R.id.communityBtn);
        ImageButton news = (ImageButton) findViewById(R.id.newsBtn);
        ImageButton btnFacebook = (ImageButton) findViewById(R.id.facebookID);
        ImageButton btnInstargam = (ImageButton) findViewById(R.id.intargramID);
        ImageButton btnTwitter = (ImageButton) findViewById(R.id.twitterID);
        ImageButton btnYoutube = (ImageButton) findViewById(R.id.youtubeID);


        depth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), depthActivity.class);
                startActivity(intent);
            }
        });

        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), communityActivity.class);
                startActivity(intent);
            }
        });

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                startActivity(intent);
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //페이스북 어플로 실행
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.facebook.com/KFA/")).setPackage("com.facebook.android"));
                } catch (ActivityNotFoundException e) {
                    //만약 페이스북 어플이 없다면 브라우저로 실행
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/KFA/")));
                }

            }
        });

        btnInstargam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.instagram.com/thekfa/")).setPackage("com.instargram.android"));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/thekfa/")));
                }
            }
        });

        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://twitter.com/thekfa")).setPackage("com.twitter.android"));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/thekfa")));
                }
            }
        });

        btnYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.youtube.com/thekfa")).setPackage("com.google.android.youtube"));
            }
        });

        db.collection("Game_info").orderBy("time")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String URL = (String) document.get("URL");
                                            if(!URL.isEmpty()){
                                                Document doc = Jsoup.connect(URL).get();
                                                String PATH = (String) document.get("path");
                                                Elements homeScore = doc.select(".home");
                                                Elements awayScore = doc.select(".away");
                                                Element title = doc.select(".api_title").get(0);
                                                Elements info = doc.select(".play_info");
                                                db.collection("Game_info").document(PATH).update("homeScore", homeScore.text(),
                                                        "awayScore",awayScore.text(),"title",title.text(),"info",info.text());
                                            }
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                            Home_vs_Away.clear();
                            for (QueryDocumentSnapshot dc : task.getResult()) {
                                GameInfo g = dc.toObject(GameInfo.class);
                                Home_vs_Away.add(g);
                            }
                            Collections.reverse(Home_vs_Away);
                            recyclerView = findViewById(R.id.game_recyclerView);
                            adapter = new gameInfoAdapter(getBaseContext(), Home_vs_Away);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.HORIZONTAL, true));
                        }
                    }

                });
    }

}
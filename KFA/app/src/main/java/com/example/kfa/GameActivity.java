package com.example.kfa;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends Activity {
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference rootRef = firebaseStorage.getReference().child("Flag_pic");
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseReference mDatabase;
    final Bundle bundle = new Bundle();
    Handler handler;
    ArrayList<String> relays = new ArrayList<>();
    ArrayList<Relay> relayArrayList = new ArrayList<>();
    relayAdapter adapter;
    startingAdapter startingAdapter;
    RecyclerView recyclerView;
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_deatail);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        WebView webView = new WebView(this);
        LinearLayout liHome = (LinearLayout) findViewById(R.id.homeDepth);
        LinearLayout liAway = (LinearLayout) findViewById(R.id.awayDepth);
        LinearLayout liGame = (LinearLayout) findViewById(R.id.gameReplay);

        TextView homeName = (TextView) findViewById(R.id.homeName);
        TextView awayName = (TextView) findViewById(R.id.awayName);
        TextView title = (TextView) findViewById(R.id.gameType);
        TextView info = (TextView) findViewById(R.id.gameInfo);
        TextView homeScore = (TextView) findViewById(R.id.homeScore);
        TextView awayScore = (TextView) findViewById(R.id.awayScore);
        ImageView homeFlag = (ImageView) findViewById(R.id.HomeTeamLogo);
        ImageView awayFlag = (ImageView) findViewById(R.id.AwayTeamLogo);

        GameInfo game = (GameInfo) intent.getSerializableExtra("game_info");
        StorageReference homeImgRef = rootRef.child(game.getHomeFlag());
        StorageReference awayImgRef = rootRef.child(game.getAwayFlag());

        if (homeImgRef != null) {
            homeImgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(GameActivity.this).load(uri).into(homeFlag);
                }
            });
        }
        if (awayImgRef != null) {
            awayImgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(GameActivity.this).load(uri).into(awayFlag);
                }
            });
        }

        homeName.setText(game.getHomeName());
        awayName.setText(game.getAwayName());
        title.setText(game.getTitle());
        info.setText(game.getInfo());
        if (!game.getHomeScore().isEmpty() && !game.getAwayScore().isEmpty()) {
            homeScore.setText(game.getHomeScore());
            homeScore.setVisibility(View.VISIBLE);
            awayScore.setText(game.getAwayScore());
            awayScore.setVisibility(View.VISIBLE);
            if (Integer.valueOf(game.getHomeScore()) > Integer.valueOf(game.getAwayScore())) {
                homeScore.setTextColor(Color.RED);
            } else if (Integer.valueOf(game.getHomeScore()) < Integer.valueOf(game.getAwayScore())) {
                awayScore.setTextColor(Color.RED);
            }
        } else {
            homeScore.setText("0");
            awayScore.setText("0");
        }
        Button homeBtn = (Button) findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liHome.setVisibility(View.VISIBLE);
                liAway.setVisibility(View.INVISIBLE);
                liGame.setVisibility(View.INVISIBLE);
            }
        });
        Button awayBtn = (Button) findViewById(R.id.awayBtn);
        awayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liHome.setVisibility(View.INVISIBLE);
                liAway.setVisibility(View.VISIBLE);
                liGame.setVisibility(View.INVISIBLE);
            }
        });
        Button gameBtn = (Button) findViewById(R.id.gameBtn);
        gameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liHome.setVisibility(View.INVISIBLE);
                liAway.setVisibility(View.INVISIBLE);
                liGame.setVisibility(View.VISIBLE);
            }
        });
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.setNetworkAvailable(true);
        webView.getSettings().setJavaScriptEnabled(true);

        //// Sets whether the DOM storage API is enabled.
        webView.getSettings().setDomStorageEnabled(true);

        // 자바스크립트인터페이스 연결
        // 이걸 통해 자바스크립트 내에서 자바함수에 접근할 수 있음.
        webView.addJavascriptInterface(new MyJavascriptInterface(), "Android");
        // 페이지가 모두 로드되었을 때, 작업 정의
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 자바스크립트 인터페이스로 연결되어 있는 getHTML를 실행
                // 자바스크립트 기본 메소드로 html 소스를 통째로 지정해서 인자로 넘김
                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);");
            }
        });
        //지정한 URL을 웹 뷰로 접근하기
        webView.loadUrl(game.getRelay_url());
        //문자중계 업데이트
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                relays = bundle.getStringArrayList("test");
                db.collection("Game_info").document(game.getPath()).update("relay", relays);
                db.collection("Game_info").document(game.getPath()).update("homeStarting",bundle.getStringArrayList("homeStarting"));
                recyclerView = findViewById(R.id.homeStartingRecycler);
                startingAdapter = new startingAdapter(getBaseContext(),bundle.getStringArrayList("homeStarting"));
                recyclerView.setAdapter(startingAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false));
            }
        };
        db.collection("Game_info").document(game.getPath()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    ArrayList<String> relay = (ArrayList<String>) document.getData().get("relay");
                    for(String text : relay){
                        relayArrayList.add(new Relay(text));
                    }
                }
                recyclerView = findViewById(R.id.relay_recycler);
                adapter = new relayAdapter(getBaseContext(), relayArrayList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false));
            }
        });
    }
    public class MyJavascriptInterface {
        @JavascriptInterface
        public void getHtml(String html) {
            //위 자바스크립트가 호출되면 여기로 html이 반환됨
            new Thread(){
                @Override
                public void run(){
                    try {
                        Document document = Jsoup.parse(html);
                        Elements el = document.select(".list_relay");
                        Elements el1 = document.select(".lineup_match");
                        ArrayList<String> relays = new ArrayList<>();
                        ArrayList<String> homeStarting = new ArrayList<>();
                        ArrayList<String> awayStarting = new ArrayList<>();
                        int count = 0;
                        for(Element element1 : el1.select("li")){
                            if(count > 10){
                                homeStarting.add(element1.text());
                                Log.v("이름",element1.text());
                            }
                            else
                                awayStarting.add(element1.text());
                        }
                        Elements elements = el.select("li");
                        for(Element element : elements ) {
                            relays.add(element.text());
                        }
                        bundle.putStringArrayList("test",relays);
                        bundle.putStringArrayList("homeStarting",homeStarting);
                        bundle.putStringArrayList("awayStarting",awayStarting);
                        Message msg = handler.obtainMessage();
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(GameActivity.this, MainActivity.class); //지금 액티비티에서 다른 액티비티로 이동하는 인텐트 설정
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    //인텐트 플래그 설정
        startActivity(intent);  //인텐트 이동
        finish();   //현재 액티비티 종료
    }
}

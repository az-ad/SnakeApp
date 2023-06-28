package com.example.snake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class AboutUsActivity extends AppCompatActivity {
    private YouTubePlayerView youTubePlayerView;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        webView = findViewById(R.id.aboutus);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/index.html");

        //--------------for youTube video play------------------------------------------//
//        youTubePlayerView = findViewById(R.id.activity_main_youtubePlayerView);
//        getLifecycle().addObserver(youTubePlayerView);
//        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                String videoId = "9iVua8tAtVk";
//                youTubePlayer.loadVideo(videoId, 0);
//                //youTubePlayer.cueVideo(videoId, 0);
//            }
//        });
    }
}
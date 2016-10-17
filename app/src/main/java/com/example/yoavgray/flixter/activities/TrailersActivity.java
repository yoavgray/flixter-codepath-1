package com.example.yoavgray.flixter.activities;

import android.os.Bundle;
import android.widget.Toast;

import com.example.yoavgray.flixter.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class TrailersActivity extends YouTubeBaseActivity {

    private static final String TRAILER_KEY_TAG = "trailerKey";
    private static final String YOUTUBE_API_KEY = "AIzaSyCJ9k8BXfHITa6SxVQ-nnfOqBcgwC1aWvY\t";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);
        final String trailerKey = getIntent().getStringExtra(TRAILER_KEY_TAG);
        YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.youtube_player_trailers);

        youTubePlayerView.initialize(YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        youTubePlayer.loadVideo(trailerKey);
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        Toast.makeText(getBaseContext(),
                                "Unable to load movie. Please try again soon",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

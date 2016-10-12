package com.example.yoavgray.flixter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.yoavgray.flixter.R;
import com.example.yoavgray.flixter.models.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String MOVIE_TAG = "movieExtra";

    @BindView(R.id.image_view_details_poster) ImageView posterImageView;
    @BindView(R.id.ratingBar) RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        Movie movie = getIntent().getParcelableExtra(MOVIE_TAG);
        getSupportActionBar().hide();
        String moviePosterUrl = movie.getPosterPath();
        Picasso.with(this).load(moviePosterUrl).fit().centerInside()
                .placeholder(R.drawable.progress_image)
//                .error(R.drawable.user_placeholder_error)
                .into(posterImageView);
        ratingBar.setNumStars(5);
        ratingBar.setRating((float)2.7);
        // Slide in from right
        overridePendingTransition(R.anim.transition_from_right, R.anim.transition_fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Slide out to right
        overridePendingTransition(R.anim.transition_fade_in, R.anim.transition_to_right);
    }
}

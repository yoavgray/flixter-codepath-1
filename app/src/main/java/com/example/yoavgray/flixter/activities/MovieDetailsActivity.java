package com.example.yoavgray.flixter.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yoavgray.flixter.R;
import com.example.yoavgray.flixter.models.Movie;
import com.example.yoavgray.flixter.models.Review;
import com.example.yoavgray.flixter.models.ReviewResults;
import com.example.yoavgray.flixter.models.TrailerResult;
import com.example.yoavgray.flixter.widgets.ReviewView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String MOVIE_TAG = "movieExtra";
    private static final String API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private static final String TRAILER_KEY_TAG = "trailerKey";
    private static final String SHOULD_PLAY_TAG = "shouldPlay";
    private AsyncHttpClient client;
    private String trailerKey;
    private boolean shouldPlay;

    @BindView(R.id.image_view_details_poster) ImageView posterImageView;
    @BindView(R.id.image_view_details_play) ImageView playImageView;
    @BindView(R.id.text_view_details_title) TextView titleTextView;
    @BindView(R.id.text_view_details_overview) TextView overviewTextView;
    @BindView(R.id.text_view_popularity_number) TextView popularityTextView;
    @BindView(R.id.text_view_details_release_date) TextView releaseDateTextView;
    @BindView(R.id.ratingBar) RatingBar ratingBar;
    @BindView(R.id.linear_layout_reviews) LinearLayout reviewsLayout;
    @BindView(R.id.text_view_no_reviews) TextView noReviewsTextView;
    @BindView(R.id.progress_bar_movie_reviews) ProgressBar reviewsProgressBar;
    @BindString(R.string.unable_to_fetch_trailer) String movieTrailerErrorString;
    @BindString(R.string.unable_to_fetch_reviews) String reviewsErrorString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        Intent startingIntent = getIntent();
        if (startingIntent != null) {
            shouldPlay = startingIntent.getBooleanExtra(SHOULD_PLAY_TAG, false);
        }
        client = new AsyncHttpClient();
        Movie movie = getIntent().getParcelableExtra(MOVIE_TAG);
        getSupportActionBar().hide();

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Picasso.with(this).load(movie.getPosterPath())
                    .placeholder(R.drawable.progress_image)
                    .into(posterImageView);
        } else { // Landscape
            Picasso.with(this).load(movie.getBackdropPath())
                    .fit().centerInside()
                    .placeholder(R.drawable.progress_image)
                    .into(posterImageView);
        }

        if (ratingBar != null) ratingBar.setRating((float) (movie.getVoteAverage() / 2));
        if (titleTextView != null) titleTextView.setText(movie.getTitle());
        if (popularityTextView != null) popularityTextView.setText(movie.getVoteAverage().toString());
        if (overviewTextView != null) overviewTextView.setText(movie.getOverview());
        if (releaseDateTextView != null) releaseDateTextView.setText(movie.getReleaseDate());

        getTrailer(movie.getId());
        getReviews(movie.getId());

        // Slide in from right
        overridePendingTransition(R.anim.transition_from_right, R.anim.transition_fade_out);
    }

    private void getTrailer(Integer id) {
        RequestParams params = new RequestParams();
        String url = "https://api.themoviedb.org/3/movie/" + id.toString() + "/videos?";
        params.put("language", "en");
        params.put("api_key", API_KEY);

        client.get(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Toast.makeText(getBaseContext(), movieTrailerErrorString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder().create();
                // Define Response class to correspond to the JSON response returned
                TrailerResult trailerResults = gson.fromJson(responseString, TrailerResult.class);
                trailerKey = (trailerResults.getResults())[0].getKey();
                if (shouldPlay) {
                    trailerPlay();
                }
            }
        });
    }

    private void getReviews(Integer id) {
        RequestParams params = new RequestParams();
        String url = "https://api.themoviedb.org/3/movie/" + id.toString() + "/reviews?";
        params.put("language","en");
        params.put("api_key", API_KEY);

        client.get(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getBaseContext(),reviewsErrorString,Toast.LENGTH_SHORT).show();
                reviewsProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder().create();
                // Define Response class to correspond to the JSON response returned
                ReviewResults movieResults = gson.fromJson(responseString, ReviewResults.class);
                Review[] reviews = movieResults.getResults();

                reviewsProgressBar.setVisibility(View.GONE);
                if (reviews.length == 0) {
                    noReviewsTextView.setVisibility(View.VISIBLE);
                }

                // Adding views dynamically
                for (Review review : reviews) {
                    ReviewView reviewView = new ReviewView(getBaseContext());
                    reviewView.setReview(review);
                    reviewsLayout.addView(reviewView);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Slide out to right
        overridePendingTransition(R.anim.transition_fade_in, R.anim.transition_to_right);
    }

    @OnClick(R.id.image_view_details_play)
    public void trailerPlay() {
        Intent i = new Intent(this, TrailersActivity.class);
        i.putExtra(TRAILER_KEY_TAG, trailerKey);
        startActivity(i);
    }
}

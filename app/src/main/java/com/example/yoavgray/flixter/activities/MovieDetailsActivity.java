package com.example.yoavgray.flixter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String MOVIE_TAG = "movieExtra";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        Intent startingIntent = getIntent();
        if (startingIntent != null) {
            shouldPlay = startingIntent.getBooleanExtra("shouldPlay", false);
        }
        client = new AsyncHttpClient();
        Movie movie = getIntent().getParcelableExtra(MOVIE_TAG);
        getSupportActionBar().hide();
        String moviePosterUrl = movie.getPosterPath();
        Picasso.with(this).load(moviePosterUrl)
                .placeholder(R.drawable.progress_image)
//                .error(R.drawable.user_placeholder_error)
                .into(posterImageView);

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
        String url = "https://api.themoviedb.org/3/movie/" + id.toString() + "/videos?language=en-US&api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        client.get(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getBaseContext(),"Unable to fetch movie trailer, please check your connection",Toast.LENGTH_SHORT).show();
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
        params.put("api_key","a07e22bc18f5cb106bfe4cc1f83ad8ed");

        client.get(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getBaseContext(),"Unable to fetch reviews, please check your connection",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder().create();
                // Define Response class to correspond to the JSON response returned
                ReviewResults movieResults = gson.fromJson(responseString, ReviewResults.class);
                Review[] reviews = movieResults.getResults();
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
        i.putExtra("TRAILER_KEY", trailerKey);
        startActivity(i);
    }
}

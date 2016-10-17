package com.example.yoavgray.flixter.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yoavgray.flixter.models.Movie;
import com.example.yoavgray.flixter.adapters.MoviesAdapter;
import com.example.yoavgray.flixter.R;
import com.example.yoavgray.flixter.models.MovieResults;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieActivity extends AppCompatActivity {
    private static final String MOVIE_TAG = "movieExtra";
    private static final String SHOULD_PLAY_TAG = "shouldPlay";
    private static final String API_KEY_TAG = "a07e22bc18f5cb106bfe4cc1f83ad8ed";

    @BindView(R.id.list_view_movies) ListView moviesListView;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout swipeRefreshLayout;
    @BindString(R.string.unable_to_fetch_movie_data) String movieDataErrorString;

    private List<Movie> movies;
    private MoviesAdapter moviesAdapter;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        client = new OkHttpClient();

        movies = new ArrayList<>();
        moviesAdapter = new MoviesAdapter(this, R.layout.movie_list_item, movies);
        moviesListView.setAdapter(moviesAdapter);
        updateMoviesList();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateMoviesList();
            }
        });
        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        moviesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getBaseContext(), MovieDetailsActivity.class);
                Movie thisMovie = movies.get(position);
                boolean isPopular = thisMovie.getVoteAverage() >= 5;
                if (isPopular) {
                    i.putExtra(SHOULD_PLAY_TAG, true);
                } else {
                    i.putExtra(SHOULD_PLAY_TAG, false);
                }
                i.putExtra(MOVIE_TAG, movies.get(position));
                startActivity(i);
                overridePendingTransition( R.anim.transition_fade_in, 0);
            }
        });

    }

    private void updateMoviesList() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.themoviedb.org/3/movie/now_playing").newBuilder();
        urlBuilder.addQueryParameter("api_key", API_KEY_TAG);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), movieDataErrorString, Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    Gson gson = new GsonBuilder().create();
                    // Define Response class to correspond to the JSON response returned
                    MovieResults movieResults = gson.fromJson(response.body().string(), MovieResults.class);
                    movies.clear();
                    movies.addAll(movieResults.getResults());

                    // Run view-related code back on the main thread
                    MovieActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            moviesAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }
}

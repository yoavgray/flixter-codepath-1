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
import com.example.yoavgray.flixter.models.Results;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {
    private static final String MOVIE_TAG = "movieExtra";

    @BindView(R.id.list_view_movies) ListView moviesListView;
    @BindView(R.id.swipe_refresh_container) SwipeRefreshLayout swipeRefreshLayout;

    private List<Movie> movies;
    private MoviesAdapter moviesAdapter;
    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        client = new AsyncHttpClient();

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
                i.putExtra(MOVIE_TAG, movies.get(position));
                startActivity(i);
                overridePendingTransition( R.anim.transition_fade_in, 0);
            }
        });

    }

    private void updateMoviesList() {
        RequestParams params = new RequestParams();
        String url = "https://api.themoviedb.org/3/movie/now_playing";
        params.put("api_key","a07e22bc18f5cb106bfe4cc1f83ad8ed");

        client.get(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getBaseContext(),"Unable to fetch movies data, please check your connection",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder().create();
                // Define Response class to correspond to the JSON response returned
                Results results = gson.fromJson(responseString, Results.class);
                movies.clear();
                movies.addAll(results.getResults());
                moviesAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}

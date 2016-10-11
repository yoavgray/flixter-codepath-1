package com.example.yoavgray.flixter.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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
    @BindView(R.id.list_view_movies) ListView moviesListView;
    private List<Movie> movies;
    private MoviesAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

//        Movie movie1 = new Movie(null, null, null, null, "kaki1", );
        movies = new ArrayList<>();
        moviesAdapter = new MoviesAdapter(this, R.layout.movie_list_item, movies);
        moviesListView.setAdapter(moviesAdapter);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String url = "https://api.themoviedb.org/3/movie/now_playing";
        params.put("api_key","a07e22bc18f5cb106bfe4cc1f83ad8ed");

        client.get(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DMovieActivity", "Failed to get Data!!!");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder().create();
                // Define Response class to correspond to the JSON response returned
                Results results = gson.fromJson(responseString, Results.class);
                movies.clear();
                movies.addAll(results.getResults());
                moviesAdapter.notifyDataSetChanged();
            }
        });
    }
}

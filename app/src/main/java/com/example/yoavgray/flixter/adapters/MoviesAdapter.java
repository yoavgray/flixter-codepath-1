package com.example.yoavgray.flixter.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yoavgray.flixter.R;
import com.example.yoavgray.flixter.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoviesAdapter extends ArrayAdapter<Movie> {

    Context context;
    int layoutResourceId;
    List<Movie> data = null;

    public MoviesAdapter(Context context, int layoutResourceId, List<Movie> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public Movie getItem(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final MovieViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new MovieViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (MovieViewHolder) row.getTag();
        }

        final Movie movie = data.get(position);
        String movieImageUrl;

        holder.titleTextView.setText(movie.getTitle());
        holder.overviewTextView.setText(movie.getOverview());

        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            movieImageUrl = movie.getPosterPath();
        } else {
            movieImageUrl = movie.getBackdropPath();
        }

        Picasso.with(context).load(movieImageUrl)
                .fit().centerCrop()
                .placeholder(R.drawable.progress_image)
                .into(holder.movieImageView);

        return row;
    }

    static class MovieViewHolder
    {
        @BindView(R.id.image_view_movie) ImageView movieImageView;
        @BindView(R.id.text_view_movie_title) TextView titleTextView;
        @BindView(R.id.text_view_movie_overview) TextView overviewTextView;

        public MovieViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class PopularMovieViewHolder
    {

        public PopularMovieViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

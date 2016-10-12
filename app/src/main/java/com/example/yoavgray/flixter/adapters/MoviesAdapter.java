package com.example.yoavgray.flixter.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    public static final int REGULAR_MOVIE_TYPE = 0;
    public static final int POPULAR_MOVIE_TYPE = 1;
    public static final int VIEW_TYPE_COUNT = POPULAR_MOVIE_TYPE + 1;

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

    // Returns the number of types of Views that will be created by getView(int, View, ViewGroup)
    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    // Get the type of View that will be created by getView(int, View, ViewGroup)
    // for the specified item.
    @Override
    public int getItemViewType(int position) {
        Movie movie = data.get(position);
        return movie.getVoteAverage() < 5 ? REGULAR_MOVIE_TYPE : POPULAR_MOVIE_TYPE;
    }

    // Given the item type, responsible for returning the correct inflated XML layout file
    private View getInflatedLayoutForType(int type) {
        switch (type) {
            case REGULAR_MOVIE_TYPE:
                return LayoutInflater.from(getContext()).inflate(R.layout.movie_list_item, null);
            case POPULAR_MOVIE_TYPE:
                return LayoutInflater.from(getContext()).inflate(R.layout.popular_movie_list_item, null);
            default:
                return LayoutInflater.from(getContext()).inflate(R.layout.movie_list_item, null);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final MovieViewHolder holder;

        if (row == null) {
            // Get the data item type for this position
            int type = getItemViewType(position);
            row = getInflatedLayoutForType(type);
            holder = new MovieViewHolder(row);
            if (row != null) {
                row.setTag(holder);
            }
        } else {
            holder = (MovieViewHolder) row.getTag();
        }

        final Movie movie = data.get(position);

        if (holder.titleTextView != null) holder.titleTextView.setText(movie.getTitle());
        if (holder.overviewTextView != null) holder.overviewTextView.setText(movie.getOverview());

        int orientation = context.getResources().getConfiguration().orientation;
        if (movie.getVoteAverage() < 5) {
            Picasso.with(context)
                    // Load poster image or backdrop according to votes on portrait orientation
                    .load(orientation == Configuration.ORIENTATION_PORTRAIT ?
                    movie.getPosterPath() : movie.getBackdropPath())
                    .fit().centerCrop()
                    .placeholder(R.drawable.progress_image)
                    .into(holder.movieImageView);
        } else {
            Picasso.with(context).load(movie.getBackdropPath())
                    .fit().centerInside()
                    .placeholder(R.drawable.progress_image)
                    .into(holder.popularMovieImageView);
        }

        return row;
    }

    static class MovieViewHolder
    {
        @Nullable @BindView(R.id.image_view_movie) ImageView movieImageView;
        @Nullable @BindView(R.id.text_view_movie_title) TextView titleTextView;
        @Nullable @BindView(R.id.text_view_movie_overview) TextView overviewTextView;
        @Nullable @BindView(R.id.image_view_popular_movie) ImageView popularMovieImageView;

        public MovieViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

package com.example.yoavgray.flixter.models;

import com.example.yoavgray.flixter.models.Movie;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yoavgray on 10/9/16.
 */

public class Results {

    Movie[] results;

    public Results(Movie[] results) {
        this.results = results;
    }

    public List<Movie> getResults() {
        return Arrays.asList(results);
    }
}

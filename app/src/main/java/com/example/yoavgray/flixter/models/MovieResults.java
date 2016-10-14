package com.example.yoavgray.flixter.models;

import java.util.Arrays;
import java.util.List;

public class MovieResults {

    Movie[] results;

    public MovieResults(Movie[] results) {
        this.results = results;
    }

    public List<Movie> getResults() {
        return Arrays.asList(results);
    }
}

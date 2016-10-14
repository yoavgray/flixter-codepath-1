package com.example.yoavgray.flixter.models;

public class TrailerResult {
    Trailer[] results;

    public TrailerResult(Trailer[] results) {
        this.results = results;
    }

    public Trailer[] getResults() {
        return results;
    }
}

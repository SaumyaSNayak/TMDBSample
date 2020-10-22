package com.saumya.tmdbsample.datamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieAPIResponse {
    @SerializedName("results")
    @Expose
    private ArrayList<MovieDetails> results;

    public ArrayList<MovieDetails> getResults() {
        return results;
    }

    public void setResults(ArrayList<MovieDetails> results) {
        this.results = results;
    }
}

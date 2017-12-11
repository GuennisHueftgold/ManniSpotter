package com.semeshky.kvg.kvgapi;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FulltextSearch {
@Expose
    @SerializedName("results")
    private List<FulltextSearchResult> mResults;

    public List<FulltextSearchResult> getResults() {
        return mResults;
    }
}

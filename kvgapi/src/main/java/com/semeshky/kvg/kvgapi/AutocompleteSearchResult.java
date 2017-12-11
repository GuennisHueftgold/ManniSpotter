package com.semeshky.kvg.kvgapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AutocompleteSearchResult {
    @Expose
    @SerializedName("shortName")
    private String mShortName;
    @Expose
    @SerializedName("name")
    private String mName;

    public String getShortName() {
        return mShortName;
    }

    public String getName() {
        return mName;
    }
}

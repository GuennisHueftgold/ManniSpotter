package com.semeshky.kvg.kvgapi;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FulltextSearchResult {
    /**
     * TODO:
     */
    @Expose
    @SerializedName("stop")
    private String mShortName;
    @Expose
    @SerializedName("stopPassengerName")
    private String mName;

    public String getShortName() {
        return mShortName;
    }

    public String getName() {
        return mName;
    }

    @Override
    public String toString() {
        return "FulltextSearchResult{" +
                "shortName='" + mShortName + '\'' +
                ", name='" + mName + '\'' +
                '}';
    }
}

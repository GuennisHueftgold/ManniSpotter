package com.semeshky.kvgspotter.search;

import android.content.SearchRecentSuggestionsProvider;

import com.semeshky.kvgspotter.BuildConfig;

public class StopSearchSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = BuildConfig.SEARCH_SUGGEST_AUTHORITY;
    public final static int MODE = DATABASE_MODE_QUERIES;

    public StopSearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}

package com.semeshky.kvgspotter.search;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.semeshky.kvgspotter.BuildConfig;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.database.Stop;

import java.util.List;

import timber.log.Timber;


public class SearchContentProvider extends ContentProvider {
    public final static String CONTENT_STOP = BuildConfig.SEARCH_SUGGEST_CONTENT_URI;
    private final static String PARAMETER_LIMIT = "limit";

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        if (selectionArgs == null || selectionArgs.length != 1) {
            return null;
        }
        Timber.d("path: %s", uri.toString());
        if (projection != null)
            Timber.d("projection args: %s", projection);
        Timber.d("selection args: %s", selectionArgs[0]);
        Timber.d("selection: %s", selection);
        int limit = 50;
        if (uri.getQueryParameter(PARAMETER_LIMIT) != null) {
            limit = Integer.parseInt(uri.getQueryParameter(PARAMETER_LIMIT));
        }
        final String[] columns = {
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_TEXT_2,
                SearchManager.SUGGEST_COLUMN_ICON_1,
                SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
                //SearchManager.SUGGEST_COLUMN_INTENT_DATA,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID,
                //SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA,
                //SearchManager.SUGGEST_COLUMN_QUERY,
                SearchManager.SUGGEST_COLUMN_SHORTCUT_ID
        };
        AppDatabase.init(this.getContext());
        final List<Stop> stops = AppDatabase
                .getInstance()
                .stopDao()
                .searchStation(selectionArgs[0], limit);
        MatrixCursor matrixCursor = new MatrixCursor(columns);
        final Uri iconUri = Uri.parse("android.resource://" + getContext().getPackageName() + "/drawable/ic_directions_bus_white_24dp");

        for (Stop stop : stops) {
            final MatrixCursor.RowBuilder builder = matrixCursor.newRow()
                    .add(BaseColumns._ID, stop.getUid())
                    .add(SearchManager.SUGGEST_COLUMN_TEXT_1, stop.getName())
                    .add(SearchManager.SUGGEST_COLUMN_TEXT_2, stop.getShortName())
                    .add(SearchManager.SUGGEST_COLUMN_ICON_1, iconUri)
                    .add(SearchManager.SUGGEST_COLUMN_INTENT_ACTION, "android.intent.action.VIEW")
                    .add(SearchManager.SUGGEST_COLUMN_INTENT_DATA, CONTENT_STOP)
                    .add(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, stop.getShortName())
                    //.add(SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA,"")
                    //.add(SearchManager.SUGGEST_COLUMN_QUERY,"")
                    .add(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, stop.getId());
        }
        return matrixCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

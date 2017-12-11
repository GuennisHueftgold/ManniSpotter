package com.semeshky.kvgspotter.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.activities.StationDetailActivity;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.database.FavoriteStationWithName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Don on 06.12.2017.
 */

public class DepartureWidgetRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new DepartureListRemoteView(this.getApplicationContext(), intent);
    }
}

class DepartureListRemoteView implements RemoteViewsService.RemoteViewsFactory {
    private final Context mContext;
    private final int mAppWidgetId;
    private List<Entry> mDepartures = new ArrayList<>();

    public DepartureListRemoteView(Context applicationContext, Intent intent) {
        this.mContext = applicationContext;
        this.mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        List<FavoriteStationWithName> items = AppDatabase
                .getInstance()
                .favoriteSationDao()
                .getAllWithName();
        this.mDepartures.clear();
        for (FavoriteStationWithName fav : items) {
            this.mDepartures.add(new Entry(fav.getId(), fav.getName(), fav.getShortName()));
        }
    }

    @Override
    public void onDestroy() {
        this.mDepartures.clear();
    }

    @Override
    public int getCount() {
        return this.mDepartures.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final Entry favorite = this.mDepartures.get(position);
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_favorite_listitem);
        rv.setTextViewText(R.id.txtTitle, favorite.name);
        rv.setTextViewCompoundDrawablesRelative(R.id.txtTitle, R.drawable.ic_directions_bus_black_24dp, 0, 0, 0);
        // SET CLICK BEHAVIOR
        Bundle extras = new Bundle();
        extras.putString(DepartureWidgetProvider.EXTRA_STOP_SHORT_NAME, favorite.shortName);
        extras.putString(DepartureWidgetProvider.EXTRA_STOP_NAME, favorite.name);
        extras.putString(StationDetailActivity.EXTRA_STATION_SHORT_NAME, favorite.shortName);
        extras.putString(StationDetailActivity.EXTRA_STATION_NAME, favorite.name);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.txtTitle, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return this.mDepartures.get(position).id;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}

class Entry {
    final String name;
    final long id;
    final String shortName;

    public Entry(long id, String name, String shortName) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", shortName='" + shortName + '\'' +
                '}';
    }
}
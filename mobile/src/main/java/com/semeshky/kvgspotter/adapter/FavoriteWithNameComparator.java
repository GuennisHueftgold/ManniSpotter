package com.semeshky.kvgspotter.adapter;

import com.semeshky.kvgspotter.database.FavoriteStationWithName;

import java.util.Comparator;


public final class FavoriteWithNameComparator implements Comparator<FavoriteStationWithName> {
    @Override
    public int compare(FavoriteStationWithName favoriteStationWithName, FavoriteStationWithName t1) {
        return favoriteStationWithName.getName().compareTo(t1.getName());
    }
}

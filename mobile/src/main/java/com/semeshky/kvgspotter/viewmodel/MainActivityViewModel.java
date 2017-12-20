package com.semeshky.kvgspotter.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.semeshky.kvg.kvgapi.KvgApiClient;
import com.semeshky.kvgspotter.adapter.HomeAdapter;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.database.FavoriteStationWithName;
import com.semeshky.kvgspotter.database.Stop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivityViewModel extends AndroidViewModel {
    public static final Function<List<HomeAdapter.DistanceStop>, List<HomeAdapter.DistanceStop>> DISTANCE_STOP_SORT_SHORT_FUNC = new Function<List<HomeAdapter.DistanceStop>, List<HomeAdapter.DistanceStop>>() {
        @Override
        public List<HomeAdapter.DistanceStop> apply(List<HomeAdapter.DistanceStop> distanceStops) throws Exception {
            Collections.sort(distanceStops, new Comparator<HomeAdapter.DistanceStop>() {
                @Override
                public int compare(HomeAdapter.DistanceStop distanceStop, HomeAdapter.DistanceStop t1) {
                    return Float.compare(distanceStop.distance, t1.distance);
                }
            });
            final int breakIndex = 5;
            if (distanceStops.size() <= breakIndex)
                return distanceStops;
            int i = 0;
            for (; i < distanceStops.size(); i++) {
                if (distanceStops.get(i).distance > 300f) {
                    break;
                }
            }
            return distanceStops.subList(0, Math.max(breakIndex, i + 1));
        }
    };
    private final static BiFunction<List<FavoriteStationWithName>, Location, List<HomeAdapter.DistanceStop>> TRANSFORM_FAVORITE = new BiFunction<List<FavoriteStationWithName>, Location, List<HomeAdapter.DistanceStop>>() {
        @Override
        public List<HomeAdapter.DistanceStop> apply(List<FavoriteStationWithName> favoriteStationWithNames, Location location) throws Exception {
            List<HomeAdapter.DistanceStop> distanceStops = new ArrayList<>();
            for (FavoriteStationWithName favoriteStationWithName : favoriteStationWithNames) {
                float distance = -1f;
                if (location != null) {
                    Location loc = new Location("");
                    loc.setLongitude(favoriteStationWithName.getLongitude() / KvgApiClient.COORDINATES_CONVERTION_CONSTANT);
                    loc.setLatitude(favoriteStationWithName.getLatitude() / KvgApiClient.COORDINATES_CONVERTION_CONSTANT);
                    distance = loc.distanceTo(location);
                }
                distanceStops
                        .add(new HomeAdapter.DistanceStop(favoriteStationWithName.getId(),
                                favoriteStationWithName.getShortName(),
                                favoriteStationWithName.getName(),
                                distance));
            }
            return distanceStops;
        }
    };
    private final static Function<List<FavoriteStationWithName>, List<HomeAdapter.DistanceStop>> MAP_FAVORITE = new Function<List<FavoriteStationWithName>, List<HomeAdapter.DistanceStop>>() {
        @Override
        public List<HomeAdapter.DistanceStop> apply(List<FavoriteStationWithName> favoriteStationWithNames) throws Exception {
            List<HomeAdapter.DistanceStop> distanceStops = new ArrayList<>();
            for (FavoriteStationWithName favoriteStationWithName : favoriteStationWithNames) {
                distanceStops
                        .add(new HomeAdapter.DistanceStop(favoriteStationWithName.getId(),
                                favoriteStationWithName.getShortName(),
                                favoriteStationWithName.getName(),
                                -1));
            }
            return distanceStops;
        }
    };
    private final static BiFunction<List<Stop>, Location, List<HomeAdapter.DistanceStop>> TRANSFORM_STOP = new BiFunction<List<Stop>, Location, List<HomeAdapter.DistanceStop>>() {
        @Override
        public List<HomeAdapter.DistanceStop> apply(List<Stop> stops, Location location) throws Exception {
            List<HomeAdapter.DistanceStop> list = new ArrayList<>();
            for (Stop stop : stops) {
                float distance = -1;
                if (location != null) {
                    Location loc = new Location("");
                    loc.setLongitude(stop.getLongitude() / KvgApiClient.COORDINATES_CONVERTION_CONSTANT);
                    loc.setLatitude(stop.getLatitude() / KvgApiClient.COORDINATES_CONVERTION_CONSTANT);
                    distance = loc.distanceTo(location);
                }
                list.add(new HomeAdapter.DistanceStop(stop.getUid(), stop.getShortName(), stop.getName(), distance));
            }
            return list;
        }
    };
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    protected void onCleared() {
        super.onCleared();
    }

    /**
     * Creates a Flowable that either uses the provided Location Flowable or just returns {@link com.semeshky.kvgspotter.adapter.HomeAdapter.DistanceStop} with a distance of -1
     *
     * @param locationFlowable
     * @return
     */
    public Flowable<List<HomeAdapter.DistanceStop>> getFavoriteFlowable(@Nullable Flowable<Location> locationFlowable) {
        Flowable<List<HomeAdapter.DistanceStop>> flowable;
        if (locationFlowable != null)
            flowable = Flowable.combineLatest(AppDatabase
                    .getInstance()
                    .favoriteSationDao()
                    .getAllWithNameFlow(), locationFlowable, TRANSFORM_FAVORITE);
        else
            flowable = AppDatabase
                    .getInstance()
                    .favoriteSationDao()
                    .getAllWithNameFlow()
                    .map(MAP_FAVORITE);
        return flowable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<HomeAdapter.DistanceStop>> createNearbyFlowable(@NonNull Flowable<Location> locationFlowable) {
        return Flowable.combineLatest(AppDatabase
                .getInstance()
                .stopDao()
                .getAllFlow(), locationFlowable, TRANSFORM_STOP)
                .map(MainActivityViewModel.DISTANCE_STOP_SORT_SHORT_FUNC)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

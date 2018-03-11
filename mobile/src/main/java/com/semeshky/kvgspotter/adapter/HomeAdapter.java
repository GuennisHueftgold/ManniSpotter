package com.semeshky.kvgspotter.adapter;


import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.viewholder.HomeAdapterViewHolder;
import com.semeshky.kvgspotter.viewholder.HomeRequestPermissionViewHolder;
import com.semeshky.kvgspotter.viewholder.ListSectionTitleViewHolder;
import com.semeshky.kvgspotter.viewholder.ListSingleLineViewHolder;
import com.semeshky.kvgspotter.viewholder.NoFavoriteViewHolder;
import com.semeshky.kvgspotter.viewholder.StopDistanceViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapterViewHolder> {

    public final static int TYPE_TITLE = 1,
            TYPE_STOP = 2,
            TYPE_FAVORITE_INFO = 3,
            TYPE_TEXT_SINGLE_LINE = 4,
            TYPE_NEARBY_STOP_INFO = 5;
    final WeakReference<HomeAdapterEventListener> mOnFavoriteClickListener;
    final List<DistanceStop> mFavoriteStationList = new ArrayList<>();
    final List<DistanceStop> mNearbyStopList = new ArrayList<>();
    final List<ListItem> mListItems = new ArrayList<>();
    private final LayoutInflater mLayoutInflater;
    boolean mHasLocationpermission = false;

    public HomeAdapter(@NonNull Context context, HomeAdapterEventListener onFavoriteSelectListener) {
        super();
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mOnFavoriteClickListener = new WeakReference<>(onFavoriteSelectListener);
        this.setHasStableIds(true);
        this.updateIndex();
    }

    @Override
    public HomeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_STOP:
                return new StopDistanceViewHolder(this.mLayoutInflater, parent);
            case TYPE_TITLE:
                return new ListSectionTitleViewHolder(this.mLayoutInflater, parent);
            case TYPE_NEARBY_STOP_INFO:
                return new HomeRequestPermissionViewHolder(this.mLayoutInflater, parent);
            case TYPE_FAVORITE_INFO:
                return new NoFavoriteViewHolder(this.mLayoutInflater, parent);
            case TYPE_TEXT_SINGLE_LINE:
                return new ListSingleLineViewHolder(this.mLayoutInflater, parent);
            default:
                throw new RuntimeException("Unkown viewType: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(HomeAdapterViewHolder holder, int position) {
        //Stub as required
    }

    @Override
    public void onBindViewHolder(HomeAdapterViewHolder holder, int position, List<Object> payloads) {
        switch (holder.getType()) {
            case TYPE_STOP:
                final DistanceStop distanceStop = (DistanceStop) this.getItem(position).tag;
                ((StopDistanceViewHolder) holder).setDistanceStop(distanceStop, payloads);
                break;
            case TYPE_FAVORITE_INFO:
                break;
            case TYPE_NEARBY_STOP_INFO:
                break;
            case TYPE_TITLE:
                final ListSectionTitleViewHolder bindingTitle = (ListSectionTitleViewHolder) holder;
                bindingTitle.setTitle((int) this.getItem(position).tag);
                bindingTitle.setPrimaryColor(true);
                break;
            case TYPE_TEXT_SINGLE_LINE:
                break;
        }
    }

    @Override
    public void onViewRecycled(HomeAdapterViewHolder viewHolder) {
        if (viewHolder instanceof StopDistanceViewHolder) {
            // ((StopDistanceViewHolder) viewHolder).getBinding().setClickListener(null);
        }
        super.onViewRecycled(viewHolder);
    }

    @Override
    public int getItemCount() {
        return this.mListItems.size();
    }

    @Override
    public long getItemId(@IntRange(from = 0) int position) {
        return this.mListItems.get(position).id;
    }

    @Override
    public int getItemViewType(@IntRange(from = 0) int position) {
        return this.mListItems.get(position).type;
    }

    public ListItem getItem(@IntRange(from = 0) int position) {
        return this.mListItems.get(position);
    }

    /**
     * @param favoriteStation
     * @see HomeAdapter#addFavorite(DistanceStop, boolean)
     */
    public void addFavorite(@NonNull DistanceStop favoriteStation) {
        this.addFavorite(favoriteStation, true);
    }

    public void addFavorite(@NonNull DistanceStop favoriteStation, boolean updateIndex) {
        this.mFavoriteStationList.add(favoriteStation);
        if (updateIndex) {
            this.updateIndex();
        }
    }

    /**
     * @param items the favorites
     * @see HomeAdapter#setFavorites(List, boolean)
     */
    public void setFavorites(@NonNull List<DistanceStop> items) {
        this.setFavorites(items, true);
    }

    public void setFavorites(@NonNull List<DistanceStop> items, boolean updateIndex) {
        this.mFavoriteStationList.clear();
        this.mFavoriteStationList.addAll(items);
        if (updateIndex) {
            this.updateIndex();
        }
    }

    /**
     * @param stop stop to add
     * @see HomeAdapter#addNearby(DistanceStop, boolean)
     */
    public void addNearby(@NonNull DistanceStop stop) {
        this.addNearby(stop, true);
    }

    public void addNearby(@NonNull DistanceStop stop, boolean updateIndex) {
        this.mNearbyStopList.add(stop);
        if (updateIndex) {
            this.updateIndex();
        }
    }

    /**
     * @param stops stops do add
     * @see HomeAdapter#setNearby(List, boolean)
     */
    public void setNearby(@NonNull List<DistanceStop> stops) {
        this.setNearby(stops, true);
    }

    public void setNearby(@NonNull List<DistanceStop> stops, boolean updateIndex) {
        this.mNearbyStopList.clear();
        this.mNearbyStopList.addAll(stops);
        if (updateIndex) {
            this.updateIndex();
        }
    }

    void updateIndex() {
        final List<ListItem> itemList = new ArrayList<>();
        itemList.add(new ListItem(1, TYPE_TITLE, R.string.favorites));
        if (this.mFavoriteStationList.size() == 0) {
            itemList.add(new ListItem(0, TYPE_FAVORITE_INFO, null));
        } else {
            for (DistanceStop distanceStop : this.mFavoriteStationList) {
                ListItem favoriteItem = new ListItem(distanceStop.id,
                        TYPE_STOP,
                        distanceStop);
                itemList.add(favoriteItem);
            }
        }
        itemList.add(new ListItem(2,
                TYPE_TITLE, R.string.nearby));
        if (this.mHasLocationpermission && this.mNearbyStopList.size() > 0) {
            for (DistanceStop stop : this.mNearbyStopList) {
                ListItem favoriteItem = new ListItem(stop.id,
                        TYPE_STOP,
                        stop);
                itemList.add(favoriteItem);
            }
        } else if (!this.mHasLocationpermission) {
            itemList.add(new ListItem(1, TYPE_NEARBY_STOP_INFO, null));
        } else {
            itemList.add(new ListItem(0, TYPE_TEXT_SINGLE_LINE, R.string.no_nearby_stops_found_yet));
        }
        final HomeAdapterDiffUtilCallback diffCallback = new HomeAdapterDiffUtilCallback(this.mListItems, itemList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback, true);
        this.mListItems.clear();
        this.mListItems.addAll(itemList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void setHasLocationPermission(boolean hasLocationPermission) {
        this.mHasLocationpermission = hasLocationPermission;
        this.updateIndex();
    }

    public interface HomeAdapterEventListener {
        void onFavoriteSelected(@NonNull String stopShortName, @Nullable String stopName);

        void onRequestPermission();
    }


    public final static class DistanceStop {
        public final String shortName;
        public final String name;
        public final float distance;
        public final long id;

        public DistanceStop(
                long id,
                @NonNull String shortName,
                @NonNull String name,
                float distance) {
            this.shortName = shortName;
            this.name = name;
            this.distance = distance;
            this.id = id;
        }

        @Override
        public String toString() {
            return "DistanceStop{" +
                    "shortName='" + shortName + '\'' +
                    ", name='" + name + '\'' +
                    ", distance=" + distance +
                    ", id=" + id +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DistanceStop that = (DistanceStop) o;

            if (Float.compare(that.distance, distance) != 0) return false;
            if (id != that.id) return false;
            if (shortName != null ? !shortName.equals(that.shortName) : that.shortName != null)
                return false;
            return name != null ? name.equals(that.name) : that.name == null;
        }

        @Override
        public int hashCode() {
            int result = shortName != null ? shortName.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (distance != +0.0f ? Float.floatToIntBits(distance) : 0);
            result = 31 * result + (int) (id ^ (id >>> 32));
            return result;
        }
    }

    static class ListItem {
        public final int type;
        public final Object tag;
        public final long id;

        ListItem(long id, int type, Object tag) {
            this.type = type;
            this.tag = tag;
            this.id = id * 10 + type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ListItem listItem = (ListItem) o;

            if (type != listItem.type) return false;
            if (id != listItem.id) return false;
            return tag != null ? tag.equals(listItem.tag) : listItem.tag == null;
        }

        @Override
        public int hashCode() {
            int result = type;
            result = 31 * result + (tag != null ? tag.hashCode() : 0);
            result = 31 * result + (int) (id ^ (id >>> 32));
            return result;
        }
    }

}

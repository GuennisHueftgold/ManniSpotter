package com.semeshky.kvgspotter.adapter;


import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.semeshky.kvg.kvgapi.KvgApiClient;
import com.semeshky.kvgspotter.BR;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.database.FavoriteStationWithName;
import com.semeshky.kvgspotter.database.Stop;
import com.semeshky.kvgspotter.databinding.VhFavoriteStationBinding;
import com.semeshky.kvgspotter.databinding.VhListSectionTitleBinding;
import com.semeshky.kvgspotter.databinding.VhNearbyStopBinding;
import com.semeshky.kvgspotter.viewholder.DataboundViewHolder;
import com.semeshky.kvgspotter.viewholder.FavoriteDataboundViewHolder;
import com.semeshky.kvgspotter.viewholder.ListSectionTitleViewHolder;
import com.semeshky.kvgspotter.viewholder.NearbyStopViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<DataboundViewHolder> {

    private final static int TYPE_TITLE = 1, TYPE_FAVORITE = 2, TYPE_FAVORITE_INFO = 3, TYPE_NEARBY_STOP = 4, TYPE_NEARBY_STOP_INFO = 5;
    private final WeakReference<OnFavoriteSelectListener> mOnFavoriteClickListener;
    private List<FavoriteStationWithName> mFavoriteStationList = new ArrayList<>();
    private List<Stop> mNearbyStopList = new ArrayList<>();
    private List<ListItem> mListItems = new ArrayList<>();
    private Location mCurrentLocation;

    public HomeAdapter(OnFavoriteSelectListener onFavoriteSelectListener) {
        super();
        this.mOnFavoriteClickListener = new WeakReference<>(onFavoriteSelectListener);
        this.setHasStableIds(true);
    }

    @Override
    public DataboundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_FAVORITE:
                return new FavoriteDataboundViewHolder(parent);
            case TYPE_NEARBY_STOP:
                return new NearbyStopViewHolder(parent);
            default:
                return new ListSectionTitleViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(DataboundViewHolder holder, int position) {
        switch (this.getItemViewType(position)) {
            case TYPE_FAVORITE:
                final FavoriteStationWithName favoriteStationWithName = (FavoriteStationWithName) this.mListItems.get(position).tag;
                final VhFavoriteStationBinding bindingFavorite = (VhFavoriteStationBinding) holder.getBinding();
                bindingFavorite.setShortName(favoriteStationWithName.getShortName());
                bindingFavorite.setTitle(favoriteStationWithName.getName());
                bindingFavorite.setVariable(BR.clickListener, this.mOnFavoriteClickListener.get());
                bindingFavorite.setStopIcon(R.drawable.ic_directions_bus_black_24dp);
                bindingFavorite.setDistance(this.mListItems.get(position).distance);
                break;
            case TYPE_FAVORITE_INFO:

                break;
            case TYPE_NEARBY_STOP:
                final Stop stop = (Stop) this.mListItems.get(position).tag;
                final VhNearbyStopBinding bindingNearbyStop = (VhNearbyStopBinding) holder.getBinding();
                bindingNearbyStop.setDistance("1nm92");
                bindingNearbyStop.setTitle(stop.getName());
            case TYPE_TITLE:
                final VhListSectionTitleBinding bindingTitle = (VhListSectionTitleBinding) holder.getBinding();
                bindingTitle.setTitle("Title");
                break;
        }
    }

    @Override
    public void onViewRecycled(DataboundViewHolder viewHolder) {
        if (viewHolder instanceof FavoriteDataboundViewHolder) {
            ((FavoriteDataboundViewHolder) viewHolder).getBinding().setClickListener(null);
        }
        super.onViewRecycled(viewHolder);
    }

    @Override
    public int getItemCount() {
        return this.mListItems.size();
    }

    @Override
    public long getItemId(int position) {
        return this.mListItems.get(position).id;
    }

    @Override
    public int getItemViewType(int position) {
        return this.mListItems.get(position).type;
    }

    public void addFavorite(FavoriteStationWithName favoriteStation) {
        this.mFavoriteStationList.add(favoriteStation);
        this.updateIndex();
    }

    public void setFavorites(List<FavoriteStationWithName> items) {
        this.mFavoriteStationList.clear();
        this.mFavoriteStationList.addAll(items);
        this.updateIndex();
    }

    public void setNearby(List<Stop> stops) {
        this.mNearbyStopList.clear();
        this.mNearbyStopList.addAll(stops);
        this.updateIndex();
    }

    public void setCurrentLocation(Location location) {
        this.mCurrentLocation = location;
        this.updateIndex();
    }

    private void updateIndex() {
        final List<ListItem> itemList = new ArrayList<>();
        itemList.add(new ListItem(TYPE_TITLE + 10, TYPE_TITLE, "TITLE"));
        if (this.mFavoriteStationList.size() == 0) {
            itemList.add(new ListItem(TYPE_FAVORITE_INFO * 10, TYPE_FAVORITE_INFO, null));
        } else {
            for (FavoriteStationWithName favoriteStationWithName : this.mFavoriteStationList) {
                float distance = -1;
                if (this.mCurrentLocation != null) {
                    final Location location = new Location("");
                    location.setLatitude(favoriteStationWithName.getLatitude() / KvgApiClient.COORDINATES_CONVERTION_CONSTANT);
                    location.setLongitude(favoriteStationWithName.getLongitude() / KvgApiClient.COORDINATES_CONVERTION_CONSTANT);
                    distance = location.distanceTo(this.mCurrentLocation);
                }
                ListItem favoriteItem = new ListItem(favoriteStationWithName.getId() * 10 + TYPE_FAVORITE,
                        TYPE_FAVORITE,
                        favoriteStationWithName,
                        distance);
                itemList.add(favoriteItem);
            }
        }
        itemList.add(new ListItem(TYPE_TITLE + 20,
                TYPE_TITLE, "TITLE 2"));
        for (Stop stop : this.mNearbyStopList) {
            ListItem favoriteItem = new ListItem(stop.getUid() * 10 + TYPE_FAVORITE,
                    TYPE_NEARBY_STOP,
                    stop);
            itemList.add(favoriteItem);
        }
        final AdapterDiffCallback diffCallback = new AdapterDiffCallback(this.mListItems, itemList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback, true);
        this.mListItems.clear();
        this.mListItems.addAll(itemList);
        diffResult.dispatchUpdatesTo(this);
    }

    public interface OnFavoriteSelectListener {
        void onFavoriteSelected(@NonNull String stopShortName, @Nullable String stopName);
    }

    private static final class AdapterDiffCallback extends DiffUtil.Callback {

        private final List<ListItem> mNewList;
        private final List<ListItem> mOldList;

        public AdapterDiffCallback(List<ListItem> oldList, List<ListItem> newList) {
            this.mOldList = oldList;
            this.mNewList = newList;
        }

        @Override
        public int getOldListSize() {
            return this.mOldList.size();
        }

        @Override
        public int getNewListSize() {
            return this.mNewList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return this.mOldList.get(oldItemPosition).id ==
                    this.mNewList.get(newItemPosition).id;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return this.mOldList.get(oldItemPosition)
                    .distance == this.mNewList.get(newItemPosition).distance;
        }

        @Nullable
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return null;
        }
    }

    private static class ListItem {
        public final int type;
        public final Object tag;
        public final long id;
        public final float distance;

        public ListItem(long id, int type, Object tag) {
            this(id, type, tag, -1);
        }

        public ListItem(long id, int type, Object tag, float distance) {
            this.type = type;
            this.tag = tag;
            this.id = id;
            this.distance = distance;
        }
    }

}

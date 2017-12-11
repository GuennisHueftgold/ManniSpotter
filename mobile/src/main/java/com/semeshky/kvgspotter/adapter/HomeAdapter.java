package com.semeshky.kvgspotter.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.BR;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.database.FavoriteStation;
import com.semeshky.kvgspotter.database.FavoriteStationWithName;
import com.semeshky.kvgspotter.viewholder.FavoriteDataboundViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<FavoriteDataboundViewHolder> {

    private final WeakReference<OnFavoriteSelectListener> mOnFavoriteClickListener;
    private List<FavoriteStationWithName> mFavoriteStationList = new ArrayList<>();

    public HomeAdapter(OnFavoriteSelectListener onFavoriteSelectListener) {
        super();
        this.mOnFavoriteClickListener = new WeakReference<>(onFavoriteSelectListener);
        this.setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return this.mFavoriteStationList.get(position).getId();
    }

    @Override
    public FavoriteDataboundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavoriteDataboundViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(FavoriteDataboundViewHolder holder, int position) {
        holder.getBinding().setFavoriteStation(this.mFavoriteStationList.get(position));
        holder.getBinding().setVariable(BR.clickListener, this.mOnFavoriteClickListener.get());
        holder.getBinding().setStopIcon(R.drawable.ic_directions_bus_black_24dp);
    }

    @Override
    public void onViewRecycled(FavoriteDataboundViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
    }

    @Override
    public int getItemCount() {
        return this.mFavoriteStationList.size();
    }

    public void addItem(FavoriteStationWithName favoriteStation) {
        this.mFavoriteStationList.add(favoriteStation);
        this.notifyDataSetChanged();
    }

    public void setItems(List<FavoriteStationWithName> items) {
        this.mFavoriteStationList.clear();
        this.mFavoriteStationList.addAll(items);
        this.notifyDataSetChanged();
    }

    public interface OnFavoriteSelectListener {
        void onFavoriteSelected(FavoriteStation favoriteStation);
    }

    public static interface OnFavoriteEventListener {
        void onRemoveFavorite(FavoriteStation favoriteStation);

        void onRefreshFavorite(FavoriteStation favoriteStation);
    }
}

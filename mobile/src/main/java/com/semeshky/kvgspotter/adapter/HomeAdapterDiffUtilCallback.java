package com.semeshky.kvgspotter.adapter;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import timber.log.Timber;

import static com.semeshky.kvgspotter.adapter.HomeAdapter.TYPE_STOP;


final class HomeAdapterDiffUtilCallback extends DiffUtil.Callback {

    final List<HomeAdapter.ListItem> mNewList;
    final List<HomeAdapter.ListItem> mOldList;

    HomeAdapterDiffUtilCallback(List<HomeAdapter.ListItem> oldList, List<HomeAdapter.ListItem> newList) {
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
        final Object oldItem = this.mOldList.get(oldItemPosition);
        final Object newItem = this.mNewList.get(newItemPosition);
        return oldItem.equals(newItem);
    }


    @Nullable
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        if (this.mNewList.get(newItemPosition).type == TYPE_STOP &&
                this.mOldList.get(oldItemPosition).type == TYPE_STOP) {
            HomeAdapter.DistanceStop oldItem = (HomeAdapter.DistanceStop) this.mOldList.get(oldItemPosition).tag;
            HomeAdapter.DistanceStop newItem = (HomeAdapter.DistanceStop) this.mNewList.get(newItemPosition).tag;
            if (oldItemPosition == 1 && newItemPosition == 1) {
                Timber.d("ZZZ %s || %s", oldItem.toString(), newItem.toString());
            }
            if (oldItem.distance >= 0f && newItem.distance >= 0f && oldItem.distance != newItem.distance) {
                return new DistanceDelta(oldItem.distance, newItem.distance);
            }
        }
        return null;
    }
}
package com.semeshky.kvgspotter.adapter;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import java.util.List;

class AbstractDataboundAdapterDiffUtilCallback<T, V extends ViewDataBinding> extends DiffUtil.Callback {

    private final List<T> mNewItems;
    private final AbstractDataboundAdapter<T, V> mAbstractDataboundAdapter;

    AbstractDataboundAdapterDiffUtilCallback(@NonNull AbstractDataboundAdapter<T, V> abstractDataboundAdapter,
                                             @NonNull List<T> newItems) {
        this.mAbstractDataboundAdapter = abstractDataboundAdapter;
        this.mNewItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return this.mAbstractDataboundAdapter.getItemCount();
    }

    @Override
    public int getNewListSize() {
        return this.mNewItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        final T oldItem = this.mAbstractDataboundAdapter.getItem(oldItemPosition);
        final T newItem = this.mNewItems.get(newItemPosition);
        return this.mAbstractDataboundAdapter.areItemsTheSame(oldItem, newItem);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final T oldItem = this.mAbstractDataboundAdapter.getItem(oldItemPosition);
        final T newItem = this.mNewItems.get(newItemPosition);
        return this.mAbstractDataboundAdapter.areContentsTheSame(oldItem, newItem);
    }
}
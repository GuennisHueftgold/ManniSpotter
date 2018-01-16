package com.semeshky.kvgspotter.adapter;

import android.databinding.ViewDataBinding;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.viewholder.DataboundViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractDataboundAdapter<T, V extends ViewDataBinding>
        extends RecyclerView.Adapter<DataboundViewHolder<V>> {

    @Nullable
    private List<T> mItems;
    // each time data is set, we update this variable so that if DiffUtil calculation returns
    // after repetitive updates, we can ignore the old calculation
    private int dataVersion = 0;

    @Override
    public final DataboundViewHolder<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        V binding = createBinding(parent, viewType);
        return new DataboundViewHolder(binding);
    }

    protected abstract V createBinding(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(DataboundViewHolder<V> holder, int position, List<Object> payloads) {
        bind(holder.getBinding(), mItems.get(position), payloads);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public void onBindViewHolder(DataboundViewHolder<V> holder, int position) {
    }

    public T getItem(int position) {
        return this.mItems.get(position);
    }

    public Comparator<T> getComparator() {
        return null;
    }

    @CallSuper
    public void setItems(@NonNull final List<T> items) {
        if (this.mItems == null) {
            this.mItems = new ArrayList<>();
        }
        final Comparator<T> comparator = getComparator();
        DiffUtil.Callback diffUtilCallback;
        final List<T> newItems = new ArrayList<>(items);
        if (comparator != null) {
            Collections.sort(newItems, comparator);
        }
        diffUtilCallback = new AbstractDataboundAdapterDiffUtilCallback<>(this, newItems);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        this.mItems.clear();
        this.mItems.addAll(newItems);
        diffResult.dispatchUpdatesTo(this);
    }

    protected abstract void bind(V binding, T item, List<Object> payloads);

    protected abstract boolean areItemsTheSame(T oldItem, T newItem);

    protected abstract boolean areContentsTheSame(T oldItem, T newItem);

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }
}

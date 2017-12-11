package com.semeshky.kvgspotter.adapter;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.viewholder.DataboundViewHolder;

import java.util.ArrayList;
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
    public final void onBindViewHolder(DataboundViewHolder<V> holder, int position) {
        //noinspection ConstantConditions
        bind(holder.getBinding(), mItems.get(position));
        holder.getBinding().executePendingBindings();
    }

    public T getItem(int position) {
        return this.mItems.get(position);
    }

    public void setItems(final List<T> items) {
        if (this.mItems == null) {
            this.mItems = items;
            notifyDataSetChanged();
            return;
        }
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return (AbstractDataboundAdapter.this.mItems == null) ? 0 : AbstractDataboundAdapter.this.mItems.size();
            }

            @Override
            public int getNewListSize() {
                return (items == null) ? 0 : items.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                final T oldItem = AbstractDataboundAdapter.this.mItems.get(oldItemPosition);
                final T newItem = items.get(newItemPosition);
                return AbstractDataboundAdapter.this.areItemsTheSame(oldItem, newItem);
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                final T oldItem = AbstractDataboundAdapter.this.mItems.get(oldItemPosition);
                final T newItem = items.get(newItemPosition);
                return AbstractDataboundAdapter.this.areContentsTheSame(oldItem, newItem);
            }
        });
        this.mItems.clear();
        this.mItems.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }

    protected abstract void bind(V binding, T item);

    protected abstract boolean areItemsTheSame(T oldItem, T newItem);

    protected abstract boolean areContentsTheSame(T oldItem, T newItem);

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public void addItem(T t) {
        if (this.mItems == null) {
            this.mItems = new ArrayList<>();
        }
        this.mItems.add(t);
    }
}
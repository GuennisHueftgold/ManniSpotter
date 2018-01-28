package com.semeshky.kvgspotter.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.database.FavoriteStationWithName;
import com.semeshky.kvgspotter.databinding.VhListSingleLineWithActionBinding;

import java.util.Comparator;
import java.util.List;

public final class SetupFavoriteAdapter extends AbstractDataboundAdapter<FavoriteStationWithName, VhListSingleLineWithActionBinding> {
    protected final FavoriteWithNameComparator mFavoriteWithNameComparator = new FavoriteWithNameComparator();

    @Override
    protected VhListSingleLineWithActionBinding createBinding(ViewGroup parent, int viewType) {
        return VhListSingleLineWithActionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    }

    @Override
    protected void bind(VhListSingleLineWithActionBinding binding, FavoriteStationWithName item, List<Object> payloads) {
        binding.setActionIcon(R.drawable.ic_delete_black_24dp);
        binding.setActionDescription(R.string.delete_favorite);
        binding.setTitleText(item.getName());
    }

    @Override
    protected boolean areItemsTheSame(FavoriteStationWithName oldItem, FavoriteStationWithName newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    protected boolean areContentsTheSame(FavoriteStationWithName oldItem, FavoriteStationWithName newItem) {
        return oldItem.equals(newItem);
    }

    public Comparator<FavoriteStationWithName> getComparator() {
        return this.mFavoriteWithNameComparator;
    }
}

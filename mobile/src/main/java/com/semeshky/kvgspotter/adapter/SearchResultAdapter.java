package com.semeshky.kvgspotter.adapter;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.guennishueftgold.trapezeapi.FulltextSearchResult;
import com.semeshky.kvgspotter.BR;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.databinding.VhSearchResultBinding;
import com.semeshky.kvgspotter.viewmodel.SearchActivityViewModel;

import java.util.List;


public final class SearchResultAdapter extends AbstractDataboundAdapter<FulltextSearchResult, VhSearchResultBinding> {

    private SearchActivityViewModel.OnSearchResultClickListener mOnSearchResultClickListener;

    public SearchResultAdapter() {
        super();
    }

    @Override
    protected VhSearchResultBinding createBinding(ViewGroup parent, int type) {
        final VhSearchResultBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.vh_search_result,
                        parent, false);
        binding.setVariable(BR.clickListener, this.mOnSearchResultClickListener);
        return binding;
    }

    @Override
    protected void bind(VhSearchResultBinding binding, FulltextSearchResult item, List<Object> payloads) {
        binding.setVariable(BR.searchResultTitle, item.getName());
        binding.setVariable(BR.searchResult, item);
        binding.setVariable(BR.clickListener, this.mOnSearchResultClickListener);
        binding.setSearchResultIcon(R.drawable.ic_directions_bus_black_24dp);
    }

    @Override
    protected boolean areItemsTheSame(FulltextSearchResult oldItem, FulltextSearchResult newItem) {
        return oldItem.getName().equals(newItem.getName());
    }

    @Override
    protected boolean areContentsTheSame(FulltextSearchResult oldItem, FulltextSearchResult newItem) {
        return oldItem.getName().equals(newItem.getName());
    }

    public void setOnSearchResultClickListener(SearchActivityViewModel.OnSearchResultClickListener onSearchResultClickListener) {
        mOnSearchResultClickListener = onSearchResultClickListener;
        notifyDataSetChanged();
    }
}

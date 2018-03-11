package com.semeshky.kvgspotter.viewholder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;

import static com.semeshky.kvgspotter.adapter.HomeAdapter.TYPE_FAVORITE_INFO;


public class NoFavoriteViewHolder extends HomeAdapterViewHolder {
    public NoFavoriteViewHolder(@NonNull LayoutInflater from, @NonNull ViewGroup parent) {
        super(from, parent,
                R.layout.vh_no_favorite, TYPE_FAVORITE_INFO);
    }
}

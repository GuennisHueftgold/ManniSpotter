package com.semeshky.kvgspotter.viewholder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.databinding.VhFavoriteStationBinding;


public class FavoriteDataboundViewHolder extends DataboundViewHolder<VhFavoriteStationBinding> {
    public FavoriteDataboundViewHolder(ViewGroup parent) {
        super((VhFavoriteStationBinding) DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.vh_favorite_station,
                        parent,
                        false));
    }
}

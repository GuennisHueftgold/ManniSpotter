package com.semeshky.kvgspotter.viewholder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.databinding.VhNoFavoriteBinding;


public class NoFavoriteViewHolder extends DataboundViewHolder<VhNoFavoriteBinding> {
    public NoFavoriteViewHolder(ViewGroup parent) {
        super((VhNoFavoriteBinding) DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.vh_no_favorite,
                        parent,
                        false));
    }
}

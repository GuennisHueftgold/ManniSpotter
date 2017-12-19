package com.semeshky.kvgspotter.viewholder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.databinding.VhNearbyStopBinding;

public final class NearbyStopViewHolder extends DataboundViewHolder<VhNearbyStopBinding> {
    public NearbyStopViewHolder(ViewGroup parent) {
        super((VhNearbyStopBinding) DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.vh_nearby_stop,
                        parent,
                        false));
    }
}

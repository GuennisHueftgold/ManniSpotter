package com.semeshky.kvgspotter.viewholder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.databinding.VhStopDistanceBinding;

public final class StopDistanceViewHolder extends DataboundViewHolder<VhStopDistanceBinding> {
    public StopDistanceViewHolder(ViewGroup parent) {
        super((VhStopDistanceBinding) DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.vh_stop_distance,
                        parent,
                        false));
    }
}

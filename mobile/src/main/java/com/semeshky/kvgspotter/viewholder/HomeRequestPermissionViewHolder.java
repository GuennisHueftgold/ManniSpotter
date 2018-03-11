package com.semeshky.kvgspotter.viewholder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;

import static com.semeshky.kvgspotter.adapter.HomeAdapter.TYPE_NEARBY_STOP_INFO;

public final class HomeRequestPermissionViewHolder extends HomeAdapterViewHolder {

    public HomeRequestPermissionViewHolder(@NonNull LayoutInflater from, @NonNull ViewGroup parent) {
        super(from, parent,
                R.layout.vh_home_request_permission, TYPE_NEARBY_STOP_INFO);
    }
}

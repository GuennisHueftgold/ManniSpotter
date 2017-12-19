package com.semeshky.kvgspotter.viewholder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.databinding.VhHomeRequestPermissionBinding;

public final class HomeRequestPermissionViewHolder extends DataboundViewHolder<VhHomeRequestPermissionBinding> {

    public HomeRequestPermissionViewHolder(ViewGroup parent) {
        super((VhHomeRequestPermissionBinding) DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.vh_home_request_permission,
                        parent,
                        false));
    }
}

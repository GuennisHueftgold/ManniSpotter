package com.semeshky.kvgspotter.viewholder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.databinding.VhListSectionTitleBinding;

public final class ListSingleLineViewHolder extends DataboundViewHolder<VhListSectionTitleBinding> {

    public ListSingleLineViewHolder(ViewGroup parent) {
        super((VhListSectionTitleBinding) DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.vh_list_single_line,
                        parent,
                        false));
    }
}
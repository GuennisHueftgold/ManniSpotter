package com.semeshky.kvgspotter.viewholder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.databinding.VhListSectionTitleBinding;

public final class ListSectionTitleViewHolder extends DataboundViewHolder<VhListSectionTitleBinding> {

    public ListSectionTitleViewHolder(ViewGroup parent) {
        super((VhListSectionTitleBinding) DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.vh_list_section_title,
                        parent,
                        false));
    }
}

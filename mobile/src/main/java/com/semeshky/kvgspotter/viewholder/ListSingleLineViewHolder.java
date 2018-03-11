package com.semeshky.kvgspotter.viewholder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;

import static com.semeshky.kvgspotter.adapter.HomeAdapter.TYPE_TEXT_SINGLE_LINE;

public final class ListSingleLineViewHolder extends HomeAdapterViewHolder {

    public ListSingleLineViewHolder(@NonNull LayoutInflater from, @NonNull ViewGroup parent) {
        super(from, parent,
                R.layout.vh_list_single_line, TYPE_TEXT_SINGLE_LINE);
    }
}
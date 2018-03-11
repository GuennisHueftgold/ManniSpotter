package com.semeshky.kvgspotter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public abstract class LayoutViewHolder extends RecyclerView.ViewHolder {
    public LayoutViewHolder(@NonNull ViewGroup parent, @LayoutRes int resId) {
        this(LayoutInflater.from(parent.getContext()), parent, resId);
    }

    public LayoutViewHolder(@NonNull LayoutInflater from, @NonNull ViewGroup parent, @LayoutRes int resId) {
        super(from.inflate(resId, parent, false));
    }
}

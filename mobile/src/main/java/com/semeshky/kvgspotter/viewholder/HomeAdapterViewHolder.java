package com.semeshky.kvgspotter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class HomeAdapterViewHolder extends LayoutViewHolder {

    private final int mType;

    public HomeAdapterViewHolder(@NonNull LayoutInflater from, @NonNull ViewGroup parent, @LayoutRes int resId, int type) {
        super(from, parent, resId);
        this.mType = type;
    }

    public int getType() {
        return mType;
    }
}

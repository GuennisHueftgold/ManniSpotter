package com.semeshky.kvgspotter.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.semeshky.kvgspotter.R;

import static com.semeshky.kvgspotter.adapter.HomeAdapter.TYPE_TITLE;

public final class ListSectionTitleViewHolder extends HomeAdapterViewHolder {

    private final TextView mTxtTitle;

    public ListSectionTitleViewHolder(@NonNull LayoutInflater from, @NonNull ViewGroup parent) {
        super(from, parent, R.layout.vh_list_section_title, TYPE_TITLE);
        this.mTxtTitle = this.itemView.findViewById(R.id.txtTitle);
    }

    public void setTitle(@StringRes int title) {
        this.mTxtTitle.setText(title);
    }

    public void setTitle(String title) {
        this.mTxtTitle.setText(title);
    }

    public void setPrimaryColor(boolean primaryColor) {

    }
}

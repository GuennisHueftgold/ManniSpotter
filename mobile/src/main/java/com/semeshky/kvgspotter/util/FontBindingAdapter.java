package com.semeshky.kvgspotter.util;

import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.widget.TextView;

public class FontBindingAdapter {

    @BindingAdapter({"bind:supTypeFace"})
    public static void setTypeFace(TextView textView, String typeface) {
        textView.setTypeface(Typeface.create(typeface, Typeface.NORMAL));
    }
}

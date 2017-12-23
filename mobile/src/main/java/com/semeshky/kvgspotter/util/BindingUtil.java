package com.semeshky.kvgspotter.util;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.widget.TextView;

import com.semeshky.kvgspotter.R;

public final class BindingUtil {

    @BindingAdapter({"supTypeFace"})
    public static void setTypeFace(TextView textView, String typeface) {
        textView.setTypeface(Typeface.create(typeface, Typeface.NORMAL));
    }

    @BindingAdapter({"sectionTitleColorPrimary"})
    public static void setListSectionTitleColor(TextView v, boolean primaryColor) {
        final TypedValue typedValue = new TypedValue();
        final Context context = v.getContext();
        if (primaryColor) {
            context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
            v.setTextColor(ContextCompat.getColor(context, typedValue.resourceId));
        } else {
            v.setTextColor(ContextCompat.getColor(context, R.color.text_secondary_black));
        }
    }
}

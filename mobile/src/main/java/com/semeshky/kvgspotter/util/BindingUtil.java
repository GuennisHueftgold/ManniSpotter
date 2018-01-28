package com.semeshky.kvgspotter.util;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.widget.ImageView;
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

    // Ref https://stackoverflow.com/a/35809319/1188256
    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, String imageUri) {
        if (imageUri == null) {
            view.setImageURI(null);
        } else {
            view.setImageURI(Uri.parse(imageUri));
        }
    }

    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, Uri imageUri) {
        view.setImageURI(imageUri);
    }

    @BindingAdapter("android:src")
    public static void setImageDrawable(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }
}

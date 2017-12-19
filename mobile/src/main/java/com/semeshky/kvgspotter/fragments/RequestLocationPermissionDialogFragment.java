package com.semeshky.kvgspotter.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.semeshky.kvgspotter.R;

public final class RequestLocationPermissionDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.dialog_request_location_permission);
        dialog.setCancelable(true);
        return dialog;
    }
}

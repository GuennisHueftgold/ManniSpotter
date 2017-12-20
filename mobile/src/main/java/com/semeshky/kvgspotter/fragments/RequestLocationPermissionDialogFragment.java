package com.semeshky.kvgspotter.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;

import java.lang.ref.WeakReference;

public final class RequestLocationPermissionDialogFragment extends DialogFragment {

    private WeakReference<OnLocationRequestDialogListener> mListenerWeakReference = new WeakReference<>(null);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_request_location_permission, container);
    }

    public void setOnLocationRequestDialogListener(@NonNull OnLocationRequestDialogListener onLocationRequestDialogListener) {
        if (this.mListenerWeakReference != null) {
            this.mListenerWeakReference.clear();
        }
        this.mListenerWeakReference = new WeakReference<>(onLocationRequestDialogListener);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext())
                .setView(R.layout.dialog_request_location_permission)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final WeakReference<OnLocationRequestDialogListener> ref =
                                RequestLocationPermissionDialogFragment
                                        .this
                                        .mListenerWeakReference;
                        if (ref.get() != null) {
                            ref.get().onApproveRequest(true);
                        }
                        ref.clear();
                        RequestLocationPermissionDialogFragment
                                .this
                                .dismiss();
                    }
                });
        return builder.create();
    }

    public interface OnLocationRequestDialogListener {
        void onApproveRequest(boolean approved);
    }
}

package com.semeshky.kvgspotter.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.viewmodel.SplashActivityViewModel;

/**
 * Sync Data
 */
public class SetupStep2Fragment extends Fragment {
    private SplashActivityViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setup_step_1, container, false);
    }

    @CallSuper
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mViewModel = ViewModelProviders.of(this.getActivity()).get(SplashActivityViewModel.class);

    }

    protected void updateStatus(@SplashActivityViewModel.SyncStatus int status) {
        if (getView() == null)
            return;

        TransitionManager.beginDelayedTransition((ViewGroup) getView().findViewById(R.id.constraintLayout));
        getView().findViewById(R.id.progressBar)
                .setVisibility(status == SplashActivityViewModel.SYNC_STATUS_SYNCHRONIZING ? View.VISIBLE : View.GONE);
        final Button syncButton = getView().findViewById(R.id.btnSynchronize);
        if (status == SplashActivityViewModel.SYNC_STATUS_SYNCHRONIZING) {
            syncButton.setEnabled(false);
            syncButton.setText(R.string.synchronizing_ellipsis);
        } else if (status == SplashActivityViewModel.SYNC_STATUS_SYNCED) {
            syncButton.setEnabled(true);
            syncButton.setText(R.string.proceed);
        } else {
            syncButton.setEnabled(true);
            syncButton.setText(R.string.synchronize);
        }
        final TextView txtStatus = getView().findViewById(R.id.txtStatus);
        if (status == SplashActivityViewModel.SYNC_STATUS_SYNCED) {
            txtStatus.setText(R.string.successfully_synchronized);
            txtStatus.setTextColor(getResources().getColor(R.color.success_green));
            txtStatus.setVisibility(View.VISIBLE);
        } else if (status == SplashActivityViewModel.SYNC_STATUS_ERROR) {
            txtStatus.setVisibility(View.VISIBLE);
            txtStatus.setTextColor(getResources().getColor(R.color.error_red));
            txtStatus.setText(R.string.an_error_occured_check_internet);
        } else {
            txtStatus.setVisibility(View.GONE);
        }
    }
}

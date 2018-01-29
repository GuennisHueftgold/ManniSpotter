package com.semeshky.kvgspotter.fragments;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;

public class SetupStep1Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setup_step_1, container, false);
    }

    @CallSuper
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        view.post(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition((ViewGroup) view.findViewById(R.id.constraintLayout));
                view.findViewById(R.id.txtDescription)
                        .setVisibility(View.VISIBLE);
            }
        });
    }
}

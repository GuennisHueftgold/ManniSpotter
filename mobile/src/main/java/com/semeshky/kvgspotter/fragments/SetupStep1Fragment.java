package com.semeshky.kvgspotter.fragments;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.constraint.ConstraintLayout;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;

public class SetupStep1Fragment extends Fragment {
    private ConstraintLayout mConstraintLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setup_step_1, container, false);
    }

    @CallSuper
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        this.mConstraintLayout = view.findViewById(R.id.constraintLayout);
    }


    @Override
    public void onResume() {
        super.onResume();
        this.mConstraintLayout.post(new Runnable() {
            @Override
            public void run() {
                Fade fade = new Fade();
                fade.addTarget(R.id.txtDescription);
                TransitionManager.beginDelayedTransition(mConstraintLayout, fade);
                mConstraintLayout.findViewById(R.id.txtDescription)
                        .setVisibility(View.VISIBLE);
            }
        });
    }
}

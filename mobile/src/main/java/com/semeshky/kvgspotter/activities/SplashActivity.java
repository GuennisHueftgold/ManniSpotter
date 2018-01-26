package com.semeshky.kvgspotter.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.ChangeBounds;
import android.support.transition.Fade;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.settings.ClientSettings;
import com.semeshky.kvgspotter.viewmodel.SplashActivityViewModel;

/**
 * Activity to be used as splashscreen on app start to display app logo
 */
public final class SplashActivity extends AppCompatActivity {
    private static final String KEY_ANIMATION_PLAYED = SplashActivity.class.getSimpleName() + ".animation_played";
    private SplashActivityViewModel mViewModel;
    protected final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btnSynchronize) {
                if (mViewModel.isSynchronized()) {
                    ClientSettings
                            .getInstance(SplashActivity.this)
                            .setFirstSetup(true);
                    gotoMainActivity();
                } else {
                    SplashActivity
                            .this
                            .mViewModel
                            .synchronize();
                }
            }
        }
    };
    private ConstraintLayout mConstraintLayout;
    private boolean mAnimationPlayed = false;

    protected void startInAnimation() {
        ConstraintSet constraintSet2 = new ConstraintSet();
        constraintSet2.clone(SplashActivity.this, R.layout.activity_splash_setup_end);
        //////////
        final TransitionSet transitionSetMain = new TransitionSet();
        final TransitionSet transitionSetSub = new TransitionSet();
        final Transition transitionScale = new ChangeBounds();
        transitionScale.addTarget(R.id.appIcon);
        final Transition transitionFadeBackground = new Fade();
        transitionFadeBackground.addTarget(R.id.colorBackground);
        transitionSetSub.setOrdering(TransitionSet.ORDERING_TOGETHER);
        transitionSetSub.addTransition(transitionScale);
        transitionSetSub.addTransition(transitionFadeBackground);

        Transition transitionFade = new Fade();
        transitionFade.addTarget(R.id.btnSynchronize);
        transitionFade.addTarget(R.id.progressBar);
        transitionFade.addTarget(R.id.txtStatus);
        transitionFade.addTarget(R.id.txtTitle);
        transitionFade.addTarget(R.id.txtFirstSetupDescription);

        transitionSetMain.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
        transitionSetMain.addTransition(transitionSetSub);
        transitionSetMain.addTransition(transitionFade);
        transitionSetMain.setStartDelay(100);
        //transitionSet.setDuration(10000);
        //////////
        TransitionManager.beginDelayedTransition(mConstraintLayout, transitionSetMain);
        constraintSet2.applyTo(mConstraintLayout);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ClientSettings.getInstance(this)
                .isFirstSetupDone()) {
            gotoMainActivity();
        } else {
            this.mAnimationPlayed = savedInstanceState != null && savedInstanceState.getBoolean(KEY_ANIMATION_PLAYED, false);
            this.setTheme(R.style.AppTheme);
            this.setContentView(this.mAnimationPlayed ? R.layout.activity_splash_setup_end : R.layout.activity_splash_setup);
            this.mConstraintLayout = this.findViewById(R.id.constraintLayout);
            this.mViewModel = ViewModelProviders.of(this).get(SplashActivityViewModel.class);
            this.mViewModel.getSyncStatus()
                    .observe(this, new Observer<Integer>() {
                        @Override
                        public void onChanged(@Nullable Integer status) {
                            if (status == null)
                                return;
                            updateStatus(status);
                        }
                    });
            this.findViewById(R.id.btnSynchronize)
                    .setOnClickListener(this.mOnClickListener);
        }
    }

    protected void updateStatus(@SplashActivityViewModel.SyncStatus int status) {
        TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.constraintLayout));
        findViewById(R.id.progressBar)
                .setVisibility(status == SplashActivityViewModel.SYNC_STATUS_SYNCHRONIZING ? View.VISIBLE : View.GONE);
        final Button syncButton = findViewById(R.id.btnSynchronize);
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
        final TextView txtStatus = findViewById(R.id.txtStatus);
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

    protected void gotoMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.mConstraintLayout != null) {
            if (!this.mAnimationPlayed) {
                this.mAnimationPlayed = true;
                this.mConstraintLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        startInAnimation();
                    }
                });
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        this.mAnimationPlayed = state.getBoolean(KEY_ANIMATION_PLAYED, false);
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putBoolean(KEY_ANIMATION_PLAYED, this.mAnimationPlayed);
    }

}

package com.semeshky.kvgspotter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.transition.ChangeBounds;
import android.support.transition.Fade;
import android.support.transition.Scene;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v7.app.AppCompatActivity;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.settings.ClientSettings;
import com.semeshky.kvgspotter.viewmodel.SplashActivityViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Activity to be used as splashscreen on app start to display app logo
 */
public final class SplashActivity extends AppCompatActivity {
    private SplashActivityViewModel mViewModel;
    private ConstraintLayout mConstraintLayout;
    private Disposable mUpdateDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ClientSettings.getInstance(this)
                .isFirstSetupDone()) {
            gotoMainActivity();
        } else {
            this.setTheme(R.style.AppTheme);
            this.setContentView(R.layout.activity_splash_setup);
            this.mConstraintLayout = this.findViewById(R.id.constraintLayout);
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

            Transition transitionScale = new ChangeBounds();
            transitionScale.addTarget(R.id.progressBar);
            transitionScale.addTarget(R.id.txtStatus);
            transitionScale.addTarget(R.id.appIcon);
            Transition transitionFade = new Fade();
            transitionFade.addTarget(R.id.progressBar);
            transitionFade.addTarget(R.id.txtStatus);
            TransitionSet transitionSet = new TransitionSet();
            transitionSet.setOrdering(TransitionSet.ORDERING_TOGETHER);
            transitionSet.addTransition(transitionScale);
            transitionSet.addTransition(transitionFade);

            final Scene scene = Scene.getSceneForLayout(mConstraintLayout,
                    R.layout.activity_splash_setup_end, SplashActivity.this);
            TransitionManager.go(scene, transitionSet);
            //TODO Lock rotation so it won't reset
            Single.timer(2, TimeUnit.SECONDS)
                    .subscribe(new DisposableSingleObserver<Long>() {
                        @Override
                        public void onSuccess(Long aLong) {
                            gotoMainActivity();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.mUpdateDisposable != null)
            this.mUpdateDisposable.dispose();
    }
}

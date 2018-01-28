package com.semeshky.kvgspotter.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.view.View;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.fragments.SetupStep1Fragment;
import com.semeshky.kvgspotter.fragments.SetupStep2Fragment;
import com.semeshky.kvgspotter.fragments.SetupStep3Fragment;
import com.semeshky.kvgspotter.fragments.SetupStep4Fragment;
import com.semeshky.kvgspotter.settings.ClientSettings;
import com.semeshky.kvgspotter.viewmodel.SplashActivityViewModel;

/**
 * Activity to be used as splashscreen on app start to display app logo
 */
public final class SplashActivity extends AppCompatActivity {
    private static final String KEY_ENTRY_ANIMATION_PLAYED = "entry_animation_played";
    private SplashActivityViewModel mViewModel;
    private ConstraintLayout mConstraintLayout;
    protected final ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (position == 0) {
                mConstraintLayout
                        .setBackgroundColor(Color.argb(Math.round(255 * (1 - positionOffset)), 74, 139, 195));
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (position >= 1) {
                mConstraintLayout.setBackgroundColor(Color.TRANSPARENT);
            }
            findViewById(R.id.btnPrevious)
                    .setEnabled(position != 0);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private ViewPager mViewPager;
    private boolean mEntryAnimationPlayed = false;
    private Ad mAd;
    protected final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnNext:
                    SplashActivity
                            .this
                            .gotoNextPage();
                    break;
                case R.id.btnPrevious:
                    SplashActivity
                            .this
                            .gotoPreviosPage();
                    break;
            }
        }
    };

    private void gotoNextPage() {
        if (this.mViewPager.getCurrentItem() == this.mAd.getCount() - 1) {
            gotoMainActivity();
        } else {
            this.mViewPager.setCurrentItem(this.mViewPager.getCurrentItem() + 1);
        }
    }

    private void gotoPreviosPage() {
        if (this.mViewPager.getCurrentItem() == 0) {
            return;
        }
        this.mViewPager.setCurrentItem(this.mViewPager.getCurrentItem() - 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ClientSettings.getInstance(this)
                .isFirstSetupDone()) {
            gotoMainActivity();
        } else {
            this.setTheme(R.style.AppTheme);
            this.setContentView(R.layout.activity_splash);
            this.mConstraintLayout = this.findViewById(R.id.constraintLayout);
            this.mViewPager = this.findViewById(R.id.viewPager);
            this.mViewModel = ViewModelProviders.of(this).get(SplashActivityViewModel.class);
            this.mAd = new Ad(this.getSupportFragmentManager());
            this.mViewPager.setAdapter(this.mAd);
            this.mViewPager
                    .addOnPageChangeListener(this.mOnPageChangeListener);
            final View btnPrevious = findViewById(R.id.btnPrevious);
            final View btnNext = findViewById(R.id.btnNext);
            btnPrevious.setOnClickListener(this.mOnClickListener);
            btnNext.setOnClickListener(this.mOnClickListener);
            final boolean animationPlayed = savedInstanceState != null && savedInstanceState.getBoolean(KEY_ENTRY_ANIMATION_PLAYED, false);
            findViewById(R.id.divider)
                    .setVisibility(animationPlayed ? View.VISIBLE : View.GONE);
            btnNext.setVisibility(animationPlayed ? View.VISIBLE : View.GONE);
            btnPrevious.setVisibility(animationPlayed ? View.VISIBLE : View.GONE);
        }
    }

    protected void startEntryAnimation() {
        this.mConstraintLayout.post(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(mConstraintLayout);
                findViewById(R.id.divider)
                        .setVisibility(View.VISIBLE);
                findViewById(R.id.btnNext)
                        .setVisibility(View.VISIBLE);
                findViewById(R.id.btnPrevious)
                        .setVisibility(View.VISIBLE);
            }
        });
    }


    protected void gotoMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!this.mEntryAnimationPlayed) {
            startEntryAnimation();
        }
        if (this.mViewPager.getCurrentItem() == 0) {
            findViewById(R.id.btnPrevious).setEnabled(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_ENTRY_ANIMATION_PLAYED, this.mEntryAnimationPlayed);
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        this.mEntryAnimationPlayed = inState.getBoolean(KEY_ENTRY_ANIMATION_PLAYED, false);
    }

    class Ad extends FragmentPagerAdapter {

        public Ad(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SetupStep1Fragment();
                case 1:
                    return new SetupStep2Fragment();
                case 2:
                    return new SetupStep3Fragment();
                case 3:
                    return new SetupStep4Fragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}

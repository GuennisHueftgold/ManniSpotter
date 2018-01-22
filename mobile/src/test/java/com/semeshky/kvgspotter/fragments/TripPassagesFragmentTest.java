package com.semeshky.kvgspotter.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.semeshky.kvgspotter.BuildConfig;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.viewmodel.TripPassagesViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startVisibleFragment;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TripPassagesFragmentTest {

    @Before
    public void setup() {

    }

    @Test
    public void shouldNotBeNull() throws Exception {
        TripPassagesFragment fragment = new TripPassagesFragment();
        startVisibleFragment(fragment, TripPassagesTestActivity.class, 1);
    }
}

class TripPassagesTestActivity extends AppCompatActivity {
    private TripPassagesViewModel mViewModel;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setTheme(R.style.AppTheme);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(1);
        setContentView(frameLayout);
        this.mViewModel = ViewModelProviders.of(this).get(TripPassagesViewModel.class);
    }
}
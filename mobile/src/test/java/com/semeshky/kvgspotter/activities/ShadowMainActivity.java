package com.semeshky.kvgspotter.activities;

import com.semeshky.kvgspotter.api.Release;

import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadows.ShadowActivity;

import java.util.ArrayList;
import java.util.List;

@Implements(MainActivity.class)
public class ShadowMainActivity extends ShadowActivity {
    @RealObject
    private MainActivity mMainActivity;
    private List<Release> mShowUpdateNoticeArgs = new ArrayList<>();

    public Release getLastShowUpdateNoticeArg() {
        if (this.mShowUpdateNoticeArgs.size() == 0)
            return null;
        return this.mShowUpdateNoticeArgs.get(this.mShowUpdateNoticeArgs.size() - 1);
    }

    public int getShowUpdateNoticeCallCount() {
        return this.mShowUpdateNoticeArgs.size();
    }

    public void reset() {
        this.mShowUpdateNoticeArgs.clear();
    }

    public void showUpdateNotice(Release release) {
        this.mShowUpdateNoticeArgs.add(release);
    }
}

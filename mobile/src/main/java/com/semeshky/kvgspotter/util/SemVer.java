package com.semeshky.kvgspotter.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class SemVer {
    private final int mMajor;
    private final int mMinor;
    private final int mPatch;
    private final String mAppendix;

    public SemVer(int major, int minor, int patch, @Nullable String appendix) {
        this.mMajor = major;
        this.mMinor = minor;
        this.mPatch = patch;
        this.mAppendix = appendix;
    }

    public static SemVer parse(@NonNull String semVer) {
        if (!semVer.matches("^[vV]?\\d+(\\.\\d+){1,2}(-.*)?$")) {
            throw new RuntimeException("Couldnt parse the string: " + semVer);
        }
        String sub = semVer.replaceAll("[vV]", "");
        final int dashPosition = sub.indexOf('-');
        final String appendix = dashPosition < 0 ? null : sub.substring(dashPosition + 1);
        if (dashPosition >= 0)
            sub = sub.substring(0, dashPosition);
        final String[] splits = sub.split("\\.");
        final int major = Integer.parseInt(splits[0]);
        final int minor = Integer.parseInt(splits[1]);
        final int patch = splits.length == 2 ? 0 : Integer.parseInt(splits[2]);
        return new SemVer(major, minor, patch, appendix);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SemVer semVer = (SemVer) o;

        if (mMajor != semVer.mMajor) return false;
        if (mMinor != semVer.mMinor) return false;
        if (mPatch != semVer.mPatch) return false;
        return mAppendix != null ? mAppendix.equals(semVer.mAppendix) : semVer.mAppendix == null;
    }

    @Override
    public String toString() {
        return "SemVer{" + mMajor +
                "." + mMinor +
                "." + mPatch +
                "-" + mAppendix +
                '}';
    }

    public boolean isNewer(SemVer other) {
        if (this.equals(other))
            return false;
        return this.mMajor >= other.mMajor &&
                this.mMinor >= other.mMinor &&
                this.mPatch >= other.mPatch;
    }

    @Override
    public int hashCode() {
        int result = mMajor;
        result = 31 * result + mMinor;
        result = 31 * result + mPatch;
        result = 31 * result + (mAppendix != null ? mAppendix.hashCode() : 0);
        return result;
    }
}

package com.semeshky.kvgspotter.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class SemVer {
    private final int mMajor;
    private final int mMinor;
    private final int mPatch;
    private final String mAppendix;

    public SemVer(int major, int minor, int patch) {
        this(major, minor, patch, null);
    }

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

    public int getMajor() {
        return mMajor;
    }

    public int getMinor() {
        return mMinor;
    }

    public int getPatch() {
        return mPatch;
    }

    public String getAppendix() {
        return mAppendix;
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
        StringBuilder stringBuilder = new StringBuilder("SemVer{")
                .append(mMajor)
                .append(".")
                .append(mMinor)
                .append(".")
                .append(mPatch);
        if (this.mAppendix != null) {
            stringBuilder
                    .append("-")
                    .append(mAppendix);
        }
        return stringBuilder
                .append("}")
                .toString();
    }

    public boolean isNewer(SemVer other) {
        if (this.equals(other))
            return false;
        if (this.mMajor > other.mMajor) {
            return true;
        } else if (this.mMajor >= other.mMajor &&
                this.mMinor > other.mMinor) {
            return true;
        } else return this.mMajor >= other.mMajor &&
                this.mMinor >= other.mMinor &&
                this.mPatch > other.mPatch;
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

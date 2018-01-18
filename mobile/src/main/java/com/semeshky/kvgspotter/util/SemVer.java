package com.semeshky.kvgspotter.util;

import android.support.annotation.NonNull;

public final class SemVer {
    private final int mMajor;
    private final int mMinor;
    private final int mPatch;

    public SemVer(int major, int minor, int patch) {
        this.mMajor = major;
        this.mMinor = minor;
        this.mPatch = patch;
    }

    public static SemVer parse(@NonNull String semVer) {
        if (!semVer.matches("^[v]?\\d+(\\.\\d+)+$")) {
            throw new RuntimeException("Couldnt parse the string: " + semVer);
        }
        final String sub = semVer.replaceAll("[vV]", "");
        final String[] splits = sub.split("\\.");
        final int major = Integer.parseInt(splits[0]);
        final int minor = Integer.parseInt(splits[0]);
        final int patch = splits.length == 2 ? 0 : Integer.parseInt(splits[0]);
        return new SemVer(major, minor, patch);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SemVer semVer = (SemVer) o;

        if (mMajor != semVer.mMajor) return false;
        if (mMinor != semVer.mMinor) return false;
        return mPatch == semVer.mPatch;
    }

    @Override
    public String toString() {
        return "SemVer{" + mMajor +
                "." + mMinor +
                "." + mPatch +
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
        return result;
    }
}

package com.semeshky.kvgspotter.adapter;


public class DistanceDelta {
    private final float mTo;
    private final float mFrom;

    public DistanceDelta(float from, float to) {
        this.mFrom = from;
        this.mTo = to;
    }

    public float getTo() {
        return mTo;
    }

    public float getFrom() {
        return mFrom;
    }

    @Override
    public String toString() {
        return "DistanceDelta{" +
                "mTo=" + mTo +
                ", mFrom=" + mFrom +
                '}';
    }
}

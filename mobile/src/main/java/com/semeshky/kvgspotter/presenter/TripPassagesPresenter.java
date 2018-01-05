package com.semeshky.kvgspotter.presenter;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.view.View;

import com.github.guennishueftgold.trapezeapi.TripPassageStop;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.adapter.TripPassagesAdapter;
import com.semeshky.kvgspotter.util.JodaUtil;

public class TripPassagesPresenter implements TripPassagesAdapter.OnStationClickListener {

    public final ObservableBoolean firstStation = new ObservableBoolean(false);
    public final ObservableBoolean lastStation = new ObservableBoolean(false);
    public final ObservableBoolean activeStop = new ObservableBoolean(false);
    private TripPassagesAdapter.OnStationClickListener mOnStationClickListener;

    public String formatStatus(Context context, TripPassageStop tripPassageStop) {
        if (tripPassageStop == null)
            return "";
        switch (tripPassageStop.getStatus()) {
            case TripPassageStop.STATUS_DEPARTED:
                return context.getString(R.string.departed);
            case TripPassageStop.STATUS_PLANNED:
                return context.getString(R.string.time_planned,
                        JodaUtil.convertLocalTime(tripPassageStop.getPlannedTime()));
            case TripPassageStop.STATUS_STOPPING:
                return context.getString(R.string.stopping);
            case TripPassageStop.STATUS_PREDICTED:
                return context.getString(R.string.time_predicted,
                        JodaUtil.convertLocalTime(tripPassageStop.getActualTime()));
            default:
                return context.getString(R.string.unknown);
        }
    }

    public void setOnStationClickListener(TripPassagesAdapter.OnStationClickListener onStationClickListener) {
        mOnStationClickListener = onStationClickListener;
    }

    @Override
    public void onStationSelected(View titleView, TripPassageStop station) {
        if (this.mOnStationClickListener != null) {
            this.mOnStationClickListener.onStationSelected(titleView, station);
        }
    }
}

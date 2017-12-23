package com.semeshky.kvgspotter.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semeshky.kvg.kvgapi.TripPassageStop;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.databinding.VhTripPassageStopBinding;
import com.semeshky.kvgspotter.presenter.TripPassagesPresenter;
import com.semeshky.kvgspotter.util.JodaUtil;

public final class TripPassagesAdapter extends AbstractDataboundAdapter<TripPassageStop, VhTripPassageStopBinding> {


    private final OnStationClickListener mOnStationClickListener;

    public TripPassagesAdapter(OnStationClickListener stationClickListener) {
        this.mOnStationClickListener = stationClickListener;
    }
    public static String formatStatus(Context context, TripPassageStop tripPassageStop) {
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

    @Override
    protected VhTripPassageStopBinding createBinding(ViewGroup parent, int type) {
        final VhTripPassageStopBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.vh_trip_passage_stop,
                        parent, false);
        return binding;
    }

    @Override
    protected void bind(VhTripPassageStopBinding binding, TripPassageStop item) {
        binding.setTripPassageStop(item);
        if (binding.getPresenter() == null) {
            final TripPassagesPresenter tripPassagesPresenter = new TripPassagesPresenter();
            tripPassagesPresenter.setOnStationClickListener(this.mOnStationClickListener);
            binding.setPresenter(tripPassagesPresenter);
        }
        final TripPassagesPresenter presenter = binding.getPresenter();
        switch (item.getStatus()) {
            case TripPassageStop.STATUS_PREDICTED:
            case TripPassageStop.STATUS_PLANNED:
            case TripPassageStop.STATUS_STOPPING:
                presenter.activeStop.set(true);
                break;
            case TripPassageStop.STATUS_DEPARTED:
            case TripPassageStop.STATUS_UNKNOWN:
            default:
                presenter.activeStop.set(false);
                break;
        }
        presenter.firstStation.set(item.getStopSeqNum() == 1);
        final TripPassageStop lastStop = this.getItem(this.getItemCount() - 1);
        presenter.lastStation.set(item.getStopSeqNum() == lastStop.getStopSeqNum());
    }

    @Override
    protected boolean areItemsTheSame(TripPassageStop oldItem, TripPassageStop newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    protected boolean areContentsTheSame(TripPassageStop oldItem, TripPassageStop newItem) {
        return oldItem.equals(newItem);
    }

    public interface OnStationClickListener {
        void onStationSelected(View titleView, TripPassageStop station);
    }
}

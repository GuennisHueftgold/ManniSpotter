package com.semeshky.kvgspotter.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.semeshky.kvg.kvgapi.TripPassageStop;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.databinding.VhTripPassageStopBinding;
import com.semeshky.kvgspotter.util.JodaUtil;

public final class TripPassagesAdapter extends AbstractDataboundAdapter<TripPassageStop, VhTripPassageStopBinding> {


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
        switch (item.getStatus()) {
            case TripPassageStop.STATUS_PREDICTED:
                binding.setActiveStop(true);
                break;
            case TripPassageStop.STATUS_PLANNED:
                binding.setActiveStop(true);
                break;
            case TripPassageStop.STATUS_STOPPING:
                binding.setActiveStop(true);
                break;
            default:
                binding.setActiveStop(false);
                break;
        }
        binding.setFirstStop(item.getStopSeqNum() == 1);
        final TripPassageStop lastStop = this.getItem(this.getItemCount() - 1);
        binding.setLastStop(item.getStopSeqNum() == lastStop.getStopSeqNum());
    }

    @Override
    protected boolean areItemsTheSame(TripPassageStop oldItem, TripPassageStop newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    protected boolean areContentsTheSame(TripPassageStop oldItem, TripPassageStop newItem) {
        return oldItem.equals(newItem);
    }

}

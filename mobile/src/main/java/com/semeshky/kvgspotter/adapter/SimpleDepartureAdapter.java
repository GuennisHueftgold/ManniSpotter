package com.semeshky.kvgspotter.adapter;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.semeshky.kvg.kvgapi.Departure;
import com.semeshky.kvgspotter.BR;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.databinding.ListitemSimpleDepartureBinding;

import org.joda.time.LocalTime;


public class SimpleDepartureAdapter extends AbstractDataboundAdapter<Departure, ListitemSimpleDepartureBinding> {
    @Override
    protected ListitemSimpleDepartureBinding createBinding(ViewGroup parent, int viewType) {
        final ListitemSimpleDepartureBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.listitem_simple_departure,
                        parent, false);
        return binding;
    }

    @Override
    protected void bind(ListitemSimpleDepartureBinding binding, Departure item) {
        binding.setVariable(BR.departure, item);
    }

    @Override
    protected boolean areItemsTheSame(Departure oldItem, Departure newItem) {
        return oldItem.getTripId().equals(newItem.getTripId()) &&
                oldItem.getRouteId().equals(newItem.getRouteId());
    }

    @Override
    protected boolean areContentsTheSame(Departure oldItem, Departure newItem) {
        return oldItem.getTripId().equals(newItem.getTripId()) &&
                oldItem.getRouteId().equals(newItem.getRouteId()) &&
                compareLocalTime(oldItem.getActualTime(), newItem.getActualTime()) &&
                compareLocalTime(oldItem.getPlannedTime(), newItem.getPlannedTime());
    }

    private final boolean compareLocalTime(LocalTime a, LocalTime b) {
        if (a == null && b == null) {
            return true;
        } else if (a == null) {
            return false;
        } else if (b == null) {
            return false;
        } else {
            return a.equals(b);
        }
    }
}

package com.semeshky.kvgspotter.adapter;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.guennishueftgold.trapezeapi.Route;
import com.github.guennishueftgold.trapezeapi.Station;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.databinding.VhStationRouteBinding;

import java.util.List;

public class RoutesAdapter extends AbstractDataboundAdapter<Route, VhStationRouteBinding> {

    public RoutesAdapter() {
        super();
        this.setHasStableIds(true);
    }

    @Override
    protected VhStationRouteBinding createBinding(ViewGroup parent, int viewType) {
        return DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.vh_station_route,
                        parent, false);
    }

    @Override
    protected void bind(VhStationRouteBinding binding, Route item, List<Object> payloads) {
        binding.setRoute(item);
        binding.setCompoundIcon(R.drawable.ic_directions_bus_black_24dp);
    }

    @Override
    protected boolean areItemsTheSame(Route oldItem, Route newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    protected boolean areContentsTheSame(Route oldItem, Route newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(this.getItem(position).getId());
    }

    public void setStation(Station station) {
        this.setItems(station.getRoutes());
    }
}

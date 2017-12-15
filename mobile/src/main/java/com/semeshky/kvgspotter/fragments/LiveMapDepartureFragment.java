package com.semeshky.kvgspotter.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.semeshky.kvg.kvgapi.Departure;
import com.semeshky.kvg.kvgapi.Station;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.activities.TripPassagesActivity;
import com.semeshky.kvgspotter.adapter.DepartureAdapter;
import com.semeshky.kvgspotter.database.Stop;
import com.semeshky.kvgspotter.databinding.FragmentLiveMapDeparturesBinding;
import com.semeshky.kvgspotter.viewmodel.ActivityLiveMapViewModel;

import io.reactivex.observers.DisposableSingleObserver;
import timber.log.Timber;

public class LiveMapDepartureFragment extends Fragment {

    private final Toolbar.OnMenuItemClickListener mToolbarMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_details:
                    //TODO
                    return true;
                default:
                    return false;
            }
        }
    };
    private ActivityLiveMapViewModel mViewModel;
    private FragmentLiveMapDeparturesBinding mBinding;
    private DepartureAdapter mDepartureAdapter;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mViewModel = ViewModelProviders.of(getActivity())
                .get(ActivityLiveMapViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_live_map_departures, container, false);
        return this.mBinding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mDepartureAdapter = new DepartureAdapter(new DepartureAdapter.Presenter() {
            @Override
            public void onOpenClick(Departure departure) {
                startActivity(TripPassagesActivity.createIntent(view.getContext(), departure));
            }
        });
        this.mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        this.mBinding.recyclerView.setAdapter(this.mDepartureAdapter);
        this.mToolbar = this.mBinding.toolbar.toolbar;
        this.mToolbar.inflateMenu(R.menu.live_map_departures);
        this.mToolbar.setOnMenuItemClickListener(this.mToolbarMenuItemClickListener);
        this.mViewModel.getSelectedStop().observe(this,
                new Observer<Stop>() {
                    @Override
                    public void onChanged(@Nullable Stop stop) {
                        if (stop == null)
                            return;
                        getStation(stop);
                        mToolbar.setTitle(stop.getName());
                        mBinding.executePendingBindings();
                    }
                });
    }

    private void getStation(Stop stop) {
        this.mViewModel.loadStation(stop.getShortName())
                .subscribe(new DisposableSingleObserver<Station>() {
                    @Override
                    public void onSuccess(Station station) {
                        mDepartureAdapter.setItems(station.getActual());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }
}

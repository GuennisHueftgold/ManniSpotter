package com.semeshky.kvgspotter.map;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;


public class VehicleClusterRenderer extends DefaultClusterRenderer<VehicleClusterItem> {
    private final Resources mResources;

    public VehicleClusterRenderer(Context context, GoogleMap map, ClusterManager<VehicleClusterItem> clusterManager) {
        super(context, map, clusterManager);
        this.mResources = context.getResources();
    }

    @Override
    protected void onBeforeClusterItemRendered(VehicleClusterItem vehicleCluster, MarkerOptions markerOptions) {
        MapStyleGenerator.vehicleMarker(this.mResources, markerOptions);
        markerOptions
                .position(vehicleCluster.getPosition())
                .title(vehicleCluster.getTitle())
                .rotation(vehicleCluster.getHeading() - 90);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<VehicleClusterItem> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);
        markerOptions
                .zIndex(2);
    }
}

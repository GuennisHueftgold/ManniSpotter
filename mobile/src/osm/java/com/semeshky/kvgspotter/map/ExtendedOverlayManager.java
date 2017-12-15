package com.semeshky.kvgspotter.map;

import android.view.MotionEvent;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.DefaultOverlayManager;
import org.osmdroid.views.overlay.TilesOverlay;

/**
 * Provides option to intercept taps that missed all available targets
 */
public class ExtendedOverlayManager extends DefaultOverlayManager {
    private OnTapMissListener mOnTapMissListener;

    public ExtendedOverlayManager(TilesOverlay tilesOverlay) {
        super(tilesOverlay);
    }

    @Override
    public boolean onSingleTapConfirmed(final MotionEvent e, final MapView pMapView) {
        final boolean result = super.onSingleTapConfirmed(e, pMapView);
        if (!result && this.mOnTapMissListener != null)
            this.mOnTapMissListener.onTapMissed();
        return result;
    }

    public void setOnTapMissListener(OnTapMissListener onTapMissListener) {
        mOnTapMissListener = onTapMissListener;
    }

    public interface OnTapMissListener {
        void onTapMissed();
    }
}

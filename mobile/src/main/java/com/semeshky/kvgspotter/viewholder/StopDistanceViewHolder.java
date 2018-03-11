package com.semeshky.kvgspotter.viewholder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.adapter.DistanceDelta;
import com.semeshky.kvgspotter.adapter.HomeAdapter;

import java.util.List;

import timber.log.Timber;

import static com.semeshky.kvgspotter.adapter.HomeAdapter.TYPE_STOP;

public final class StopDistanceViewHolder extends HomeAdapterViewHolder {
    private final ImageView mIvIcon;
    private final TextView mTxtTitle, mTxtDistance;
    private final String mDistanceMeter, mDistanceKilometer;
    private ValueAnimator mDistanceAnimator = null;

    public StopDistanceViewHolder(@NonNull LayoutInflater from, @NonNull ViewGroup parent) {
        super(from, parent, R.layout.vh_stop_distance, TYPE_STOP);
        this.mIvIcon = this.itemView.findViewById(R.id.ivIcon);
        this.mTxtTitle = this.itemView.findViewById(R.id.txtTitle);
        this.mTxtDistance = this.itemView.findViewById(R.id.txtSubtitle);
        final Resources resources = parent.getResources();
        this.mDistanceKilometer = resources.getString(R.string.x_kilometer_short);
        this.mDistanceMeter = resources.getString(R.string.x_meter_short);
    }

    public void setDistanceStop(HomeAdapter.DistanceStop distanceStop, List<Object> changes) {
        this.mIvIcon.setImageResource(R.drawable.ic_directions_bus_black_24dp);
        this.mTxtTitle.setText(distanceStop.name);
        if (distanceStop.distance < 0) {
            this.mTxtDistance.setVisibility(View.GONE);
        } else {
            this.mTxtDistance.setVisibility(View.VISIBLE);
            DistanceDelta delta = null;
            if (changes != null && changes.size() > 0) {
                for (Object object : changes) {
                    if (object instanceof DistanceDelta) {
                        delta = (DistanceDelta) object;
                    }
                }
            }
            if (delta != null) {
                startDistanceAnimation(delta);
            } else {
                this.mTxtDistance.setText(formatDistance(distanceStop.distance));
            }
        }
    }

    void startDistanceAnimation(DistanceDelta delta) {
        if (this.mDistanceAnimator != null && this.mDistanceAnimator.isRunning()) {
            Timber.d("Cancel animation");
            this.mDistanceAnimator.cancel();
        }
        this.mDistanceAnimator = ValueAnimator.ofFloat(delta.getFrom(), delta.getTo());
        this.mDistanceAnimator.addUpdateListener(new DistanceUpdateListener());
        this.mDistanceAnimator.addListener(new DistanceListener(delta.getFrom(), delta.getTo()));
        this.mDistanceAnimator.setDuration(300);
        this.mDistanceAnimator.setInterpolator(new AccelerateInterpolator());
        this.mDistanceAnimator.start();
    }

    String formatDistance(float distance) {
        if (distance >= 1000f) {
            return String.format(mDistanceKilometer, distance / 1000f);
        } else {
            return String.format(mDistanceMeter, distance);
        }
    }

    final class DistanceListener implements Animator.AnimatorListener {

        private final float mEndValue;
        private final float mStartValue;

        DistanceListener(float startValue, float endValue) {
            this.mStartValue = startValue;
            this.mEndValue = endValue;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            mTxtDistance.setText(formatDistance(this.mStartValue));
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mTxtDistance.setText(formatDistance(this.mEndValue));
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    final class DistanceUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mTxtDistance.setText(formatDistance((Float) animation.getAnimatedValue()));
        }
    }

}

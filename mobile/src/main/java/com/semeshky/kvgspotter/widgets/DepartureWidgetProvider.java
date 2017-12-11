package com.semeshky.kvgspotter.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.activities.StationDetailActivity;

import timber.log.Timber;

public class DepartureWidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_STOP_SHORT_NAME = "extra_stop_short_name";
    public static final String ACTION_OPEN_FAVORITE = "action_open_favorite";
    public static final String EXTRA_STOP_NAME = "extra_stop_name";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < N; i++) {
            final int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            final Intent remoteViewService = new Intent(context, DepartureWidgetRemoteViewService.class);
            // Add the app widget ID to the intent extras.

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_departure);

            views.setRemoteAdapter(R.id.listView, remoteViewService);

            // The empty view is displayed when the collection has no items.
            // It should be in the same layout used to instantiate the RemoteViews
            // object above.
            views.setEmptyView(R.id.listView, R.id.emptyView);
            // Tell the AppWidgetManager to perform an update on the current app widget
            final Intent stopIntent = new Intent(context, StationDetailActivity.class);// StationDetailActivity.createIntent(context);
            stopIntent.setAction(ACTION_OPEN_FAVORITE);
            stopIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            stopIntent.setData(Uri.parse(stopIntent.toUri(Intent.URI_INTENT_SCHEME)));
            Timber.d("uri: " + Uri.parse(stopIntent.toUri(Intent.URI_INTENT_SCHEME)).toString());
            PendingIntent stopPendingIntent = PendingIntent.getActivity(context,
                    appWidgetId,
                    stopIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            views.setPendingIntentTemplate(R.id.listView, stopPendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.d("recieved");
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(ACTION_OPEN_FAVORITE)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            final String shortName = intent.getStringExtra(EXTRA_STOP_SHORT_NAME);
            final String name = intent.getStringExtra(EXTRA_STOP_NAME);
            final Intent stationDetailIntent = StationDetailActivity.createIntent(context, shortName);
            context.startActivity(stationDetailIntent);
            //Toast.makeText(context, "Touched view " + shortName, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }
}

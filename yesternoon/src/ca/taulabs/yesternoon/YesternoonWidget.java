package ca.taulabs.yesternoon;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class YesternoonWidget extends AppWidgetProvider
{
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    final int N = appWidgetIds.length;

    // Perform this loop procedure for each App Widget that belongs to this provider
    for (int i=0; i<N; i++) {
        int appWidgetId = appWidgetIds[i];

        // Create an Intent to launch YesternoonActivity
        Intent intent = new Intent(context, YesternoonActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Get the layout for the App Widget and attach an on-click listener
        // to the button
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widgetlayout);
        views.setOnClickPendingIntent(R.id.widgetButton, pendingIntent);

        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
  }
}

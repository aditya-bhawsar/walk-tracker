package com.aditya.walktracker.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.view.View;
import android.widget.RemoteViews;

import com.aditya.walktracker.R;
import com.aditya.walktracker.database.WalkDatabase;
import com.aditya.walktracker.database.WalkEntity;

public class LastWalkWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        WalkDatabase mDb = WalkDatabase.getInstance(context);
        int walkId = mDb.walkDao().getLastWalk();
        WalkEntity walk =mDb.walkDao().loadWalkByIDWidget(walkId);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.last_walk_widget_provider);

        if(walk!=null){
            views.setViewVisibility(R.id.walk_lay, View.VISIBLE);
            views.setViewVisibility(R.id.appwidget_text, View.GONE);
            views.setImageViewResource(R.id.walk_iv, walk.getWalkType());
            views.setTextViewText(R.id.time_tv, context.getString(R.string.timeText, walk.getStartTime(), walk.getEndTime()));
            views.setTextViewText(R.id.distance_tv,context.getString(R.string.distanceText, walk.getDistance()));
            views.setTextViewText(R.id.date_tv,context.getString(R.string.dateText, walk.getDate()));
        }
        else{
            views.setViewVisibility(R.id.walk_lay, View.GONE);
            views.setViewVisibility(R.id.appwidget_text, View.VISIBLE);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void onUpdateManual(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        for(int i :appWidgetIds){
            LastWalkWidgetProvider.updateAppWidget(context,appWidgetManager,i);
        }
    }
}


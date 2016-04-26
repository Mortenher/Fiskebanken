package com.example.morten.fiskebanken.activities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.morten.fiskebanken.R;
import com.example.morten.fiskebanken.database.FishDataSource;
import com.example.morten.fiskebanken.database.SQLiteHelper;
import com.example.morten.fiskebanken.utility.Fisk;

import java.sql.SQLException;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetActivity extends AppWidgetProvider {


    static FishDataSource fishDataSource;
    static String fishtype;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_activity);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int i : appWidgetIds) {
            startInternet(context, appWidgetManager, i);
        }
    }
    private void startInternet(Context context, AppWidgetManager appWidgetManager, int widgetID){

        fishDataSource = new FishDataSource(context);
        try {
            fishDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Fisk> fisker = fishDataSource.getAllFisk();
        for(Fisk fisk : fisker){
            Fisk fisken = fisker.get(fisker.size() - 1);
            fishtype = fisken.getType();
        }

        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_activity);
        Uri uri = Uri.parse("http://no.wikipedia.org/wiki/" + fishtype);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        PendingIntent pIntent = PendingIntent.getActivity(context, widgetID, intent, 0);
        // viewID - our clickable view ID
        widgetView.setOnClickPendingIntent(R.id.widgetbutton, pIntent);

        appWidgetManager.updateAppWidget(widgetID, widgetView);



    }




    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


package com.mthotengera.alarmmanagernotification.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.mthotengera.alarmmanagernotification.R;
import com.mthotengera.alarmmanagernotification.model.Quote;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by mt250219 on 10/12/15.
 */
public class AlarmService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getQuoteService();
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void getQuoteService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuoteService quoteService = retrofit.create(QuoteService.class);

        Call<Quote> call = quoteService.getQuote();
        call.enqueue(new Callback<Quote>() {
            @Override
            public void onResponse(Response<Quote> response, Retrofit retrofit) {
                displayNotification(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("MainActivity", t.getMessage());
            }
        });
    }

    public void displayNotification(Quote response){
        Notification.Builder mBuilder = new Notification.Builder(this).setSmallIcon(R.drawable.afterlight)
                .setContentTitle(response.getAuthor())
                .setContentText(response.getText());
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(1000, mBuilder.build());
    }
}

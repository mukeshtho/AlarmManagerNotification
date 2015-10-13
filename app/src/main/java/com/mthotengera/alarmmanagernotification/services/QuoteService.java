package com.mthotengera.alarmmanagernotification.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mthotengera.alarmmanagernotification.model.Quote;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by mt250219 on 10/13/15.
 */
public interface QuoteService{

    @GET("/quote/random")
    Call<Quote> getQuote();
}

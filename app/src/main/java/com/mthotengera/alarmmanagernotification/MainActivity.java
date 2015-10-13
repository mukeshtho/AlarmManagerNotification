package com.mthotengera.alarmmanagernotification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mthotengera.alarmmanagernotification.model.Quote;
import com.mthotengera.alarmmanagernotification.services.QuoteService;

import java.util.Calendar;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;

public class MainActivity extends AppCompatActivity {

    private String QUOTE = "Innovation distinguishes between a leader and a follower.";
    private TimePicker mTimepicker;
    public static final String BASE_URL = "http://192.168.1.67:3700";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTimepicker = (TimePicker) findViewById(R.id.quoteTimePicker);
        mTimepicker.setIs24HourView(true);
        mTimepicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Log.d("MainActivity", "Time Changed " + hourOfDay + " : " + minute);
                Snackbar.make(view, "Selected Time " + hourOfDay + ":" + minute, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setAlarmEvent(View view){
        Intent alarmIntent = new Intent(view.getContext(), AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(), 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar firingCal= Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

//        firingCal.set(Calendar.HOUR_OF_DAY, 8); // At the hour you wanna fire
//        firingCal.set(Calendar.MINUTE, 0); // Particular minute
//        firingCal.set(Calendar.SECOND, 0); // particular second
        firingCal.set(Calendar.SECOND, 5);
        if(firingCal.compareTo(currentCal) < 0) {
//            firingCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        Long intendedTime = firingCal.getTimeInMillis();
        alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
    }
    public void fetchQuote(View view){
        setAlarmEvent(view);
    }
    public void getJSON(View view){
        getQuoteService();
    }

    private void getQuoteService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuoteService quoteService = retrofit.create(QuoteService.class);

        Call<Quote> call = quoteService.getQuote();
        call.enqueue(new Callback<Quote>() {
            @Override
            public void onResponse(Response<Quote> response, Retrofit retrofit) {
                updateQuote(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("MainActivity", t.getMessage());
            }
        });
    }

    private void updateQuote(Quote response){
        ((TextView) findViewById(R.id.quote_txt)).setText(response.getText());
        ((TextView) findViewById(R.id.author_txt)).setText(response.getAuthor());
    }

}


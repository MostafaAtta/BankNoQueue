package com.atta.banknoqueue;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.atta.banknoqueue.classes.NotificationService;
import com.atta.banknoqueue.classes.SessionManager;

import java.util.concurrent.TimeUnit;

public class TicketDetailseActivity extends AppCompatActivity {

    TextView timerValue, serviceText, ticketIdText, branchNameText , cusOnQText, ticketDateText, estimatedTicketDateText;

    // Session Manager Class
    SessionManager session;


    CountDownTimer countDownTimer;
    private Intent nServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detailse);


        // Session class instance
        session = new SessionManager(getApplicationContext());

        nServiceIntent = new Intent(TicketDetailseActivity.this, NotificationService.class);

        timerValue =(TextView) findViewById(R.id.time_text);
        serviceText =(TextView) findViewById(R.id.service_txt);
        ticketIdText =(TextView) findViewById(R.id.ticket_id);
        branchNameText =(TextView) findViewById(R.id.branch_text);
        cusOnQText =(TextView) findViewById(R.id.cus_q_text);
        ticketDateText =(TextView) findViewById(R.id.ticket_date_text);
        estimatedTicketDateText =(TextView) findViewById(R.id.estimated_date_text);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/led.ttf");

        timerValue.setTypeface(custom_font);

        timerValue.setText(String.valueOf(session.getKeyWaitingTime()));
        ticketIdText.setText(session.getKeyTicketNumber());
        serviceText.setText("Service: " + session.getKeyService());
        branchNameText.setText(session.getKeyBranchName() + " Branch ");
        cusOnQText.setText("Customer in queue: " + String.valueOf(session.getKeyNumberOnQueue()));
        ticketDateText.setText(session.getKeyTicketDate());
        estimatedTicketDateText.setText(session.getKeyEstimatedDate());

        startTimer(session.getKeyWaitingTime());

        startNotification("Your service time", (int) session.getKeyWaitingTime());


        if (session.getKeyWaitingTime() > 900000) {
            startNotification("About 15 min till  Your service time", (int) (session.getKeyWaitingTime() - 900000));
        }
    }

    //Start Countdown method
    private void startTimer(long noOfMinute) {
        countDownTimer = new CountDownTimer(noOfMinute, 60000) {

            public void onTick(long millisUntilFinished) {
                long timeInHours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long timeInMinutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(timeInHours);
                //Convert milliseconds into hour,minute and seconds
                String ms = String.format("%02d:%02d",timeInHours,timeInMinutes);
                session.setKeyWaitingTime(millisUntilFinished);
                timerValue.setText(ms);
            }

            public void onFinish() {

                countDownTimer = null;
                timerValue.setText("00:00");
            }
        }.start();

    }
    private void startNotification(String message, int millis){

        nServiceIntent.setAction("ACTION_NOTIFY");
        nServiceIntent.putExtra("EXTRA_MESSAGE", message);
        nServiceIntent.putExtra("EXTRA_TIMER", millis);
        startService(nServiceIntent);
    }


}

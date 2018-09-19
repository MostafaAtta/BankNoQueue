package com.atta.banknoqueue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView ticketCard, currencyCard, branchesCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ticketCard = (CardView) findViewById(R.id.ticket);
        currencyCard = (CardView) findViewById(R.id.currency);
        branchesCard = (CardView) findViewById(R.id.branch_card);

        ticketCard.setOnClickListener(this);
        currencyCard.setOnClickListener(this);
        branchesCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == ticketCard){
            Intent intent = new Intent(MainActivity.this, TicketActivity.class);
            startActivity(intent);
        }else if (view == currencyCard){
            Intent intent = new Intent(MainActivity.this, CurrencyRateActivity.class);
            startActivity(intent);

        }else if (view == branchesCard){
            Intent intent = new Intent(MainActivity.this, BranchesActivity.class);
            startActivity(intent);

        }

    }
}

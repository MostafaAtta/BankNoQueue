package com.atta.banknoqueue;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atta.banknoqueue.classes.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CurrencyRateActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeContainer;

    public TextView curr1, curr2, curr3, curr4, curr5, curr6, curr7, curr8, curr9, curr10;

    /** Tag for the log messages */
    public final String LOG_TAG = com.atta.banknoqueue.CurrencyRateActivity.class.getSimpleName();


    public ArrayList<Double> curr = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_rate);

        curr1 = (TextView) findViewById(R.id.cur1);
        curr2 = (TextView) findViewById(R.id.cur2);
        curr3 = (TextView) findViewById(R.id.cur3);
        curr4 = (TextView) findViewById(R.id.cur4);
        curr5 = (TextView) findViewById(R.id.cur5);
        curr6 = (TextView) findViewById(R.id.cur6);
        curr7 = (TextView) findViewById(R.id.cur7);
        curr8 = (TextView) findViewById(R.id.cur8);
        curr9 = (TextView) findViewById(R.id.cur9);
        curr10 = (TextView) findViewById(R.id.cur10);
        getData();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.

                curr1.setText("00.00");
                curr2.setText("00.00");
                curr3.setText("00.00");
                curr4.setText("00.00");
                curr5.setText("00.00");
                curr6.setText("00.00");
                curr7.setText("00.00");
                curr8.setText("00.00");
                curr9.setText("00.00");
                curr10.setText("00.00");
                getData();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void getData(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.RATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonResponse) {
                        Log.d(LOG_TAG, jsonResponse);
                        curr.clear();

                        try {
                            // Create a JSONObject from the JSON response string
                            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                            JSONObject featureArray = baseJsonResponse.getJSONObject("quotes");

                            double cur1 = featureArray.getDouble("USDEGP");
                            curr1.setText(String.format("%.4f",cur1));
                            curr2.setText(String.format("%.4f",cur1/featureArray.getDouble("USDEUR")));
                            curr3.setText(String.format("%.4f",cur1/featureArray.getDouble("USDRUB")));
                            curr4.setText(String.format("%.4f",cur1/featureArray.getDouble("USDJPY")));
                            curr5.setText(String.format("%.4f",cur1/featureArray.getDouble("USDGBP")));
                            curr6.setText(String.format("%.4f",cur1/featureArray.getDouble("USDCAD")));
                            curr7.setText(String.format("%.4f",cur1/featureArray.getDouble("USDTRY")));
                            curr8.setText(String.format("%.4f",cur1/featureArray.getDouble("USDKRW")));
                            curr9.setText(String.format("%.4f",cur1/featureArray.getDouble("USDCHF")));
                            curr10.setText(String.format("%.4f",cur1/featureArray.getDouble("USDCNY")));

                        } catch (JSONException e) {
                            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
                        }
                        Log.d(LOG_TAG, "done");

                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG, error.toString());
                Toast.makeText(CurrencyRateActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(CurrencyRateActivity.this);
        requestQueue.add(stringRequest);

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(stringRequest);
        //return curr;
    }



}

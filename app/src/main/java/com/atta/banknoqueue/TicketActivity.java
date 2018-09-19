package com.atta.banknoqueue;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atta.banknoqueue.classes.Branch;
import com.atta.banknoqueue.classes.Constants;
import com.atta.banknoqueue.classes.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TicketActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener{

    /** Tag for the log messages */
    public final String LOG_TAG = com.atta.banknoqueue.TicketActivity.class.getSimpleName();

    private static Spinner areaSpinner, branchSpinner, servicesSpinner;

    private static List<String> areas = new ArrayList<>();

    private static List<Branch> branches = new ArrayList<>();

    private static List<String> services = new ArrayList<String>(){{
        add("Select Service");
    }};


    // Session Manager Class
    SessionManager session;

    ArrayAdapter<String> adapter1;
    ArrayAdapter<Branch> adapter2;
    ArrayAdapter<String> adapter3;

    Button statusButton, ticketButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);


        // Session class instance
        session = new SessionManager(getApplicationContext());

        areaSpinner = (Spinner)findViewById(R.id.area);
        branchSpinner = (Spinner)findViewById(R.id.branch);
        servicesSpinner = (Spinner)findViewById(R.id.service);

        // Spinner click listener
        areaSpinner.setOnItemSelectedListener(this);
        branchSpinner.setOnItemSelectedListener(this);
        servicesSpinner.setOnItemSelectedListener(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, services);


        // Drop down layout style - list view with radio button
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter1 to spinner
        servicesSpinner.setAdapter(adapter3);
        getAreas();

        statusButton = (Button) findViewById(R.id.check_status);
        ticketButton = (Button) findViewById(R.id.get_ticket);

        statusButton.setOnClickListener(this);
        ticketButton.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (adapterView.getId() == areaSpinner.getId()){

            if (position != 0){
                branches.clear();
                branches.add(new Branch(0, "Select Branch"));
                branchSpinner.setSelection(0);
                getBranches(position);
            }else {
                branches.clear();
                branches.add(new Branch(0, "Select Branch"));
                branchSpinner.setSelection(0);

                services.clear();
                services.add("Select Service");
                servicesSpinner.setSelection(0);
            }
        }else if (adapterView.getId() == branchSpinner.getId()){
            if (position != 0){
                services.add("Teller");
                services.add("Customer Service");
                services.add("Back Office");
                adapter3.notifyDataSetChanged();
            }

        }else if (adapterView.getId() == servicesSpinner.getId()){

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void getAreas() {

        areas.clear();
        areas.add("Select Area");

        branches.add(new Branch(0, "Select Branch"));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.AREAS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonResponse) {
                        Log.d(LOG_TAG, jsonResponse);


                        // Create an empty ArrayList that we can start adding mobiles to

                        try {
                            // Create a JSONObject from the JSON response string
                            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                            // Extract the JSONArray associated with the key called "features",
                            // which represents a list of features (or mobiles).
                            JSONArray featureArray = baseJsonResponse.getJSONArray("Areas");

                            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
                            for (int i=0; i<featureArray.length();i++) {

                                JSONObject currentArea = featureArray.getJSONObject(i);
                                int id = currentArea.getInt("id");
                                String name = currentArea.getString("areaname");

                                areas.add(name);

                            }

                        } catch (JSONException e) {
                            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
                        }
                        Log.d(LOG_TAG, "done");

                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG, error.toString());
                Toast.makeText(TicketActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(TicketActivity.this);
        requestQueue.add(stringRequest);

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, areas);


        // Drop down layout style - list view with radio button
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter1 to spinner

        areaSpinner.setAdapter(adapter1);




        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter2 = new ArrayAdapter<Branch>(this,android.R.layout.simple_spinner_item, branches);


        // Drop down layout style - list view with radio button
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter1 to spinner

        branchSpinner.setAdapter(adapter2);
    }

    public void getBranches(final int areaID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Branches_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonResponse) {
                        Log.d(LOG_TAG, jsonResponse);


                        // Create an empty ArrayList that we can start adding mobiles to

                        try {
                            // Create a JSONObject from the JSON response string
                            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                            // Extract the JSONArray associated with the key called "features",
                            // which represents a list of features (or mobiles).
                            JSONArray featureArray = baseJsonResponse.getJSONArray("branches");

                            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
                            for (int i=0; i<featureArray.length();i++) {

                                JSONObject currentMobile = featureArray.getJSONObject(i);
                                int id = currentMobile.getInt("id");
                                String name = currentMobile.getString("branchname");

                                Branch branch = new Branch(id, name);

                                branches.add(branch);

                            }

                            // Create an ArrayAdapter using the string array and a default spinner layout
                            adapter2.notifyDataSetChanged();

                        } catch (JSONException e) {
                            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
                        }
                        Log.d(LOG_TAG, "done");

                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG, error.toString());
                Toast.makeText(TicketActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("areaid", String.valueOf(areaID));
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(TicketActivity.this);
        requestQueue.add(stringRequest);

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(stringRequest);
        //return areas;

    }

    @Override
    public void onClick(View view) {
        if (view == statusButton){
            checkBranchStatus();
        }else if (view == ticketButton){
            newTicket();
        }
    }

    private void newTicket() {

        if (servicesSpinner.getSelectedItemPosition() == 0){
            Toast.makeText(TicketActivity.this,"Select a service",Toast.LENGTH_LONG).show();
            return;
        }

        final int branchPosition = branchSpinner.getSelectedItemPosition();
        final int servicePosition = servicesSpinner.getSelectedItemPosition();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.NEW_TICKET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonResponse) {
                        Log.d(LOG_TAG, jsonResponse);


                        // Create an empty ArrayList that we can start adding mobiles to

                        try {
                            // Create a JSONObject from the JSON response string
                            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                            int id = baseJsonResponse.getInt("id");
                            String branchName = baseJsonResponse.getString("branchname");
                            String ticketNumber = baseJsonResponse.getString("ticket_num");
                            int numberOnQ = baseJsonResponse.getInt("num_on_Q");
                            String service = baseJsonResponse.getString("service");

                            long waitingTimeInMillisecond = baseJsonResponse.getInt("waiting_time") * 60 * 1000;

                            int waitingTime = baseJsonResponse.getInt("waiting_time");


                            long timeInHours = TimeUnit.MILLISECONDS.toHours(waitingTimeInMillisecond);
                            long timeInMinutes = TimeUnit.MILLISECONDS.toMinutes(waitingTimeInMillisecond) -
                                    TimeUnit.HOURS.toMinutes(timeInHours);
                            int h = waitingTime / 60;
                            int m = waitingTime % 60;
                            String waitingTimeHHMM = h+":"+m;

                            Date currentDate = Calendar.getInstance().getTime();

                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                            String ticketDate = df.format(currentDate);

                            Calendar now = Calendar.getInstance();

                            Calendar tmp = (Calendar) now.clone();
                            tmp.add(Calendar.HOUR_OF_DAY, (int) timeInHours);
                            tmp.add(Calendar.MINUTE, (int) timeInMinutes);

                            Date estimatedDate = tmp.getTime();

                            String estimatedTicketDate = df.format(estimatedDate);

                            session.createTicke(ticketNumber, branchName, numberOnQ, service, waitingTimeInMillisecond, ticketDate, estimatedTicketDate);

                            Intent intent = new Intent(TicketActivity.this, TicketDetailseActivity.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
                        }
                        Log.d(LOG_TAG, "done");

                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG, error.toString());
                Toast.makeText(TicketActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("branchid", String.valueOf(branches.get(branchPosition).getId()));
                params.put("service", String.valueOf(services.get(servicePosition)));
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(TicketActivity.this);
        requestQueue.add(stringRequest);
    }

    private void checkBranchStatus() {

        if (servicesSpinner.getSelectedItemPosition() == 0){
            Toast.makeText(TicketActivity.this,"Select a service",Toast.LENGTH_LONG).show();
            return;
        }

        final int position = branchSpinner.getSelectedItemPosition();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Branches_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonResponse) {
                        Log.d(LOG_TAG, jsonResponse);


                        // Create an empty ArrayList that we can start adding mobiles to

                        try {
                            // Create a JSONObject from the JSON response string
                            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                            int id = baseJsonResponse.getInt("id");
                            String name = baseJsonResponse.getString("branchname");
                            int tellerQ = baseJsonResponse.getInt("teller_no");
                            int customerServiceQ = baseJsonResponse.getInt("cust_service_no");
                            int backOfficeQ = baseJsonResponse.getInt("back_office_no");

                            int waitingTime;

                            AlertDialog alertDialog = new AlertDialog.Builder(TicketActivity.this).create();
                            alertDialog.setTitle(branches.get(position).getName() + " Branch");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "New ticket",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            newTicket();
                                        }
                                    });

                            switch (servicesSpinner.getSelectedItemPosition()){

                                case 1:
                                    //Toast.makeText(TicketActivity.this,String.valueOf(tellerQ*15),Toast.LENGTH_LONG).show();
                                    waitingTime = tellerQ*5;
                                    break;
                                case 2:
                                    //Toast.makeText(TicketActivity.this,String.valueOf(customerServiceQ*20),Toast.LENGTH_LONG).show();
                                    waitingTime = customerServiceQ*8;
                                    break;
                                case 3:
                                    //Toast.makeText(TicketActivity.this,String.valueOf(BackOfficeQ*20),Toast.LENGTH_LONG).show();
                                    waitingTime = backOfficeQ*8;
                                    break;
                                default:
                                    waitingTime = 0;
                            }

                            int h = waitingTime / 60;
                            int m = waitingTime % 60;
                            String newTime = String.format("%02d:%02d",h,m);

                            String branchStatus = "Branch status: ";
                            String msg;

                            if (waitingTime <= 20){

                                alertDialog.setMessage(Html.fromHtml(branchStatus + "<font color='#4CAF50'>Free</font><br>"
                                        +"<br>Waiting time is: " +"<font color='#4CAF50'>" + newTime + "</font>"));

                            }else if (waitingTime <= 40){
                                alertDialog.setMessage(Html.fromHtml(branchStatus + "<font color='#FFEB3B'>Normal</font><br>"
                                        + "<br>Waiting time is: " +"<font color='#FFEB3B'>" + newTime + "</font>"));
                            }else{
                                alertDialog.setMessage(Html.fromHtml(branchStatus + "<font color='#F44336'>Crowded</font><br>"
                                        + "<br>Waiting time is: " +"<font color='#F44336'>" + newTime + "</font>"));
                            }
                            alertDialog.show();

                        } catch (JSONException e) {
                            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
                        }
                        Log.d(LOG_TAG, "done");

                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG, error.toString());
                Toast.makeText(TicketActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("branchid", String.valueOf(branches.get(position).getId()));
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(TicketActivity.this);
        requestQueue.add(stringRequest);
    }
}

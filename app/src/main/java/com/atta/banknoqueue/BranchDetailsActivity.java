package com.atta.banknoqueue;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

public class BranchDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    /** Tag for the log messages */
    public final String LOG_TAG = BranchDetailsActivity.class.getSimpleName();

    TextView branchNameText, addressText, tellerText, csText, backText;

    ImageView locationImage;

    Button newTicket;

    Branch branch;

    String servise;

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_details);



        // Session class instance
        session = new SessionManager(getApplicationContext());

        branch = (Branch) getIntent().getSerializableExtra("branch");

        branchNameText = (TextView) findViewById(R.id.branch_name_text);
        addressText = (TextView) findViewById(R.id.address_text);
        tellerText = (TextView) findViewById(R.id.teller_text);
        csText = (TextView) findViewById(R.id.cs_text);
        backText = (TextView) findViewById(R.id.back_text);
        locationImage = (ImageView) findViewById(R.id.location_image);
        newTicket = (Button) findViewById(R.id.ticket_btn);

        branchNameText.setText(branch.getName());
        addressText.setText("Address: " + branch.getAddress());

        tellerText.setText("Teller Q       : " + branch.getTellerQ());
        csText.setText("Customer Service Q: " + branch.getCustomerServiceQ());
        backText.setText("Back Office Q:  " + branch.getBackOfficeQ());


        locationImage.setOnClickListener(this);
        newTicket.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == locationImage){
            //shop.getLocationLatitude() +", "+ shop.getLocationLongitude()
            Uri locationUri = Uri.parse("geo:0,0?q="+ branch.getLocationLatitude() +", "+ branch.getLocationLongitude()
                    + "("+ branch.getName()+")");

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, locationUri);
            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps");
            // Attempt to start an activity that can handle the Intent

            startActivity(mapIntent);
        }else if (v == newTicket){
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(BranchDetailsActivity.this);
            alertDialog.setTitle("New Ticket");
            alertDialog.setMessage("Select the required service");

            final Spinner servicesSpinner = new Spinner(BranchDetailsActivity.this);
            servicesSpinner.setBackgroundResource(android.R.drawable.btn_dropdown);
            final List<String> services = new ArrayList<String>(){{
                add("Select Service");
                add("Teller");
                add("Customer Service");
                add("Back Office");
            }};
            ArrayAdapter<String> adapter3 =
                    new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, services);

            // Drop down layout style - list view with radio button
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter1 to spinner
            servicesSpinner.setAdapter(adapter3);


            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            servicesSpinner.setLayoutParams(lp);
            alertDialog.setView(servicesSpinner);
            alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {



                    if (servicesSpinner.getSelectedItemPosition() == 0){
                        Toast.makeText(BranchDetailsActivity.this,"Select a service",Toast.LENGTH_LONG).show();
                        return;
                    }else {

                        newTicket(services.get(servicesSpinner.getSelectedItemPosition()));
                    }
                }
            });
            alertDialog.show();
        }
    }

    public int dpToPx(int dp) {
        float density = BranchDetailsActivity.this.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    private void newTicket(final String service) {


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

                            Intent intent = new Intent(BranchDetailsActivity.this, TicketDetailseActivity.class);
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
                Toast.makeText(BranchDetailsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("branchid", String.valueOf(branch.getId()));
                params.put("service", service);
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(BranchDetailsActivity.this);
        requestQueue.add(stringRequest);
    }
}

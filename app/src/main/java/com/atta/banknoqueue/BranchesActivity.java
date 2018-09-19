package com.atta.banknoqueue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.atta.banknoqueue.classes.CustomListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BranchesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /** Tag for the log messages */
    public final String LOG_TAG = BranchesActivity.class.getSimpleName();

    ArrayList<Branch> listArray = new ArrayList<Branch>();

    CustomListViewAdapter customListViewAdapter;

    ListView listView;

    private static Spinner areaSpinner;

    private static List<String> areas = new ArrayList<>();

    ArrayAdapter<String> adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches);

        areaSpinner = (Spinner)findViewById(R.id.area);

        // Spinner click listener
        areaSpinner.setOnItemSelectedListener(this);

        getAreas();

        listView = (ListView) findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Branch branch = customListViewAdapter.getItem(position);

                Intent intent = new Intent(BranchesActivity.this,BranchDetailsActivity.class);

                intent.putExtra("branch", branch);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_LONG).show();
            }
        });

        //Toast.makeText(getApplicationContext(), location, Toast.LENGTH_LONG).show();
    }

    public void getAreas() {

        areas.clear();
        areas.add("All Branches");

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
                Toast.makeText(BranchesActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(BranchesActivity.this);
        requestQueue.add(stringRequest);

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, areas);


        // Drop down layout style - list view with radio button
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter1 to spinner

        areaSpinner.setAdapter(adapter1);


    }

    private void displayBranches(final int areaId) {

        String url;
        final Map<String,String> params;

        if (areaId == 0) {
            url = Constants.GET_BRANCHES_URL;
            params = null;

        }else {
            url = Constants.Branches_URL;
            params = new HashMap<String, String>();
            params.put("areaid", String.valueOf(areaId));
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Create a JSONObject from the JSON response string
                    JSONObject baseJsonResponse = new JSONObject(response);

                    if(!baseJsonResponse.getBoolean("error")){


                        customListViewAdapter = new CustomListViewAdapter(BranchesActivity.this, listArray);

                        customListViewAdapter.clear();
                        listArray.clear();

                        // which represents a list of features (or mobiles).
                        JSONArray featureArray = baseJsonResponse.getJSONArray("branches");

                        for (int i=0; i<featureArray.length();i++) {

                            JSONObject currentMobile = featureArray.getJSONObject(i);
                            int id = currentMobile.getInt("id");
                            String name = currentMobile.getString("branchname");
                            String areaName = currentMobile.getString("area_name");
                            int tellerQ = currentMobile.getInt("teller_no");
                            int customerServiceQ = currentMobile.getInt("cust_service_no");
                            int backOfficeQ = currentMobile.getInt("back_office_no");
                            String address = currentMobile.getString("address");
                            String location_longitude = currentMobile.getString("location_longitude");
                            String location_latitude = currentMobile.getString("location_latitude");




                            Branch branch = new Branch(id, name, areaName, tellerQ, customerServiceQ, backOfficeQ,
                                    address, location_latitude, location_longitude);

                            listArray.add(branch);

                        }
                        listView.setAdapter(customListViewAdapter);
                        //customListViewAdapter.addAll(listArray);

                    }else{

                        Toast.makeText(getApplicationContext(), baseJsonResponse.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    Toast.makeText(BranchesActivity.this,"error",Toast.LENGTH_LONG).show();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BranchesActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == areaSpinner.getId()){

            displayBranches(position);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        displayBranches(0);
    }
}

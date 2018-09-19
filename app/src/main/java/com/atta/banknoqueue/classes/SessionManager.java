package com.atta.banknoqueue.classes;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mosta on 3/14/2018.
 */

public class SessionManager {


    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "TrafficManager";

    // All Shared Preferences Keys
    private static final String KEY_TICKET_NUMBER = "ticketNumber";

    // User name (make variable public to access from outside)
    public static final String KEY_BRANCE_NAME = "branchName";

    // User Name (make variable public to access from outside)
    public static final String KEY_NUMBER_ON_QUEUE = "numberOnQ";

    // User Name (make variable public to access from outside)
    public static final String KEY_SERVICE = "service";

    // User Name (make variable public to access from outside)
    public static final String KEY_WAITING_TIME = "waitingTime";

    // User Name (make variable public to access from outside)
    public static final String KEY_TICKET_DATE = "ticketDate";

    // User Name (make variable public to access from outside)
    public static final String KEY_ESTIMATED_DATE = "estimatedTicketDate";


    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createTicke(String ticketNumber, String branchName, int numberOnQ, String service, long waitingTime,
                            String ticketDate, String estimatedTicketDate){


        // Storing ticketNumber in pref
        editor.putString(KEY_TICKET_NUMBER, ticketNumber);

        // Storing Password in pref
        editor.putString(KEY_BRANCE_NAME, branchName);

        // Storing Password in pref
        editor.putInt(KEY_NUMBER_ON_QUEUE, numberOnQ);

        // Storing Password in pref
        editor.putString(KEY_SERVICE, service);

        // Storing Password in pref
        editor.putLong(KEY_WAITING_TIME, waitingTime);

        // Storing Password in pref
        editor.putString(KEY_TICKET_DATE, ticketDate);

        // Storing Password in pref
        editor.putString(KEY_ESTIMATED_DATE, estimatedTicketDate);

        // commit changes
        editor.commit();
    }

    public String getKeyTicketNumber() {
        return pref.getString(KEY_TICKET_NUMBER, "No Ticket");
    }

    public String getKeyBranchName() {
        return pref.getString(KEY_BRANCE_NAME, "No Ticket");
    }

    public int getKeyNumberOnQueue() {
        return pref.getInt(KEY_NUMBER_ON_QUEUE, 0);
    }

    public String getKeyService() {
        return pref.getString(KEY_SERVICE, "No Ticket");
    }

    public long getKeyWaitingTime() {
        return pref.getLong(KEY_WAITING_TIME, 0);
    }

    public String getKeyTicketDate() {
        return pref.getString(KEY_TICKET_DATE, "No Ticket");
    }

    public String getKeyEstimatedDate() {
        return pref.getString(KEY_ESTIMATED_DATE, "No Ticket");
    }



    public void setKeyWaitingTime(long waitingTime) {

        editor.putLong(KEY_WAITING_TIME, waitingTime);

        // commit changes
        editor.commit();
    }
}

package com.atta.banknoqueue.classes;

import java.io.Serializable;

/**
 * Created by mosta on 3/13/2018.
 */

public class Branch  implements Serializable {

    private int mID, mTellerQ, mCustomerServiceQ, mBackOfficeQ;
    private String mName, mAreaName, mAddress, mLocationLatitude, mLocationLongitude;

    public Branch(int id, String name, String areaName, int tellerQ, int customerServiceQ, int backOfficeQ,
                  String address, String locationLatitude, String locationLongitude){

        mID = id;
        mName = name;
        mAreaName = areaName;
        mTellerQ = tellerQ;
        mCustomerServiceQ = customerServiceQ;
        mBackOfficeQ = backOfficeQ;
        mAddress = address;
        mLocationLatitude = locationLatitude;
        mLocationLongitude = locationLongitude;

    }


    public Branch(int id, String name){

        mID = id;
        mName = name;

    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return mID;
    }

    @Override
    public String toString() {
        return mName;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getLocationLatitude() {
        return mLocationLatitude;
    }

    public String getLocationLongitude() {
        return mLocationLongitude;
    }

    public int getTellerQ() {
        return mTellerQ;
    }

    public int getCustomerServiceQ() {
        return mCustomerServiceQ;
    }

    public int getBackOfficeQ() {
        return mBackOfficeQ;
    }

    public String getAreaName() {
        return mAreaName;
    }
}

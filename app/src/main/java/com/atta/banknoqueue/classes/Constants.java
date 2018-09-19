package com.atta.banknoqueue.classes;

/**
 * Created by mosta on 1/16/2018.
 */

public class Constants {

    public static final String RATE_URL =
            "http://apilayer.net/api/live?access_key=1680584d6c2c64b8a76306249a4714b2&currencies=USD,EUR,RUB,JPY,GBP,CAD,TRY,KRW,CHF,CNY,EGP&format=1";

    private static final String ROOT_URL = "http://ec2-18-191-38-255.us-east-2.compute.amazonaws.com//bank_noqueue";

    public static final String AREAS_URL = ROOT_URL + "/get_all_areas.php";

    public static final String Branches_URL = ROOT_URL + "/get_branches_by_area.php";

    public static final String Branches_DATA_URL = ROOT_URL + "/get_branch_data_by_id.php";

    public static final String NEW_TICKET_URL = ROOT_URL + "/inset_ticket.php";

    public static final String GET_BRANCHES_URL = ROOT_URL + "/get_branches.php";

}

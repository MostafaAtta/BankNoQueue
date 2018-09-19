package com.atta.banknoqueue.classes;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.atta.banknoqueue.R;

import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter<Branch> {


    public CustomListViewAdapter(Activity context, List<Branch> branches) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, branches);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the  object located at this position in the list
        Branch currentBranch = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView branchNameText = (TextView) listItemView.findViewById(R.id.branch_text_view);

        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        branchNameText.setText(currentBranch.getName());

        int customerAvg = (currentBranch.getTellerQ() + currentBranch.getCustomerServiceQ()
                + currentBranch.getBackOfficeQ())/3;


        String status;

        TextView statusNameText = (TextView) listItemView.findViewById(R.id.status_text_view);

        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView


        if(customerAvg <= 4){
            status = "Free";
            statusNameText.setTextColor(Color.GREEN);
        }else if(customerAvg <= 8){
            status = "Normal";
            statusNameText.setTextColor(Color.BLUE);
        }else{
            status = "Crowded";
            statusNameText.setTextColor(Color.RED);
        }

        statusNameText.setText(status);


        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}

package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by poran on 10/2/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private  static final  String LOCATION_SEPARATOR=" of ";


    Context context;



    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes){
       super(context,0,earthquakes);



    }


    @Override
    public int getCount() {
        return 0;
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView=view;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                    R.layout.earthquake_list_item, viewGroup, false);
        }


       //view=inflater.inflate(R.layout.earthquake_list_item,null);
        // Find the earthquake at the given position in the list of earthquakes
        Earthquake currentEarthquake = getItem(i);

        // Find the TextView with view ID magnitude
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);
        // Display the magnitude of the current earthquake in that TextView

        String formattedMagnitude=formatMagnitude(currentEarthquake.getMagnitude());
        magnitudeView.setText(formattedMagnitude);

        GradientDrawable magnitudeCircle= (GradientDrawable) magnitudeView.getBackground();
        int magnitudeColor=getMagnitudeColor(currentEarthquake.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        // Find the TextView with view ID location
        TextView locationView = (TextView) listItemView.findViewById(R.id.location);
        // Display the location of the current earthquake in that TextView

        String orginalLocation=currentEarthquake.getLocation();
        String primaryLocation;
        String offSetLocation;
        if(orginalLocation.contains(LOCATION_SEPARATOR)){
            String []parts=orginalLocation.split(LOCATION_SEPARATOR);

            offSetLocation=parts[0]+LOCATION_SEPARATOR;
            primaryLocation=parts[1];

        }else {

            offSetLocation=context.getString(R.string.near_the);

            primaryLocation=orginalLocation;
        }
        locationView.setText(primaryLocation+" "+offSetLocation);

        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(currentEarthquake.getmTimeInMilliseconds());

        // Find the TextView with view ID date
        TextView dateView = (TextView) view.findViewById(R.id.date);
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);
        // Display the date of the current earthquake in that TextView
        dateView.setText(formattedDate);

        // Find the TextView with view ID time
        TextView timeView = (TextView) view.findViewById(R.id.time);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);
        // Display the time of the current earthquake in that TextView
        timeView.setText(formattedTime);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeResourceId;
        int magnitudeFloor= (int) Math.floor(magnitude);
        switch (magnitudeFloor){
            case 0:
            case 1:
                magnitudeResourceId=R.color.magnitude1;
                break;
            case 2:
                magnitudeResourceId=R.color.magnitude2;
                break;
            case 3:
                magnitudeResourceId=R.color.magnitude3;
                break;
            case 4:
                magnitudeResourceId=R.color.magnitude4;
                break;
            case 5:
                magnitudeResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeResourceId = R.color.magnitude10plus;
                break;
        }

        return ContextCompat.getColor(context,magnitudeResourceId);

    }

    private String formatMagnitude(double magnitude) {

        DecimalFormat magnitudeFormat=new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);

    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}

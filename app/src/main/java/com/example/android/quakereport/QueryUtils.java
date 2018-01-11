package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Sample JSON response for a USGS query */

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     *
     */
    private final static String LOG_TAG=QueryUtils.class.getName();
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Earthquake> fetchEarthquakeData(String requestUrl){
        URL mUrl=createUrl(requestUrl);
        String jsonRespone=" ";
       try{
           jsonRespone=makeHTTPRequest(mUrl);
       }catch (IOException e){
           Log.d(LOG_TAG,"Problem makeing the url connection"+e);

       }

       List<Earthquake> earthquakes=extractEarthquakes(jsonRespone);
        return earthquakes;

    }

    private static String makeHTTPRequest (URL mUrl) throws IOException{
        String jsonRespose=" ";
        if(mUrl==null){
            return jsonRespose;

        }

        HttpURLConnection urlConnection=null;
        InputStream inputstream=null;

        try{
            urlConnection= (HttpURLConnection) mUrl.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200){
                inputstream=urlConnection.getInputStream();
                jsonRespose=readFromStream(inputstream);

            }else{
                Log.e(LOG_TAG,"Error Respose code "+urlConnection.getResponseCode());
            }

        }catch (IOException e){
            Log.d(LOG_TAG,"Failed Connection Established",e);



        }
        finally {

            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputstream!=null){
                inputstream.close();
            }
        }

        return jsonRespose;






    }

    private static String readFromStream(InputStream inputstream) throws IOException {
            StringBuilder output=new StringBuilder();

        if(inputstream!=null){
            InputStreamReader inputStreamReader=new InputStreamReader(inputstream, Charset.forName("UTF-8"));
            BufferedReader reader=new BufferedReader(inputStreamReader);

            String line=reader.readLine();

            while (line!=null){
                output.append(line);
                line=reader.readLine();
            }
        }

        return output.toString();





    }


    private static URL createUrl(String requestUrl) {
        URL url=null;
        try {
             url=new URL(requestUrl);

        } catch (MalformedURLException e) {
           Log.d(LOG_TAG,"Problem building the urls",e);
        }

        return url;
    }



    public static List<Earthquake> extractEarthquakes(String earthquakeJson) {

        if(TextUtils.isEmpty(earthquakeJson)){
            return  null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject root=new JSONObject(earthquakeJson);
            JSONArray earthquakeArray=root.getJSONArray( "features");


            for(int i=0;i<earthquakeArray.length();i++){
                JSONObject currentQuake=earthquakeArray.getJSONObject(i);
                JSONObject properties=currentQuake.getJSONObject("properties");
                double magnitude=properties.getDouble("mag");
                String place=properties.getString("place");
                long time=properties.getLong("time");

                String url=properties.getString("url");



                Earthquake earthquake=new Earthquake(magnitude,place,time,url);
                earthquakes.add(earthquake);

                Log.d(LOG_TAG,"magnitude :"+magnitude+"place: "+place+"time :"+time+" Url :"+url);



            }

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}
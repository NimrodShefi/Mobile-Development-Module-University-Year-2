package com.example.volleysession;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // preparing the requests with URLs and RequestQueue object
        requestQueue = Volley.newRequestQueue(this);
        String url = "https://www.cardiff.ac.uk/";
        String spaceXURL = "https://api.spacexdata.com/v2/launches/latest"; // this returns a JSON object
        String jsonURL = "https://reqres.in/api/users/";

        // first attempt at getting data from a website
//        stringRequestExample(url);

        //json GET request example
//        jsonRequestExampleGet(spaceXURL);

        // json POST request example
//        jsonRequestExamplePost(jsonURL);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MainFragment())
                .commit();

    }

    private void jsonRequestExamplePost(String url){
        JSONObject dataToSend = new JSONObject();
        try {
            dataToSend.put("name", "somebody");
            dataToSend.put("job", "some job");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, // the type of request. E.g.: GET, POST, DELETE PUT, etc.
                url, // defined above
                dataToSend, // data to send with the request, broken for GET, works for POST
                new Response.Listener<JSONObject>() { // what happens when process succeeds
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("RESPONSE", response.toString(2)); // anything with JSONObject, has to have something to handle error messages, such as a try/catch
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { // what to do should something go wrong
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "ERROR");
                        if(error.getMessage() != null){
                            Log.d("ERROR", error.getMessage());
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void jsonRequestExampleGet(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, // the type of request. E.g.: GET, POST, DELETE PUT, etc.
                url, // defined above
                null, // data to send with the request, broken for GET, works for POST
                new Response.Listener<JSONObject>() { // what happens when process succeeds
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("RESPONSE", response.toString(2)); // anything with JSONObject, has to have something to handle error messages, such as a try/catch
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { // what to do should something go wrong
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "ERROR");
                        if(error.getMessage() != null){
                            Log.d("ERROR", error.getMessage());
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void stringRequestExample(String url){
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, // the type of request. E.g.: GET, POST, DELETE PUT, etc.
                url, // defined above
                new Response.Listener<String>() { // what happens when process succeeds
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", response);
                    }
                },
                new Response.ErrorListener() { // what to do should something go wrong
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.getMessage() != null){
                            Log.d("ERROR", error.getMessage());
                        }
                    }
                }
        );

        requestQueue.add(stringRequest);
    }
}

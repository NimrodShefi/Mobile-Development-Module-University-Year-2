package com.example.volleysession;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainFragment extends Fragment {

    private RequestQueue requestQueue;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        textView = view.findViewById(R.id.fragment_text);

        // do fragment UI stuff
        Context context = getContext();
        if (context == null){
            return view;
        }

        // set up volley
        requestQueue = Volley.newRequestQueue(context); // when in main activity, 'this' can be put as the context, but in fragments, it is impossible

        String spaceXURL = "https://api.spacexdata.com/v2/launches/latest"; // this returns a JSON object
        jsonRequestExampleGet(spaceXURL);

        return view;
    }


    private void jsonRequestExampleGet(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, // the type of request. E.g.: GET, POST, DELETE PUT, etc.
                url, // defined above
                null, // data to send with the request, broken for GET, works for POST
                new Response.Listener<JSONObject>() { // what happens when process succeeds
                    @Override
                    public void onResponse(JSONObject response) {
                        String mostRecentRocket;
                        try {
                            mostRecentRocket = response.getJSONObject("rocket").getString("rocket_name");

                            Log.d("RESPONSE", response.toString(2)); // anything with JSONObject, has to have something to handle error messages, such as a try/catch
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mostRecentRocket = "Unknown";
                        }
                        textView.setText(mostRecentRocket);
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

}
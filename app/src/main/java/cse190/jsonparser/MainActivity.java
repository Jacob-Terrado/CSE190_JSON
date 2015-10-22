package cse190.jsonparser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        /**********************************************************************
         * 1st Param = HTTP Method to use (Request.Method.GET or
         *             Request.Method.POST, etc.)
         * 2nd Param = Stringified URL
         * 3rd Param = JSONObject to post (use null if you don't plan on
         *             posting)
         * 4th Param = Listener object whose response you define in the
         *             onResponse call; the JSONObject parameter is what you
         *             retrieve from the HTTP Call
         * 5th Param = ErrorListener; to respond/ take appropriate action in
         *             case of a failed request [you can pass null to ignore
         *             errors]
         *********************************************************************/
        String URL = "http://api.androidhive.info/contacts/";
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e(TAG, "jsonObject = " + jsonObject.toString());
                        /******************************************************
                         * try {
                         *   JSON Manipulation occurs here. You need to know
                         *   how the JSON you are parsing is ordered to be able
                         *   to pull the String/ values from it.
                         * } catch (JSONException e) {
                         *   e.printStackTrace();
                         * }
                         *****************************************************/
                        try {
                            JSONArray jArray = jsonObject.getJSONArray("contacts");
                            Log.e(TAG, "contacts = " + jArray.toString());

                            JSONObject first = jArray.getJSONObject(0);
                            Log.e(TAG, "first = " + first.toString());

                            String id = first.getString("id");
                            String name = first.getString("name");
                            Log.e(TAG, "name = " + name + " | id = " + id);

                        } catch (JSONException e) {
                            Log.e(TAG, "error");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }
                });

        /**********************************************************************
         * This statement is crucial! This request is what will make the HTTP
         * call to the request you made!
         *
         * If you plan on making multiple REST calls, make sure you only use
         * one request queue!
         *********************************************************************/
        requestQueue = Volley.newRequestQueue(this); // Parameter for
        requestQueue.add(jsonRequest);               // newRequestQueue is a Context

        /**********************************************************************
         * POST Request Structure:
         *
         * JSONObject objectToPost = new JSONObject();
         * JsonObjectRequest postRequest = new JsonObjectRequest
         *  (Request.Method.POST, URL, objectToPost,
         *  new Response.Listener<JSONObject>() {
         *      @Override
         *      public void onResponse(JSONObject response) {
         *          // Parse response from POST Call
         *      }
         *  }, new Response.ErrorListener() {
         *      @Override
         *      public void onErrorResponse(VolleyError error) {
         *          // Handle Errors
         *      }
         *  }) {
         *      @Override
         *      protected Map<String, String> getParams() {
         *          Map<String, String>  params = new HashMap<>();
         *          // Insert Post parameters in params
         *          return params;
         *      }
         * };
         * ADD TO THE QUEUE
         *********************************************************************/
    }

    @Override
    protected void onStop() {
        super.onStop();

        requestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true; // Returning true will cancel the requests
            }
        });
    }
}
package com.example.sksunny_subway;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //백엔드로 받아온 라인 정보
    ArrayList<String> lines = new ArrayList<>();
    static RequestQueue requestQueue;
    final String API_KEY = BuildConfig.API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdapterSpinner adapterlines;

        Button register_btn = (Button) findViewById(R.id.register_btn);
        ImageView Image_Search = findViewById(R.id.Image_Search);
        EditText Et_Search = (EditText) findViewById(R.id.Et_Search);

        EditText start_nextst = (EditText) findViewById(R.id.start_nextst);
        EditText arrive_nextst = (EditText) findViewById(R.id.arrive_nextst);
        start_nextst.setInputType(InputType.TYPE_NULL);
        arrive_nextst.setInputType(InputType.TYPE_NULL);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        Image_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String station = Et_Search.getText().toString();
                //백엔드로 받아오는데 response가 true이면 입력 아니면 확인해주세요 메세지 띄우기
                getStationList();
            }
        });


        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getCookie();
                getRoot();
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getStationList() {

        try {
            // request body
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("stationName", "디지털미디어시티");
            jsonParams.put("APP_KEY", API_KEY);
            Log.i("request body", jsonParams.toString());

//            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://3.39.25.196:8000/station/getStationList";

            // Request a jsonObject response from the provided URL.
            JsonRequest jsonRequest = new JsonRequest(Request.Method.POST, url, jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                ArrayList<String> arr = new ArrayList<>();
                                JSONObject data = response.getJSONObject("data");
                                JSONObject headers = response.getJSONObject("headers");
                                JSONArray jsonarr = data.getJSONArray("Line");
                                for (int i = 0; i < jsonarr.length();i++){
                                    arr.add(jsonarr.getString(i));
                                }
                                lines = arr;
                                Log.i("data", String.valueOf(data));
                                Log.i("headers", String.valueOf(headers));
                                Log.d("cookie", String.valueOf(headers.getString("Set-Cookie")));
                            } catch (JSONException e) {
                                Log.e("error", Log.getStackTraceString(e));
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse respone = error.networkResponse;
                    if (error instanceof ServerError&& respone != null){
                        try{
                            String res = new String(respone.data, HttpHeaderParser.parseCharset(respone.headers, "utf-8"));
                            Log.e("volley error", res);
                        } catch (UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                    }
                    Log.e("e", Log.getStackTraceString(error));
                }
            });

            // By adding the request to the RequestQueue, make the request.
            requestQueue.add(jsonRequest);
            // Instantiate the RequestQueue.
        } catch (JSONException ex) {
            Log.e("exception", ex.toString());
        }
    }

    public void getRoot() {

//        RequestQueue queue = Volley.newRequestQueue(this);
        try {
            // request body
            JSONObject startAt = new JSONObject();
            startAt.put("line", "경의선");
            startAt.put("next", "수색");

            JSONObject endAt = new JSONObject();
            endAt.put("line", "6호선");
            endAt.put("next", "월드컵경기장");

            JSONObject jsonParams = new JSONObject();
            jsonParams.put("startAt", startAt);
            jsonParams.put("endAt", endAt);
            Log.i("request body", jsonParams.toString());

            String url = "http://3.39.25.196:8000/root/getRoot";

            // Request a jsonObject response from the provided URL.
            JsonRequest jsonRequest = new JsonRequest(Request.Method.POST, url, jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject data = response.getJSONObject("data");
                                JSONObject headers = response.getJSONObject("headers");
                                Log.i("root data", String.valueOf(data));
                                Log.i("root headers", String.valueOf(headers));
                            } catch (JSONException e) {
                                Log.e("client error", Log.getStackTraceString(e));
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", Log.getStackTraceString(error));
                }
            });

            // By adding the request to the RequestQueue, make the request.
            requestQueue.add(jsonRequest);
            // Instantiate the RequestQueue.
        } catch (JSONException ex) {
            Log.e("exception", ex.toString());
        }

    }

    public void getCookie() {

//        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://3.39.25.196:8000/cookie";

        // Request a jsonObject response from the provided URL.
        JsonRequest jsonRequest = new JsonRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            JSONObject headers = response.getJSONObject("headers");
                            Log.i("cookie data", String.valueOf(data));
                            Log.i("cookie headers", String.valueOf(headers));
                        } catch (JSONException e) {
                            Log.e("cookie error", Log.getStackTraceString(e));
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cookie err", Log.getStackTraceString(error));
            }
        });

        // By adding the request to the RequestQueue, make the request.
        requestQueue.add(jsonRequest);
        // Instantiate the RequestQueue.

    }
}
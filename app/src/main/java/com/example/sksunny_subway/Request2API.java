package com.example.sksunny_subway;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Request2API {

    public JSONObject getStationList(Context context, RequestQueue requestQueue, JSONObject jsonParams) throws JSONException {
        final String API_KEY = BuildConfig.API_KEY;
        JSONObject response = new JSONObject();
        try {
            // request body
            jsonParams.put("APP_KEY", API_KEY);
            Log.i("request body", jsonParams.toString());

            String url = "http://3.39.25.196:8000/station/getStationList";

            // Request a jsonObject response from the provided URL.
            JsonRequest jsonRequest = new JsonRequest(Request.Method.POST, url, jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject res) {

                            try {
                                // get data and headers from the received response
                                JSONObject data = res.getJSONObject("data");
                                JSONObject headers = res.getJSONObject("headers");
                                response.put("Line", data.getJSONArray("Line"));
                                Log.i("response", response.toString());

                                // get cookie from the received headers
                                String recieved_cookie = headers.getString("Set-Cookie");
                                // set cookie the sharedPreferences as the recieved_cookie
                                SharedPreferences cookie = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
                                SharedPreferences.Editor cookie_editor = cookie.edit();
                                cookie_editor.putString("cookie", recieved_cookie);
                                cookie_editor.apply();

                                // Logs for debugging
                                Log.i("data", String.valueOf(data));
                                Log.i("headers", String.valueOf(headers));
                                Log.d("cookie", recieved_cookie);
                            } catch (JSONException e) {
                                Log.e("error", Log.getStackTraceString(e));
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse respone = error.networkResponse;
                    if (error instanceof ServerError && respone != null) {
                        Toast.makeText(context, "정확한 역 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(context, RegisterActivity.class);
//                        startActivity(intent);
                        try {
                            String res = new String(respone.data, HttpHeaderParser.parseCharset(respone.headers, "utf-8"));
                            Log.e("volley error", res);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    if (error instanceof NoConnectionError) {
                        Toast.makeText(context, "인터넷 상태가 좋지 않습니다", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(context, RegisterActivity.class);
//                        startActivity(intent);
                    }
                    Log.e("e", Log.getStackTraceString(error));
                }
            });
            // By adding the request to the RequestQueue, make the request.
            requestQueue.add(jsonRequest);
        } catch (JSONException ex) {
            Log.e("exception", ex.toString());
        }
        return response;
    }
}

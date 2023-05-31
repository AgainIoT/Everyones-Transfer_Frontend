package com.example.sksunny_subway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DifActivity extends AppCompatActivity {
    static RequestQueue requestQueue;
    ArrayList<String> lines;
    RecyclerView lowerScroll;
    ArrayList<ItemTest> data;
    ArrayList<String> originContentData = new ArrayList<>();
    StringRecyclerViewAdapter upperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dif);

        AppCompatButton maintainBtn = findViewById(R.id.maintainBtn);
        AppCompatButton completeBtn = findViewById(R.id.completeBtn);

        ExecutorService executorService = Executors.newCachedThreadPool();

        Future<ArrayList<String>> future = executorService.submit(() -> {
            String json = getSharedPreferences("originContent", MODE_PRIVATE).getString("originContent", null);
            ArrayList<String> urls = new ArrayList<>();
            if (json != null) {
                try {
                    JSONArray a = new JSONArray(json);
                    for (int i = 0; i < a.length(); i++) {
                        String url = a.optString(i);
                        urls.add(url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("body change", json);
            }
            return urls;
        });

        SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("originContent")) {
                    while (true) {
                        if (future.isDone()) {
                            try {
                                originContentData = future.get();
                                upperAdapter.notifyDataSetChanged();
                            } catch (ExecutionException e) {
                                throw new RuntimeException(e);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }
                    }
                }
            }
        };


        SharedPreferences sharedPreferences = this.getSharedPreferences("originContent", MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        Intent intent = getIntent();
        data = intent.getParcelableArrayListExtra("data");

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        maintainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patchRoot();
                Context context = getApplicationContext();
                Toast.makeText(context, "기존 내용을 유지합니다.", Toast.LENGTH_SHORT).show();
                if (context.getSharedPreferences("done", context.MODE_PRIVATE).getBoolean("done", false)) {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, RegisterActivity.class);
                    intent.putExtra("lines", lines);
                    startActivity(intent);
                }
            }
        });

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patchBlock();
                patchRoot();
                Context context = getApplicationContext();
                Toast.makeText(context, "수정된 내용이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                if (context.getSharedPreferences("done", context.MODE_PRIVATE).getBoolean("done", false)) {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, RegisterActivity.class);
                    intent.putExtra("lines", lines);
                    startActivity(intent);
                }
            }
        });


        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView upperScroll = findViewById(R.id.upperScroll);
        upperScroll.setLayoutManager(new LinearLayoutManager(this));

        lowerScroll = findViewById(R.id.lowerScroll);
        lowerScroll.setLayoutManager(new LinearLayoutManager((this)));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        upperAdapter = new StringRecyclerViewAdapter(getApplicationContext(), originContentData);
        upperScroll.setAdapter(upperAdapter);

        CustomAdapter lowerAdapter = new CustomAdapter(getApplicationContext(), data);
        lowerScroll.setAdapter(lowerAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(lowerAdapter));
        mItemTouchHelper.attachToRecyclerView(lowerScroll);

        //radiogroup 추가
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.navbar);

        RadioButton elevator = (RadioButton) findViewById(R.id.elevator);
        RadioButton walk = (RadioButton) findViewById(R.id.walk);
        RadioButton pass = (RadioButton) findViewById(R.id.pass);
        RadioButton getOff = (RadioButton) findViewById(R.id.getOff);

        elevator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemTest item = new Elevator();
                data.add(item);
                lowerAdapter.notifyItemInserted(data.size());
            }
        });

        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemTest item = new Walk();
                data.add(item);
                lowerAdapter.notifyItemInserted(data.size());
            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemTest item = new Pass();
                data.add(item);
                lowerAdapter.notifyItemInserted(data.size());
            }
        });

        getOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemTest item = new Getoff();
                data.add(item);
                lowerAdapter.notifyItemInserted(data.size());
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void patchBlock() {

        // request body
        JSONObject jsonParams = new JSONObject();
        ArrayList<String> arr = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            ItemTest one = data.get(i);
            if (one instanceof Elevator) {
                arr.add(((Elevator) one).getStartFloor() + "층에서 " + ((Elevator) one).getEndFloor() + "층으로 이동");
            } else if (one instanceof Walk) {
                arr.add(((Walk) one).getDirection() + "으로" + String.valueOf(((Walk) one).getDistance()) + "m 이동");
            } else if (one instanceof Pass) {
                arr.add("개찰구로 통과");
            } else if (one instanceof Getoff) {
                arr.add(String.valueOf(((Getoff) one).getCarNo()) + " - " + String.valueOf((((Getoff) one).getDoorNo())) + "에서 하차");
            }
        }

        JSONArray jsonArray = new JSONArray(arr);
        try {
            jsonParams.put("content", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("request body", jsonParams.toString());

        String url = "http://3.39.25.196:8000/block/patchBlock";

        // Request a jsonObject response from the provided URL.
        JsonRequest jsonRequest = new JsonRequest(Request.Method.PATCH, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            // get data and headers from the received response
                            JSONObject data = response.getJSONObject("data");
                            JSONObject headers = response.getJSONObject("headers");
                            // Logs for debugging
                            Log.i("body", String.valueOf(data));
                            Log.i("headers", String.valueOf(headers));

                        } catch (JSONException e) {
                            Log.e("error", Log.getStackTraceString(e));
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse respone = error.networkResponse;
                if (error instanceof ServerError && respone != null) {
                    Toast.makeText(getApplicationContext(), "정확한 경로 정보를 입력하세요", Toast.LENGTH_SHORT).show();
                    try {
                        String res = new String(respone.data, HttpHeaderParser.parseCharset(respone.headers, "utf-8"));
                        Log.e("volley error", res);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "인터넷 상태가 좋지 않습니다", Toast.LENGTH_SHORT).show();
                }
                Log.e("e", Log.getStackTraceString(error));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                // put the cookie in the SharedPreferences
                Context context = getApplicationContext();
                SharedPreferences cookie = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
                String content = cookie.getString("cookie", "I got no cookie");
                params.put("cookie", content);
                Log.d("cookie", content);
                return params;
            }
        };
        // By adding the request to the RequestQueue, make the request.
        requestQueue.add(jsonRequest);
    }

    public void patchRoot() {

        String url = "http://3.39.25.196:8000/root/patchRoot";

        // Request a jsonObject response from the provided URL.
        JsonRequest jsonRequest = new JsonRequest(Request.Method.PATCH, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            // get data and headers from the received response
                            JSONObject data = response.getJSONObject("data");
                            JSONObject headers = response.getJSONObject("headers");
                            // Logs for debugging
                            Log.i("root body", String.valueOf(data));
                            Log.i("headers", String.valueOf(headers));

                        } catch (JSONException e) {
                            Log.e("error", Log.getStackTraceString(e));
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse respone = error.networkResponse;
                if (error instanceof ServerError && respone != null) {
                    Toast.makeText(getApplicationContext(), "정확한 경로 정보를 입력하세요", Toast.LENGTH_SHORT).show();
                    try {
                        String res = new String(respone.data, HttpHeaderParser.parseCharset(respone.headers, "utf-8"));
                        Log.e("volley error", res);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "인터넷 상태가 좋지 않습니다", Toast.LENGTH_SHORT).show();
                }
                Log.e("e", Log.getStackTraceString(error));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                // put the cookie in the SharedPreferences
                Context context = getApplicationContext();
                SharedPreferences cookie = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
                String content = cookie.getString("cookie", "I got no cookie");
                params.put("cookie", content);
                Log.d("cookie", content);
                return params;
            }
        };
        // By adding the request to the RequestQueue, make the request.
        requestQueue.add(jsonRequest);
    }

}

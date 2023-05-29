package com.example.sksunny_subway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    final String API_KEY = BuildConfig.API_KEY;

    static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        ArrayList<String> lines = intent.getStringArrayListExtra("lines");
        AdapterSpinner adapterlines;

        ArrayList<String> floors = new ArrayList<>(Arrays.asList("B5층", "B4층", "B3층", "B2층", "B1층", "1층", "2층", "3층", "4층", "5층"));
        ArrayList<String> locations = new ArrayList<>(Arrays.asList("승강장", "대합실", "외부"));
        AdapterSpinner adapterfloors;
        AdapterSpinner adapterlocations;


        LinearLayout btn_next = findViewById(R.id.layout_next);
        LinearLayout btn_finish = findViewById(R.id.layout_finish);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DifActivity.class);
                startActivity(intent);
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DifActivity.class);
                startActivity(intent);
            }
        });

        // 층 선택 Dropdown
        Spinner spinner_stfloors = findViewById(R.id.spinner_stfloors);
        Spinner spinner_arfloors = findViewById(R.id.spinner_arfloors);
        adapterfloors = new AdapterSpinner(this, floors); //그 값을 넣어줌
        spinner_stfloors.setAdapter(adapterfloors);
        spinner_arfloors.setAdapter(adapterfloors);

        //호선 선택 Dropdown
        Spinner spinner_stlines = findViewById(R.id.spinner_stlines);
        Spinner spinner_arlines = findViewById(R.id.spinner_arlines);
        adapterlocations = new AdapterSpinner(this, lines); //그 값을 넣어줌
        spinner_stlines.setAdapter(adapterlocations);
        spinner_arlines.setAdapter(adapterlocations);

        // 장소 선택 Dropdown
        Spinner spinner_stlocations = findViewById(R.id.spinner_stlocations);
        Spinner spinner_arlocations = findViewById(R.id.spinner_arlocations);
        adapterlocations = new AdapterSpinner(this, locations); //그 값을 넣어줌
        spinner_stlocations.setAdapter(adapterlocations);
        spinner_arlocations.setAdapter(adapterlocations);

        //arrayList
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add(String.format("TEXT %d", i));
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView upperScroll = findViewById(R.id.upperScroll);
        upperScroll.setLayoutManager(new LinearLayoutManager((this)));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        RecyclerAdapter upperAdapter = new RecyclerAdapter(list);
        upperScroll.setAdapter(upperAdapter);

        //radiogroup 추가
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.navbar);

        RadioButton elevator = (RadioButton)findViewById(R.id.elevator);
        RadioButton walk = (RadioButton)findViewById(R.id.walk);
        RadioButton pass = (RadioButton)findViewById(R.id.pass);
        RadioButton getOff = (RadioButton)findViewById(R.id.getOff);

        elevator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add("elevator");
                upperAdapter.notifyItemInserted(list.size());
            }
        });

        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add("walk");
                upperAdapter.notifyItemInserted(list.size());
            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add("pass");
                upperAdapter.notifyItemInserted(list.size());
            }
        });

        getOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add("getOff");
                upperAdapter.notifyItemInserted(list.size());
            }
        });
    }

    public void getStationList(String stationName) {
        try {
            // request body
            JSONObject jsonParams = new JSONObject();

            JSONObject from = new JSONObject();
            from.put("floor", "floor");
            from.put("line", "line");
            from.put("location", "location");
            jsonParams.put("from", from);

            JSONObject to = new JSONObject();
            to.put("floor", "floor");
            to.put("line", "line");
            to.put("location", "location");
            jsonParams.put("to", to);

            Log.i("request body", jsonParams.toString());

            String url = "http://3.39.25.196:8000/block/getBlock";

            // Request a jsonObject response from the provided URL.
            JsonRequest jsonRequest = new JsonRequest(Request.Method.POST, url, jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                // get data and headers from the received response
                                JSONObject data = response.getJSONObject("data");
                                JSONObject headers = response.getJSONObject("headers");

                                // Logs for debugging
                                Log.i("data", String.valueOf(data));
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
                        Toast.makeText(getApplicationContext(), "정확한 역 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
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
        } catch (JSONException ex) {
            Log.e("exception", ex.toString());
        }
    }
}
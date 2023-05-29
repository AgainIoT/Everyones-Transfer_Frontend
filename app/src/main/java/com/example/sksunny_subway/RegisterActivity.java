package com.example.sksunny_subway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import android.widget.Spinner;

public class RegisterActivity extends AppCompatActivity {

    final String API_KEY = BuildConfig.API_KEY;
    ArrayList<String> floors = new ArrayList<>(Arrays.asList("B5층", "B4층", "B3층", "B2층", "B1층", "1층", "2층", "3층", "4층", "5층"));
    ArrayList<String> locations = new ArrayList<>(Arrays.asList("승강장", "대합실", "외부"));
    ArrayList<String> directions = new ArrayList<>(Arrays.asList("왼쪽", "오른쪽", "직진", "후진"));
    AdapterSpinner adapterfloors;
    AdapterSpinner adapterlocations;

    static RequestQueue requestQueue;

    Spinner spinner_stfloors;
    Spinner spinner_arfloors;
    Spinner spinner_stlines;
    Spinner spinner_arlines;
    Spinner spinner_stlocations;
    Spinner spinner_arlocations;

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

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBlock();
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
        spinner_stfloors = findViewById(R.id.spinner_stfloors);
        spinner_arfloors = findViewById(R.id.spinner_arfloors);
        adapterfloors = new AdapterSpinner(this, floors); //그 값을 넣어줌
        spinner_stfloors.setAdapter(adapterfloors);
        spinner_arfloors.setAdapter(adapterfloors);

        //호선 선택 Dropdown
        spinner_stlines = findViewById(R.id.spinner_stlines);
        spinner_arlines = findViewById(R.id.spinner_arlines);
        adapterlocations = new AdapterSpinner(this, lines); //그 값을 넣어줌
        spinner_stlines.setAdapter(adapterlocations);
        spinner_arlines.setAdapter(adapterlocations);

        // 장소 선택 Dropdown
        spinner_stlocations = findViewById(R.id.spinner_stlocations);
        spinner_arlocations = findViewById(R.id.spinner_arlocations);
        adapterlocations = new AdapterSpinner(this, locations); //그 값을 넣어줌
        spinner_stlocations.setAdapter(adapterlocations);
        spinner_arlocations.setAdapter(adapterlocations);

        //arrayList
        ArrayList<ListItem> list = new ArrayList<>();

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView upperScroll = findViewById(R.id.scroll);
        upperScroll.setLayoutManager(new LinearLayoutManager((this)));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        RecyclerAdapter upperAdapter = new RecyclerAdapter();
        upperScroll.setAdapter(upperAdapter);

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(upperAdapter));
        mItemTouchHelper.attachToRecyclerView(upperScroll);

        upperAdapter.setItems(list);

        //radiogroup 추가
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.navbar);

        RadioButton elevator = (RadioButton) findViewById(R.id.elevator);
        RadioButton walk = (RadioButton) findViewById(R.id.walk);
        RadioButton pass = (RadioButton) findViewById(R.id.pass);
        RadioButton getOff = (RadioButton) findViewById(R.id.getOff);

        elevator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItem item = new ListItem("elevator",0);
                list.add(item);
                upperAdapter.notifyItemInserted(list.size());
            }
        });

        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItem item = new ListItem("walk",0);
                list.add(item);
                upperAdapter.notifyItemInserted(list.size());
            }
        });

        //얘는 확정! 더 손 안 대도 됨!
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItem item = new ListItem("pass",0);
                list.add(item);
                upperAdapter.notifyItemInserted(list.size());
            }
        });

        getOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItem item = new ListItem("getOff",0);
                list.add(item);
                upperAdapter.notifyItemInserted(list.size());
            }
        });
    }

    public void getBlock() {
        try {
            // request body
            JSONObject jsonParams = new JSONObject();

            JSONObject from = new JSONObject();
            String stfloor = spinner_stfloors.getSelectedItem().toString();
            from.put("floor", stfloor.substring(0, stfloor.length() - 1));
            from.put("line", spinner_stlines.getSelectedItem().toString());
            from.put("location", spinner_stlocations.getSelectedItem().toString());
            jsonParams.put("from", from);

            JSONObject to = new JSONObject();
            String arfloor = spinner_arfloors.getSelectedItem().toString();
            to.put("floor", arfloor.substring(0, arfloor.length() - 1));
            to.put("line", spinner_arlines.getSelectedItem().toString());
            to.put("location", spinner_arlocations.getSelectedItem().toString());
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
        } catch (JSONException ex) {
            Log.e("exception", ex.toString());
        }
    }
}
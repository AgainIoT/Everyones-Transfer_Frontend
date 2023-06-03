package com.banana.sksunny_subway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.banana.sksunny_subway.ListItems.Elevator;
import com.banana.sksunny_subway.ListItems.Getoff;
import com.banana.sksunny_subway.ListItems.ListItem;
import com.banana.sksunny_subway.ListItems.Pass;
import com.banana.sksunny_subway.ListItems.Walk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RegisterActivity extends AppCompatActivity {
    ArrayList<String> floors = new ArrayList<>(Arrays.asList("B5층", "B4층", "B3층", "B2층", "B1층", "1층", "2층", "3층", "4층", "5층"));
    ArrayList<String> locations = new ArrayList<>(Arrays.asList("승강장", "대합실", "외부"));
    AdapterSpinner adapterfloors;
    AdapterSpinner adapterlines;
    AdapterSpinner adapterlocations;

    //arrayList
    ArrayList<ListItem> data = new ArrayList<>();   // 사용자한테 입력 받은 상세 경로
    ArrayList<String> lines = new ArrayList<>();    // 해당 역을 지나는 노선들로

    static RequestQueue requestQueue;

    Spinner spinner_stfloors;
    Spinner spinner_arfloors;
    Spinner spinner_stlines;
    Spinner spinner_arlines;
    Spinner spinner_stlocations;
    Spinner spinner_arlocations;

    String arfloors;
    String arlines;
    String arlocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        lines = StringArray.getStringArrayPref(getApplicationContext(), "lines");

        LinearLayout btn_next = findViewById(R.id.layout_next);
        LinearLayout btn_finish = findViewById(R.id.layout_finish);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.size() == 0) {
                    Toast.makeText(getApplicationContext(), "정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    getBlock();
                    SharedPreferences shared_save_nextpath = getSharedPreferences("save_nextpath", MODE_PRIVATE);
                    SharedPreferences.Editor editor_save_nextpath = shared_save_nextpath.edit();
                    if (!spinner_arfloors.getSelectedItem().toString().isEmpty()) {
                        editor_save_nextpath.putString("arfloors", spinner_arfloors.getSelectedItem().toString());
                    }
                    if (!spinner_arlines.getSelectedItem().toString().isEmpty()) {
                        editor_save_nextpath.putString("arlines", spinner_arlines.getSelectedItem().toString());
                    }
                    if (!spinner_arlocations.getSelectedItem().toString().isEmpty()) {
                        editor_save_nextpath.putString("arlocations", spinner_arlocations.getSelectedItem().toString());
                    }
                    editor_save_nextpath.apply();

                    SharedPreferences done = getSharedPreferences("done", MODE_PRIVATE);
                    SharedPreferences.Editor done_editor = done.edit();
                    done_editor.putBoolean("done", false);
                    done_editor.apply();
                }
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.size() == 0) {
                    Toast.makeText(getApplicationContext(), "정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    getBlock();

                    SharedPreferences done = getSharedPreferences("done", MODE_PRIVATE);
                    SharedPreferences.Editor done_editor = done.edit();
                    done_editor.putBoolean("done", true);
                    done_editor.apply();

                    SharedPreferences shared_save_nextpath = getSharedPreferences("save_nextpath", MODE_PRIVATE);
                    SharedPreferences.Editor editor_save_nextpath = shared_save_nextpath.edit();
                    editor_save_nextpath.putString("arfloors", "B5");
                    editor_save_nextpath.putString("arlocations", "승강장");
                    editor_save_nextpath.apply();

                    lines.clear();
                    lines.add("호선 입력");
                    StringArray.setStringArrayPref(getApplicationContext(), "lines", lines);

                    Intent intent = new Intent(getApplicationContext(), DifActivity.class);
                    intent.putParcelableArrayListExtra("data", data);
                    startActivity(intent);
                }
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
        adapterlines = new AdapterSpinner(this, lines); //그 값을 넣어줌
        spinner_stlines.setAdapter(adapterlines);
        spinner_arlines.setAdapter(adapterlines);


        // 장소 선택 Dropdown
        spinner_stlocations = findViewById(R.id.spinner_stlocations);
        spinner_arlocations = findViewById(R.id.spinner_arlocations);
        adapterlocations = new AdapterSpinner(this, locations); //그 값을 넣어줌
        spinner_stlocations.setAdapter(adapterlocations);
        spinner_arlocations.setAdapter(adapterlocations);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView scroll = findViewById(R.id.scroll);
        scroll.setLayoutManager(new LinearLayoutManager((this)));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), data);
        scroll.setAdapter(customAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(customAdapter));
        mItemTouchHelper.attachToRecyclerView(scroll);

        //radiogroup 추가
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.navbar);

        RadioButton elevator = (RadioButton) findViewById(R.id.elevator);
        RadioButton walk = (RadioButton) findViewById(R.id.walk);
        RadioButton pass = (RadioButton) findViewById(R.id.pass);
        RadioButton getOff = (RadioButton) findViewById(R.id.getOff);

        elevator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItem item = new Elevator();
                data.add(item);
                customAdapter.notifyItemInserted(data.size());
            }
        });

        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItem item = new Walk();
                data.add(item);
                customAdapter.notifyItemInserted(data.size());
            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItem item = new Pass();
                data.add(item);
                customAdapter.notifyItemInserted(data.size());
            }
        });

        getOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItem item = new Getoff();
                data.add(item);
                customAdapter.notifyItemInserted(data.size());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //사용자가 다음 상세 경로를 입력할 때 전에 정보가 유지되도록함
        SharedPreferences shared_save_nextpath = getSharedPreferences("save_nextpath", MODE_PRIVATE);
        arfloors = shared_save_nextpath.getString("arfloors", "");
        arlines = shared_save_nextpath.getString("arlines", "");
        arlocations = shared_save_nextpath.getString("arlocations", "");

        lines = StringArray.getStringArrayPref(getApplicationContext(), "lines");

        spinner_stlines = findViewById(R.id.spinner_stlines);
        spinner_arlines = findViewById(R.id.spinner_arlines);
        adapterlocations = new AdapterSpinner(this, lines); //그 값을 넣어줌
        spinner_stlines.setAdapter(adapterlocations);
        spinner_arlines.setAdapter(adapterlocations);

        if (!arfloors.isEmpty()) {
            spinner_stfloors.setSelection(floors.indexOf(arfloors));
        }

        if (!arlines.isEmpty()) {
            spinner_stlines.setSelection(lines.indexOf(arlines));
        }

        if (!arlocations.isEmpty()) {
            spinner_stlocations.setSelection(locations.indexOf(arlocations));
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
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

                            ExecutorService executorService = Executors.newCachedThreadPool();

                            Future<String> future = executorService.submit(() -> {
                                try {
                                    // get data and headers from the received response
                                    JSONObject data = response.getJSONObject("data");
                                    JSONObject headers = response.getJSONObject("headers");
                                    // Logs for debugging
                                    Log.i("body", String.valueOf(data));
                                    Log.i("headers", String.valueOf(headers));

                                    JSONArray jsonarr = data.getJSONArray("originContent");
                                    SharedPreferences originContent = getSharedPreferences("originContent", MODE_PRIVATE);
                                    SharedPreferences.Editor originContent_editor = originContent.edit();
                                    originContent_editor.putString("originContent", jsonarr.toString());
                                    originContent_editor.apply();

                                } catch (JSONException e) {
                                    Log.e("error", Log.getStackTraceString(e));
                                }
                                return "sadf";
                            });
                            while (true) {
                                if (future.isDone()) {
                                    try {
                                        future.get();
                                    } catch (ExecutionException e) {
                                        throw new RuntimeException(e);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                }
                            }
                            Intent intent = new Intent(getApplicationContext(), DifActivity.class);
                            intent.putParcelableArrayListExtra("data", data);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse respone = error.networkResponse;
                    if (error instanceof ServerError && respone != null) {
                        Toast.makeText(getApplicationContext(), "getBlock error", Toast.LENGTH_SHORT).show();
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
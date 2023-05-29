package com.example.sksunny_subway;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.ArrayMap;
import android.util.Log;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.android.volley.NoConnectionError;
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
    final String API_KEY = BuildConfig.API_KEY;

    ArrayList<String> lines = new ArrayList<>(); // train lines passing through the station
    private static final String SETTINGS_PLAYER_JSON = "settings_item_json";
    AdapterSpinner adapterlines;

    EditText Et_Search;
    EditText start_nextst;
    EditText arrive_nextst;
    Spinner startLine_spinner;
    Spinner endLine_spinner;
    static RequestQueue requestQueue;
    Boolean search_result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startLine_spinner = findViewById(R.id.spinner_stline);
        endLine_spinner = findViewById(R.id.spinner_arline);

        lines.add("호선 입력");

        Button register_btn = findViewById(R.id.register_btn);
        ImageView Image_Search = findViewById(R.id.Image_Search);
        Et_Search = findViewById(R.id.Et_Search);

        start_nextst = findViewById(R.id.start_nextst);
        arrive_nextst = findViewById(R.id.arrive_nextst);

        try{
            SharedPreferences shared_et_search = getSharedPreferences("et_search", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
            String str_et_search = shared_et_search.getString("et_search","");
            Et_Search.setText(str_et_search);

            SharedPreferences shared_start_nextst = getSharedPreferences("start_nextst", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
            String str_start_nextst = shared_start_nextst.getString("start_nextst","");
            start_nextst.setText(str_start_nextst);

            SharedPreferences shared_arrive_nextst = getSharedPreferences("arrive_nextst", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
            String str_arrive_nextst= shared_arrive_nextst.getString("arrive_nextst","");
            arrive_nextst.setText(str_arrive_nextst);

            lines= getStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON);

            SharedPreferences shared_search_result = getSharedPreferences("search_result", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
            search_result = shared_search_result.getBoolean("search_result", false);
        }
        catch(Exception e){

        }

        if(!search_result){
            start_nextst.setInputType(InputType.TYPE_NULL);
            arrive_nextst.setInputType(InputType.TYPE_NULL);
        }

        adapterlines = new AdapterSpinner(this, lines); //그 값을 넣어줌
        startLine_spinner.setAdapter(adapterlines);
        endLine_spinner.setAdapter(adapterlines);

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        SharedPreferences cookie = getSharedPreferences("cookie", Context.MODE_PRIVATE);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        Image_Search.setOnClickListener(view -> {
            Et_Search.clearFocus();
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            getStationList();
        });


        register_btn.setOnClickListener(view -> {
            String start_next = start_nextst.getText().toString();
            String arrive_next = arrive_nextst.getText().toString();
            getRoot();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences shared_et_search= getSharedPreferences("et_search", MODE_PRIVATE);    // test 이름의 기본모드 설정
        SharedPreferences.Editor editor_et_search= shared_et_search.edit(); //sharedPreferences를 제어할 editor를 선언
        editor_et_search.putString("et_search",Et_Search.getText().toString()); // key,value 형식으로 저장
        editor_et_search.commit();

        SharedPreferences shared_start_nextst= getSharedPreferences("start_nextst", MODE_PRIVATE);    // test 이름의 기본모드 설정
        SharedPreferences.Editor editor_start_nextst = shared_start_nextst.edit(); //sharedPreferences를 제어할 editor를 선언
        editor_start_nextst.putString("start_nextst",start_nextst.getText().toString()); // key,value 형식으로 저장
        editor_start_nextst.commit();

        SharedPreferences shared_arrive_nextst= getSharedPreferences("arrive_nextst", MODE_PRIVATE);    // test 이름의 기본모드 설정
        SharedPreferences.Editor editor_arrive_nextst = shared_arrive_nextst.edit(); //sharedPreferences를 제어할 editor를 선언
        editor_start_nextst.putString("arrive_nextst",arrive_nextst.getText().toString()); // key,value 형식으로 저장
        editor_arrive_nextst.commit();

        setStringArrayPref(getApplicationContext(), SETTINGS_PLAYER_JSON, lines);

        SharedPreferences shared_search_result= getSharedPreferences("search_result", MODE_PRIVATE);    // test 이름의 기본모드 설정
        SharedPreferences.Editor editor_search_result = shared_search_result.edit(); //sharedPreferences를 제어할 editor를 선언
        editor_search_result.putBoolean("search_result",search_result); // key,value 형식으로 저장
        editor_search_result.commit();
    }

    private void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    private ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
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
        }
        return urls;
    }

    public void getStationList() {
        try {
            // request body
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("stationName", Et_Search.getText().toString());
            jsonParams.put("APP_KEY", API_KEY);
            Log.i("request body", jsonParams.toString());

            String url = "http://3.39.25.196:8000/station/getStationList";

            // Request a jsonObject response from the provided URL.
            JsonRequest jsonRequest = new JsonRequest(Request.Method.POST, url, jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                // get data and headers from the received response
                                JSONObject data = response.getJSONObject("data");
                                JSONObject headers = response.getJSONObject("headers");

                                // insert every lines into the 'lines' array
                                ArrayList<String> lineArr = new ArrayList<>();
                                JSONArray jsonarr = data.getJSONArray("Line");
                                for (int i = 0; i < jsonarr.length(); i++) {
                                    lineArr.add(jsonarr.getString(i));
                                }
                                lines.clear();
                                lines.addAll(lineArr);
                                if (adapterlines != null) {
                                    adapterlines.notifyDataSetChanged();
                                }
                                search_result = true;
                                start_nextst.setInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
                                arrive_nextst.setInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);

                                // get cookie from the received headers
                                String recieved_cookie = headers.getString("Set-Cookie");
                                // set cookie the sharedPreferences as the recieved_cookie
                                Context context = getApplicationContext();
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
                    start_nextst.setInputType(InputType.TYPE_NULL);
                    arrive_nextst.setInputType(InputType.TYPE_NULL);
                    search_result = false;
                    NetworkResponse respone = error.networkResponse;
                    if (error instanceof ServerError && respone != null) {
                        Toast.makeText(getApplicationContext(), "정확한 역 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                        startActivity(intent);
                        try {
                            String res = new String(respone.data, HttpHeaderParser.parseCharset(respone.headers, "utf-8"));
                            Log.e("volley error", res);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    if (error instanceof NoConnectionError) {
                        Toast.makeText(getApplicationContext(), "인터넷 상태가 좋지 않습니다", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                        startActivity(intent);
                    }
                    Log.e("e", Log.getStackTraceString(error));
                }
            });
            // By adding the request to the RequestQueue, make the request.
            requestQueue.add(jsonRequest);
        } catch (JSONException ex) {
            Log.e("exception", ex.toString());
        }
    }

    private void getRoot() {

        try {
            // request body
            JSONObject startAt = new JSONObject();
            startAt.put("line", startLine_spinner.getSelectedItem());
            startAt.put("next", start_nextst.getText().toString());

            JSONObject endAt = new JSONObject();
            endAt.put("line", endLine_spinner.getSelectedItem());
            endAt.put("next", arrive_nextst.getText().toString());

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
                            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                            intent.putExtra("lines", lines);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse respone = error.networkResponse;
                    Toast.makeText(getApplicationContext(), "다음역 이름이 정확하지 않습니다", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                    if (error instanceof ServerError && respone != null) {
                        try {
                            String res = new String(respone.data, HttpHeaderParser.parseCharset(respone.headers, "utf-8"));
                            Log.e("volley error", res);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
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
package com.banana.sksunny_subway;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;

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
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    final String API_KEY = BuildConfig.API_KEY;

    ArrayList<String> lines = new ArrayList<>(); // train lines passing through the station
    AdapterSpinner adapterlines;

    EditText Et_Search;
    EditText start_nextst;
    EditText arrive_nextst;
    Spinner startLine_spinner;
    Spinner endLine_spinner;
    static RequestQueue requestQueue;
    Boolean search_result = false;
    String startLine;
    String endLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startLine_spinner = findViewById(R.id.spinner_stline);
        endLine_spinner = findViewById(R.id.spinner_arline);

        Button register_btn = findViewById(R.id.register_btn);
        ImageView Image_Search = findViewById(R.id.Image_Search);
        // 검색창에서 키보드 엔터 치면 검색
        Et_Search = findViewById(R.id.Et_Search);
        Et_Search.setOnKeyListener((v, keyCode, event) -> {

            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Et_Search.getWindowToken(), 0);    //hide keyboard
                searchStation();
                return true;
            }
            return false;
        });

        start_nextst = findViewById(R.id.start_nextst);
        arrive_nextst = findViewById(R.id.arrive_nextst);

        try {
            SharedPreferences shared_save_main = getSharedPreferences("save_main", MODE_PRIVATE);
            String str_et_search = shared_save_main.getString("et_search", "");
            Et_Search.setText(str_et_search);
            String str_start_nextst = shared_save_main.getString("start_nextst", "");
            String str_arrive_nextst = shared_save_main.getString("arrive_nextst", "");

            start_nextst.setText(str_start_nextst);
            arrive_nextst.setText(str_arrive_nextst);
            search_result = shared_save_main.getBoolean("search_result", false);

            lines = StringArray.getStringArrayPref(getApplicationContext(), "lines");

            startLine = shared_save_main.getString("startLine", "");
            endLine = shared_save_main.getString("endLine", "");
        } catch (Exception e) {

        }

        if (!search_result) {
            start_nextst.setInputType(InputType.TYPE_NULL);
            arrive_nextst.setInputType(InputType.TYPE_NULL);
        }

        adapterlines = new AdapterSpinner(this, lines); //그 값을 넣어줌
        startLine_spinner.setAdapter(adapterlines);
        endLine_spinner.setAdapter(adapterlines);

        if (lines.size() == 0) {
            lines.add("호선 입력");
            adapterlines.notifyDataSetChanged();
        }


        if (!startLine.isEmpty()) {
            startLine_spinner.setSelection(lines.indexOf(startLine));
        }
        if (!endLine.isEmpty()) {
            endLine_spinner.setSelection(lines.indexOf(endLine));
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        Image_Search.setOnClickListener(view -> {
            Et_Search.clearFocus();
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            searchStation();
        });


        register_btn.setOnClickListener(view -> {
            register();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences shared_save_main = getSharedPreferences("save_main", MODE_PRIVATE);
        SharedPreferences.Editor editor_save_main = shared_save_main.edit(); //sharedPreferences를 제어할 editor를 선언
        editor_save_main.putString("et_search", Et_Search.getText().toString()); // key,value 형식으로 저장
        editor_save_main.putString("start_nextst", start_nextst.getText().toString());
        editor_save_main.putString("arrive_nextst", arrive_nextst.getText().toString());
        if (!start_nextst.getText().toString().isEmpty()) {
            editor_save_main.putString("start_nextst", start_nextst.getText().toString());
        }
        if (!arrive_nextst.getText().toString().isEmpty()) {
            editor_save_main.putString("arrive_nextst", arrive_nextst.getText().toString());
        }
        editor_save_main.putBoolean("search_result", search_result);
        editor_save_main.apply();

        StringArray.setStringArrayPref(getApplicationContext(), "lines", lines);
    }

    private long backpressedTime = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                String alertTitle = "앱을 종료합니다.";
                String buttonMessage = "저장하고 어플을 종료하시겠습니까?";
                String buttonYes = "네";
                String buttonNo = "아니요";

                if (System.currentTimeMillis() > backpressedTime + 2000) {
                    backpressedTime = System.currentTimeMillis();
                    Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
                } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(alertTitle)
                            .setMessage(buttonMessage)
                            .setNegativeButton(buttonYes, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    SharedPreferences shared_save_main = getSharedPreferences("save_main", MODE_PRIVATE);
                                    SharedPreferences.Editor editor_save_main = shared_save_main.edit(); //sharedPreferences를 제어할 editor를 선언
                                    editor_save_main.putString("et_search", Et_Search.getText().toString()); // key,value 형식으로 저장
                                    editor_save_main.putString("start_nextst", start_nextst.getText().toString());
                                    editor_save_main.putString("arrive_nextst", arrive_nextst.getText().toString());
                                    if (!start_nextst.getText().toString().isEmpty()) {
                                        editor_save_main.putString("start_nextst", start_nextst.getText().toString());
                                    }
                                    if (!arrive_nextst.getText().toString().isEmpty()) {
                                        editor_save_main.putString("arrive_nextst", arrive_nextst.getText().toString());
                                    }
                                    editor_save_main.putBoolean("search_result", search_result);
                                    editor_save_main.apply();

                                    StringArray.setStringArrayPref(getApplicationContext(), "lines", lines);
                                    moveTaskToBack(true);
                                    finish();
                                }
                            })
                            .setPositiveButton(buttonNo, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Et_Search.setText("");
                                    start_nextst.setText("");
                                    arrive_nextst.setText("");
                                    search_result = false;
                                    lines.clear();
                                    lines.add("호선 입력");
                                    adapterlines.notifyDataSetChanged();

                                    SharedPreferences shared_save_main = getSharedPreferences("save_main", MODE_PRIVATE);
                                    SharedPreferences.Editor editor_save_main = shared_save_main.edit(); //sharedPreferences를 제어할 editor를 선언
                                    editor_save_main.putString("et_search", Et_Search.getText().toString()); // key,value 형식으로 저장
                                    editor_save_main.putString("start_nextst", start_nextst.getText().toString());
                                    editor_save_main.putString("arrive_nextst", arrive_nextst.getText().toString());
                                    editor_save_main.putBoolean("search_result", search_result);
                                    if (!start_nextst.getText().toString().isEmpty()) {
                                        editor_save_main.putString("start_nextst", start_nextst.getText().toString());
                                    }
                                    if (!arrive_nextst.getText().toString().isEmpty()) {
                                        editor_save_main.putString("arrive_nextst", arrive_nextst.getText().toString());
                                    }
                                    editor_save_main.apply();
                                    StringArray.setStringArrayPref(getApplicationContext(), "lines", lines);
                                    finish();
                                }
                            })
                            .show();
                }
        }
        return true;
    }

    public void searchStation() {
        search_result = true;
        start_nextst.setInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
        arrive_nextst.setInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
        getStationList();

//        JSONObject jsonParams = new JSONObject();
//        try{
//            jsonParams.put("stationName", Et_Search.getText().toString());
//            JSONObject response =  new Request2API().getStationList(getApplicationContext(), requestQueue, jsonParams);
//            Log.i("response", response.toString());
//            // insert every lines into the 'lines' array
//            ArrayList<String> lineArr = new ArrayList<>();
//            JSONArray jsonarr = response.getJSONArray("Line");
//            for (int i = 0; i < jsonarr.length(); i++) {
//                lineArr.add(jsonarr.getString(i));
//            }
//            start_nextst.setInputType(InputType.TYPE_NULL);
//            arrive_nextst.setInputType(InputType.TYPE_NULL);
//            search_result = false;
//            lines.clear();
//            lines.addAll(lineArr);
//            if (adapterlines != null) {
//                adapterlines.notifyDataSetChanged();
//            }
//        } catch (JSONException e){
//            Log.e("JSONException", e.toString());
//        }
    }

    public void register() {
        if (TextUtils.isEmpty(start_nextst.getText().toString()) && TextUtils.isEmpty(start_nextst.getText().toString())) {
            Toast.makeText(getApplicationContext(), "승하차 한 역의 다음 역을 입력해주세요", Toast.LENGTH_LONG).show();
        } else {
            getRoot();
        }
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

                                StringArray.setStringArrayPref(getApplicationContext(), "lines", lines);

                                // get cookie from the received headers
                                String recieved_cookie = headers.getString("Set-Cookie");
                                // set cookie the sharedPreferences as the recieved_cookie
                                Context context = getApplicationContext();
                                SharedPreferences cookie = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
                                SharedPreferences.Editor cookie_editor = cookie.edit();
                                cookie_editor.putString("cookie", recieved_cookie);
                                cookie_editor.apply();

                                // Logs for debugging
                                Log.i("body", String.valueOf(data));
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
                                Log.i("body", String.valueOf(data));
                                Log.i("headers", String.valueOf(headers));
                            } catch (JSONException e) {
                                Log.e("client error", Log.getStackTraceString(e));
                            }

                            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
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
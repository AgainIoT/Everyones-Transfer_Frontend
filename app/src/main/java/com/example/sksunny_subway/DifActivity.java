package com.example.sksunny_subway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DifActivity extends AppCompatActivity {
    static RequestQueue requestQueue;
    ArrayList<ListItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dif);

        AppCompatButton maintainBtn = findViewById(R.id.maintainBtn);
        AppCompatButton completeBtn = findViewById(R.id.completeBtn);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        maintainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Toast.makeText(context, "기존 내용을 유지합니다.", Toast.LENGTH_SHORT).show();
                if (context.getSharedPreferences("done", context.MODE_PRIVATE).getBoolean("done", false)){
                    Intent intent  = new Intent(context, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, RegisterActivity.class);
                    startActivity(intent);
                }
            }
        });

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                patchBlock();
                Toast.makeText(context, "수정된 내용이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                if (context.getSharedPreferences("done", context.MODE_PRIVATE).getBoolean("done", false)){
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, RegisterActivity.class);
                    startActivity(intent);
                }
            }
        });


        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        TextView upperScroll = findViewById(R.id.upperScroll);
        ArrayList<String> originContent = StringArray.getStringArrayPref(getApplicationContext(), "originContent");
        upperScroll.setText(originContent.toString());
        Log.i("sadfsadfasdf", originContent.toString());
        RecyclerView lowerScroll = findViewById(R.id.lowerScroll);
        lowerScroll.setLayoutManager(new LinearLayoutManager((this)));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        RecyclerAdapter lowerAdapter = new RecyclerAdapter();
        lowerScroll.setAdapter(lowerAdapter);

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(lowerAdapter));
        mItemTouchHelper.attachToRecyclerView(lowerScroll);


        items = new ArrayList<>();
        ListItem item1 = new ListItem("박진우", 0);
        ListItem item2 = new ListItem("윤민우", 0);
        ListItem item3 = new ListItem("이채영", 0);
        items.add(item1);
        items.add(item2);
        items.add(item3);

        ArrayList<ListItem> list = new ArrayList<>();
        lowerAdapter.setItems(list);

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
                lowerAdapter.notifyItemInserted(list.size());
            }
        });

        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItem item = new ListItem("walk",0);
                list.add(item);
                lowerAdapter.notifyItemInserted(list.size());
            }
        });


        //얘는 확정! 더 손 안 대도 됨!
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItem item = new ListItem("pass",0);
                list.add(item);
                lowerAdapter.notifyItemInserted(list.size());
            }
        });

        getOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItem item = new ListItem("getOff",0);
                list.add(item);
                lowerAdapter.notifyItemInserted(list.size());
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
        for (int i = 0; i < items.size(); i++){
            arr.add(items.get(i).getName());
        }

        JSONArray jsonArray = new JSONArray(arr);
        try{
            jsonParams.put("jsonArray", jsonArray);
        } catch (JSONException e){
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
    }
}